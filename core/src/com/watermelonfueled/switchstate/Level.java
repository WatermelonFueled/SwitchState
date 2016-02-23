package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Sets up and loads a specific level of the game.
 */
public class Level {
    private final String TAG = "LEVEL";

    public int mapWidth, mapHeight;
    public Enemy[] enemies;
    private EnemyFactory enemyFactory;


    /**
     * Constructor for Level
     */
    public Level() {
        enemyFactory = EnemyFactory.instantiate();
    }

    /**
     * Loads the level, creating game objects and setting up the levels properties
     * @param id the level to load
     * @param player the player object, for setting start position
     */
    public void loadLevel(int id, Player player){
        Gdx.app.log(TAG, "Loading level: "+id);
        // load data from appropriate level file
        Json json = new Json();
        Gdx.app.debug(TAG, "Opening level"+id+".json");
        LevelData data = json.fromJson(LevelData.class, Gdx.files.internal("level"+id+".json"));

        enemies = new Enemy[data.enemyData.length];
        ArrayList<MovePattern> movePatterns = new ArrayList<MovePattern>(data.movePatternData.length);

        Gdx.app.debug(TAG, "Creating move patterns");
        for (MovePatternData moveData : data.movePatternData){
            movePatterns.add(new MovePattern(moveData.duration, moveData.loopMode, moveData.pointsX, moveData.pointsY));
        }

        Gdx.app.debug(TAG, "Creating enemies");
        EnemyData enemyData;
        Enemy enemy;
        for (int i = 0; i < data.enemyData.length; i++) {
            enemyData = data.enemyData[i];
            enemy = enemyFactory.createEnemy(enemyData.id);
            enemy.setStartPoint(enemyData.startX,enemyData.startY);
            enemy.setPattern(movePatterns.get(enemyData.movePatternId));
            enemies[i] = enemy;
        }

        Gdx.app.debug(TAG, "Set player");
        player.setPosition(data.playerX, data.playerY);

        Gdx.app.debug(TAG, "Set map");
        mapWidth = data.mapW;
        mapHeight = data.mapH;
        Gdx.app.log(TAG, "Loading level complete.");
    }

}
