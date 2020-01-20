package jp.ac.uryukyu.ie.e195723;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

import static jp.ac.uryukyu.ie.e195723.utils.BlockPosUtil.BLOCK_BASE_SIZE;

/**
 * 機能のないブロック
 */
public class SimpleBlock extends StageObject {
    /**
     * コンストラクタ
     * @param scene 所属するシーン
     * @param name 名前
     * @param texture TextureのFileHandle
     * @param hitPoint 耐久値
     * @param blockSize ブロックのサイズ。整数値。基本はVector2(1, 1)。縦長なブロックなどでカスタム。
     * @param position 配置位置
     */
    public SimpleBlock(Scene scene, String name, FileHandle texture, float hitPoint, Vector2 blockSize, Vector2 position) {
        super(scene, name, texture, PhysicsMode.Passive, hitPoint, TeamCode.Object);
        changeSize(BLOCK_BASE_SIZE*blockSize.x, BLOCK_BASE_SIZE*blockSize.y);
        setPosition(position.x, position.y);
    }

    @Override
    public boolean receiveDamage(float amount, StageObject attacker){
        System.out.println("damaged! HP:"+getHitPoint());
        return super.receiveDamage(amount, attacker);
    }

    @Override
    public void onDeath(StageObject attacker) {
        remove();
    }
}
