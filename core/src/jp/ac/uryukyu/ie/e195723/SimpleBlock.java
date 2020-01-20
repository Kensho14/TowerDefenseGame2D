package jp.ac.uryukyu.ie.e195723;

import com.badlogic.gdx.files.FileHandle;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

public class SimpleBlock extends GameObject {
    public SimpleBlock(Scene scene, String name, FileHandle texture) {
        super(scene, name, texture, PhysicsMode.Passive);
    }
}
