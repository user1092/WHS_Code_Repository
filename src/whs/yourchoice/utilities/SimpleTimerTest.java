/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

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
	
	private SimpleTimer timer;
	
	private Boolean timerDone;
	
	private Integer runTime = 12000;
	private Integer interactTime = 3000;
	private Integer upperLimit;
	private Integer lowerLimit;
	
	private Long timeTaken;
	
	
	private Double tenPercentOfRunTime;
	private Double tenPercentOfInteractTime;
	
	
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
		
		tenPercentOfInteractTime = (interactTime * 0.1);
		
		upperLimit = interactTime + Integer.valueOf(tenPercentOfInteractTime.intValue());
		
		lowerLimit = interactTime - Integer.valueOf(tenPercentOfInteractTime.intValue());
		
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
				timer.pauseTimer();
				pauseTimer.startTimer(interactTime);
				timer.resumeTimer();
				
				pauseTimer.startTimer(interactTime);
				timer.pauseTimer();
				pauseTimer.startTimer(interactTime);
				timer.resumeTimer();
				
				pauseTimer.startTimer(interactTime);
				timer.pauseTimer();
				pauseTimer.startTimer(interactTime);
				timer.resumeTimer();
			}
		};
		pauseThread.start();
		
		timerDone = timer.startTimer(runTime);
		
		Long endTime = System.nanoTime();
		
		timeTaken = ((endTime - startTime) / 1000000);
		
		tenPercentOfInteractTime = (interactTime * 0.1);
		
		upperLimit = interactTime + interactTime + interactTime + runTime 
						+ Integer.valueOf(tenPercentOfInteractTime.intValue());
		
		lowerLimit = interactTime + interactTime + interactTime + runTime 
						- Integer.valueOf(tenPercentOfInteractTime.intValue());
				
		assertTrue(Integer.valueOf(timeTaken.intValue()) >= lowerLimit);
		assertTrue(Integer.valueOf(timeTaken.intValue()) <= upperLimit);
		assertEquals(true, timerDone);
	}
}
