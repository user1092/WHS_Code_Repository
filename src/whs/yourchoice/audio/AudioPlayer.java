/**
 * 
 */
package whs.yourchoice.audio;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Class for the client's back end handling playing audio files 
 * 
 * @author		user1092, guest501
 * @version		v0.2 04/03/2016
 */
public class AudioPlayer {
	
	private EmbeddedMediaPlayer mediaPlayer;
	
	/**
	 * Constructor
	 * 
	 * Create a new player from the mediaPlayerFactory.
	 * 
	 * @param mediaPlayerFactory
	 */
	public AudioPlayer(MediaPlayerFactory mediaPlayerFactory) {
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
	}
	
	/**
	 * Method to load a local audio file ready to play.
	 * 
	 * @param audioFilename	-	The path to the audio file to be played
	 */
	public void loadLocalAudio(String audioFilename) {
		mediaPlayer.prepareMedia(audioFilename);
	}
	
	/**
	 * Method to play audio from an RTP port
	 * 
	 * @param host	-	The IP address of the server
	 * @param rtpPort	-	The RTP port the server is streaming audio on
	 */
	public void playStreamedAudio(String host, int rtpPort) {
		mediaPlayer.playMedia("rtp://@"+ host + ":" + rtpPort);
	}
	
	/**
	 * Method to Play or Pause the Audio depending on the current state.
	 */
	public void playPauseAudio() {
		if(mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		} else {
			mediaPlayer.play();
		}
	}
	
	/**
	 * Method to play a loaded audio file.
	 */
	public void playAudio() {
		mediaPlayer.play();
	}
	
	/**
	 * Method to play an audio file without first loading.
	 * 
	 * @param audioFile	-	The audio file to be played
	 */
	public void playAudio(String audioFile) {
		mediaPlayer.playMedia(audioFile);
	}
		
	/**
	 * Method to pause a loaded audio file.
	 */
	public void pauseAudio() {
		mediaPlayer.pause();
	}
	
	/**
	 * Method to stop a loaded audio file.
	 */
	public void stopAudio() {
		mediaPlayer.stop();
	}
	
	/**
	 * Method to mute a loaded audio file.
	 * 
	 * @param mute	-	boolean value set true to mute audio, else false.
	 */
	public void muteAudio(boolean mute) {
		mediaPlayer.mute(mute);
	}
	
	/**
	 * Method to toggle mute for a loaded audio file.
	 */
	public void toggleMuteAudio() {
		mediaPlayer.mute();
	}
	
	/**
	 * Method to set the volume of the audio player.
	 * 
	 * @param level - Volume level from 0 to 200, above 100 may result in distorted audio.
	 */
	public void setAudioVolume(int level) {
		mediaPlayer.setVolume(level);
	}
	
	public int getAudioVolume() {
		return mediaPlayer.getVolume();
	}
	
	/**
	 * Method to loop an audio file
	 * 
	 * @param loop	-	boolean value set true to loop audio, else false.
	 */
	public void loopAudio(boolean loop) {
		mediaPlayer.setRepeat(loop);
	}
	
	/**
	 * Method to toggle loop for an audio file
	 */
	public void toggleLoopAudio() {
		if(mediaPlayer.getRepeat()) {
			mediaPlayer.setRepeat(false);
		} else {
			mediaPlayer.setRepeat(true);
		}
		
	}

	/**
	 * Method to clean up and release all players
	 */
	public void releasePlayer() {
		mediaPlayer.stop();
		mediaPlayer.release();
	}
	
}
