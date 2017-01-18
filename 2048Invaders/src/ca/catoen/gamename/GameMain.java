package ca.catoen.gamename;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameMain extends Game {

	GameMain game;
	ActionResolver actionResolver;
	
	public GameMain(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
	}

	/*public GameMain() {
		super();
	}*/

	@Override
	public void create() {
		game = this;
		setScreen(new MainMenu(game));
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
