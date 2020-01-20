package jp.ac.uryukyu.ie.e195723.mobs.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import jp.ac.uryukyu.ie.e195723.StageObject;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;
import jp.ac.uryukyu.ie.e195723.mobs.MobBase;

public class SimpleSoldierAI implements IGameScript {
    private long attackCoolTime;
    private float attackPoint;
    private Sound attackSound;

    private long lastAttackTime;

    public SimpleSoldierAI(float attackPoint, long attackCoolTime){
        this.attackCoolTime = TimeUtils.millisToNanos(attackCoolTime);
        lastAttackTime = TimeUtils.nanoTime();
        this.attackPoint = attackPoint;
        attackSound = Gdx.audio.newSound(Gdx.files.internal("sound/machinegun-firing1.mp3"));
    }

    public void attack(StageObject soldier, StageObject target){
        target.receiveDamage(attackPoint, soldier);
        attackSound.play();
    }

    @Override
    public void start(GameObject gameObject) {

    }

    @Override
    public void update(GameObject gameObject, float delta) {

    }

    @Override
    public void destroy(GameObject gameObject) {

    }

    @Override
    public void onCollisionEnter(GameObject gameObject, GameObject target) {

    }

    @Override
    public void onCollision(GameObject gameObject, GameObject target) {
        if (!(target instanceof StageObject)) return;
        StageObject.TeamCode targetTeam = ((StageObject)target).getTeamCode();
        if (targetTeam == StageObject.TeamCode.Enemy){
            ((MobBase)gameObject).getMovingLogic().setOnStay(true);
            if (TimeUtils.timeSinceNanos(lastAttackTime) > attackCoolTime){
                attack(((StageObject)gameObject), ((StageObject)target));
                lastAttackTime = TimeUtils.nanoTime();
            }
        }
    }

    @Override
    public void onCollisionExit(GameObject gameObject, GameObject target) {

    }
}
