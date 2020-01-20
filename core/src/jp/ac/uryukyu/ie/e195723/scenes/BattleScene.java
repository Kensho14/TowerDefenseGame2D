package jp.ac.uryukyu.ie.e195723.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import jp.ac.uryukyu.ie.e195723.engine.Scene;
import jp.ac.uryukyu.ie.e195723.mobs.Zombie;

public class BattleScene extends Scene {
    private Texture backgroundTex;

    public BattleScene(){
        useCollisionDebugLine = true;
    }

    public void LoadSample(){
        backgroundTex = new Texture(Gdx.files.internal("img/stage/back01.png"));
        new Zombie(this, new Vector2(0, 0));
        new Zombie(this, new Vector2(-5, 0));
    }

    @Override
    public void draw(){
        if (backgroundTex != null){
            getBatch().begin();
            getBatch().draw(backgroundTex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            getBatch().end();
        }
        super.draw();
    }
}
