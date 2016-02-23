package com.watermelonfueled.switchstate;

/**
 * Data object holds level data loaded from the appropriate level file. See {@link Level}
 */
public class LevelData {
    public MovePatternData[] movePatternData;
    public com.watermelonfueled.switchstate.EnemyData[] enemyData;
    public float playerX, playerY;
    public int mapW, mapH;
    public LevelData(){}
}
