package whs.yourchoice.utilities;

import static org.junit.Assert.*;

import java.util.Timer;

import org.junit.Test;

public class SimpleTimerTest {
	
	Integer time = 12000;
	
	boolean isTimerDone;
	
	@Test
	public void setup() {

		long startTime = System.nanoTime();
		System.out.println(startTime);
		SimpleTimer timer = new SimpleTimer();
		while(timer.timer(time));
		long endTime = System.nanoTime();
		System.out.println(endTime);
		
		//assertEquals((endTime - startTime), time);
	}
}
