package jp.ac.uryukyu.ie.e195723;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import jp.ac.uryukyu.ie.e195723.scenes.BattleScene;

public class GameMain extends ApplicationAdapter {
	private BattleScene currentScene;

	@Override
	public void create () {
		currentScene = new BattleScene();
		currentScene.loadSample();
		currentScene.start();
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(137, 189, 222, 1);//空色?
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		currentScene.act(Gdx.graphics.getDeltaTime());
		currentScene.draw();
	}

	@Override
	public void dispose () {
		super.dispose();
		currentScene.dispose();
	}

	@Override
	public void pause(){
		currentScene.pause();
	}

	@Override
	public void resume(){
		currentScene.resume();
	}
}
