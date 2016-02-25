package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Screen for playing the game.
 */
public class GameScreen implements Screen {
    private final String TAG = "GameScreen";    // log tag
    private SwitchStateGame game;
    private OrthographicCamera camera;

    public enum GameState { RUNNING, PAUSED, GAMEOVER }
    GameState gameState;

    private Group gameUI, pauseMenu;
    private Button pauseButton, resumeButton, mainMenuButton, controllerFront, controllerBack;

    private Level level;

    public Player player;
    public enum PlayerState { FROZEN, MOVING }
    PlayerState playerState;

    public volatile float stateTime;
    public volatile float gameTime;

    /**
     * Constructor sets up game in (selected) level.
     * @param game
     */
    public GameScreen(SwitchStateGame game) {
        Gdx.app.log(TAG, "Game screen started.");
        this.game = game;
        camera = game.camera;
        player = new Player();

        setupUI();
        setupInput();
        setGamePaused();

        level = new Level();
    }

    /**
     * Sets up input handlers. Stage for pause button and pause menu actions. {@link GameInput} for player control.
     */
    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game.stage);                   // stage for UI
        multiplexer.addProcessor(new GameInput(player, this, controllerFront, controllerBack));  // player control
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Creates the UI: pause, resume, main menu
     */
    private void setupUI() {
        Gdx.app.debug(TAG, "Creating game UI...");
        pauseButton = new Button(AssetManager.pauseButton);
        pauseButton.setSize(32f, 32f);
        pauseButton.setPosition(game.stage.getWidth() - pauseButton.getWidth() * 1.2f,
                game.stage.getHeight() - pauseButton.getWidth() * 1.2f);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause();
            }
        });
        controllerBack = new Button(AssetManager.controllerBack);
        controllerBack.setSize(64, 64);
        controllerFront = new Button(AssetManager.controllerFront);
        controllerFront.setSize(32,32);
        gameUI = new Group();
        gameUI.addActor(pauseButton);
        gameUI.addActor(controllerBack);
        gameUI.addActor(controllerFront);
        // Pause menu
        float pauseMenuButtonWidth = 128f;

        resumeButton = new Button(AssetManager.resumeButton);
        resumeButton.setText("RESUME");
        resumeButton.setSize(pauseMenuButtonWidth, pauseMenuButtonWidth / 4);
        resumeButton.setPosition((game.stage.getWidth() - pauseMenuButtonWidth) / 2,
                game.stage.getHeight() / 2 + 2f);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameRunning();
            }
        });

        mainMenuButton = new Button(AssetManager.resumeButton);
        mainMenuButton.setText("MAIN MENU");
        mainMenuButton.setSize(pauseMenuButtonWidth, pauseMenuButtonWidth / 4);
        mainMenuButton.setPosition((game.stage.getWidth() - pauseMenuButtonWidth) / 2,
                game.stage.getHeight() / 2 - 2f - pauseMenuButtonWidth / 4);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(TAG, "Switching to start screen...");
                game.setScreen(game.startScreen);
            }
        });
        pauseMenu = new Group();
        pauseMenu.addActor(resumeButton);
        pauseMenu.addActor(mainMenuButton);

        //TODO confirm dialog when moving to main menu


        setPauseMenuVisible(false);

        Gdx.app.debug(TAG, "Created game UI.");
    }

    /**
     * Loads the level
     * @param levelId the level to load
     */
    public void loadLevel(int levelId){
        level.loadLevel(levelId, player);
        game.renderer.setLevel(level);
    }

    /**
     * Called when screen renders itself. Updates the game and all game elements if running and draws them.
     * @param delta time (s) since last render call
     */
    public void render(float delta) {
        switch (gameState){
            case RUNNING:
                stateTime += delta;
                gameTime += delta;
                game.renderer.draw(gameTime);
                update(delta);
                break;
            case PAUSED:
            case GAMEOVER:
                game.renderer.draw(gameTime);
                break;
        }
    }

    /**
     * Update game elements and checks interactions after movements.
     * @param delta time (s) since last update
     */
    private void update(float delta) {
        switch (playerState) {
            case FROZEN:
                for (Enemy enemy : level.enemies) {
                    enemy.move(delta);
                }
                break;
            case MOVING:
                player.move(delta);
                checkBounds();
                //TODO wall collision -> cancel movement
                //enemies
                for (Enemy enemy : level.enemies) {
                    if (enemy.collides(player)) {
                        enemy.setPattern(null);
                    }
                    enemy.move(delta);
                }
                repositionCamera();
                break;
        }
    }

    /**
     * Keeps player within the bounds of the level.
     */
    private void checkBounds() {
        // left border, right border, bottom border, top border
        if (player.getX() < 0) { player.setX(0f); }
        else if (player.getX() > level.mapWidth - player.getWidth()) { player.setX(level.mapWidth - player.getWidth()); }
        if (player.getY() < 0) { player.setY(0f); }
        else if (player.getY() > level.mapHeight - player.getHeight()) { player.setY(level.mapHeight - player.getHeight()); }
    }

    /**
     * Moves camera to follow the player in the center while staying within the bounds of the level.
     */
    private void repositionCamera() {
        camera.position.x = player.getX()+player.getWidth()/2;
        camera.position.y = player.getY()+player.getHeight()/2;
        camera.position.x = MathUtils.clamp(camera.position.x, game.CAM_WIDTH/2, level.mapWidth-game.CAM_WIDTH/2);
        camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight/2, level.mapHeight-camera.viewportHeight/2);
    }

    /**
     * Called when the screen size changes. Camera is updated with the new screen size.
     * @param width the new width of the screen
     * @param height the new height of the screen
     */
    public void resize(int width, int height) {
        camera.viewportWidth = game.CAM_WIDTH;
        camera.viewportHeight = game.CAM_WIDTH * height/width;
        camera.update();
        game.renderer.resizeCamera();
        repositionCamera();
        game.stage.getViewport().update(width,height,false);
    }

    /**
     * Called when the application is paused. Is also called before application is destroyed.
     */
    public void pause() {
        setGamePaused();
        setPauseMenuVisible(true);
    }

    /**
     * Called when the application is resumed.
     */
    public void resume() {

    }

    /**
     * Shows or hides the pause menu
     * @param visible true for showing the pause menu, false hides pause menu
     */
    private void setPauseMenuVisible(boolean visible){
        if (visible){
            pauseMenu.setVisible(true);
        } else {
            pauseMenu.setVisible(false);
        }
    }

    /**
     * Called when screen becomes current screen of the game.
     */
    public void show() {
        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.stage.addActor(gameUI);
        game.stage.addActor(pauseMenu);
        setGameRunning();
    }

    /**
     * Called when this screen is no longer the displayed screen for the game.
     */
    public void hide() {
        pause();
        pauseMenu.remove();
        gameUI.remove();
    }

    /**
     * Unused. Called to dispose of any resources of this screen.
     */
    public void dispose() {
    }

    /**
     * Sets player state to frozen.
     */
    public void setFrozen() {
        playerState = PlayerState.FROZEN; stateTime = 0f;
        controllerBack.setVisible(false);
        controllerFront.setVisible(false);
    }

    /**
     * Sets player state to moving.
     */
    public void setMoving() {
        playerState = PlayerState.MOVING;
        stateTime = 0f;
        controllerBack.setVisible(true);
        controllerFront.setVisible(true);
    }

    /**
     * Sets game state to running.
     */
    public void setGameRunning() {
        gameState = GameState.RUNNING;
        setPauseMenuVisible(false);
    }

    /**
     * Sets game state to paused.
     */
    public void setGamePaused() {
        gameState = GameState.PAUSED;
        setFrozen();
    }

    /**
     * Sets game state to game over
     */
    public void setGameGameOver() { gameState = GameState.GAMEOVER; }

}
