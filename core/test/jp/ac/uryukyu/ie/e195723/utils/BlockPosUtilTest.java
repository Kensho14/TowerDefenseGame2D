package jp.ac.uryukyu.ie.e195723.utils;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockPosUtilTest {

    @Test
    public void getBlockPos() {
        assertEquals(new Vector2(0, 0), BlockPosUtil.getBlockPos(new Vector2(BlockPosUtil.BLOCK_BASE_SIZE/2, BlockPosUtil.BLOCK_BASE_SIZE/2)));
    }

    @Test
    public void getPosFromBlockPos() {
        assertEquals(new Vector2(BlockPosUtil.BLOCK_BASE_SIZE, BlockPosUtil.BLOCK_BASE_SIZE), BlockPosUtil.getPosFromBlockPos(new Vector2(1, 1)));
    }
}