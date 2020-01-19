package jp.ac.uryukyu.ie.e195723.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import jp.ac.uryukyu.ie.e195723.GameMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;//720p
		config.height = 720;
		config.title = "Tower Defense Game 2D";
		new LwjglApplication(new GameMain(), config){
			@Override
			public void exit()
			{
				super.exit();
				getApplicationListener().dispose();
			}
		};
	}
}
