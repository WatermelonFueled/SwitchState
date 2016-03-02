package com.watermelonfueled.switchstate;

/**
 * Data object holds level data loaded from the appropriate level file. See {@link Level}
 */
public class LevelData {
    public MovePatternData[] movePatternData;
    public EnemyData[] enemyData;
    public WallData[] wallData;
    public float playerX, playerY;
    public int mapW, mapH;
    public LevelData(){}
}
