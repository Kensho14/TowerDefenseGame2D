package jp.ac.uryukyu.ie.e195723.engine.example;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import jp.ac.uryukyu.ie.e195723.engine.GameObject;
import jp.ac.uryukyu.ie.e195723.engine.IGameScript;
import jp.ac.uryukyu.ie.e195723.engine.Scene;

import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
	private Scene scene;
	private List<GameObject> raindrops;
	private Music mainMusic;
	private Sound dropletSound;
	private long lastDropTime;

	private Skin uiSkin;
	private Table uiTable;
	private Label scoreLabel;

	private int score;
	
	@Override
	public void create () {
		scene = new Scene();
		scene.useCollisionDebugLine = true;
		score = 0;

		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));
		mainMusic.setLooping(true);
		mainMusic.play();
		dropletSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop24.wav"));

		GameObject bucket = new GameObject(scene, "bucket", Gdx.files.internal("bucket.png"), GameObject.PhysicsMode.Active);
		SimpleMoveScript simpleMoveScript = new SimpleMoveScript(200, false);
		bucket.attachScript(simpleMoveScript);

		uiSkin = new Skin(Gdx.files.internal("skins/testskin/assets/uiskin.json"));
		uiTable = new Table();
		uiTable.setFillParent(true);
		scene.addActor(uiTable);
		uiTable.setDebug(true);

		uiTable.left().top();
		scoreLabel = new Label("Score: 1", uiSkin);
		uiTable.add(scoreLabel);

		scene.start();
	}

	private void spawnRaindrop(){
		GameObject drop = new GameObject(scene, "droplet", Gdx.files.internal("droplet.png"), GameObject.PhysicsMode.Passive);
		drop.attachScript(new IGameScript() {
			@Override
			public void start(GameObject gameObject) {

			}

			@Override
			public void update(GameObject gameObject, float delta) {
				gameObject.setY(gameObject.getY()-200*delta);
				if (gameObject.getY() < 0){
					gameObject.remove();
				}
			}

			@Override
			public void destroy(GameObject gameObject) {

			}

			@Override
			public void onCollisionEnter(GameObject gameObject, GameObject target) {
				if (target.getName().equals("bucket")){
					dropletSound.play();
					score++;
					scoreLabel.setText("Score: "+score);
					System.out.println("Score: "+score);
					gameObject.remove();
				}
			}

			@Override
			public void onCollision(GameObject gameObject, GameObject target) {

			}

			@Override
			public void onCollisionExit(GameObject gameObject, GameObject target) {

			}
		});
		drop.setPosition(MathUtils.random(0, scene.getWidth()-drop.getWidth()), scene.getHeight()-drop.getHeight());
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scene.act(Gdx.graphics.getDeltaTime());
		scene.draw();
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		scene.dispose();
	}

	@Override
	public void pause(){
		scene.pause();
	}

	@Override
	public void resume(){
		scene.resume();
	}
}
