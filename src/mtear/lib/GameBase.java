/**
	Author: Nic Wilson
	File: mtear/lib/GameBase.java

	An application launcher for the LibGDX library
	Works with the agile mtear library for ease of game creation
*/
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

/**
	GameBase
	The main class entry point for the game.
	Extended by subclasses to implement a game object.
*/
public abstract class GameBase implements ApplicationListener, GestureListener{

	//Static references to game assets
	public static ArrayList<Texture> _alltextures = new ArrayList<Texture>();
	public static ArrayList<Sound> _allsounds = new ArrayList<Sound>();
	public static ArrayList<Music> _allmusics = new ArrayList<Music>();
	public static ArrayList<BitmapFont> _allfonts = new ArrayList<BitmapFont>();
	public static SpriteBatch _batch;
	//Static reference eto the device screen width and height
	public static int _W, _H;
	//Game save settings
	Preferences _savesettings;
	//Background color rgb
	private float cr=0, cg=0, cb=0;

	/**
		create
		Occurs when the game is created.
		Initializes asset variables and needed values, 
		initializes any subclass objects, and sets up Input detection
	*/
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
	
	/** 
		Subclass method for initializing game objects 
	*/
	protected abstract void init();

	/**
		resize
		Occurs when the plane is resized. Commented out for Android.
	*/
	@Override
	public void resize(int width, int height) {
		//_W = width;
		//_H = height;
	}
	
	/**
		Npow
		A simple rewriting of Math.pow.
		(the Math.pow method has known issues in this version of LibGDX)
	*/
	public static int Npow(int b, int e){
		if(e <= 0) return 1;
		if(e == 1) return b;
		else return b * Npow(b, e-1);
	}

	/**
		render
		The main game loop method.
		Update the game state using the abstract method
			then clear and redraw the screen.
	*/
	@Override
	public void render() {
		update();
		Gdx.gl.glClearColor(cr, cg, cb, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		_batch.begin();
		draw();
		_batch.end();
	}
	
	/**
		setClearColor
		Change the default background color of the Game.
		The parameters are rgb numerical values.
	*/
	public void setClearColor(float cr, float cg, float cb){
		this.cr = cr;
		this.cg = cg;
		this.cb = cb;
	}
	
	/**
		Abstract methods for the subclass Update and Draw methods
		Update is where the game logic updates each frame.
		Draw is how the game chooses to draw its resources.
	*/
	protected abstract void update();
	protected abstract void draw();

	/**
		pause (UNUSED)
		Part of the LibGDX superclass
		Logic for when the game is paused
	*/
	@Override
	public void pause() {
	}

	/**
		resume (UNUSED)
		Part of the LibGDX superclass
		Logic for when the game is resumed from pause
	*/
	@Override
	public void resume() {
	}

	/**
		dispose
		Called as the Game is closing
		Dispose of all the textures and game assets before calling destroy
	*/
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
	
	/**
		Abstract method for the subclass
		Called as the game finishes exiting
	*/
	protected abstract void destroy();
	
	/**
		Overrideable gesture methods for pinch, zoom, and longPress
	*/
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
		Vector2 pointer1, Vector2 pointer2) {	return false;	}
	public boolean zoom(float initialDistance, float distance) { return false;}
	public boolean longPress(float x, float y) {  return false;	}
}
