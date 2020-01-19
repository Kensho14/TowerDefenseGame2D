package jp.ac.uryukyu.ie.e195723.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;

public class Scene extends Stage {
    private List<GameObject> gameObjects;
    private boolean isPaused;
    private ShapeRenderer debugRender;
    private List<GameObject> gameObjectToRemove;

    public boolean useCollisionDebugLine;

    public Scene(Viewport viewport){
        super(viewport);
        gameObjects = new ArrayList<>();
        gameObjectToRemove = new ArrayList<>();
        debugRender = new ShapeRenderer();
        useCollisionDebugLine = false;
    }
    public Scene(){
        this(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    }

    public void addGameObject(GameObject gameObject){
        gameObjects.add(gameObject);
        addActor(gameObject);
        if (!isPaused) gameObject.start();
    }
    public void removeGameObject(GameObject gameObject){
        gameObjectToRemove.add(gameObject);
    }

    public void start(){
        for (GameObject gameObject : gameObjects){
            gameObject.start();
        }
    }
    public void pause(){
        isPaused = true;
        for (GameObject gameObject : gameObjects){
            gameObject.pause();
        }
    }
    public void resume(){
        isPaused = false;
        for (GameObject gameObject : gameObjects){
            gameObject.resume();
        }
    }

    @Override
    public void dispose(){
        super.dispose();
        for (GameObject gameObject : gameObjects){
            gameObject.dispose();
        }
    }

    @Override
    public void draw(){
        for (GameObject gameObject : gameObjectToRemove){
            gameObjects.remove(gameObject);
        }
        gameObjectToRemove.clear();
        checkCollisions();
        super.draw();
        if (useCollisionDebugLine){
            debugRender.begin(ShapeRenderer.ShapeType.Line);
            for (GameObject gameObject : gameObjects){
                if (gameObject.getPhysicsMode() == GameObject.PhysicsMode.None) continue;
                Rectangle rectangle = gameObject.getCollisionRectangle();
                debugRender.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
            debugRender.end();
        }
    }

    private void checkCollisions(){
        float margin = 10;
        for (GameObject gameObject : gameObjects){
            if (gameObject.getPhysicsMode() != GameObject.PhysicsMode.Active) continue;
            for (GameObject target : gameObjects){
                if (target.getPhysicsMode() == GameObject.PhysicsMode.None) continue;
                Rectangle targetRect = target.getCollisionRectangle();
                Rectangle gameObjectRect = gameObject.getCollisionRectangle();
                if (targetRect.x-gameObjectRect.x > targetRect.width/2+gameObjectRect.width/2+margin) continue;//絶対当たらなそうな位置にいる場合
                if (gameObjectRect.overlaps(targetRect)){
                    gameObject.onCollision(target);
                    target.onCollision(gameObject);
                    if (!gameObject.collidingGameObjects.contains(target)){
                        gameObject.collidingGameObjects.add(target);
                        gameObject.onCollisionEnter(target);
                        target.onCollisionEnter(gameObject);
                    }
                }else if (gameObject.collidingGameObjects.contains(target)){
                    gameObject.collidingGameObjects.remove(target);
                    gameObject.onCollisionExit(target);
                    target.onCollisionExit(gameObject);
                }
            }
        }
    }

}
