package ca.catoen.gamename;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayScreen implements Screen {

	final int SCREEN_WIDTH = Gdx.graphics.getWidth();
	final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	final int BLOCK_WIDTH = Gdx.graphics.getWidth() / 5;
	private boolean paused;

	public static Preferences prefs = Gdx.app.getPreferences("my-Preferences");

	boolean hm[] = new boolean[20];

	SpriteBatch batch;
	Texture background;
	Texture MainMenuBackground, pausedLabel, pauseButton;

	double paceFactor = 1;

	CButton nuke, slow, sort;

	Music theme;

	int score = 0;
	float pace = 0.5f;

	BitmapFont font;
	BitmapFont shadow;
	CButton pause_button, play_button, home_button, replay_button,
			sound_button;

	ArrayList<Enemy> enemies;
	ArrayList<Player> players;
	Iterator<Player> playersIterator;
	Iterator<Enemy> enemiesIterator;

	int MaxValue = 0;
	int randomizer;
	float time = 0, slowTime = 0;
	char direction;
	boolean touched, drawShade = false;
	boolean waiting = false;
	boolean changed = false;
	float wait = 0f;
	boolean soundOff = false;
	boolean currentlySlowed = false;
	Texture shadeSquare, soundButtonOff, soundButtonOn;

	boolean hasNuke = true, hasSlow = true, hasSort = true;

	int chunk = (SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5)) / 2;

	GameMain game;

	public PlayScreen(GameMain game) {
		this.game = game;
		// Gdx.input.setInputProcessor(this);
		// Gdx.input.setCatchBackKey(true);

		theme = Gdx.audio.newMusic(Gdx.files.internal("theme.mp3"));
		theme.setLooping(true);
	}

	@Override
	public void render(float delta) {

		// Gdx.gl.glClearColor(1, 1, 1, 1);
		// Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		time += Gdx.graphics.getDeltaTime();

		checkIfPauseButtonIsTouched();

		if (pause_button.isHit()) {
			paused = true;
			theme.pause();
		}

		else if (!paused) {
			if (slow.isHit() && hasSlow) {
				hasSlow = false;
				currentlySlowed = true;
				background = new Texture(
						Gdx.files.internal("FrozenBackground.png"));
				slow.setTexture("SlowButton_transparant.png");
			} else if (currentlySlowed) {
				slowTime += Gdx.graphics.getDeltaTime();
				paceFactor = 2;
				if (slowTime > 10) {
					currentlySlowed = false;
					paceFactor = 1;
					background = new Texture(
							Gdx.files.internal("GameBackground.png"));
				}
			}

			if (sort.isHit() && hasSort) {
				sort();
			}
			if (nuke.isHit() && hasNuke) {
				nuke();
			} else if (!nuke.getIsClicked() && !slow.getIsClicked()
					&& !sort.getIsClicked()) {

				if (!waiting) {
					updatePlayer();
				}
				if (!waiting && !pause_button.getIsClicked()) {
					updatePlayer();
				}

				updatePace();
				afterUnpause();

				updatePace();
				afterUnpause();

				if (changed)
					evaluatePlayer();

				if (time >= pace) {
					makeEnemies();
					updateEnemies();
					time = 0;
					randomizer = (int) (Math.random() * 15); // 0 - 14
					getMaxValue();
					if (randomizer <= 4)
						drawShade = true;
					else
						drawShade = false;
				}

				almostGameOverWait();
			}
		}

		batch.begin();
		batch.draw(MainMenuBackground, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch.draw(background, 0, (SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5)) / 2,
				SCREEN_WIDTH, 6 * SCREEN_WIDTH / 5);
		if (drawShade)
			batch.draw(shadeSquare, SCREEN_WIDTH / 5 * randomizer, BLOCK_WIDTH
					* 6 + (SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5)) / 2
					- BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH);
		drawEnemies();
		drawPlayer();

		font.draw(batch, " " + score, 0, SCREEN_HEIGHT - SCREEN_WIDTH / 40);
		batch.draw(pauseButton, SCREEN_WIDTH - SCREEN_WIDTH / 7 - 10,
				SCREEN_HEIGHT - SCREEN_WIDTH / 7 - 10, SCREEN_WIDTH / 7,
				SCREEN_WIDTH / 7);
		nuke.drawButton();
		slow.drawButton();
		sort.drawButton();
		batch.end();

		paused();

		// checkIfPauseButtonIsTouched();
	}

	private void checkIfPauseButtonIsTouched() {
		if (Gdx.input.isTouched()) {
			if (Gdx.input.getX() > SCREEN_WIDTH - SCREEN_WIDTH / 7
					&& Gdx.input.getX() < SCREEN_WIDTH
					&& Gdx.input.getY() < SCREEN_WIDTH / 7
					&& Gdx.input.getY() > 0) {
				paused = true;
			}
		}

		batch.begin();
		font.draw(batch, " " + score, 0, SCREEN_HEIGHT - SCREEN_WIDTH / 40);
		pause_button.drawButton();
		batch.end();

		paused();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	public void afterUnpause() {
		if (waiting) {
			if (!Gdx.input.isTouched()) {
				waiting = false;
			}
		}
	}

	public void almostGameOverWait() {
		if (players.size() > 5) {
			wait += Gdx.graphics.getDeltaTime();
			if (wait > 1) {
				game.setScreen(new GameOver(game, score, MaxValue));
				dispose();
			}
		}
	}

	public void paused() {
		if (paused) {
			if (sound_button.isHit()) {
				if (soundOff)
					sound_button.setTexture("SoundOn.png");
				else
					sound_button.setTexture("SoundOff.png");

				soundOff = !soundOff;
				prefs.putBoolean("Sound", soundOff);
				prefs.flush();
				System.out.println(soundOff);

			}
			if (sound_button.isHit() && !prefs.getBoolean("Sound")) {
				sound_button.setTexture("soundOff.png");
				prefs.putBoolean("Sound", true);
			}
			// if (Gdx.input.isTouched()){
			if (play_button.isHit()) {
				paused = false;
				waiting = true;
				if (!soundOff)
					theme.play();
			}
			// }
			if (home_button.isHit()) {
				game.setScreen(new MainMenu(game));
				theme.stop();
			}
			if (replay_button.isHit()) {
				game.setScreen(new PlayScreen(game));
				theme.stop();
			}
			batch.begin();

			play_button.drawButton();
			home_button.drawButton();
			replay_button.drawButton();
			sound_button.drawButton();
			batch.end();
		}
	}

	@Override
	public void show() {

		System.out.println("HEIGHT : " + SCREEN_HEIGHT);
		System.out.println("WIDTH : " + SCREEN_WIDTH);

		Texture.setEnforcePotImages(false);
		batch = new SpriteBatch();

		soundButtonOn = new Texture(Gdx.files.internal("SoundOn.png"));
		soundButtonOff = new Texture(Gdx.files.internal("SoundOff.png"));

		shadeSquare = new Texture(Gdx.files.internal("ShadeSquare.png"));
		pauseButton = new Texture(Gdx.files.internal("Pause.png"));
		MainMenuBackground = new Texture(
				Gdx.files.internal("HowToPlayBackground.png"));

		background = new Texture(Gdx.files.internal("GameBackground.png"));

		pause_button = new CButton(SCREEN_WIDTH - SCREEN_WIDTH / 7 - 10,
				SCREEN_HEIGHT - SCREEN_WIDTH / 7 - 10, SCREEN_WIDTH / 7,
				SCREEN_WIDTH / 7, "Pause.png", batch);
		play_button = new CButton(SCREEN_WIDTH / 2
				- (int) (SCREEN_HEIGHT / 9.6), SCREEN_HEIGHT / 2
				- (int) (SCREEN_HEIGHT / 9.6), (int) (SCREEN_HEIGHT / 4.8),
				(int) (SCREEN_HEIGHT / 4.8), "circle_play.png", batch);
		home_button = new CButton(SCREEN_WIDTH / 2
				- (int) (SCREEN_HEIGHT / 4.8), SCREEN_HEIGHT / 2
				- (int) (SCREEN_HEIGHT / 4), (int) (SCREEN_HEIGHT / 9.6),
				(int) (SCREEN_HEIGHT / 9.6), "circle_home.png", batch);
		replay_button = new CButton(SCREEN_WIDTH / 2
				+ (int) (SCREEN_HEIGHT / 9.6), SCREEN_HEIGHT / 2
				- (int) (SCREEN_HEIGHT / 4), (int) (SCREEN_HEIGHT / 9.6),
				(int) (SCREEN_HEIGHT / 9.6), "circle_redo.png", batch);
		if (prefs.getBoolean("Sound"))
			sound_button = new CButton(SCREEN_WIDTH / 2
					- (int) (SCREEN_HEIGHT / 19.2), SCREEN_HEIGHT / 2
					- (int) (SCREEN_HEIGHT / 4), (int) (SCREEN_HEIGHT / 9.6),
					(int) (SCREEN_HEIGHT / 9.6), "soundOff.png", batch);
		else
			sound_button = new CButton(SCREEN_WIDTH / 2
					- (int) (SCREEN_HEIGHT / 19.2), SCREEN_HEIGHT / 2
					- (int) (SCREEN_HEIGHT / 4), (int) (SCREEN_HEIGHT / 9.6),
					(int) (SCREEN_HEIGHT / 9.6), "SoundOn.png", batch);

		players = new ArrayList<Player>();
		enemies = new ArrayList<Enemy>();

		players.add(new Player(new Vector2(
				SCREEN_WIDTH / 2 - (BLOCK_WIDTH) / 2,
				(SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5)) / 2), 0));

		nuke = new CButton(SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 14.4),
				(SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5)) / 2
						+ (int) (SCREEN_WIDTH / 7.2) / 2 - chunk,
				(int) (SCREEN_WIDTH / 7.2), (int) (SCREEN_WIDTH / 7.2),
				"nuke.png", batch);
		slow = new CButton(SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 14.4)
				+ SCREEN_WIDTH / 3, (SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5))
				/ 2 + (int) (SCREEN_WIDTH / 7.2) / 2 - chunk,
				(int) (SCREEN_WIDTH / 7.2), (int) (SCREEN_WIDTH / 7.2),
				"SlowButton.png", batch);
		sort = new CButton(SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 14.4)
				- SCREEN_WIDTH / 3, (SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5))
				/ 2 + (int) (SCREEN_WIDTH / 7.2) / 2 - chunk,
				(int) (SCREEN_WIDTH / 7.2), (int) (SCREEN_WIDTH / 7.2),
				"sort.png", batch);

		// players.add(new Player(new Vector2(SCREEN_WIDTH / 2
		// - (BLOCK_WIDTH) / 2,
		// BLOCK_WIDTH + SCREEN_HEIGHT / 20), 0));

		// font =
		// style = new LabelStyle(font, Color.BLACK);
		//
		// label = new Label("TheLazyTryhard Tutorials", style);
		// label.setPosition((Gdx.graphics.getWidth() / 2) - (label.getWidth() /
		// 2), Gdx.graphics.getHeight() - label.getHeight());

		font = new BitmapFont(Gdx.files.internal("text.fnt"), true);
		shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"), true);
		font.setScale(SCREEN_WIDTH / 500f, -SCREEN_WIDTH / 500f);
		shadow.setScale(SCREEN_WIDTH / 500f, -SCREEN_WIDTH / 500f);

		soundOff = prefs.getBoolean("Sound");

		if (!soundOff)
			theme.play();

		// Unlock 2 just for playing
		unlockAchievement(0);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		theme.dispose();

	}

	public void updatePace() {
		if (score < 20)
			pace = (float) (0.45f * paceFactor);
		else {
			pace = (float) (0.9577 * Math.pow(0.75 * score, -0.225) * paceFactor);
			if (pace < 0.14)
				pace = 0.14f;
		}
	}

	public void updatePlayer() {
		boolean isValid = true;
		playersIterator = players.iterator();

		// Update direction
		if (Gdx.input.isTouched() && !touched) {
			touched = true;
			if (Gdx.input.getX() < SCREEN_WIDTH / 2) {
				direction = 'r';
			} else
				direction = 'l';
		} else if (!Gdx.input.isTouched()) {
			touched = false;
			direction = 'n';
		}

		// Check if valid move

		while (playersIterator.hasNext()) {
			Player currentPlayer = playersIterator.next();
			enemiesIterator = enemies.iterator();
			while (enemiesIterator.hasNext()) {
				Enemy currentEnemy = enemiesIterator.next();
				if (((direction == 'l'
						&& currentPlayer.getPosition().x + BLOCK_WIDTH == currentEnemy
								.getPosition().x && currentPlayer.getPosition().y == currentEnemy
						.getPosition().y) || (direction == 'r'
						&& currentPlayer.getPosition().x - BLOCK_WIDTH == currentEnemy
								.getPosition().x && currentPlayer.getPosition().y == currentEnemy
						.getPosition().y))
						&& currentPlayer.getValue() != currentEnemy.getValue()) {
					isValid = false;
				}
			}
		}

		// Side collisions
		if (isValid) {
			changed = true;
			playersIterator = players.iterator();

			while (playersIterator.hasNext()) {
				Player currentPlayer = playersIterator.next();
				enemiesIterator = enemies.iterator();

				// Update position
				if (direction == 'l') {
					if (currentPlayer.getPosition().x < SCREEN_WIDTH
							- BLOCK_WIDTH)
						currentPlayer.setPosition(new Vector2(currentPlayer
								.getPosition().x + BLOCK_WIDTH, currentPlayer
								.getPosition().y));
				} else if (direction == 'r') {
					if (currentPlayer.getPosition().x > 0)
						currentPlayer.setPosition(new Vector2(currentPlayer
								.getPosition().x - BLOCK_WIDTH, currentPlayer
								.getPosition().y));
				}

				// Check collisions
				while (enemiesIterator.hasNext()) {
					Enemy currentEnemy = enemiesIterator.next();
					if (currentPlayer.getPosition().x == currentEnemy
							.getPosition().x
							&& currentPlayer.getPosition().y == currentEnemy
									.getPosition().y
							&& currentPlayer.getValue() == currentEnemy
									.getValue()) {
						currentPlayer.setValue(currentPlayer.getValue() + 1);
						score += (int) Math.pow(2, currentEnemy.getValue() + 1);
						enemiesIterator.remove();
					}
				}

			}
		}

		// Top block collision
		enemiesIterator = enemies.iterator();
		Player currentPlayer = players.get(players.size() - 1);
		while (enemiesIterator.hasNext()) {
			Enemy currentEnemy = enemiesIterator.next();
			if (currentPlayer.getPosition().x == currentEnemy.getPosition().x
					&& currentPlayer.getPosition().y == currentEnemy
							.getPosition().y - BLOCK_WIDTH) {
				players.add(new Player(currentEnemy.getPosition(), currentEnemy
						.getPrevPosition(), currentEnemy.getValue()));
				score += (int) Math.pow(2, currentEnemy.getValue() + 1);
				enemiesIterator.remove();
				changed = true;
				System.out.println("whamo");
			}
		}

		direction = 'n';
	}

	public void updateEnemies() {
		enemiesIterator = enemies.iterator();
		while (enemiesIterator.hasNext()) {
			Enemy currentEnemy = enemiesIterator.next();
			currentEnemy.update();
			// if (currentEnemy.getPosition().y < (SCREEN_HEIGHT - (6 / 5 *
			// SCREEN_WIDTH)) / 10) {
			if (currentEnemy.getPosition().y < (SCREEN_HEIGHT - (6 * SCREEN_WIDTH / 5)) / 2) {
				enemiesIterator.remove();
				System.out.println(currentEnemy.getPosition().y);
			}

		}
	}

	public void makeEnemies() {
		if (randomizer <= 4) {
			int tempRandom = (int) (Math.random() * MaxValue);
			enemies.add(new Enemy(new Vector2(randomizer * BLOCK_WIDTH,
					BLOCK_WIDTH * 6 + (SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5))
							/ 2), new Vector2(BLOCK_WIDTH, BLOCK_WIDTH),
					tempRandom));
		}
	}

	public void drawEnemies() {
		enemiesIterator = enemies.iterator();
		while (enemiesIterator.hasNext()) {
			Enemy currentEnemy = enemiesIterator.next();
			batch.draw(currentEnemy.getCurrentFrame(),
					currentEnemy.getPosition().x, currentEnemy.getPosition().y,
					BLOCK_WIDTH, BLOCK_WIDTH);
		}
	}

	public void getMaxValue() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getValue() > MaxValue) {
				MaxValue = players.get(i).getValue();
				unlockAchievement(MaxValue);
			}
		}
	}

	public void drawPlayer() {
		playersIterator = players.iterator();
		while (playersIterator.hasNext()) {
			Player currentPlayer = playersIterator.next();
			batch.draw(currentPlayer.getCurrentFrame(),
					currentPlayer.getPosition().x,
					currentPlayer.getPosition().y, BLOCK_WIDTH, BLOCK_WIDTH);
		}
	}

	public void evaluatePlayer() {
		for (int i = 0; i < players.size() - 1; i++) {
			if (players.get(i).getValue() == players.get(i + 1).getValue()) {
				players.get(i).setValue(players.get(i).getValue() + 1);
				players.remove(i + 1);
				i--;
			}
		}

		// reinforce y values
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setPosition(
					new Vector2(players.get(i).getPosition().x, i * BLOCK_WIDTH
							+ (SCREEN_HEIGHT - 6 * (SCREEN_WIDTH / 5)) / 2));
		}

		changed = false;
	}

	public void nuke() {
		while (players.size() > 1) {
			players.remove(1);
		}
		MaxValue = players.get(0).getValue();
		hasNuke = false;
		nuke.setTexture("nuke_transparant.png");
	}

	public void sort() {
		Collections.sort(players);
		hasSort = false;
		sort.setTexture("sort_transparant.png");
	}

	public void unlockAchievement(int i) {
		if (!hm[i]) {
			// Only try to unlock an achievement if the user is logged in
			// Otherwise the next line will fail
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(i);
			hm[i] = true;
		}
	}

}
