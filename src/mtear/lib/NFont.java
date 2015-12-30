/**
	Author: Nic Wilson
	File: mtear/lib/NFont.java

	A class for handling LibGDX format font files
*/
package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
	A class for handling LibGDX format font files
*/
public class NFont {

	//Hold the font internally as a BitmapFont (LibGDX)
	private BitmapFont font;

	/**
		Basic constructor
		Load the font from the data files given the name argument
		Register the font afterward
	*/
	public NFont(String name){
		font = new BitmapFont(Gdx.files.internal("data/" + name + ".fnt"),
			false);
		GameBase._allfonts.add(font);
	}

	/**
		getFont
		Return the font
	*/
	public BitmapFont getFont(){
		return font;
	}

	/**
		draw
		Draw text with the held font at the given location on the main screen
	*/
	public void draw(String text, float x, float y){
		font.draw(GameBase._batch, text, x, y);
	}
}
