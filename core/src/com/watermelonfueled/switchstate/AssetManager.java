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

    private static AnimationFactory animationFactory;

    // UI
    public static TextureRegion pauseButton, resumeButton, backArrow;

    // Player related
    public static Animation playerFrozenAnimation;
    public static Animation playerMovingAnimation;

    // Enemy related
    private static final int ENEMY_COUNT = 1;
    public static Animation[] enemyAnimations = new Animation[ENEMY_COUNT];
    //public static Animation[] enemySleepAnimations = new Animation[ENEMY_COUNT];

    /**
     * Loads all the assets
     */
    public static void load() {
        Gdx.app.log(TAG,"Loading assets...");
        texture = new Texture("texture.png");
        font = new BitmapFont();

        animationFactory = AnimationFactory.instantiate();

        loadUI();
        loadPlayer();
        loadEnemies();
        loadLevelBGs(1);
        //loadAudio();
        Gdx.app.log(TAG,"Finished loading assets.");
    }

    /**
     * Loads assets for the UI
     */
    private static void loadUI() {
        pauseButton = new TextureRegion(texture,0,384,32,32);
        resumeButton = new TextureRegion(texture,0,416,32,32);
        backArrow = new TextureRegion(texture,64,448,64,64);
    }

    /**
     * Loads assets for {@link Player}; Frozen animation, Moving animation
     */
    private static void loadPlayer() {
        Gdx.app.log(TAG, "Loading player assets...");
        playerMovingAnimation = animationFactory.getAnimation(1,texture);
        playerFrozenAnimation = animationFactory.getAnimation(0,texture);
        Gdx.app.log(TAG,"Player loaded.");
    }

    /**
     * Loads assets for {@link Enemy}; Moving animation; Sleep animation
     */
    private static void loadEnemies() {
        Gdx.app.log(TAG,"Loading enemies assets...");
        // for each enemy type
        for (int i = 0; i < ENEMY_COUNT; i++) {
            enemyAnimations[i] = animationFactory.getAnimation(i+2,texture);
        }
        Gdx.app.log(TAG,"Enemies loaded.");
    }

    /**
     * Loads the backgrounds for each {@link Level}.
     * @param levelCount the number of levels in the game
     */
    private static void loadLevelBGs(int levelCount) {
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
