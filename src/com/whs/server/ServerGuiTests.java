/**
 * 
 */
package com.whs.server;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.junit.Test;

/**
 * @author ch1092
 *
 */
public class ServerGuiTests {

	@Test
	public void socketShouldOpenOnClickingOpenSockets() {
		ServerGui serverGui;
		serverGui = new ServerGui();
		
		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bot.mouseMove(200,35);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		//add time between press and release or the input event system may 
		//not think it is a click
		try{Thread.sleep(250);}catch(InterruptedException e){}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		try{Thread.sleep(250);}catch(InterruptedException e){}
				
		assertEquals(true,serverGui.areSocketsOpen());
		
		ServerGui.server.closeSockets();
		
		bot.mouseMove(600,10);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		//add time between press and release or the input event system may 
		//not think it is a click
		try{Thread.sleep(250);}catch(InterruptedException e){}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	@Test
	public void socketShouldCloseOnClickingCloseSockets() {
		ServerGui serverGui;
		serverGui = new ServerGui();
		Robot bot = null;
		
		try {
			bot = new Robot();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bot.mouseMove(200,35);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		//add time between press and release or the input event system may 
		//not think it is a click
		try{Thread.sleep(250);}catch(InterruptedException e){}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		try{Thread.sleep(250);}catch(InterruptedException e){}
		
		assertEquals(true,serverGui.areSocketsOpen());
		
		try {
			bot = new Robot();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bot.mouseMove(350,35);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		//add time between press and release or the input event system may 
		//not think it is a click
		try{Thread.sleep(250);}catch(InterruptedException e){}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		try{Thread.sleep(250);}catch(InterruptedException e){}
				
		assertEquals(false,serverGui.areSocketsOpen());
	}

}
