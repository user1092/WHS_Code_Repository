package whs.yourchoice.utilities;

import java.util.Timer;
import java.util.TimerTask;

/**
* Class for creating a simple timer that returns true when complete or false when cancelled.
* 
* @author cd828 & ch1092
* @version v0.1 06/04/16
*/
public class SimpleTimer{
	
	private Timer timer;
	private Boolean timerDone = false;
	private Boolean cancelled = false;
	
	
	/**
	 * Method to start a timer incrementing to the specified time.
	 * 
	 * @param time	-	The time for the timer to increment to
	 * @return			true if the timer succeeded, false if the timer was cancelled	
	 */
	public Boolean startTimer(Integer time) {
		timer(time);
		while(!timerDone && !cancelled);
		return timerDone;
		
	}
	
	
	/**
	 * Method to start a TimerTask setting timerDone true when complete.
	 * 
	 * @param time	-	The amount of time the timer should run before timerDone is set true
	 */
	private void timer(Integer time) {
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				timerDone = true;
			}
		}, time);
	}


	/**
	 * Method to stop the running timer.
	 */
	public void stopTimer() {
		cancelled = true;
		timer.cancel();
	}


	public void pauseTimer() throws InterruptedException {
		System.out.println("tell to pause");
		timer.wait();
		System.out.println("paused");
	}


	public void resumeTimer() {
		System.out.println("tell to unpause");
		timer.notifyAll();
		System.out.println("unpaused");
	}
	
	
}
