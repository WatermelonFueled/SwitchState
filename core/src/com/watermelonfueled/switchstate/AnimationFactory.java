package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Handles creation of animation objects
 */
public class AnimationFactory {

    private ArrayList<AnimationPrototype> prototypes;

    private AnimationFactory() {}

    /**
     * Creates the factory, loading the {@link AnimationPrototype} data from file
     * @return the factory itself
     */
    public static AnimationFactory instantiate() {
        Json json = new Json();
        AnimationFactory factory = json.fromJson(AnimationFactory.class,
                Gdx.files.internal("animationPrototypes.json"));
        return factory;
    }

    /**
     * Creates the animation from the texture using the data found in the prototype
     * @param id the id of the animation prototype
     * @param texture main texture to get texture regions from
     * @return the created Animation
     */
    public Animation getAnimation(int id, Texture texture){
        AnimationPrototype prototype = prototypes.get(id);
        TextureRegion[] frames = new TextureRegion[prototype.animFrameCount];
        for (int i = 0, f = 0; i < prototype.textNumFrames.length; i++) {
            TextureRegion frame = new TextureRegion(texture, prototype.textX[i],prototype.textY[i],
                    prototype.textWidth, prototype.textHeight);
            for (int j = 0; j < prototype.textNumFrames[i]; j++, f++) {
                frames[f] = frame;
            }
        }
        Animation animation = new Animation(prototype.animLength, frames);
        switch (prototype.loopType){
            case 1:
                animation.setPlayMode(Animation.PlayMode.LOOP);
                break;
            case 2:
                animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                break;
            case 3:
                animation.setPlayMode(Animation.PlayMode.NORMAL);
        }
        return animation;
    }
}
