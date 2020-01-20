package jp.ac.uryukyu.ie.e195723.mobs;

import com.badlogic.gdx.files.FileHandle;
import jp.ac.uryukyu.ie.e195723.dataObj.MobData;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

public class MobBase extends GameObject {
    private float moveSpeed;
    private float hitPoint;
    private ISkill attackSkill;

    public MobBase(Scene scene, MobData mobData) {
        super(scene, mobData.name, null, mobData.physicsMode);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }
}
