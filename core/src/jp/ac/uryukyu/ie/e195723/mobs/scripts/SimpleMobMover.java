package jp.ac.uryukyu.ie.e195723.mobs.scripts;

import jp.ac.uryukyu.ie.e195723.StageObject;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;

/**
 * Mobを一定速度で移動させるスクリプト。
 * TeamCode.Objectに当たるか，もしくはonStayがtrueのときには一時停止する。
 */
public class SimpleMobMover implements IGameScript {
    private float moveSpeed;
    private boolean isTouched;
    private boolean onStay;

    public SimpleMobMover(float moveSpeed){
        this.moveSpeed = moveSpeed;
    }

    public void setOnStay(boolean onStay){
        this.onStay = onStay;
    }
    public boolean getOnStay(){
        return onStay;
    }

    @Override
    public void start(GameObject gameObject) {

    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if (!isTouched && !onStay){
            gameObject.setPosition(gameObject.getX()+moveSpeed * delta, gameObject.getY());
        }
        isTouched = false;
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
        if (((StageObject)target).getTeamCode() == StageObject.TeamCode.Object) isTouched = true;
    }

    @Override
    public void onCollisionExit(GameObject gameObject, GameObject target) {
        if (!(target instanceof StageObject)) return;
        if (((StageObject)target).getTeamCode() == StageObject.TeamCode.Object){
            isTouched = false;
        }
    }
}
