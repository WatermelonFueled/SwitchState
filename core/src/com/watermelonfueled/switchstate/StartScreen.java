package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Start menu screen.
 */
public class StartScreen implements Screen {
    final SwitchStateGame game;
    private final String TAG = "StartScreen";   // log tag

    /**
     * Constructor for the start screen.
     * @param game the game
     */
    public StartScreen(SwitchStateGame game){
        Gdx.app.log(TAG, "Start screen started.");
        this.game = game;
    }

    /**
     * Called when this screen becomes the screen to display.
     */
    public void show() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Called when screen renders itself.
     * @param delta time since last render
     */
    public void render(float delta) {

        if (Gdx.input.isTouched()) {
            startGame();
        }
    }

    /**
     * Starts the game by switching to the game screen.
     */
    private void startGame() {
        Gdx.app.log(TAG, "Initializing game screen...");
        game.setScreen(game.gameScreen);
        //dispose();
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
        game.stage.getViewport().update(width,height,false);
    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {

    }
}
