package jp.ac.uryukyu.ie.e195723.engine.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;

public class SimpleMoveScript implements IGameScript {
    private int moveSpeed = 10;
    private boolean hasVertical;

    public SimpleMoveScript(int speed, boolean hasVertical){
        this.moveSpeed = speed;
        this.hasVertical = hasVertical;
    }

    @Override
    public void start(GameObject gameObject) {
        System.out.println("SimpleMoveScript: speed is "+moveSpeed);
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        int horizontal = 0;
        int vertical = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontal = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            horizontal = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            vertical = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            vertical = -1;
        }
        if (!hasVertical) vertical = 0;
        gameObject.setPosition(gameObject.getX()+moveSpeed * horizontal * delta, gameObject.getY()+moveSpeed * vertical * delta);
    }

    @Override
    public void destroy(GameObject gameObject) {
        System.out.println("SMS: destroy()");
    }

    @Override
    public void onCollisionEnter(GameObject gameObject, GameObject target) {

    }

    @Override
    public void onCollision(GameObject gameObject, GameObject target) {

    }

    @Override
    public void onCollisionExit(GameObject gameObject, GameObject target) {

    }
}
