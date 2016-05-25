/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import whs.yourchoice.audio.AudioPlayer;
import whs.yourchoice.utilities.SimpleTimer;
import whs.yourchoice.video.VideoPlayer;
import javafx.application.Platform;
import javafx.scene.Node;

/**
* Class for controlling objects on a slide.
* 
* Can set nodes to appear or disappear.
* Can set audio to play or stop.
* 
* @author cd828 & ch1092
* @version v0.3 13/05/16
*/
public class ObjectTimingControl {
	
	private Node tempNode;
	private VideoPlayer tempVideoPlayer;
	private AudioPlayer tempAudioPlayer;
	private int masterVolume;
	private boolean masterMute;
	private int time;
	private boolean visible;
	private SimpleTimer displayTimer;
	private Boolean displayTimerDone = null;
	private Boolean wasRunning = false;
	
	
	/**
	 * Constructor used to setup for a node
	 * 
	 * @param tempNode
	 * @param time
	 * @param visible
	 */
	public ObjectTimingControl(Node tempNode, int time, boolean visible) {
		this.tempNode = tempNode;
		this.time = time;
		this.visible = visible;
		displayTimer = new SimpleTimer();
	}
	
	public ObjectTimingControl(Node tempNode, int time, boolean visible, VideoPlayer tempVideoPlayer) {
		this.tempNode = tempNode;
		this.time = time;
		this.visible = visible;
		this.tempVideoPlayer = tempVideoPlayer;
		displayTimer = new SimpleTimer();
	}
	
	/**
	 * Constructor used to setup for an audioPlayer
	 * 
	 * @param tempAudioPlayer
	 * @param time
	 * @param visible
	 */
	public ObjectTimingControl(AudioPlayer tempAudioPlayer, int time, boolean visible, int masterVolume, boolean masterMute) {
		this.tempAudioPlayer = tempAudioPlayer;
		this.time = time;
		this.visible = visible;
		this.masterMute = masterMute;
		this.masterVolume = masterVolume;
		displayTimerDone = null;
		displayTimer = new SimpleTimer();
	}
	
	public Boolean start() {
		// Only run timer if time greater than zero
		if (time > 0) {
			wasRunning = true;
			displayTimerDone = displayTimer.startTimer(time);
			wasRunning = false;
		}
		else {
			displayTimerDone = true;
		}
		
		// Adjust visibility of node or play/stop audio
		if ((true == displayTimerDone) && !(null == tempNode)) {
			tempNode.setVisible(visible);
			System.out.println("Node Name: " + tempNode.getId());
			if ((null != tempVideoPlayer) && (visible)) {
				Platform.runLater(new Runnable() {
					@Override public void run() {
						tempVideoPlayer.playVideo();
					}
				});
			}
			if ((null != tempVideoPlayer) && !(visible)) {
				Platform.runLater(new Runnable() {
					@Override public void run() {
						tempVideoPlayer.stopVideo();
					}
				});
			}
		}
		if ((true == displayTimerDone) && (null == tempNode)) {
			if (visible) {
				tempAudioPlayer.playAudio();
				// Fixes "feature" with vlc where audio is not observed if not playing
				while(-1 == tempAudioPlayer.getAudioVolume()) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				tempAudioPlayer.setAudioVolume(masterVolume);
				tempAudioPlayer.muteAudio(masterMute);
			}
			else {
				tempAudioPlayer.stopAudio();
			}
		}
		
		// Wait until the timer is finished to return the value
		while(null == displayTimerDone) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return displayTimerDone;
	}
	
	/**
	 * Method to stop the timer
	 */
	public void stop() {
		displayTimer.stopTimer();
	}
	
	/**
	 * Method to pause the timer
	 */
	public void pause() {
		displayTimer.pauseTimer();
	}
	
	/**
	 * Method to resume the timer
	 */
	public void resume() {
		displayTimer.resumeTimer();
	}
	
	/**
	 * Method to check if the timer is running
	 * 
	 * @return the timerIsRunning
	 */
	public Boolean isRunning() {
		return displayTimer.isRunning();
	}
	
	/**
	 * Method to see if the timer was running but was paused
	 * 
	 * @return the timerWasRunning
	 */
	public Boolean wasRunning() {
		return wasRunning;
	}

}
