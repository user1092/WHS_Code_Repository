package whs.yourchoice.utilities;

import javafx.scene.Node;

public class SlideTimingControl {
	
	private Node tempNode;
	private int startTime;
	private int duration;
	private SimpleTimer displayTimer;
	
	
	public SlideTimingControl(Node tempNode, int startTime, int duration) {
		this.tempNode = tempNode;
		this.startTime = startTime;
		this.duration = duration;
	}
	
	public void start() {
		// Thread to pause timer under test after interactTime
		Thread timingThread = new Thread("Timing") {
			public void run() {
				boolean displayTimerDone = false;
				displayTimer = new SimpleTimer();
				displayTimerDone = displayTimer.startTimer(startTime);
				
				if (true == displayTimerDone) {
					tempNode.setVisible(true);
					
					boolean hideTimerDone = false;
					//displayTimer = new SimpleTimer();
					hideTimerDone = displayTimer.startTimer(duration);
					
					if (true == hideTimerDone) {
						tempNode.setVisible(false);
					}
				}
			}
		};
		timingThread.start();
	}
	
	public void stop() {
		displayTimer.stopTimer();
	}
	
	public void pause() {
		displayTimer.pauseTimer();
	}
	
	public void resume() {
		displayTimer.resumeTimer();
	}

}
