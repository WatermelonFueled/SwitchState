package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Handles player control by the user. Specifically handles touch down, dragged and up motions. Manages input in screen coordinates (
 */
public class GameInput extends InputAdapter {
    private final String TAG = "GAME INPUT";    // log tag
    private final float MAX_DISTANCE = 40;      // max distance from touchdown point for max player speed.
    private Vector2 down;                       // touch down position

    private GameScreen gameScreen;
    private Player player;

    private Actor controllerFront, controllerBack;  // analog stick ui

    /**
     * Constructor for gameinput
     * @param player {@link Player}
     * @param gameScreen {@link GameScreen}
     */
    public GameInput(Player player, GameScreen gameScreen, Actor controllerFront, Actor controllerBack) {
        this.gameScreen = gameScreen;
        this.player = player;
        down = new Vector2();
        this.controllerFront = controllerFront;
        this.controllerBack = controllerBack;
        controllerFront.setOrigin(controllerFront.getWidth()/2,controllerFront.getHeight()/2);
        controllerBack.setOrigin(controllerBack.getWidth()/2,controllerBack.getHeight()/2);
    }

    /**
     * Called when screen is touched initially. Sets player state to moving.
     * @param x x coordinate
     * @param y y coordinate
     * @param pointer unused
     * @param button unused
     * @return returns true after handling touch
     */
    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        down.set(x, y);
        switch (gameScreen.gameState){
            case RUNNING:
                gameScreen.setMoving();
                controllerBack.setPosition(x - controllerBack.getOriginX(),
                        controllerBack.getStage().getHeight() - y - controllerBack.getOriginY());
                controllerFront.setPosition(x - controllerFront.getOriginX(),
                        controllerFront.getStage().getHeight() - y - controllerFront.getOriginY());
                break;
            case PAUSED:
            case GAMEOVER:
        }
        return true;
    }

    /**
     * Called when finger is dragged on screen. Moves the player in the direction and speed proportional to how far the finger was dragged from touch down location.
     * @param x x coordinate
     * @param y y coordinate
     * @param pointer unused
     * @return true
     */
    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        switch (gameScreen.gameState){
            case RUNNING:
                float angle = MathUtils.atan2(y - down.y, down.x - x);
                Gdx.app.log("ANGLE","angle: "+angle);
                float clampedDistRatio = MathUtils.clamp(down.dst(x, y),0,MAX_DISTANCE) / MAX_DISTANCE;
                player.setDirection(angle, clampedDistRatio);
                controllerFront.setPosition(clampedDistRatio * -16 * MathUtils.cos(angle) + controllerBack.getX()+controllerBack.getOriginX()-controllerFront.getOriginX(),
                        clampedDistRatio * 16 * MathUtils.sin(-angle) + controllerBack.getY()+controllerBack.getOriginY()-controllerFront.getOriginY());
                break;
            case PAUSED:
            case GAMEOVER:
        }
        return true;
    }

    /**
     * Called when finger lifts off screen. Stops the player and sets player state to frozen.
     * @param x x coordinate
     * @param y y coordinate
     * @param pointer unused
     * @param button unused
     * @return true
     */
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        switch (gameScreen.gameState){
            case RUNNING:
                gameScreen.setFrozen();
                player.stop();
                break;
            case PAUSED:

                break;
            case GAMEOVER:

                break;
        }
        return true;
    }
}
