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
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.Scene;
import jp.ac.uryukyu.ie.e195723.mobs.MobBase;
import jp.ac.uryukyu.ie.e195723.mobs.SimpleSoldier;
import jp.ac.uryukyu.ie.e195723.mobs.Zombie;
import jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.BLOCK_BASE_SIZE;
import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.getPosFromBlockPos;
import static jp.ac.uryukyu.ie.e195723.utils.FormatUtils.getFormattedTimeText;

public class BattleScene extends Scene {
    private Texture backgroundTex;
    private Music backgroundMusic;

    private Skin uiSkin;
    private Table uiTable;
    private Table inventoryUiTable;
    private Container<Label> gameFinishScreen;
    private Label gameFinishLabel;

    private Label timerLabel;
    private Label moneyLabel;

    private int moneyAmount;
    private long remainingTime;//ms
    private long lastUpdateTime;//ms
    private long lastEnemySpawnTime;//ms
    private List<UnitDataTimePair> enemySpawnQue;

    private List<UnitData> inventoryData;
    private UnitData selectedUnitData;
    private Texture selectedUnitIcon;

    private List<MobBase> enemies;
    private boolean isPaused;

    private Map<String, UnitData> unitDataBank;

    public class UnitDataTimePair{
        public UnitData unitData;
        public long milliseconds;

        public UnitDataTimePair(UnitData unitData, long milliseconds){
            this.unitData = unitData;
            this.milliseconds = milliseconds;
        }
    }

    public BattleScene(){
        //TODO:余裕があったらハードコーディングを直して，jsonから読み込む
        unitDataBank = new HashMap<>();
        unitDataBank.put("drum_can", new UnitData("drum_can", 20, "Drum", "img/stage/drum_can.png"));
        unitDataBank.put("iron2", new UnitData("iron2", 50, "IronBlock", "img/stage/iron2.png"));
        unitDataBank.put("soldier1", new UnitData("soldier1", 100, "Soldier", "img/characters/soldier_chara_33.png"));
        unitDataBank.put("zombie1", new UnitData("zombie1", 10, "Zombie", "img/characters/zombie_chara_16.png"));

        useCollisionDebugLine = false;
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
            enemies.add(new Zombie(this, getPosFromBlockPos(blockPosition)));
        }
    }

    private void finishGame(boolean isVictory){
        pause();
        System.out.println("game finish: "+isVictory);
        if (isVictory){
            gameFinishLabel.setText("You Win!");
        }else{
            gameFinishLabel.setText("Game Over!");
        }
        gameFinishLabel.setVisible(true);
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

        gameFinishLabel = new Label("Game Over", uiSkin);
        gameFinishLabel.setVisible(false);
        gameFinishScreen = new Container<>(gameFinishLabel);
        gameFinishScreen.setFillParent(true);
        addActor(gameFinishScreen);
    }

    public void loadSample(){
        //本来はjson等から動的に読み込むが，ここではハードコーディング
        lastUpdateTime = TimeUtils.millis();
        lastEnemySpawnTime = TimeUtils.millis();
        remainingTime = 3*60*1000;
        moneyAmount = 250;
        inventoryData = new ArrayList<>();
        inventoryData.add(unitDataBank.get("drum_can"));
        inventoryData.add(unitDataBank.get("iron2"));
        inventoryData.add(unitDataBank.get("soldier1"));
        enemies = new ArrayList<>();
        enemySpawnQue = new ArrayList<>();
        setupUI();
        setupBackground();
        //床を設置
        for (int i=0; i < (Gdx.graphics.getWidth()/ BLOCK_BASE_SIZE); i++){
            new SimpleBlock(this, "floor_block", Gdx.files.internal("img/stage/stone_t.png"), 5000, new Vector2(1, 1), getPosFromBlockPos(new Vector2(i, 0)));
        }
        //キューに登録 (10秒ごとにゾンビをスポーン)
        enemySpawnQue.add(new UnitDataTimePair(unitDataBank.get("zombie1"), 100));
        enemySpawnQue.add(new UnitDataTimePair(unitDataBank.get("zombie1"), 10000));
        enemySpawnQue.add(new UnitDataTimePair(unitDataBank.get("zombie1"), 10000));
    }

    /**
     * Enemyが右端に到達した際にEnemyから呼ばれる
     */
    public void enemyScored(){
        finishGame(false);
    }

    /**
     * すべての敵mobの配列を返す
     * @return 敵Mobの配列
     */
    public MobBase[] getAllEnemies(){
        return enemies.toArray(new MobBase[enemies.size()]);
    }

    @Override
    public void draw(){
        if (backgroundTex != null){
            getBatch().begin();
            getBatch().draw(backgroundTex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            getBatch().end();
        }
        super.draw();
        if (!isPaused){
            remainingTime -= TimeUtils.timeSinceMillis(lastUpdateTime);
            lastUpdateTime = TimeUtils.millis();
            timerLabel.setText("RemainingTime: "+getFormattedTimeText(remainingTime));
            if (remainingTime <= 0){
                finishGame(true);
            }

            if (enemySpawnQue.size() > 0 && TimeUtils.timeSinceMillis(lastEnemySpawnTime) >= enemySpawnQue.get(0).milliseconds){
                //spawn enemy
                lastEnemySpawnTime = TimeUtils.millis();
                spawnUnit(enemySpawnQue.remove(0).unitData, new Vector2(-1,1));
            }
        }
        if (selectedUnitIcon != null){
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            getCamera().unproject(mousePos);
            getBatch().begin();
            getBatch().draw(selectedUnitIcon, mousePos.x, mousePos.y);
            getBatch().end();
        }
    }

    @Override
    public void pause(){
        super.pause();
        isPaused = true;
    }

    @Override
    public void resume(){
        super.resume();
        isPaused = false;
    }

    @Override
    public void removeGameObject(GameObject gameObject){
        super.removeGameObject(gameObject);
        if (enemies.contains(gameObject)){
            System.out.println("remove enemy");
            enemies.remove(gameObject);
            if (enemies.size() == 0 && enemySpawnQue.size() == 0){
                finishGame(true);
            }
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
