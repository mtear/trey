/**
	Author: Nic Wilson
	File: mtear/lib/NBitmap.java

	A class for handling a Bitmap in LibGDX
	Handles texture and sprite of the Bitmap and other region functions
*/
package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
	A bitmap class for LibGDX
*/
public class NBitmap {
	private Texture texture;
	private Sprite sprite;
	private float width, height;
	
	/**
		Basic Constructor
		@param name The name of the png in the data files to create from
	*/
	public NBitmap(String name){
		texture = new Texture(Gdx.files.internal("data/" + name + ".png"));
		initSprite();
	}
	
	/**
		Another simple constructor
		@param name The name of the png in the data files to create from
		@param w The width to resize to
		@param h The height to resize to
	*/
	public NBitmap(String name, float w, float h){
		texture = new Texture(Gdx.files.internal("data/" + name + ".png"));
		initSprite();
		setSize(w, h);
	}
	
	/**
		Create a bitmap from a Pixmap (pixel map) object
	*/
	public NBitmap(Pixmap pixmap){
		texture = new Texture(pixmap);
		initSprite();
	}

	/**
		Create a bitmap from a sprite sheet region and set to a given size
	*/
	public NBitmap(AtlasRegion region, float w, float h){
		sprite = new Sprite(region);
		this.width = w; this.height = h;
		sprite.setSize(w, h);
	}
	
	/**
		setRegion
		Set the region for the sprite
		This can be used for sprite sheet effects
	*/
	public void setRegion(AtlasRegion region){
		sprite.setRegion(region);
		sprite.setSize(width, height);
	}
	
	/**
		initSprite
		Initializes the sprite from the texture,
			centers the position of the sprite to the origin,
			and registers the texture in the mtear lib manager
	*/
	private void initSprite(){
		sprite = new Sprite(texture);
		sprite.setPosition(0, 0);
		registerTexture();
	}
	
	/**
		registerTexture
		Register the texture with the mtear lib manager
			so that it will be destroyed when the app is closed
	*/
	private void registerTexture(){
		GameBase._alltextures.add(texture);
	}
	
	/**
		setSize
		Resize the image
	*/
	public void setSize(float w, float h){
		sprite.setSize(w, h);
		width = w; height = h;
	}
	
	/**
		setPosition
		Moves the image of the sprite.
		Each bitmap keeps their own internal position of where to be drawn
	*/
	public void setPosition(float x, float y){
		sprite.setPosition(x, y);
	}
	
	/**
		draw
		Draws the sprite on the main screen at its internal position
	*/
	public void draw(){
		sprite.draw(GameBase._batch);
	}
	
	/**
		drawAt
		Draws the sprite on the main screen at a given position
	*/
	public void drawAt(float x, float y){
		sprite.setPosition(x, y);
		sprite.draw(GameBase._batch);
	}
	
	/**
		getWidth
		Returns the current width of the image
	*/
	public float getWidth(){
		return width;
	}
	
	/**
		getHeight
		Returns the current height of the image
	*/
	public float getHeight(){
		return height;
	}
}
