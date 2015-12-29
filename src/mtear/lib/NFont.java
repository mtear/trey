package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class NFont {
	private BitmapFont font;
	public NFont(String name){
		font = new BitmapFont(Gdx.files.internal("data/" + name + ".fnt"),false);
		GameBase._allfonts.add(font);
	}
	public BitmapFont getFont(){
		return font;
	}
	public void draw(String text, float x, float y){
		font.draw(GameBase._batch, text, x, y);
	}
}
