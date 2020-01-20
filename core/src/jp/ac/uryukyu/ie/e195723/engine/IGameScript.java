package jp.ac.uryukyu.ie.e195723.engine;

/**
 * GameObjectにアタッチするスクリプトのインターフェース
 */
public interface IGameScript {
    /**
     * シーンが開始した際に呼ばれる
     * @param gameObject このスクリプトがアタッチされたGameObject
     */
    void start(GameObject gameObject);

    /**
     * 毎フレームごとに呼ばれる
     * @param gameObject このスクリプトがアタッチされたGameObject
     * @param delta deltaTime
     */
    void update(GameObject gameObject, float delta);

    /**
     * このスクリプトがアタッチされたGameObjectが破棄される際に呼ばれる
     * @param gameObject このスクリプトがアタッチされたGameObject
     */
    void destroy(GameObject gameObject);

    /**
     * 他のオブジェクトと衝突した際にはじめに一回だけ呼ばれる
     * @param gameObject このスクリプトがアタッチされたGameObject
     * @param target 衝突相手
     */
    void onCollisionEnter(GameObject gameObject, GameObject target);

    /**
     * 他のオブジェクトと衝突している間，毎フレーム呼ばれる
     * @param gameObject このスクリプトがアタッチされたGameObject
     * @param target 衝突相手
     */
    void onCollision(GameObject gameObject, GameObject target);

    /**
     * 他のオブジェクトと衝突し，通り抜けた際に呼ばれる
     * @param gameObject このスクリプトがアタッチされたGameObject
     * @param target 衝突相手
     */
    void onCollisionExit(GameObject gameObject, GameObject target);
}
