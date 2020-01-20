package jp.ac.uryukyu.ie.e195723.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import jp.ac.uryukyu.ie.e195723.SimpleBlock;
import jp.ac.uryukyu.ie.e195723.dataObj.UnitData;
import jp.ac.uryukyu.ie.e195723.engine.Scene;
import jp.ac.uryukyu.ie.e195723.mobs.SimpleSoldier;
import jp.ac.uryukyu.ie.e195723.mobs.Zombie;
import jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil;

import java.util.ArrayList;
import java.util.List;

import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.BLOCK_BASE_SIZE;
import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.getPosFromBlockPos;
import static jp.ac.uryukyu.ie.e195723.utils.FormatUtils.getFormattedTimeText;

public class BattleScene extends Scene {
    private Texture backgroundTex;
    private Music backgroundMusic;

    private Skin uiSkin;
    private Table uiTable;
    private Table inventoryUiTable;

    private Label timerLabel;
    private Label moneyLabel;

    private int moneyAmount;
    private long remainingTime;//ms
    private long lastUpdateTime;//ms

    private List<UnitData> inventoryData;
    private UnitData selectedUnitData;
    private Texture selectedUnitIcon;

    public BattleScene(){
        useCollisionDebugLine = true;
        Gdx.input.setInputProcessor(this);
    }

    /**
     * お金の使用を試みる。　成功すればtrueが返る。
     * @param amount 金額
     * @return 成否
     */
    public boolean useMoney(int amount){
        if (moneyAmount >= amount){
            moneyAmount -= amount;
            moneyLabel.setText("Money: "+moneyAmount);
            return true;
        }else{
            return false;
        }
    }

    public void spawnUnit(UnitData unitData, Vector2 blockPosition){
        //TODO:余裕があったらハードコーディングを直して，jsonから読み込む
        if (unitData.id.equalsIgnoreCase("iron2")){
            new SimpleBlock(this, "iron2", Gdx.files.internal("img/stage/iron2.png"),
                    15, new Vector2(1, 1), getPosFromBlockPos(blockPosition));
        }
        if (unitData.id.equalsIgnoreCase("drum_can")){
            new SimpleBlock(this, "drum_can", Gdx.files.internal("img/stage/drum_can.png"),
                    9, new Vector2(1, 2), getPosFromBlockPos(blockPosition));
        }
        if (unitData.id.equalsIgnoreCase("soldier1")){
            new SimpleSoldier(this, getPosFromBlockPos(blockPosition));
        }
        if (unitData.id.equalsIgnoreCase("zombie1")){
            new Zombie(this, getPosFromBlockPos(blockPosition));
        }
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
        uiTable.setDebug(false);

        uiTable.left().top();
        timerLabel = new Label("RemainingTime: "+getFormattedTimeText(remainingTime), uiSkin);
        uiTable.add(timerLabel).left();
        uiTable.row();
        moneyLabel = new Label("Money: "+moneyAmount, uiSkin);
        uiTable.add(moneyLabel).left();

        inventoryUiTable = new Table();
        inventoryUiTable.setFillParent(true);
        addActor(inventoryUiTable);
        HorizontalGroup hGroup = new HorizontalGroup();
        for (final UnitData unitData : inventoryData){
            TextButton button = new TextButton(unitData.displayName+"\ncost:"+unitData.spawnCost, uiSkin);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println("select unit: "+unitData.id);
                    if (useMoney(unitData.spawnCost)){
                        selectedUnitData = unitData;
                        selectedUnitIcon = new Texture(Gdx.files.internal(unitData.iconPath));
                    }
                }
            });
            hGroup.addActor(button);
        }
        inventoryUiTable.right().top();
        inventoryUiTable.add(new Label("inventory", uiSkin));
        inventoryUiTable.row();
        ScrollPane scrollPane = new ScrollPane(hGroup, uiSkin);
        inventoryUiTable.add(scrollPane);
    }

    public void loadSample(){
        lastUpdateTime = TimeUtils.millis();
        remainingTime = 3*60*1000;
        moneyAmount = 500;
        inventoryData = new ArrayList<>();
        inventoryData.add(new UnitData("iron2", 50, "Iron", "img/stage/iron2.png"));
        inventoryData.add(new UnitData("drum_can", 20, "Drum can", "img/stage/drum_can.png"));
        inventoryData.add(new UnitData("soldier1", 100, "Soldier", "img/characters/soldier_chara_33.png"));
        setupUI();
        setupBackground();
        for (int i=0; i < (Gdx.graphics.getWidth()/ BLOCK_BASE_SIZE); i++){
            new SimpleBlock(this, "floor_block", Gdx.files.internal("img/stage/stone_t.png"), 5000, new Vector2(1, 1), getPosFromBlockPos(new Vector2(i, 0)));
        }
        new SimpleBlock(this, "floor_block", Gdx.files.internal("img/stage/iron2.png"), 10, new Vector2(1, 1), getPosFromBlockPos(new Vector2(4, 1)));
        spawnUnit(new UnitData("zombie1", 10, "Zombie", "img/characters/zombie_chara_33.png"), new Vector2(-1,1));
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
        if (selectedUnitIcon != null){
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            getCamera().unproject(mousePos);
            getBatch().begin();
            getBatch().draw(selectedUnitIcon, mousePos.x, mousePos.y);
            getBatch().end();
        }
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button){
        boolean temp = super.touchUp(x,y,pointer,button);
        if (!temp && selectedUnitData != null){
            Vector3 touchPos = new Vector3(x, y, 0);
            getCamera().unproject(touchPos);
            Vector2 blockPos = BlockPosUtil.getBlockPos(new Vector2(touchPos.x, touchPos.y));
            System.out.println("touch block: "+blockPos.x+", "+blockPos.y);
            //spawn
            spawnUnit(selectedUnitData, blockPos);
            selectedUnitData = null;
            selectedUnitIcon.dispose();
            selectedUnitIcon = null;
        }
        return temp;
    }
}
