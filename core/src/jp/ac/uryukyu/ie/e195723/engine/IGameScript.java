package jp.ac.uryukyu.ie.e195723.engine;

public interface IGameScript {
    void start(GameObject gameObject);
    void update(GameObject gameObject, float delta);
    void destroy(GameObject gameObject);
    void onCollisionEnter(GameObject gameObject, GameObject target);
    void onCollision(GameObject gameObject, GameObject target);
    void onCollisionExit(GameObject gameObject, GameObject target);
}
