package com.watermelonfueled.switchstate;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Player class. Extends on superclass by adding velocity and direction.
 */
public class Player extends GameRectangle {
    private final int MAX_SPEED = 25; // units/s
    private float rotation;
    private Vector2 velocity;

    /**
     * Constructor for player.
     */
    public Player() { super(3,3); velocity = new Vector2(); }

    /**
     * Moves the player according to its current velocity.
     * @param delta time (s) since last update in game
     */
    public void move(float delta) {
        translate(  -velocity.x * delta,
                    -velocity.y * delta);
    }

    /**
     * Called to cancel a previous move. Used for walls and other impassable obstacles
     * @param delta time (s) since last update in game
     */
    public void cancelMove(float delta) {
        translate(  velocity.x * delta,
                    velocity.y * delta);
    }

    /**
     * Sets the direction of the player and velocity in that new direction
     * @param direction new direction (radians)
     * @param scale % of max speed
     */
    public void setDirection(float direction, float scale){
        velocity.set(   MathUtils.cos(direction) * MAX_SPEED * scale,
                        MathUtils.sin(direction) * MAX_SPEED * scale);
        rotation = MathUtils.radiansToDegrees * direction + 90;
    }

    /**
     * Sets player velocity to zero.
     */
    public void stop() { velocity.setZero(); }

    /**
     * Get player rotation/direction
     * @return rotation in degrees
     */
    public float getRotation() { return rotation; }

    /**
     * Gets the x component of velocity
     * @return x of velocity
     */
    public float getVelX() { return velocity.x; }

    /**
     * Gets the y component of velocity
     * @return y of velocity
     */
    public float getVelY() { return velocity.y; }
}
