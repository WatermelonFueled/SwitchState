package com.watermelonfueled.switchstate;


import com.badlogic.gdx.math.Vector2;

/**
 * Enemies in the game.
 */
public class Enemy extends GameRectangle {

    private MovePattern pattern;    // MovePatterns shared by enemies of same type
    private Vector2 translation;    // Translation amount updated according to the movement pattern
    private Vector2 startPoint;
    private float moveTime;         // Time spent moving

    /**
     * Constructor for an enemy of specified size. Will require it's starting point to be set afterwards.
     * @param width enemy width
     * @param height enemy height
     */
    public Enemy(float width, float height){
        super(width, height);
        moveTime = 0f;
        translation = new Vector2();
        startPoint = new Vector2();     //will require setting of startPoint
    }

    /**
     * Constructor sets both size and starting point.
     * @param x enemy starting point x coordinate
     * @param y enemy starting point y coordinate
     * @param width enemy width
     * @param height enemy height
     */
    public Enemy(float x, float y, float width, float height) {
        super(x,y,width,height);
        moveTime = 0f;
        translation = new Vector2();
        startPoint = new Vector2(x,y);
    }

    /**
     * Sets the starting point
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setStartPoint(float x, float y) {
        startPoint.set(x,y);
    }

    /**
     * Sets the enemy's movement pattern.
     * @param pattern the {@link MovePattern}
     */
    public void setPattern(MovePattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Updates the enemy's position according to its movement pattern.
     * @param delta time (s) since last update
     * @see GameScreen#render(float)
     */
    public void move(float delta) {
        if (pattern == null){ return; } //no movement pattern
        else {
            moveTime += delta;
            if (pattern.update(moveTime, translation)) { // update translation vector
                setPosition(startPoint.x + translation.x, startPoint.y + translation.y);
            } else {
                // pattern is non-looping and has reached its end
            }
        }
    }
}
