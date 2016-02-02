package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * Handles the drawing of elements on to the device screen in game
 */
public class Renderer implements Disposable{
    private SwitchStateGame game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 camPos;
    private GameScreen screen;
    private Level level;
    private float viewportW, viewportH, mapW, mapH;

    //Assets
    private TextureRegion levelBG;

    /**
     * Constructor for renderer
     * @param game the game
     * @param camera the orthographic camera
     */
    public Renderer(SwitchStateGame game, OrthographicCamera camera) {
        Gdx.gl.glClearColor(0,0,0,1);
        this.game = game;
        this.camera = camera;
        camPos = camera.position;
        batch = new SpriteBatch();
    }

    // TODO set screen for start screen as well
    public void setGameScreen(GameScreen screen){
        this.screen = screen;
    }

    /**
     * Setup required assets and properties of the inputted level
     * @param level current level
     */
    public void setLevel(Level level){
        this.level = level;
        mapW = level.mapWidth;
        mapH = level.mapHeight;

        levelBG = AssetManager.levelBGs[game.currentLevel - 1];
    }

    /**
     * Draws all the game elements on the screen in their respective positions
     * @param gameTime amount of time that has passed in game
     */
    public void draw(float gameTime){
        clear();
        batch.begin();
        drawBG();
        switch (screen.playerState){
            case FROZEN:
                drawPlayer(AssetManager.playerFrozenAnimation.getKeyFrame(gameTime));
                drawEnemies(AssetManager.enemy1Sleep);
                break;
            case MOVING:
                drawPlayer(AssetManager.playerMovingAnimation.getKeyFrame(gameTime));
                drawEnemies(AssetManager.enemy1Animation.getKeyFrame(gameTime));
                break;
        }
        batch.end();
    }

    /**
     * Draws the level background with a parallax effect
     */
    private void drawBG(){
        batch.draw(levelBG, camPos.x / 2 + viewportW / 2 * (camPos.x - mapW / 2) / (mapW - viewportW),
                camPos.y / 2 + viewportH / 2 * (camPos.y - mapH / 2) / (mapH - viewportH),
                0, 0, mapW / 2, mapH / 2, 1f, 1f, 0);
    }

    /**
     * Draws the player.
     * @param keyframe the current animation keyframe of the player
     */
    private void drawPlayer(TextureRegion keyframe) {
        batch.draw(keyframe, screen.player.getX(), screen.player.getY(), screen.player.getWidth()/2,
                screen.player.getHeight()/2, screen.player.getWidth(), screen.player.getHeight(),
                1.5f, 2f, screen.player.getRotation());
    }

    /**
     * Draws the enemies.
     * @param keyframe current animation keyframe of the enemies
     */
    private void drawEnemies(TextureRegion keyframe) {
        for (Enemy enemy : level.enemies) {
            batch.draw(keyframe, enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        }
    }

    /**
     * Clears the screen (OpenGL) and resets the camera and spritebatch in preparation for a new draw.
     */
    private void clear(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    /**
     * Called after camera was resized (usually from a screen resize).
     */
    public void resizeCamera() {
        viewportW = camera.viewportWidth;
        viewportH = camera.viewportHeight;
    }

    /**
     * Releases all the renderer's resources.
     */
    public void dispose() {
        batch.dispose();
    }
}
