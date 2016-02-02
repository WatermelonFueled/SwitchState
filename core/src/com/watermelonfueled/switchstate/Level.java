package com.watermelonfueled.switchstate;

import com.badlogic.gdx.math.Vector2;

/**
 * Sets up and loads a specific level of the game.
 */
public class Level {

    public int mapWidth, mapHeight;
    public Enemy[] enemies;
    public MovePattern[] movePatterns;
    public Vector2[] checkpoints;

    /**
     * Constructor for a level. Sets up the map, enemies and their properties.
     */
    public Level() {

        // SAMPLE LEVEL FOR TESTING
        enemies = new Enemy[4];
        movePatterns = new MovePattern[3];

        mapWidth = 500;
        mapHeight = 500;

        for (int i = 0; i < movePatterns.length; i++) {
            movePatterns[i] = new MovePattern();
            movePatterns[i].setDuration(3 + i);
        }
        movePatterns[0].setLoopOnce().setPoints(0,0,10,0,13,15);
        movePatterns[1].setLoopCircular().setPoints(0,0,0,15,9,10);
        movePatterns[2].setLoopPingpong().setPoints(0,0,14,14,10,18);

        for (int i = 0; i < enemies.length; i++){
            enemies[i] = new Enemy(10 * i + 10,15*i,3,3);
        }
        enemies[0].setPattern(movePatterns[0]);
        enemies[1].setPattern(movePatterns[1]);
        enemies[2].setPattern(movePatterns[2]);
        enemies[3].setPattern(movePatterns[0]);
    }

}
