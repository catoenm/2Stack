package ca.catoen.gamename;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MainMenu implements Screen {
	private final int row = 1;
	private final int col = 6;

	private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	private final int SCREEN_WIDTH = Gdx.graphics.getWidth();
	final int BLOCK_WIDTH = Gdx.graphics.getWidth() / 5;

	public static Preferences prefs = Gdx.app.getPreferences("my-Preferences");

	long score = 15;
	boolean timerDone = true;
	boolean touched = false;
	boolean soundOff = false;
	Stage stage;

	Music theme;
	boolean playing = false;
	
	BitmapFont font;

	BitmapFont shadow;

	Iterator<TransEnemy> enemiesIterator;
	ArrayList<TransEnemy> enemies;
	int randomizer;

	float pace = 0.3f;

	// A useless comment
	
	Texture texture, startButton, playButton, soundButtonOn, soundButtonOff,
			highScoresButton, howToPlayButton, creditsButton, title;
	TextureAtlas buttonAtlas;
	TextButtonStyle buttonStyle;
	TextButton button;
	Skin skin;
	float statetime = 0;

	Texture background;

	CButton play_button, credits_button, htp_button, hs_button, sound_button,
			ach_button;

	float time;

	SpriteBatch batch;

	GameMain game;
	
	public MainMenu(GameMain game) {
		game.actionResolver.loginGPGS();
		this.game = game;
		
		theme = Gdx.audio.newMusic(Gdx.files.internal("MenuMusic.mp3"));
		theme.setLooping(true);
	}
	
	public MainMenu(GameMain game, Music theme) {
		game.actionResolver.loginGPGS();
		this.game = game;
		
		this.theme = theme;
	}

	@Override
	public void render(float delta) {
		time += Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		soundOff = prefs.getBoolean("Sound");
		if (!soundOff && !playing){
			theme.play();
			playing = true;
		}
		else if (soundOff && playing){
			playing = false;
			theme.pause();
		}
			touched = true;
			
			if (play_button.isHit()){
				theme.stop();
				game.setScreen(new PlayScreen(game));
			} else if (htp_button.isHit()) {
				game.setScreen(new HowToPlayScreen(game, theme));
			} else if (credits_button.isHit()) {
				game.setScreen(new CreditScreen(game, theme));
			} else if (ach_button.isHit()) {
				if (game.actionResolver.getSignedInGPGS())
					game.actionResolver.getAchievementsGPGS();
				else
					game.actionResolver.loginGPGS();
			}
			// SOUND TOGGLE
			else if (sound_button.isHit()) {
				soundOff = !soundOff;
				prefs.putBoolean("Sound", soundOff);
				prefs.flush();
				System.out.println(soundOff);

			}
			// LEADERBOARDS
			else if (hs_button.isHit()) {
				if (game.actionResolver.getSignedInGPGS())
					game.actionResolver.getLeaderboardGPGS();
				else
					game.actionResolver.loginGPGS();
			}
		if (!Gdx.input.isTouched())
			touched = false;

		if (time >= pace) {
			makeEnemies();
			updateEnemies();
			time = 0;
		}

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		drawEnemies();

		play_button.drawButton();
		hs_button.drawButton();
		htp_button.drawButton();
		credits_button.drawButton();
		ach_button.drawButton();

		if (soundOff)
			batch.draw(soundButtonOff, SCREEN_WIDTH
					- (int) (SCREEN_WIDTH / 10.8) - 10, SCREEN_HEIGHT
					- (int) (SCREEN_HEIGHT / 19.2) - 10,
					(int) (SCREEN_WIDTH / 10.8), (int) (SCREEN_HEIGHT / 19.2));
		else
			batch.draw(soundButtonOn, SCREEN_WIDTH
					- (int) (SCREEN_WIDTH / 10.8) - 10, SCREEN_HEIGHT
					- (int) (SCREEN_HEIGHT / 19.2) - 10,
					(int) (SCREEN_WIDTH / 10.8), (int) (SCREEN_HEIGHT / 19.2));
		
		batch.draw(title, SCREEN_WIDTH/2 - (int) (SCREEN_WIDTH / 2.7), SCREEN_HEIGHT
				- (int) (SCREEN_HEIGHT / 9.6) - (int) (SCREEN_WIDTH / 2.7),
				(int) (SCREEN_WIDTH / 1.35), (int) (SCREEN_WIDTH / 2.7));
		
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Texture.setEnforcePotImages(false);
		background = new Texture(Gdx.files.internal("TitleScreen.png"));
		batch = new SpriteBatch();

		enemies = new ArrayList<TransEnemy>();

		soundOff = prefs.getBoolean("Sound");

		soundButtonOn = new Texture(Gdx.files.internal("SoundOn.png"));
		soundButtonOff = new Texture(Gdx.files.internal("SoundOff.png"));

		title = new Texture(Gdx.files.internal("title.png"));
		
		play_button = new CButton(
				SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 5.4), SCREEN_HEIGHT
						/ 2 - (int) (SCREEN_HEIGHT / 19.2),
				(int) (SCREEN_WIDTH / 2.7), (int) (SCREEN_HEIGHT / 9.6),
				"PlayButton.png", batch);

		ach_button = new CButton(SCREEN_WIDTH / 2
				- (int) (SCREEN_WIDTH * 0.025) - (int) (SCREEN_WIDTH / 5.4),
				(int) (Gdx.graphics.getHeight() / 28.4),
				(int) (Gdx.graphics.getWidth() / 5.4),
				(int) (Gdx.graphics.getHeight() / 19.2), "AchievementsButton.png",
				batch);
		credits_button = new CButton(
				SCREEN_WIDTH / 2 + (int) (SCREEN_WIDTH * 0.025),
				(int) (Gdx.graphics.getHeight() / 28.4),
				(int) (Gdx.graphics.getWidth() / 5.4),
				(int) (Gdx.graphics.getHeight() / 19.2),
				"CreditsButton.png", batch);
		hs_button = new CButton(SCREEN_WIDTH / 2 - 3
				* (int) (SCREEN_WIDTH * 0.025) - 4
				* (int) (SCREEN_WIDTH / 10.8),
				(int) (Gdx.graphics.getHeight() / 28.4),
				(int) (Gdx.graphics.getWidth() / 5.4),
				(int) (Gdx.graphics.getHeight() / 19.2), "LeaderboardsButton.png",
				batch);
		htp_button = new CButton(SCREEN_WIDTH / 2 + 3
				* (int) (SCREEN_WIDTH * 0.025) + 2
				* (int) (SCREEN_WIDTH / 10.8),
				(int) (Gdx.graphics.getHeight() / 28.4),
				(int) (Gdx.graphics.getWidth() / 5.4),
				(int) (Gdx.graphics.getHeight() / 19.2),
				"HelpButton.png", batch);
		sound_button = new CButton(SCREEN_WIDTH
					- (int) (SCREEN_WIDTH / 10.8) - 10, SCREEN_HEIGHT
					- (int) (SCREEN_HEIGHT / 19.2) - 10,
					(int) (SCREEN_WIDTH / 10.8), (int) (SCREEN_HEIGHT / 19.2), "SoundOff.png", batch);
		
	}

	public void updateEnemies() {
		enemiesIterator = enemies.iterator();
		while (enemiesIterator.hasNext()) {
			TransEnemy currentEnemy = enemiesIterator.next();
			currentEnemy.update();
			if (currentEnemy.getPosition().y < 0) {
				enemiesIterator.remove();
			}

		}
	}

	public void makeEnemies() {
		randomizer = (int) (Math.random() * 14); // 0 - 14
		if (randomizer <= 4) {
			int tempRandom = (int) (Math.random() * 25);
			enemies.add(new TransEnemy(new Vector2(randomizer * BLOCK_WIDTH,
					BLOCK_WIDTH * 8), new Vector2(BLOCK_WIDTH, BLOCK_WIDTH),
					tempRandom));

		}
	}

	public void drawEnemies() {
		enemiesIterator = enemies.iterator();
		while (enemiesIterator.hasNext()) {
			TransEnemy currentEnemy = enemiesIterator.next();
			batch.draw(currentEnemy.getCurrentFrame(),
					currentEnemy.getPosition().x, currentEnemy.getPosition().y,
					BLOCK_WIDTH, BLOCK_WIDTH);
		}
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