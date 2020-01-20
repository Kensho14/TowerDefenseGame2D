package jp.ac.uryukyu.ie.e195723.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import jp.ac.uryukyu.ie.e195723.SimpleBlock;
import jp.ac.uryukyu.ie.e195723.engine.Scene;
import jp.ac.uryukyu.ie.e195723.mobs.Zombie;
import jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil;

import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.BLOCK_BASE_SIZE;
import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.getPosFromBlockPos;
import static jp.ac.uryukyu.ie.e195723.utils.FormatUtils.getFormattedTimeText;

public class BattleScene extends Scene {
    private Texture backgroundTex;
    private Music backgroundMusic;

    private Skin uiSkin;
    private Table uiTable;

    private Label timerLabel;
    private Label moneyLabel;

    private int moneyAmount;
    private long remainingTime;//ms
    private long lastUpdateTime;//ms

    public BattleScene(){
        useCollisionDebugLine = true;
    }

    void setupBackground(){
        backgroundTex = new Texture(Gdx.files.internal("img/stage/back01.png"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/The-Nine.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    void setupUI(){
        uiSkin = new Skin(Gdx.files.internal("skins/testskin/assets/uiskin.json"));
        uiTable = new Table();
        uiTable.setFillParent(true);
        addActor(uiTable);
        uiTable.setDebug(useCollisionDebugLine);

        uiTable.left().top();
        timerLabel = new Label("RemainingTime: "+getFormattedTimeText(remainingTime), uiSkin);
        uiTable.add(timerLabel).left();
        uiTable.row();
        moneyLabel = new Label("Money: "+moneyAmount, uiSkin);
        uiTable.add(moneyLabel).left();
    }

    void setupInputProc(){
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchUp (int x, int y, int pointer, int button){
                Vector3 touchPos = new Vector3(x, y, 0);
                getCamera().unproject(touchPos);
                Vector2 blockPos = BlockPosUtil.getBlockPos(new Vector2(touchPos.x, touchPos.y));
                System.out.println("touch block: "+blockPos.x+", "+blockPos.y);
                return true;
            }
        });
    }

    public void loadSample(){
        lastUpdateTime = TimeUtils.millis();
        remainingTime = 3*60*1000;
        moneyAmount = 500;
        setupUI();
        setupBackground();
        for (int i=0; i < (Gdx.graphics.getWidth()/ BLOCK_BASE_SIZE); i++){
            new SimpleBlock(this, "floor_block", Gdx.files.internal("img/stage/stone_t.png"), 5000, new Vector2(1, 1), getPosFromBlockPos(new Vector2(i, 0)));
        }
        new SimpleBlock(this, "floor_block", Gdx.files.internal("img/stage/iron2.png"), 10, new Vector2(1, 1), getPosFromBlockPos(new Vector2(4, 1)));
        new Zombie(this, new Vector2(0, BLOCK_BASE_SIZE));
        //new Zombie(this, new Vector2(-5, BLOCK_BASE_SIZE));

        setupInputProc();
    }

    public void enemyScored(){
        pause();
        System.out.println("Game over!");
    }

    @Override
    public void draw(){
        if (backgroundTex != null){
            getBatch().begin();
            getBatch().draw(backgroundTex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            getBatch().end();
        }
        super.draw();
        remainingTime -= TimeUtils.timeSinceMillis(lastUpdateTime);
        lastUpdateTime = TimeUtils.millis();
        timerLabel.setText("RemainingTime: "+getFormattedTimeText(remainingTime));
    }
}
