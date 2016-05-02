package whs.yourchoice.utilities;

import java.util.Timer;
import java.util.TimerTask;

/**
* Class for creating a simple timer that returns true when complete or false when cancelled.
* 
* @author cd828 & ch1092
* @version v0.2 28/04/16
*/
public class SimpleTimer{
	
	private Timer timer;
	
	private Boolean timerDone = false;
	private Boolean cancelled = false;
	private Boolean timerIsRunning = false;
	
	private Integer timeRemaining;
	private Long startTime;
	private Long pausedTime;
	
	
	/**
	 * Method to start a timer incrementing to the specified time.
	 * 
	 * @param time	-	The time for the timer to increment to
	 * @return			true if the timer succeeded, false if the timer was cancelled	
	 */
	public Boolean startTimer(Integer time) {
		timerDone = false;
		cancelled = false;		
		
		timeRemaining = time;
		startTime = System.nanoTime();

		timer(time);
		
		while(!timerDone && !cancelled) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return timerDone;
	}
	
	
	/**
	 * Method to start a TimerTask setting timerDone true when complete.
	 * 
	 * @param time	-	The amount of time the timer should run before timerDone is set true
	 */
	private void timer(Integer time) {
		timerIsRunning = true;
		timer = new Timer();
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
		timerIsRunning = false;
		timer.cancel();
		timer.purge();
	}


	/**
	 * Method to pause the running timer
	 */
	public void pauseTimer() {
		pausedTime = System.nanoTime();
		timer.cancel();
		timer.purge();
		timerIsRunning = false;
		Long timeTaken = ((pausedTime - startTime) / 1000000);
		timeRemaining = timeRemaining - Integer.valueOf(timeTaken.intValue());
	}
	
	/**
	 * Method to resume a paused timer
	 */
	public void resumeTimer() {
		startTime = System.nanoTime();
		timer(timeRemaining);
	}
	
	/**
	 * Method to see if the timer is running
	 * 
	 * @return the timerIsRunning
	 */
	public Boolean isRunning() {
		return timerIsRunning;
	}
}
