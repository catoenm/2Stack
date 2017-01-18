package ca.catoen.gamename;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player implements Comparable<Player>{

	private static final int col = 5;
	private static final int row = 5;

	int value;
	int score = 0;
	Vector2 position, prevPosition;
	Rectangle bounds;
	Texture texture;
	public static boolean touched;

	Animation animation;
	Texture playerTexture;
	TextureRegion[] frames;
	TextureRegion currentFrame;

	public Player(Vector2 position, int value) {
		this.position = position;
		this.prevPosition = position;
		this.value = value;
		// texture = new Texture(Gdx.files.internal(textureLoc));

		playerTexture = new Texture(Gdx.files.internal("blocks.png"));
		TextureRegion[][] tmp = TextureRegion
				.split(playerTexture, playerTexture.getHeight() / col,
						playerTexture.getWidth() / row);
		frames = new TextureRegion[col * row];

		int index = 0;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				frames[index++] = tmp[i][j];
				// System.out.println ("i " + i + " j " + j);
			}
		}

		animation = new Animation(1, frames);
		currentFrame = animation.getKeyFrame(value);
	}

	public Player(Vector2 position, Vector2 prevPosition, int value) {
		this.position = position;
		this.prevPosition = prevPosition;
		this.value = value;
		// texture = new Texture(Gdx.files.internal(textureLoc));

		playerTexture = new Texture(Gdx.files.internal("blocks.png"));
		TextureRegion[][] tmp = TextureRegion
				.split(playerTexture, playerTexture.getHeight() / col,
						playerTexture.getWidth() / row);
		frames = new TextureRegion[col * row];

		int index = 0;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				frames[index++] = tmp[i][j];
				// System.out.println ("i " + i + " j " + j);
			}
		}

		animation = new Animation(1, frames);
		currentFrame = animation.getKeyFrame(value);
	}

	public void update() {
		if (Gdx.input.isTouched() && !touched) {
			touched = true;
			prevPosition = position;
			if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2) {
				position.x -= Gdx.graphics.getWidth() / 5;
			} else {
				position.x += Gdx.graphics.getWidth() / 5;
			}
		} else if (!Gdx.input.isTouched()) {
			touched = false;
		}

		if (position.x < 0)
			position.x = 0;
		if (position.x > Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 5)
			position.x = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 5;

	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public Vector2 getPrevPosition() {
		return prevPosition;
	}

	public void setPrevPosition(Vector2 prevPosition) {
		this.prevPosition = prevPosition;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y,
				Gdx.graphics.getWidth() / 5, Gdx.graphics.getWidth() / 5);
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
		this.currentFrame = animation.getKeyFrame(value);
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	@Override
	public int compareTo(Player p2) {
		return p2.getValue() - value;
	}
}
