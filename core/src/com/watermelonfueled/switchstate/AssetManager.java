package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Handles all the assets (textures, sounds, images) for the game and allows easy access for other classes.
 */
public class AssetManager {
    private static final String TAG = "ASSET MANAGER";      //tag for log
    public static Texture texture;                          //texture file
    public static BitmapFont font;                          //font used throughout game

    public static TextureRegion[] levelBGs;
    private static final int LEVEL_BG_WIDTH = 256;

    // Player related
    public static Animation playerFrozenAnimation;
    public static TextureRegion[] playerFrozenFrames;
    private static final int FROZEN_FRAME_COUNT = 20;
    public static Animation playerMovingAnimation;
    public static TextureRegion[] playerMovingFrames;

    // Enemy related
    public static Animation enemy1Animation;
    public static TextureRegion[] enemy1Frames;
    private static final int ENEMY1_FRAME_COUNT = 8;
    public static TextureRegion enemy1Sleep;

    /**
     * Loads all the assets
     */
    public static void load() {
        Gdx.app.log(TAG,"Loading assets...");
        texture = new Texture("texture.png");
        font = new BitmapFont();

        loadPlayer();
        loadEnemies();
        loadLevelBGs(1);
        //loadAudio();
        Gdx.app.log(TAG,"Finished loading assets.");
    }

    /**
     * Loads assets for {@link Player}; Frozen animation, Moving animation
     */
    public static void loadPlayer() {
        Gdx.app.log(TAG,"Loading player assets...");
        playerFrozenFrames = new TextureRegion[FROZEN_FRAME_COUNT];
        playerMovingFrames = new TextureRegion[FROZEN_FRAME_COUNT];
        for (int i = 0; i < FROZEN_FRAME_COUNT; i++){
            playerFrozenFrames[i] = new TextureRegion(texture,i*48,0,48,64);
            playerMovingFrames[i] = new TextureRegion(texture,i*48,64,48,64);
        }
        playerFrozenAnimation = new Animation(0.1f, playerFrozenFrames);
        playerFrozenAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        playerMovingAnimation = new Animation(0.1f, playerMovingFrames);
        playerMovingAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Gdx.app.log(TAG,"Player loaded.");
    }

    /**
     * Loads assets for {@link Enemy}; Moving animation; Sleep animation
     */
    public static void loadEnemies() {
        Gdx.app.log(TAG,"Loading enemies assets...");
        //enemy1
        Gdx.app.debug(TAG,"Loading enemy 1...");
        enemy1Frames = new TextureRegion[ENEMY1_FRAME_COUNT];
        TextureRegion face1 = new TextureRegion(texture,960,0,32,32);
        for (int i = 0; i < ENEMY1_FRAME_COUNT - 3; i++){
            enemy1Frames[i] = face1;
        }
        enemy1Frames[ENEMY1_FRAME_COUNT-2] = face1;
        enemy1Frames[ENEMY1_FRAME_COUNT-3] = new TextureRegion(texture,992,0,32,32);
        enemy1Frames[ENEMY1_FRAME_COUNT-1] = new TextureRegion(texture,960,32,32,32);
        enemy1Animation = new Animation(0.05f, enemy1Frames);
        enemy1Animation.setPlayMode(Animation.PlayMode.LOOP);
        enemy1Sleep = new TextureRegion(texture,992,32,32,32);

        Gdx.app.log(TAG,"Enemies loaded.");
    }

    /**
     * Loads the backgrounds for each {@link Level}.
     * @param levelCount the number of levels in the game
     */
    public static void loadLevelBGs(int levelCount) {
        Gdx.app.log(TAG, "Loading level backgrounds...");
        levelBGs = new TextureRegion[levelCount];
        for (int i = 0; i < levelCount; i++) {
            Gdx.app.debug(TAG, "Loading background for level "+(i+1));
            levelBGs[i] = new TextureRegion(texture,i * LEVEL_BG_WIDTH, 128, LEVEL_BG_WIDTH,LEVEL_BG_WIDTH);
        }
        Gdx.app.log(TAG, "Level backgrounds loaded");
    }

    // TODO audio loading

    /**
     * Releases all resources loaded by this.
     */
    public static void dispose(){
        texture.dispose();
    }
}
