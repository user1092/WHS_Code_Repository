package whs.yourchoice.client;

import whs.yourchoice.audio.AudioPlayer;
import whs.yourchoice.utilities.SimpleTimer;
import javafx.scene.Node;

public class SlideTimingControl {
	
	private Node tempNode;
	private AudioPlayer tempAudioPlayer;
	private int time;
	private boolean visible;
	private SimpleTimer displayTimer;
	private Boolean displayTimerDone = null;
	private Boolean wasRunning = false;
	
	
	public SlideTimingControl(Node tempNode, int time, boolean visible) {
		this.tempNode = tempNode;
		this.time = time;
		this.visible = visible;
		displayTimer = new SimpleTimer();
	}
	
	public SlideTimingControl(AudioPlayer tempAudioPlayer, int time, boolean visible) {
		this.tempAudioPlayer = tempAudioPlayer;
		this.time = time;
		this.visible = visible;
		displayTimerDone = null;
		displayTimer = new SimpleTimer();
	}
	
	public Boolean start() {		
		// Thread to run both timers in, to not stall main program
//		Thread timingThread = new Thread("Timing") {
//			public void run() {
				System.out.println("time for timer: " + time);
				if (time > 0) {
					System.out.println("starting timer");
					wasRunning = true;
					displayTimerDone = displayTimer.startTimer(time);
					wasRunning = false;
					System.out.println("timer line finished");
				}
				else {
					displayTimerDone = true;
				}
				
				System.out.println("timer done is " + displayTimerDone);
				
				if ((true == displayTimerDone) && !(null == tempNode)) {
					tempNode.setVisible(visible);
					System.out.println("node is visible: " + visible);
				}
				if ((true == displayTimerDone) && (null == tempNode)) {
					if (visible) {
						tempAudioPlayer.playAudio();
					}
					else {
						tempAudioPlayer.stopAudio();
					}
					System.out.println("audioPlayer is Playing: " + visible);
				}
				System.out.println("END OF TIMING THREAD IN STC");
//			}
//		};
//		timingThread.start();
		
		System.out.println("displayTimerDone: " + displayTimerDone);
		
		// Wait until the timer is finished to return the value
		while(null == displayTimerDone) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("returning from STC: " + displayTimerDone);
		return displayTimerDone;
	}
	
	public void stop() {
		System.out.println("stopping timer");
		displayTimer.stopTimer();
	}
	
	public void pause() {
		System.out.println("pausing in STC");
		displayTimer.pauseTimer();
		System.out.println("paused in STC");
	}
	
	public void resume() {
		displayTimer.resumeTimer();
	}
	
	/**
	 * @return the timerIsRunning
	 */
	public Boolean isRunning() {
		return displayTimer.isRunning();
	}
	
	/**
	 * @return the timerWasRunning
	 */
	public Boolean wasRunning() {
		return wasRunning;
	}

}
