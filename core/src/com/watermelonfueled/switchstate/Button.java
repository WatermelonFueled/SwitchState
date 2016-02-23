package com.watermelonfueled.switchstate;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.watermelonfueled.switchstate.AssetManager;

/**
 * General UI button for use by player in menus and in game overlay/HUD.
 */
public class Button extends Actor {

    private TextureRegion region;   // Button appearance
    private String text;            // Text to display on the button (if any)

    /**
     * Constructor for a button.
     * @param region the {@link TextureRegion} for the button's appearance
     */
    public Button(TextureRegion region) { this.region = region; }

    /**
     * Sets the text of the button.
     * @param text button's new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Draws the button at it's location.
     * @param batch the batch taking in all the drawable elements on screen
     * @param parentAlpha alpha of parent element for matching the button's alpha
     */
    @Override
    public void draw (Batch batch, float parentAlpha) {
        // TODO apply parentAlpha
        // Draw button texture
        batch.draw(region, getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1f,1f,0);
        // Draw button text, if any
        if (text != null) {
            AssetManager.font.draw(batch, text,
                    getX(), getY()+getHeight()/2+ AssetManager.font.getCapHeight()/2,
                    getWidth(), Align.center, true);
        }

    }
}
