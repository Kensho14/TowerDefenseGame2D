package jp.ac.uryukyu.ie.e195723;

import com.badlogic.gdx.files.FileHandle;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

/**
 * hitPointを持つ，mob,objectに共通する基底クラス
 */
public abstract class StageObject extends GameObject {
    public enum TeamCode{
        Defense,
        Enemy,
        Object
    }

    private float maxHitPoint;
    private TeamCode teamCode;

    private float hitPoint;

    public StageObject(Scene scene, String name, FileHandle texture, PhysicsMode physicsMode, float hitPoint, TeamCode teamCode) {
        super(scene, name, texture, physicsMode);
        this.hitPoint = hitPoint;
        maxHitPoint = hitPoint;
        this.teamCode = teamCode;
    }

    /**
     * TeamCodeを返す
     * @return TeamCode
     */
    public TeamCode getTeamCode(){
        return teamCode;
    }

    /**
     * hitPointを返す
     * @return hitPoint
     */
    public float getHitPoint(){
        return hitPoint;
    }

    /**
     * ダメージを受け取る。 今回の攻撃でヒットポイントが０以下になったらtrueを返す。
     * @param amount ダメージ量。負数を指定すれば回復。
     * @param attacker 攻撃者。または，直接的な原因になったStageObject。
     * @return 今回の攻撃でヒットポイントが０以下になったらtrueを返す。
     */
    public boolean receiveDamage(float amount, StageObject attacker){
        hitPoint -= amount;
        if (hitPoint <= 0){
            hitPoint = 0;
            onDeath(attacker);
        }
        if (hitPoint > maxHitPoint) hitPoint = maxHitPoint;
        return hitPoint <= 0;
    }

    /**
     * hitPointが0になったときに呼び出される
     * @param attacker 原因を作ったStageObject
     */
    public abstract void onDeath(StageObject attacker);
}
