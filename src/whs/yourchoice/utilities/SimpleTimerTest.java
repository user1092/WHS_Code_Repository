package whs.yourchoice.utilities;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Class for testing the SimpleTimer class.
 * 
 * NOT FOR RELEASE!
 * 
 * @author		ch1092, cd828
 * @version		v0.1 06/04/2016
 */
public class SimpleTimerTest {
	
	SimpleTimer timer;
	
	Boolean timerDone;
	
	Integer runTime = 12000;
	Integer interactTime = 6000;
	Integer upperLimit;
	Integer lowerLimit;
	
	Long timeTaken;
	
	
	Double tenPercentOfRunTime;
	Double tenPercentOfCancelTime;
	
	
	/**
	 * Test that the timer runs for the specified time and returns true.
	 */
	@Test
	public void timerShouldRunForSpecifiedTime() {

		Long startTime = System.nanoTime();
		
		timer = new SimpleTimer();
		
		timerDone = timer.startTimer(runTime);
		
		Long endTime = System.nanoTime();
		
		timeTaken = ((endTime - startTime) / 1000000);
		
		tenPercentOfRunTime = (runTime * 0.1);
		
		upperLimit = runTime + Integer.valueOf(tenPercentOfRunTime.intValue());
		
		lowerLimit = runTime - Integer.valueOf(tenPercentOfRunTime.intValue());
		
		assertTrue(Integer.valueOf(timeTaken.intValue()) >= lowerLimit);
		assertTrue(Integer.valueOf(timeTaken.intValue()) <= upperLimit);
		assertEquals(true, timerDone);
	}
	
	
	/**
	 * Tests that a timer can be cancelled and returns false.
	 */
	@Test
	public void timerShouldBeAbleToBeCancelled() {

		Long startTime = System.nanoTime();
		
		timer = new SimpleTimer();
		
		// Thread to cancel timer under test after interactTime
		Thread cancelThread = new Thread("Cancel") {
			public void run() {
				SimpleTimer cancelTimer = new SimpleTimer();
				cancelTimer.startTimer(interactTime);
				timer.stopTimer();
			}
		};
		cancelThread.start();
		
		timerDone = timer.startTimer(runTime);
		
		Long endTime = System.nanoTime();
		
		timeTaken = ((endTime - startTime) / 1000000);
		
		tenPercentOfCancelTime = (interactTime * 0.1);
		
		upperLimit = interactTime + Integer.valueOf(tenPercentOfCancelTime.intValue());
		
		lowerLimit = interactTime - Integer.valueOf(tenPercentOfCancelTime.intValue());
		
		assertTrue(Integer.valueOf(timeTaken.intValue()) >= lowerLimit);
		assertTrue(Integer.valueOf(timeTaken.intValue()) <= upperLimit);
		assertEquals(false, timerDone);
	}
	
	
	/**
	 * Tests that a timer can be pause and resumed and returns true.
	 */
	@Test
	public void timerShouldBeAbleToBePaused() {

		Long startTime = System.nanoTime();
		
		timer = new SimpleTimer();
		
		// Thread to pause timer under test after interactTime
		Thread pauseThread = new Thread("Pause") {
			public void run() {
				SimpleTimer pauseTimer = new SimpleTimer();
				pauseTimer.startTimer(interactTime);
				try {
					timer.pauseTimer();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pauseTimer.startTimer(interactTime);
				timer.resumeTimer();
			}
		};
		pauseThread.start();
		
		timerDone = timer.startTimer(runTime);
		
		Long endTime = System.nanoTime();
		
		timeTaken = ((endTime - startTime) / 1000000);
		
		tenPercentOfCancelTime = (interactTime * 0.1);
		
		upperLimit = interactTime + runTime 
						+ Integer.valueOf(tenPercentOfCancelTime.intValue());
		
		lowerLimit = interactTime + runTime 
						- Integer.valueOf(tenPercentOfCancelTime.intValue());
		
		assertTrue(Integer.valueOf(timeTaken.intValue()) >= lowerLimit);
		assertTrue(Integer.valueOf(timeTaken.intValue()) <= upperLimit);
		assertEquals(true, timerDone);
	}
}
