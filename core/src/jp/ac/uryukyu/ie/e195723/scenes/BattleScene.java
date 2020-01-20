package jp.ac.uryukyu.ie.e195723.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import jp.ac.uryukyu.ie.e195723.SimpleBlock;
import jp.ac.uryukyu.ie.e195723.engine.Scene;
import jp.ac.uryukyu.ie.e195723.mobs.Zombie;

import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.BLOCK_BASE_SIZE;
import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.getPosFromBlockPos;

public class BattleScene extends Scene {
    private Texture backgroundTex;

    public BattleScene(){
        useCollisionDebugLine = true;
    }

    public void LoadSample(){
        backgroundTex = new Texture(Gdx.files.internal("img/stage/back01.png"));
        for (int i=0; i < (Gdx.graphics.getWidth()/ BLOCK_BASE_SIZE); i++){
            new SimpleBlock(this, "floor_block", Gdx.files.internal("img/stage/stone_t.png"), 5000, new Vector2(1, 1), getPosFromBlockPos(new Vector2(i, 0)));
        }
        new SimpleBlock(this, "floor_block", Gdx.files.internal("img/stage/iron2.png"), 10, new Vector2(1, 1), getPosFromBlockPos(new Vector2(4, 1)));
        new Zombie(this, new Vector2(0, BLOCK_BASE_SIZE));
        //new Zombie(this, new Vector2(-5, BLOCK_BASE_SIZE));
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
