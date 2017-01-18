package ca.catoen.gamename;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CreditScreen implements Screen {

	final int SCREEN_WIDTH = Gdx.graphics.getWidth();
	final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

	GameMain game;
	SpriteBatch batch;
	Texture background, credits, title;
	CButton home;
	Music theme;

	public CreditScreen(GameMain game, Music theme) {
		this.game = game;
		this.theme = theme;
	}

	@Override
	public void render(float delta) {

		if (home.isHit())
			game.setScreen(new MainMenu(game, theme));

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch.draw(title, SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 2.16),
				SCREEN_HEIGHT - (int) (SCREEN_HEIGHT / 5.4),
				(int) (SCREEN_WIDTH / 1.08), (int) (SCREEN_WIDTH / 3.6));
		batch.draw(credits, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		home.drawButton();
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("TitleScreen.png"));
		title = new Texture(Gdx.files.internal("credits_title.png"));
		credits = new Texture(Gdx.files.internal("Credits.png"));
		home = new CButton((int) (SCREEN_WIDTH / 2 - SCREEN_WIDTH / 10.8),
				(int) (SCREEN_HEIGHT / 38.4),
				(int) (SCREEN_WIDTH / 5.4),
				(int) (SCREEN_WIDTH / 5.4), "circle_home.png", batch);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
