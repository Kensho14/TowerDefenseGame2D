package jp.ac.uryukyu.ie.e195723.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import jp.ac.uryukyu.ie.e195723.engine.Scene;
import jp.ac.uryukyu.ie.e195723.mobs.scripts.SimpleSoldierAI;
import jp.ac.uryukyu.ie.e195723.mobs.scripts.ZombieSimpleAI;
import jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil;

public class SimpleSoldier extends MobBase {
    public static final float DEFAULT_HITPOINT = 7;
    public static final float DEFAULT_MOVESPEED = 0;
    public static final String TEX_PATH = "img/characters/soldier_chara_33.png";

    public SimpleSoldier(Scene scene, Vector2 position) {
        super(scene, "mob_soldier", Gdx.files.internal(TEX_PATH), PhysicsMode.Active, DEFAULT_MOVESPEED, DEFAULT_HITPOINT, TeamCode.Defense);
        changeSize(48, 62);
        setPosition(position.x, position.y);
        attachScript(new SimpleSoldierAI(5, 4*1000, 4* BlockPosUtil.BLOCK_BASE_SIZE));
    }
}
