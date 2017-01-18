package ca.catoen.gamename;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TransEnemy {

	private static final int col = 5;
	private static final int row = 5;

	Vector2 position, prevPosition, size;
	Texture enemy;
	int value;

	Animation animation;
	Texture enemyTexture;
	TextureRegion[] frames;
	TextureRegion currentFrame;

	public TransEnemy(Vector2 position, Vector2 size, int value) {
		this.position = position;
		this.prevPosition = position;
		this.size = size;
		this.value = value;

		enemyTexture = new Texture(Gdx.files.internal("blocks_transparant.png"));
		TextureRegion[][] tmp = TextureRegion.split(enemyTexture,
				enemyTexture.getHeight() / col, enemyTexture.getWidth() / row);
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
	
	public TransEnemy(Vector2 position, Vector2 prevPosition, Vector2 size, int value) {
		this.position = position;
		this.prevPosition = prevPosition;
		this.size = size;
		this.value = value;

		enemyTexture = new Texture(Gdx.files.internal("blocks.png"));
		TextureRegion[][] tmp = TextureRegion.split(enemyTexture,
				enemyTexture.getHeight() / col, enemyTexture.getWidth() / row);
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
		// bounds.set(position.x, position.y, size.x, size.y);
		prevPosition = position;
		position.y -= Gdx.graphics.getWidth() / 5;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(enemyTexture, position.x, position.y,
				Gdx.graphics.getWidth() / 5, Gdx.graphics.getWidth() / 5);
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

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public TextureRegion getCurrentFrame() {
		return currentFrame;
	} 
}

