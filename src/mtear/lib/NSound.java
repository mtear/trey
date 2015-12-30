/**
	Author: Nic Wilson
	File: mtear/lib/NSound.java

	A class for handling a LibGDX sound file
*/
package mtear.lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
	A class for handling a LibGDX sound file
*/
public class NSound {

	//Internal LibGDX sound object
	private Sound sound;

	/**
		Basic constructor
		Load the sound file from the data files given the name of the mp3 file
		Register the sound afterward
	*/
	public NSound(String name){
		sound = Gdx.audio.newSound(Gdx.files.internal("data/" + name + ".mp3"));
		GameBase._allsounds.add(sound);
	}

	/**
		play
		Play the sound
	*/
	public void play(){
		sound.play();
	}
}
