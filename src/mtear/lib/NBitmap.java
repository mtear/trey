package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class NBitmap {
	private Texture texture;
	private Sprite sprite;
	private float width, height;
	
	public NBitmap(String name){
		texture = new Texture(Gdx.files.internal("data/" + name + ".png"));
		initSprite();
	}
	
	public NBitmap(String name, float w, float h){
		texture = new Texture(Gdx.files.internal("data/" + name + ".png"));
		initSprite();
		setSize(w, h);
	}
	
	public NBitmap(Pixmap pixmap){
		texture = new Texture(pixmap);
		initSprite();
	}
	
	public NBitmap(AtlasRegion region, float w, float h){
		sprite = new Sprite(region);
		this.width = w; this.height = h;
		sprite.setSize(w, h);
	}
	
	public void setRegion(AtlasRegion region){
		sprite.setRegion(region);
		sprite.setSize(width, height);
	}
	
	private void initSprite(){
		sprite = new Sprite(texture);
		sprite.setPosition(0, 0);
		registerTexture();
	}
	
	private void registerTexture(){
		GameBase._alltextures.add(texture);
	}
	
	public void setSize(float w, float h){
		sprite.setSize(w, h);
		width = w; height = h;
	}
	
	public void setPosition(float x, float y){
		sprite.setPosition(x, y);
	}
	
	public void draw(){
		sprite.draw(GameBase._batch);
	}
	
	public void drawAt(float x, float y){
		sprite.setPosition(x, y);
		sprite.draw(GameBase._batch);
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
}
