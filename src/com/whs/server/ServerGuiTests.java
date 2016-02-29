/**
 * ServerGuiTests.java		v0.6 23/02/2016
 * 
 * 
 */
package com.whs.server;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.junit.Test;

/**
 * Class for testing the server's front end controlling the back end through buttons. 
 * 
 * NOT FOR RELEASE!
 * 
 * @author		user1092, guest501
 * @version		v0.6 23/02/2016
 */
public class ServerGuiTests {
	
	/**
	 * Method to test that the open sockets and close sockets work.
	 */
	@Test
	public void socketShouldOpenAndCloseOnClickingButtons() {
		ServerGui serverGui;
		serverGui = new ServerGui();
		
		Robot bot = null;
		
		try {
			bot = new Robot();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Move the mouse over the open sockets button.
		bot.mouseMove(200,35);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		// Wait for 250ms to allow the press to be registered.
		try{Thread.sleep(250);}catch(InterruptedException e){}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		// Wait for 250ms to allow the release to be registered.
		try{Thread.sleep(250);}catch(InterruptedException e){}
		
		assertEquals(true,serverGui.areSocketsOpen());
		
		// Move the mouse over the close sockets button.
		bot.mouseMove(350,35);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		// Wait for 250ms to allow the press to be registered. 
		try{Thread.sleep(250);}catch(InterruptedException e){}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		// Wait for 250ms to allow the release to be registered.
		try{Thread.sleep(250);}catch(InterruptedException e){}
		
		assertEquals(false,serverGui.areSocketsOpen());
	}

}
