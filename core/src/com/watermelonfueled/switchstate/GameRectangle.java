package com.watermelonfueled.switchstate;

/**
 * Superclass of interacting objects in the game. Is a basic rectangle.
 */
public class GameRectangle {
    private float x,y,width,height;

    /**
     * Constructor setting the size of the GameRectangle.
     * @param width width
     * @param height height
     */
    public GameRectangle(float width, float height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor with set size and position.
     * @param x x coordinate
     * @param y y coordinate
     * @param width width
     * @param height height
     */
    public GameRectangle(float x, float y, float width, float height){
        setPosition(x,y);
        this.width = width;
        this.height = height;
    }

    /**
     * Sets position at x,y
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translates GameRectangle by x and y
     * @param x amount to translate in x axis
     * @param y amount to translate in y axis
     */
    public void translate(float x, float y) {
        translateX(x);
        translateY(y);
    }

    public void translateX(float x) {
        this.x += x;
    }

    public void translateY(float y) {
        this.y += y;
    }

    /**
     * Checks for collision between this and another GameRectangle.
     * @param obj
     * @return true if there is a collision, false otherwise
     */
    public boolean collides(GameRectangle obj) {
        if (collidesX(obj) && collidesY(obj)){
            return true;
        } else {
            return false;
        }
    }

    public boolean collidesX(GameRectangle obj) {
        if (x + width < obj.x || x > obj.x + obj.width) {
            return false;
        } else {
            return true;
        }
    }

    public boolean collidesY(GameRectangle obj) {
        if (y + height < obj.y || y > obj.y + obj.height) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the width.
     * @return the width
     */
    public float getWidth() { return width; }

    /**
     * Gets the height.
     * @return the height
     */
    public float getHeight() { return height; }

    /**
     * Gets the x coordinate.
     * @return x coordinate
     */
    public float getX() { return x; }

    /**
     * Sets the x coordinate.
     * @param x new x coordinate
     */
    public void setX(float x) { this.x = x; }

    /**
     * Gets the y coordinate.
     * @return y coordinate
     */
    public float getY() { return y; }

    /**
     * Sets the y coordinate
     * @param y new y coordinate
     */
    public void setY(float y) { this.y = y; }

}
