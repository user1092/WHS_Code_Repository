package whs.yourchoice.utilities;

import java.util.Timer;
import java.util.TimerTask;

/**
* 
* @author user828 & user1092
* @version v0.1 28/03/16
*/

public class SimpleTimer{
	

	private Boolean timerDone = false;
	
	public boolean timer(Integer time) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				timerDone = true;
			}
		}, time);
		while(!timerDone);
		return timerDone;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
