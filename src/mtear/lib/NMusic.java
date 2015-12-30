/**
	Author: Nic Wilson
	File: mtear/lib/NMusic.java

	A class for handling music in LibGDX
*/
package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
	A class for handling music in LibGDX
*/
public class NMusic {

	//Internal LibGDX music object
	private Music music;

	/**
		Basic constructor
		Load the music file from the data files given the mp3 file name
		Register the music afterward
	*/
	public NMusic(String name){
		music = Gdx.audio.newMusic(Gdx.files.internal("data/" + name + ".mp3"));
		GameBase._allmusics.add(music);
	}

	/**
		play
		Play the music
	*/
	public void play(){
		music.play();
	}

	/**
		stop
		Stop playing the music
	*/
	public void stop(){
		music.stop();
	}

	/**
		setLooping
		Determine if the music should loop once finished
		@param looping True if the music should loop,
			false otherwise
	*/
	public void setLooping(boolean looping){
		music.setLooping(looping);
	}
}