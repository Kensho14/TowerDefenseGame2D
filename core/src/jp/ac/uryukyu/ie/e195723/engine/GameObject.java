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

public class GameObject extends Group {
    private Texture texture;
    private TextureRegion textureRegion;
    private List<IGameScript> attachedScriptList;
    private boolean isPaused;
    private PhysicsMode physicsMode;
    private Rectangle collisionRectangle;
    private Scene belongedScene;

    public Set<GameObject> collidingGameObjects;

    public enum PhysicsMode{
        Active,//相手との衝突判定を行う
        Passive,//自分から判定はしないが，当たる。
        None//衝突判定で完全に無視される。
    }

    public GameObject(Scene scene, String name, FileHandle texture, PhysicsMode physicsMode){
        isPaused = true;
        setName(name);
        this.texture = new Texture(texture);
        textureRegion = new TextureRegion(this.texture, this.texture.getWidth(), this.texture.getHeight());
        attachedScriptList = new ArrayList<IGameScript>();
        collidingGameObjects = new HashSet<>();
        scene.addGameObject(this);
        setSize(this.texture.getWidth(), this.texture.getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());
        collisionRectangle = new Rectangle(getX(), getY(), getWidth(), getHeight());
        this.physicsMode = physicsMode;
        belongedScene = scene;
    }
    public GameObject(Scene scene, String name, FileHandle texture){
        this(scene, name, texture, PhysicsMode.None);
    }

    public void start(){
        isPaused = false;
        for (IGameScript script : attachedScriptList){
            script.start(this);
        }
    }

    public void pause(){
        isPaused = true;
    }
    public void resume(){
        isPaused = false;
    }

    public void dispose(){
        for (IGameScript script : attachedScriptList){
            script.destroy(this);
        }
        texture.dispose();
    }

    public Texture getTexture(){
        return texture;
    }

    public void setPhysicsMode(PhysicsMode mode){
        this.physicsMode = mode;
    }
    public PhysicsMode getPhysicsMode(){
        return physicsMode;
    }

    /**
     * 当たり判定用のRectangleを返す。 x,yはStage座標。
     * @return rectangle
     */
    public Rectangle getCollisionRectangle(){
        //Vector2 stagePos = localToStageCoordinates(new Vector2(getX(), getY()));
        collisionRectangle.setPosition(getX(), getY());
        return collisionRectangle;
    }
    public void setCollisionRectangleSize(Vector2 size){
        collisionRectangle.width = size.x;
        collisionRectangle.height = size.y;
    }

    public void attachScript(IGameScript script){
        attachedScriptList.add(script);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
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

    public void onCollisionEnter(GameObject target){
        for (IGameScript script : attachedScriptList){
            script.onCollisionEnter(this, target);
        }
    }
    public void onCollision(GameObject target){
        for (IGameScript script : attachedScriptList){
            script.onCollision(this, target);
        }
    }
    public void onCollisionExit(GameObject target){
        for (IGameScript script : attachedScriptList){
            script.onCollisionExit(this, target);
        }
    }
}
