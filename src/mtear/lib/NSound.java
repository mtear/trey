package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class NSound {
	private Sound sound;
	public NSound(String name){
		sound = Gdx.audio.newSound(Gdx.files.internal("data/" + name + ".mp3"));
		GameBase._allsounds.add(sound);
	}
	public void play(){
		sound.play();
	}
}
