package ca.catoen.gamename;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameOver implements Screen {

	private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	private final int SCREEN_WIDTH = Gdx.graphics.getWidth();
	public static Preferences prefs = Gdx.app.getPreferences("my-Preferences");

	GameMain game;
	SpriteBatch batch;

	Texture background, game_over;

	CButton redo_button, home_button, leaderboards_button, achievements_button;

	BitmapFont font, font2;
	BitmapFont shadow, shadow2;

	int change, score, highScore, maxValue;

	Vector2 position;

	public GameOver(GameMain game, int score, int MaxValue) {
		this.game = game;
		this.score = score;
		this.maxValue = MaxValue;
	}

	@Override
	public void render(float delta) {
		if (position.x <= 0)
			change = 2;
		if (position.x >= SCREEN_WIDTH / 4)
			change = -2;

		position.x += change;

		// if (Gdx.input.isTouched()) {
		if (leaderboards_button.isHit()) {
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.getLeaderboardGPGS();
			else
				game.actionResolver.loginGPGS();
		} else if (achievements_button.isHit()) {
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.getAchievementsGPGS();
			else
				game.actionResolver.loginGPGS();
		} else if (home_button.isHit()) {
			game.setScreen(new MainMenu(game));
		} else if (redo_button.isHit()) {
			game.setScreen(new PlayScreen(game));
		}

		// }

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch.draw(game_over, SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 2.16),
				SCREEN_HEIGHT - (int) (SCREEN_HEIGHT / 5.4),
				(int) (SCREEN_WIDTH / 1.08), (int) (SCREEN_WIDTH / 3.6));
		shadow2.draw(batch, "Score: " + score, SCREEN_WIDTH / 20, SCREEN_HEIGHT
				- (int) (SCREEN_HEIGHT / 5.4) - (int) (SCREEN_HEIGHT / 19.2));
		font2.draw(batch, "Score: " + score, SCREEN_WIDTH / 20 - 1,
				SCREEN_HEIGHT - (int) (SCREEN_HEIGHT / 5.4)
						- (int) (SCREEN_HEIGHT / 19.2));
		shadow2.draw(batch, "Best: " + highScore, SCREEN_WIDTH / 20,
				SCREEN_HEIGHT - (int) (SCREEN_HEIGHT / 5.4)
						- (int) (SCREEN_HEIGHT / 19.2) - SCREEN_HEIGHT / 10);
		font2.draw(batch, "Best: " + highScore, SCREEN_WIDTH / 20 - 1,
				SCREEN_HEIGHT - (int) (SCREEN_HEIGHT / 5.4)
						- (int) (SCREEN_HEIGHT / 19.2) - SCREEN_HEIGHT / 10);

		redo_button.drawButton();
		home_button.drawButton();
		leaderboards_button.drawButton();
		achievements_button.drawButton();

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

		game_over = new Texture(Gdx.files.internal("game_over.png"));

		redo_button = new CButton(
				SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 5.4), SCREEN_HEIGHT
						/ 2 - (int) (SCREEN_HEIGHT / 4.8),
				(int) (SCREEN_WIDTH / 2.7), (int) (SCREEN_HEIGHT / 4.8),
				"circle_redo.png", batch);

		home_button = new CButton(SCREEN_WIDTH / 9, SCREEN_HEIGHT / 2
				- (int) (SCREEN_HEIGHT / 2.5), (int) (SCREEN_WIDTH / 5.4),
				(int) (SCREEN_WIDTH / 5.4), "circle_home.png", batch);

		leaderboards_button = new CButton((int) (SCREEN_WIDTH / 4.5)
				+ (int) (SCREEN_WIDTH / 5.4), SCREEN_HEIGHT / 2
				- (int) (SCREEN_HEIGHT / 2.5), (int) (SCREEN_WIDTH / 5.4),
				(int) (SCREEN_WIDTH / 5.4), "circle_leaderboards.png", batch);

		achievements_button = new CButton((int) (SCREEN_WIDTH / 3)
				+ (int) (SCREEN_WIDTH / 2.7), SCREEN_HEIGHT / 2
				- (int) (SCREEN_HEIGHT / 2.5), (int) (SCREEN_WIDTH / 5.4),
				(int) (SCREEN_WIDTH / 5.4), "circle_achievements.png", batch);

		font = new BitmapFont(Gdx.files.internal("text.fnt"), true);
		shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"), true);
		font.setScale(SCREEN_WIDTH / 500f, -SCREEN_WIDTH / 450f);
		shadow.setScale(SCREEN_WIDTH / 500f, -SCREEN_WIDTH / 450f);

		font2 = new BitmapFont(Gdx.files.internal("text.fnt"), true);
		shadow2 = new BitmapFont(Gdx.files.internal("shadow.fnt"), true);
		font2.setScale(SCREEN_WIDTH / 700f, -SCREEN_WIDTH / 675f);
		shadow2.setScale(SCREEN_WIDTH / 700f, -SCREEN_WIDTH / 675f);

		position = new Vector2(0, 0);

		if (prefs.getInteger("HighScore", 0) < score) {
			prefs.putInteger("HighScore", score);
			prefs.flush();
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.submitScoreGPGS(score);
		}

		highScore = prefs.getInteger("HighScore");
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
