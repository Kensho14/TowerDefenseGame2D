package jp.ac.uryukyu.ie.e195723.mobs.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import jp.ac.uryukyu.ie.e195723.StageObject;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;
import jp.ac.uryukyu.ie.e195723.scenes.BattleScene;

public class ZombieSimpleAI implements IGameScript {
    private long attackCoolTime;
    private float attackPoint;
    private Sound attackSound;
    private Sound barkVoice;

    private long lastAttackTime;
    private long lastBarkTime;

    /**
     * コンストラクタ
     * @param attackCoolTime 攻撃のクールタイム(ms)
     */
    public ZombieSimpleAI(long attackCoolTime, float attackPoint){
        this.attackCoolTime = TimeUtils.millisToNanos(attackCoolTime);
        lastAttackTime = TimeUtils.nanoTime();
        this.attackPoint = attackPoint;
        attackSound = Gdx.audio.newSound(Gdx.files.internal("sound/zombie-meal1.mp3"));
        barkVoice = Gdx.audio.newSound(Gdx.files.internal("sound/zombie-voice1.mp3"));
    }

    public void attack(StageObject zombie, StageObject target){
        target.receiveDamage(attackPoint, zombie);
        attackSound.play();
    }

    @Override
    public void start(GameObject gameObject) {

    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if (gameObject.getX() > Gdx.graphics.getWidth()){
            ((BattleScene)gameObject.getStage()).enemyScored();
        }
        if (TimeUtils.timeSinceNanos(lastBarkTime) > 15*TimeUtils.millisToNanos(1000)){
            barkVoice.play();
            lastBarkTime = TimeUtils.nanoTime();
        }
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
        if (targetTeam == StageObject.TeamCode.Object || targetTeam == StageObject.TeamCode.Defense){
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
