package jp.ac.uryukyu.ie.e195723.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * 座標計算のユーティリティ
 */
public class BlockPosUtil {
    public static final int BLOCK_BASE_SIZE = 32;

    /**
     * シーンでの座標をブロックに換算した，ブロックでの整数座標を返す
     * @param position 変換する座標
     * @return ブロックでの整数座標
     */
    public static Vector2 getBlockPos(Vector2 position){
        float xTemp = MathUtils.floor(position.x/BLOCK_BASE_SIZE);
        float yTemp = MathUtils.floor(position.y/BLOCK_BASE_SIZE);
        return new Vector2(xTemp, yTemp);
    }

    /**
     * ブロック座標を通常座標へ変換する
     * @param blockPos ブロック座標
     * @return 通常座標
     */
    public static Vector2 getPosFromBlockPos(Vector2 blockPos){
        return new Vector2(blockPos.x*BLOCK_BASE_SIZE, blockPos.y*BLOCK_BASE_SIZE);
    }
}
