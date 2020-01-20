package jp.ac.uryukyu.ie.e195723.mobs;

import com.badlogic.gdx.files.FileHandle;
import jp.ac.uryukyu.ie.e195723.StageObject;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

/**
 * Mobに共通の基底クラス。
 * ヒットポイント，移動速度等を持つ
 */
public class MobBase extends StageObject {
    private ISkill attackSkill;
    private SimpleMobMover movingLogic;

    public MobBase(Scene scene, String name, FileHandle texture, PhysicsMode physicsMode, float moveSpeed, float hitPoint, StageObject.TeamCode teamCode) {
        super(scene, name, texture, physicsMode, hitPoint, teamCode);
        movingLogic = new SimpleMobMover(moveSpeed);
        attachScript(movingLogic);
    }

    @Override
    public void onDeath(StageObject attacker) {

    }
}
