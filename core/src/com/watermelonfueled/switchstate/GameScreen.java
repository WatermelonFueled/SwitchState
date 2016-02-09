package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Screen for playing the game.
 */
public class GameScreen implements Screen {
    private final String TAG = "GameScreen";    // log tag
    private SwitchStateGame game;
    private OrthographicCamera camera;

    public enum GameState { RUNNING, PAUSED, GAMEOVER }
    GameState gameState;

    public Button pauseButton, resumeButton;

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

        setGamePaused();
        setFrozen();

        setupInput();
        setupUI();
        loadLevel();
    }

    /**
     * Sets up input handlers. Stage for pause button and pause menu actions. {@link GameInput} for player control.
     */
    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game.stage);                   // stage for UI
        multiplexer.addProcessor(new GameInput(player, this));  // player control
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Creates the UI: pausing, resuming
     */
    private void setupUI() {
        pauseButton = new Button(AssetManager.pauseButton);
        pauseButton.setPosition(game.stage.getWidth()-35,game.stage.getHeight()-35);
        pauseButton.setSize(32f, 32f);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGamePaused();
                pauseButton.setVisible(false);
                resumeButton.setVisible(true);
            }
        });

        resumeButton = new Button(AssetManager.resumeButton);
        resumeButton.setText("RESUME");
        resumeButton.setPosition(game.stage.getWidth() / 2, game.stage.getHeight() / 2);
        resumeButton.setSize(128f, 32f);
        resumeButton.setVisible(false);
        resumeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                setGameRunning();
                pauseButton.setVisible(true);
                resumeButton.setVisible(false);
            }
        });

        game.stage.addActor(pauseButton);
        game.stage.addActor(resumeButton);
    }

    /**
     * Loads the {@link Level}.
     */
    private void loadLevel(){
        level = new Level();
        game.renderer.setLevel(level);
        player.setPosition(10, 15);
    }

    /**
     * Called when screen becomes current screen of the game.
     */
    public void show() {
        setGameRunning();
        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
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
                break;
            case GAMEOVER:
                break;
        }
        game.stage.draw();
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
        setGamePaused(); //TODO pause menu
    }

    /**
     * Called when the application is resumed.
     */
    public void resume() {
        setGameRunning(); //TODO keep at pause menu for manual resume
    }

    /**
     * Called when this screen is no longer the displayed screen for the game.
     */
    public void hide() {
    }

    /**
     * Unused. Called to dispose of any resources of this screen.
     */
    public void dispose() {
    }

    /**
     * Sets player state to frozen.
     */
    public void setFrozen() { playerState = PlayerState.FROZEN; stateTime = 0f; }

    /**
     * Sets player state to moving.
     */
    public void setMoving() { playerState = PlayerState.MOVING; stateTime = 0f; }

    /**
     * Sets game state to running.
     */
    public void setGameRunning() { gameState = GameState.RUNNING; }

    /**
     * Sets game state to paused.
     */
    public void setGamePaused() { gameState = GameState.PAUSED; }

    /**
     * Sets game state to game over
     */
    public void setGameGameOver() { gameState = GameState.GAMEOVER; }

}
