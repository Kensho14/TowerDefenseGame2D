package jp.ac.uryukyu.ie.e195723.mobs;

import com.badlogic.gdx.files.FileHandle;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

public class MobBase extends GameObject {
    public enum TeamCode{
        Defense,
        Enemy
    }

    private float maxHitPoint;
    private ISkill attackSkill;
    private SimpleMobMover movingLogic;
    private TeamCode teamCode;

    private float hitPoint;

    public MobBase(Scene scene, String name, FileHandle texture, PhysicsMode physicsMode, float moveSpeed, float hitPoint, TeamCode teamCode) {
        super(scene, name, texture, physicsMode);
        movingLogic = new SimpleMobMover(moveSpeed);
        attachScript(movingLogic);
        this.hitPoint = hitPoint;
        maxHitPoint = hitPoint;
        this.teamCode = teamCode;
    }

    public TeamCode getTeamCode(){
        return teamCode;
    }
    public float getHitPoint(){
        return hitPoint;
    }

    /**
     * ダメージを受け取る。 今回の攻撃でヒットポイントが０以下になったらtrueを返す。
     * @param amount ダメージ量。負数を指定すれば回復。
     * @return 今回の攻撃でヒットポイントが０以下になったらtrueを返す。
     */
    public boolean receiveDamage(float amount){
        hitPoint -= amount;
        if (hitPoint < 0){
            hitPoint = 0;
        }
        if (hitPoint > maxHitPoint) hitPoint = maxHitPoint;
        return hitPoint <= 0;
    }
}
