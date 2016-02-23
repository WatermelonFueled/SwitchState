package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Main game class.
 */
public class SwitchStateGame extends Game{
    private final String TAG = "GAME";

    public final int GAME_WIDTH = 540;
    public final int GAME_HEIGHT = 960;
    public final float CAM_WIDTH = 50f;

    public Preferences progress;        // players saved progress
    public final int LEVEL_COUNT = 1;   // total number of levels in the game
    public int currentLevel, currentStage, unlockedLevels;

    public StartScreen startScreen;
    public GameScreen gameScreen;
    public Renderer renderer;
    public Stage stage;
    public OrthographicCamera camera;

    /**
     * Called when game is launched. Sets up all the necessary resources for the game.
     */
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.log(TAG, "Game started.");
        Gdx.app.debug(TAG, "Loading assets...");
        // TODO loading screen
        Json json = new Json();

        AssetManager.load();
        loadProgress();
        Gdx.app.debug(TAG, "Finished loading assets.");

        currentLevel = 1;   // for testing

        camera = new OrthographicCamera(GAME_WIDTH, GAME_HEIGHT);
        stage = new Stage(new StretchViewport(540,960));
        renderer = new Renderer(this, camera);
        startScreen = new StartScreen(this);
        gameScreen = new GameScreen(this);
        renderer.setGameScreen(gameScreen);

        Gdx.app.log(TAG,"Initializing start screen...");
        this.setScreen(startScreen);
    }

    /**
     * Calls render on the current screen.
     */
    public void render() {
        super.render();
    }

    /**
     * Called to release game resources. Saves player progress as well.
     */
    public void dispose() {
        Gdx.app.log(TAG, "Saving progress...");
        saveProgress();
        Gdx.app.log(TAG, "Saved Progress.");

        renderer.dispose();
        stage.dispose();

        Gdx.app.debug(TAG, "Disposing assets...");
        AssetManager.dispose();
        Gdx.app.debug(TAG, "Disposed assets.");
    }

    /**
     * Loads the players progress.
     */
    private void loadProgress() {
        progress = Gdx.app.getPreferences("SwitchStateGame_SavedProgress");
        currentLevel = progress.getInteger("currentLevel", 1);
        currentStage = progress.getInteger("currentStage", 1);
        unlockedLevels = progress.getInteger("unlockedLevels", 1);
    }

    /**
     * Saves the players progress.
     */
    private void saveProgress() {
        progress.putInteger("currentLevel", currentLevel);
        progress.putInteger("currentStage", currentStage);
        progress.putInteger("unlockedLevels", unlockedLevels);
        progress.flush();
    }

    public boolean savedProgressExists(){
        if (currentLevel > 1 || currentStage > 1) {
            return true;
        } else {
            return false;
        }
    }
}
