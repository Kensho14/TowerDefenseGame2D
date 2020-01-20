package jp.ac.uryukyu.ie.e195723.mobs;

import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;

public class SimpleMobMover implements IGameScript {
    private float moveSpeed;
    private boolean isTouched;

    public SimpleMobMover(float moveSpeed){
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void start(GameObject gameObject) {

    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if (isTouched) return;
        gameObject.setPosition(gameObject.getX()+moveSpeed * delta, gameObject.getY());
    }

    @Override
    public void destroy(GameObject gameObject) {

    }

    @Override
    public void onCollisionEnter(GameObject gameObject, GameObject target) {
        isTouched = true;
    }

    @Override
    public void onCollision(GameObject gameObject, GameObject target) {

    }

    @Override
    public void onCollisionExit(GameObject gameObject, GameObject target) {
        isTouched = false;
    }
}
