/**
 * 
 */
package com.whs.client;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

/**
 * Class for the client's back end handling playing audio files 
 * 
 * @author		user1092, guest501
 * @version		v0.1 01/03/2016
 */
public class AudioPlayer {
	
	private HeadlessMediaPlayer headlessPlayer;
	
	/**
	 * Constructor
	 * 
	 * Point to the VLC library path and create a new player.
	 */
	public AudioPlayer(MediaPlayerFactory mediaPlayerFactory) {
		headlessPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
	}
	
	/**
	 * Method to load a local audio file ready to play.
	 * 
	 * @param audioFilename	-	The path to the audio file to be played
	 */
	public void loadLocalAudio(String audioFilename) {
		headlessPlayer.prepareMedia(audioFilename);
	}
	
	public void playStreamedAudio() {
		String host = "127.0.0.1";
		int rtpPort = 5555;
		headlessPlayer.playMedia("rtp://@"+ host + ":" + rtpPort);
	}
	
	/**
	 * Method to Play or Pause the Audio depending on the current state.
	 */
	public void playPauseAudio() {
		if(headlessPlayer.isPlaying()) {
			headlessPlayer.pause();
		} else {
			headlessPlayer.play();
		}
	}
	
	/**
	 * Method to play a loaded audio file.
	 * 
	 * loadLocalAudio must be called first!
	 */
	public void playAudio() {
		headlessPlayer.play();
	}
	
	/**
	 * Method to pause a loaded audio file.
	 */
	public void pauseAudio() {
		headlessPlayer.pause();
	}
	
	/**
	 * Method to stop a loaded audio file.
	 */
	public void stopAudio() {
		headlessPlayer.stop();
	}
	
	/**
	 * Method to mute a loaded audio file.
	 * 
	 * @param mute	-	boolean value set true to mute audio, else false.
	 */
	public void muteAudio(boolean mute) {
		headlessPlayer.mute(mute);
	}
	
	/**
	 * Method to toggle mute for a loaded audio file.
	 */
	public void toggleMuteAudio() {
		headlessPlayer.mute();
	}
	
	/**
	 * Method to set the volume of the audio player.
	 * 
	 * @param level - Volume level from 0 to 200, above 100 may result in distorted audio.
	 */
	public void setAudioVolume(int level) {
		headlessPlayer.setVolume(level);
	}
	
	/**
	 * Method to loop an audio file
	 * 
	 * @param loop	-	boolean value set true to loop audio, else false.
	 */
	public void loopAudio(boolean loop) {
		headlessPlayer.setRepeat(loop);
	}
	
	/**
	 * Method to toggle loop for an audio file
	 */
	public void toggleLoopAudio() {
		if(headlessPlayer.getRepeat()) {
			headlessPlayer.setRepeat(false);
		} else {
			headlessPlayer.setRepeat(true);
		}
		
	}

	/**
	 * Method to clean up and release all players
	 * 
	 * Must be called on destruction of window
	 */
	public void releasePlayer(MediaPlayerFactory mediaPlayerFactory) {
		headlessPlayer.stop();
		headlessPlayer.release();
		mediaPlayerFactory.release();
	}
	
}
