package jp.ac.uryukyu.ie.e195723.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

public class Zombie extends MobBase {
    public static final float DEFAULT_HITPOINT = 10;
    public static final float DEFAULT_MOVESPEED = 10;
    public static final String TEX_PATH = "img/characters/zombie_chara_16.png";

    public Zombie(Scene scene, Vector2 position) {
        super(scene, "mob_zombie", Gdx.files.internal(TEX_PATH), PhysicsMode.Active, DEFAULT_MOVESPEED, DEFAULT_HITPOINT, TeamCode.Enemy);
        changeSize(48, 62);
        setPosition(position.x, position.y);
    }
}
