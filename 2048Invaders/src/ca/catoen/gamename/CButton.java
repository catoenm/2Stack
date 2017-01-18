package ca.catoen.gamename;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CButton {
	private int width, height, posX, posY;
	private Texture texture;
	private SpriteBatch batch;
	private boolean isClicked;
	
	public CButton(int posX, int posY, int width, int height, String name, SpriteBatch batch){
		this.texture = new Texture(Gdx.files.internal(name));
		this.batch = batch;
		this.width = width;
		this.height = height;
		this.posX = posX;
		this.posY = posY;
		this.isClicked = false;
	}
	
	public boolean isHit(){
		if(Gdx.input.isTouched()){
			if (Gdx.input.getX() > posX && Gdx.input.getX() < posX + width && 
				Gdx.input.getY() < Gdx.graphics.getHeight() - posY && Gdx.input.getY() > Gdx.graphics.getHeight() - posY - height){
				isClicked = true;
			}
		}
		else if (isClicked){
			isClicked = false;
			return true;
		}
		return false;
	}
	
	public void drawButton(){
		batch.draw(texture, posX, posY, width, height);
	}
	
	public boolean getIsClicked(){
		return isClicked;
	}
	
	public void setTexture(String name){
		this.texture = new Texture(Gdx.files.internal(name));
	}
	
}
