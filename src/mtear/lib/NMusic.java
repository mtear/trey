package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class NMusic {
	private Music music;
	public NMusic(String name){
		music = Gdx.audio.newMusic(Gdx.files.internal("data/" + name + ".mp3"));
		GameBase._allmusics.add(music);
	}
	public void play(){
		music.play();
	}
	public void stop(){
		music.stop();
	}
	public void setLooping(boolean looping){
		music.setLooping(looping);
	}
}