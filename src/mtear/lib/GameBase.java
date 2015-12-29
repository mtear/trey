package mtear.lib;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public abstract class GameBase implements ApplicationListener, GestureListener{
	public static ArrayList<Texture> _alltextures = new ArrayList<Texture>();
	public static ArrayList<Sound> _allsounds = new ArrayList<Sound>();
	public static ArrayList<Music> _allmusics = new ArrayList<Music>();
	public static ArrayList<BitmapFont> _allfonts = new ArrayList<BitmapFont>();
	public static SpriteBatch _batch;
	public static int _W, _H;
	Preferences _savesettings;
	private float cr=0, cg=0, cb=0;
	@Override
	public void create() {
		_savesettings = Gdx.app.getPreferences("savesettings");
		_W = Gdx.graphics.getWidth();
		_H = Gdx.graphics.getHeight();
		_batch = new SpriteBatch();
		init();
		
		GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
	}
	
	protected abstract void init();

	@Override
	public void resize(int width, int height) {
		//_W = width;
		//_H = height;
	}
	
	public static int Npow(int b, int e){
		if(e <= 0) return 1;
		if(e == 1) return b;
		else return b * Npow(b, e-1);
	}

	@Override
	public void render() {
		update();
		Gdx.gl.glClearColor(cr, cg, cb, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		_batch.begin();
		draw();
		_batch.end();
	}
	
	public void setClearColor(float cr, float cg, float cb){
		this.cr = cr;
		this.cg = cg;
		this.cb = cb;
	}
	
	protected abstract void update();
	
	protected abstract void draw();

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		_batch.dispose();
		for(Texture t : _alltextures){
			t.dispose();
		}
		for(Sound s : _allsounds){
			s.dispose();
		}
		for(Music m : _allmusics){
			m.dispose();
		}
		for(BitmapFont f : _allfonts){
			f.dispose();
		}
		destroy();
	}
	
	protected abstract void destroy();
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,Vector2 pointer1, Vector2 pointer2) {	return false;	}
	public boolean zoom(float initialDistance, float distance) {		return false;	}
	public boolean longPress(float x, float y) {  return false;	}
}
