package jp.ac.uryukyu.ie.e195723.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import jp.ac.uryukyu.ie.e195723.engine.example.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Engine2D Sample";
		new LwjglApplication(new MyGdxGame(), config){
			@Override
			public void exit()
			{
				super.exit();
				getApplicationListener().dispose();
			}
		};
	}
}
