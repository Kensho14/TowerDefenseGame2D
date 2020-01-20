package jp.ac.uryukyu.ie.e195723;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

public class SimpleBlock extends StageObject {
    public static final int BLOCK_BASE_SIZE = 32;

    public SimpleBlock(Scene scene, String name, FileHandle texture, float hitPoint, Vector2 blockSize, Vector2 position) {
        super(scene, name, texture, PhysicsMode.Passive, hitPoint, TeamCode.Object);
        changeSize(BLOCK_BASE_SIZE*blockSize.x, BLOCK_BASE_SIZE*blockSize.y);
        setPosition(position.x, position.y);
    }

    @Override
    public void onDeath(StageObject attacker) {

    }
}
