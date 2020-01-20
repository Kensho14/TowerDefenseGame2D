package jp.ac.uryukyu.ie.e195723.engine;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Groupを包み込んで利便性を高めたゲーム内でのオブジェクトの基底クラス
 */
public class GameObject extends Group {
    private Texture texture;
    private TextureRegion textureRegion;
    private List<IGameScript> attachedScriptList;
    private boolean isPaused;
    private PhysicsMode physicsMode;
    private Rectangle collisionRectangle;
    private Scene belongedScene;

    public Set<GameObject> collidingGameObjects;

    /**
     * 衝突判定時の振る舞い
     */
    public enum PhysicsMode{
        Active,//相手との衝突判定を行う
        Passive,//自分から判定はしないが，当たる。
        None//衝突判定で完全に無視される。
    }

    /**
     * コンストラクタ
     * @param scene オブジェクトが所属するScene
     * @param name オブジェクトの名前（重複可能）
     * @param texture textureのFileHandle
     * @param physicsMode 衝突判定時の振る舞い
     */
    public GameObject(Scene scene, String name, FileHandle texture, PhysicsMode physicsMode){
        isPaused = true;
        setName(name);
        if (texture != null){
            this.texture = new Texture(texture);
            textureRegion = new TextureRegion(this.texture, this.texture.getWidth(), this.texture.getHeight());
        }
        attachedScriptList = new ArrayList<IGameScript>();
        collidingGameObjects = new HashSet<>();
        scene.addGameObject(this);
        setSize(this.texture.getWidth(), this.texture.getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());
        collisionRectangle = new Rectangle(0, 0, getWidth(), getHeight());
        this.physicsMode = physicsMode;
        belongedScene = scene;
    }
    public GameObject(Scene scene, String name, FileHandle texture){
        this(scene, name, texture, PhysicsMode.None);
    }

    /**
     * Sceneから呼ばれる
     */
    public void start(){
        isPaused = false;
        for (IGameScript script : attachedScriptList){
            script.start(this);
        }
    }

    /**
     * Sceneから呼ばれる
     */
    public void pause(){
        isPaused = true;
    }

    /**
     * Sceneから呼ばれる
     */
    public void resume(){
        isPaused = false;
    }

    /**
     * Sceneから呼ばれる
     */
    public void dispose(){
        for (IGameScript script : attachedScriptList){
            script.destroy(this);
        }
        texture.dispose();
    }

    /**
     * textureを返す
     * @return Texture
     */
    public Texture getTexture(){
        return texture;
    }

    /**
     * PhysicsModeを設定
     * @param mode physicsMode
     */
    public void setPhysicsMode(PhysicsMode mode){
        this.physicsMode = mode;
    }

    /**
     * physicsModeを取得
     * @return
     */
    public PhysicsMode getPhysicsMode(){
        return physicsMode;
    }

    /**
     * 当たり判定用のRectangleを返す。
     * @return rectangle
     */
    public Rectangle getCollisionRectangle(){
        return new Rectangle(getX()+collisionRectangle.x, getY()+collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
    }

    /**
     * 当たり判定用のボックスの位置，サイズを設定する。
     * @param rect x,yは基準点からのズレ。width,heightはサイズ。
     */
    public void setCollisionRectangleSize(Rectangle rect){
        collisionRectangle = rect;
    }

    /**
     * サイズを変更する。
     * setSize, setBounds, setCollisionRectangleSize をひとまとめに実行するメソッド。
     * @param width width
     * @param height height
     */
    public void changeSize(float width, float height){
        setSize(width, height);
        setBounds(getX(), getY(), getWidth(), getHeight());
        setCollisionRectangleSize(new Rectangle(0, 0, getWidth(), getHeight()));
    }

    /**
     * IGameScriptを実装したクラス（スクリプト）をアタッチする
     * @param script IGameScriptを実装したクラス
     */
    public void attachScript(IGameScript script){
        attachedScriptList.add(script);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if (texture == null) return;
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta){
        if (isPaused) return;
        super.act(delta);
        for (IGameScript script : attachedScriptList){
            script.update(this, delta);
        }
    }

    @Override
    public boolean remove(){
        belongedScene.removeGameObject(this);
        dispose();
        return super.remove();
    }

    /**
     * Sceneから呼ばれる
     * @param target target
     */
    public void onCollisionEnter(GameObject target){
        for (IGameScript script : attachedScriptList){
            script.onCollisionEnter(this, target);
        }
    }

    /**
     * Sceneから呼ばれる
     * @param target target
     */
    public void onCollision(GameObject target){
        for (IGameScript script : attachedScriptList){
            script.onCollision(this, target);
        }
    }

    /**
     * Sceneから呼ばれる
     * @param target target
     */
    public void onCollisionExit(GameObject target){
        for (IGameScript script : attachedScriptList){
            script.onCollisionExit(this, target);
        }
    }
}
