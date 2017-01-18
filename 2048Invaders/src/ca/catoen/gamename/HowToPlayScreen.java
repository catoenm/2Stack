package ca.catoen.gamename;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HowToPlayScreen implements Screen {

	private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
	private final int SCREEN_WIDTH = Gdx.graphics.getWidth();

	BitmapFont font;
	
	Music theme;

	Texture title, background;
	Texture[] text, images, images2;

	float pace = 1f;
	float time = 0;

	SpriteBatch batch;

	int screen;
	int index;
	int instruction = 0;

	int change = 1;

	boolean touched;
	boolean refreshed = true;
	boolean animate = false;

	GameMain game;

	public HowToPlayScreen(GameMain game, Music theme) {
		this.game = game;
		this.theme = theme;
	}

	@Override
	public void render(float delta) {

		time += Gdx.graphics.getDeltaTime();

		if (Gdx.input.isTouched() && refreshed) {
			if (instruction == 8)
				game.setScreen(new MainMenu(game, theme));
			else {
				instruction++;
				time = 0;
				animate = false;
				refreshed = false;
			}
		}

		if (refreshed == false) {
			if (!Gdx.input.isTouched())
				refreshed = true;
		}

		if (time >= pace) {
			if (animate)
				animate = false;
			else
				animate = true;
			time = 0;
		}

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch.draw(title, SCREEN_WIDTH / 2 - (int) (SCREEN_WIDTH / 2.16),
				SCREEN_HEIGHT - (int) (SCREEN_HEIGHT / 5.4),
				(int) (SCREEN_WIDTH / 1.08), (int) (SCREEN_WIDTH / 3.6));

		if (animate
				&& (instruction == 2 || instruction == 5 || instruction == 6))
			batch.draw(images2[instruction], SCREEN_WIDTH / 2
					- (int) (SCREEN_WIDTH / 5.4),
					(int) (SCREEN_HEIGHT / 2 - SCREEN_WIDTH / 3.6),
					(int) (SCREEN_WIDTH / 2.7), (int) (SCREEN_WIDTH / 1.8));
		else
			batch.draw(images[instruction], SCREEN_WIDTH / 2
					- (int) (SCREEN_WIDTH / 5.4),
					(int) (SCREEN_HEIGHT / 2 - SCREEN_WIDTH / 3.6),
					(int) (SCREEN_WIDTH / 2.7), (int) (SCREEN_WIDTH / 1.8));

		batch.draw(text[instruction], SCREEN_WIDTH / 2
				- (int) (SCREEN_WIDTH / 2.16), 0, (int) (SCREEN_WIDTH / 1.08),
				(int) (SCREEN_WIDTH / 2.7));
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		text = new Texture[9];
		images = new Texture[9];
		images2 = new Texture[9];
		background = new Texture(Gdx.files.internal("TitleScreen.png"));
		title = new Texture(Gdx.files.internal("how_to_play.png"));

		text[0] = new Texture(Gdx.files.internal("instrs1.png"));
		text[1] = new Texture(Gdx.files.internal("instrs2.png"));
		text[2] = new Texture(Gdx.files.internal("instrs3.png"));
		text[3] = new Texture(Gdx.files.internal("instrs4.png"));
		text[4] = new Texture(Gdx.files.internal("instrs5.png"));
		text[5] = new Texture(Gdx.files.internal("instrs6.png"));
		text[6] = new Texture(Gdx.files.internal("instrs7.png"));
		text[7] = new Texture(Gdx.files.internal("instrs8.png"));
		text[8] = new Texture(Gdx.files.internal("instrs9.png"));

		images[0] = new Texture(Gdx.files.internal("image1.png"));
		images[1] = new Texture(Gdx.files.internal("image2.png"));
		images[2] = new Texture(Gdx.files.internal("image3.png"));
		images[3] = new Texture(Gdx.files.internal("image4.png"));
		images[4] = new Texture(Gdx.files.internal("image5.png"));
		images[5] = new Texture(Gdx.files.internal("image6.png"));
		images[6] = new Texture(Gdx.files.internal("image7.png"));
		images[7] = new Texture(Gdx.files.internal("image8.png"));
		images[8] = new Texture(Gdx.files.internal("image9.png"));

		images2[2] = new Texture(Gdx.files.internal("image33.png"));
		images2[5] = new Texture(Gdx.files.internal("image66.png"));
		images2[6] = new Texture(Gdx.files.internal("image77.png"));

		font = new BitmapFont(Gdx.files.internal("text.fnt"), true);
		font.setScale(SCREEN_WIDTH / 500f, -SCREEN_WIDTH / 450f);

		index = 0;

		touched = false;
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
