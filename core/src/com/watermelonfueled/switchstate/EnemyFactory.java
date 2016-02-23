package com.watermelonfueled.switchstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Handles creation of enemy objects
 */
public class EnemyFactory {

    private ArrayList<EnemyPrototype> prototypes;

    private EnemyFactory() {}

    /**
     * Creates the factory, loading the {@link EnemyPrototype} data from file
     * @return the factory itself
     */
    public static EnemyFactory instantiate() {
        Json json = new Json();

        EnemyFactory enemyFactory = json.fromJson(EnemyFactory.class, Gdx.files.internal("enemyPrototypes.json"));

        return enemyFactory;
    }

    /**
     * Creates an enemy object based on a prototype
     * @param id the id of the enemy prototype
     * @return the created enemy object
     */
    public Enemy createEnemy(int id){
        EnemyPrototype prototype = prototypes.get(id);
        Enemy enemy = new Enemy(prototype.width, prototype.height);
        enemy.setAnimationId(prototype.animationId);

        return enemy;
    }
}
