package com.watermelonfueled.switchstate;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Handles player control by the user. Specifically handles touch down, dragged and up motions. Manages input in screen coordinates (
 */
public class GameInput extends InputAdapter {
    private final String TAG = "GAME INPUT";    // log tag
    private final int MAX_DISTANCE_SQ = 4900;   // 70^2 - squared for less expensive distance calculations
    private Vector2 down;                       // touch down position

    private GameScreen game;
    private Player player;

    /**
     * Constructor for gameinput
     * @param player {@link Player}
     * @param game {@link GameScreen}
     */
    public GameInput(Player player, GameScreen game) {
        this.game = game;
        this.player = player;
        down = new Vector2();
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
        down.x = x;
        down.y = y;
        switch (game.gameState){
            case RUNNING:
                game.setMoving();
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
        switch (game.gameState){
            case RUNNING:
                // angle, scale
                player.setDirection(MathUtils.atan2(y - down.y, down.x - x),
                        MathUtils.clamp(down.dst2(x,y) / MAX_DISTANCE_SQ, 0f, 1f));
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
        switch (game.gameState){
            case RUNNING:
                game.setFrozen();
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
