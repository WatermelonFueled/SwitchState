package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Start menu screen.
 */
public class StartScreen implements Screen {
    final SwitchStateGame game;
    private final String TAG = "StartScreen";   // log tag

    private Group levelLoadMenu;
    private Button startButton, loadLevelButton, backToMainButton;
    private Button[] levelButtons;
    private final float BUTTON_WIDTH = 256f;

    /**
     * Constructor for the start screen.
     * @param game the game
     */
    public StartScreen(SwitchStateGame game){
        Gdx.app.log(TAG, "Start screen started.");
        this.game = game;
        setupMainUI();
        setupLevelLoaderUI();
        setLevelLoaderVisible(false);
    }

    /**
     * Creates the UI for the main menu; menu buttons
     */
    private void setupMainUI() {
        Gdx.app.debug(TAG, "Creating start screen UI...");
        startButton = new Button(AssetManager.resumeButton);
        if (game.savedProgressExists()){
            startButton.setText("CONTINUE");
        } else {
            startButton.setText("NEW GAME");
        }
        startButton.setSize(BUTTON_WIDTH, BUTTON_WIDTH / 4);
        startButton.setPosition((game.stage.getWidth() - startButton.getWidth()) / 2, 512f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });

        loadLevelButton = new Button(AssetManager.resumeButton);
        loadLevelButton.setText("LOAD LEVEL");
        loadLevelButton.setSize(BUTTON_WIDTH, BUTTON_WIDTH / 4);
        loadLevelButton.setPosition((game.stage.getWidth() - loadLevelButton.getWidth()) / 2, 436f);
        loadLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setLevelLoaderVisible(true);
            }
        });


        Gdx.app.debug(TAG, "Created start screen UI.");
    }

    /**
     * Creates the level loading menu UI
     */
    private void setupLevelLoaderUI() {
        // level loading menu group holding all of its elements
        levelLoadMenu = new Group();
        levelLoadMenu.setSize(game.stage.getWidth() - 40, game.stage.getHeight() - 40);
        levelLoadMenu.setPosition((game.stage.getWidth()-levelLoadMenu.getWidth())/2, (game.stage.getHeight()-levelLoadMenu.getHeight())/2);

        // button for each level of the game
        levelButtons = new Button[game.LEVEL_COUNT];
        Button levelButton;
        for (int i = 0; i < levelButtons.length; i++){
            levelButtons[i] = new Button(AssetManager.resumeButton);
            levelButton = levelButtons[i];
            levelButton.setText("Level: " + (i + 1));
            levelButton.setSize(BUTTON_WIDTH, BUTTON_WIDTH / 4);
            levelButton.setPosition(0, levelLoadMenu.getHeight() - levelButton.getHeight() - i * BUTTON_WIDTH / 3);
            final int index = i+1;
            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.currentLevel = index;
                    startGame();
                }
            });
            levelLoadMenu.addActor(levelButton);
        }

        // back arrow
        backToMainButton = new Button(AssetManager.backArrow);
        backToMainButton.setSize(32f,32f);
        backToMainButton.setPosition(levelLoadMenu.getWidth() - backToMainButton.getWidth() * 1.2f,
                backToMainButton.getHeight() * 1.2f);
        backToMainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setLevelLoaderVisible(false);
            }
        });
        levelLoadMenu.addActor(backToMainButton);

    }

    /**
     * Show or hide the level loader sub-menu (go between main menu and level sub-menu)
     * @param visible true to show the level loader sub-menu, false for the main menu
     */
    private void setLevelLoaderVisible(boolean visible){
        if (visible) {
            startButton.setVisible(false);
            loadLevelButton.setVisible(false);
            levelLoadMenu.setVisible(true);
        } else {
            startButton.setVisible(true);
            loadLevelButton.setVisible(true);
            levelLoadMenu.setVisible(false);
        }
    }

    /**
     * Called when screen renders itself.
     * @param delta time since last render
     */
    public void render(float delta) {
        game.renderer.clear();
        game.stage.draw();
    }

    /**
     * Starts the game by switching to the game screen.
     */
    public void startGame() {
        Gdx.app.log(TAG, "Switching to game screen...");
        game.gameScreen.loadLevel(game.currentLevel);
        setLevelLoaderVisible(false);
        game.setScreen(game.gameScreen);
    }

    /**
     * Called when screen size changes.
     * @param width new screen width
     * @param height new screen height
     */
    public void resize(int width, int height) {
        game.camera.viewportWidth = game.GAME_WIDTH;
        game.camera.viewportHeight = game.GAME_HEIGHT;
        game.camera.update();
        game.renderer.resizeCamera();
        game.stage.getViewport().update(width, height, false);
    }

    public void pause() {

    }

    public void resume() {

    }

    /**
     * Called when this screen becomes the screen to display.
     */
    public void show() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.stage.addActor(startButton);
        game.stage.addActor(loadLevelButton);
        game.stage.addActor(levelLoadMenu);
    }

    /**
     * Called when this screen is removed as the screen to display
     */
    public void hide() {
        startButton.remove();
        loadLevelButton.remove();
        levelLoadMenu.remove();
    }

    public void dispose() {

    }
}
