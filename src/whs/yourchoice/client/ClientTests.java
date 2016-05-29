/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import whs.yourchoice.presentation.RegisteredModuleEntry;
import whs.yourchoice.server.ServerInterfacer;

/**
 * Class for testing the clients back end handling communications to the server.
 * 
 * NOT FOR RELEASE!
 * 
 * @author		ch1092, skq501
 * @version		v0.7 06/04/2016
 */
public class ClientTests {

	// Declare a client to be used
	Client client;
	
	// Declare the host and port of the server
	String host = "127.0.0.1";
	int port = 1138;
	
	/*
	 *  Declare a server to be used, must use the ServerInterfacer class
	 *  in order to access the internal methods. 
	 */
	ServerInterfacer server;
	
	// Declare object relating to the item receive as access from within a thread.
	ObjectInputStream inputStream = null;
	Object itemReceived = null;
	
	
	/**
	 * Before every test, a server should be instantiated and sockets opened then
	 * wait for a clients connection.
	 * A client should then be instantiated and the sockets opened.
	 */
	@Before
	public void setup() {
		server = new ServerInterfacer();
		server.openSocket();
		server.checkAndAcceptClientConnections();
		
		client = new Client();
		try {
			client.openSocket(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * After every test, the client's sockets should close
	 * then the server's sockets should close. 
	 */
	@After
	public void closeSockets() {
		//client.closeSocket();
		
		server.closeSocket();
		
		client.closeSocket();
	}
		
	
	/**
	 * Test that a client can send data on an open socket.
	 * 
	 * clientShouldReceieveRequestedModule shows this works,
	 * Client and Server evolved and broke this test
	 */
	@Ignore
	@Test
	public void clientShouldSendDataOnAnOpenSocket() {
		Object itemToSend = "HEYYY";
		
		// Thread for test to listen to Client
		Thread listenThread = new Thread("Listen") {
			public void run() {
				try {
					// Can no longer do as server listens to client as soon as it connects
					//itemReceived = server.receiveData(client.getID());
					// Now Server will pass back the object as soon as it receives it
					itemReceived = client.receiveData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		listenThread.start();
		
		// Send data from the client to the server
		try {
			client.sendData(itemToSend);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Wait until the server has received the data.
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
	}

	
	/**
	 * Test that a client can receive data from an open socket.
	 * 
	 * clientShouldReceieveModuleList shows this works,
	 * Client and Server evolved and broke this test
	 */
	@Ignore
	@Test 
	public void clientShouldReceiveDataFromAnOpenSocket() {
		Object itemToSend = "HEYYY";
			
		/* Thread for client to listen to test method
		 * check the client receives the string HEYYY
		 */
		Thread listenThread = new Thread("Listen") {
			public void run() {
				try {
					itemReceived = client.receiveData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		listenThread.start();
		
		// Write the object to the client
		server.sendData(itemToSend, client.getID());
				
		// Wait until the thread is finished to ensure client has received the data
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
		
	}
	
	
	/**
	 * Test that the client gets assigned the ID 0 as it should be the only client connected.
	 */
	@Test
	public void clientShouldReceieveAnID() {
		System.out.println("Client ID: " + client.getID());
		assertEquals(0, client.getID());
	}

	/**
	 * Test to assure that the client receives a list of modules from the server and
	 * parses them correctly
	 */
	@Test
	public void clientShouldReceieveModuleList() {
		//searching for individual module
		RegisteredModuleEntry module = client.moduleList.searchModuleCode("ELE00004C");
				
		assertEquals("ELE00004C", module.getCode());
		System.out.println(module.getCode());
		assertEquals("Electronics", module.getCourse());
		System.out.println(module.getCourse());
		assertEquals("Electronics and Electromagnetics", module.getStream());
		System.out.println(module.getStream());
		assertEquals(1, module.getYear());
		System.out.println(module.getYear());
		assertEquals("Analogue Electronics", module.getTitle());
		System.out.println(module.getTitle());
		assertEquals("AnalogueElectronics.zip", module.getFileName());
		System.out.println(module.getFileName());
				
		// searching for a different module
		module = client.moduleList.searchModuleCode("PH000079");
				
		assertEquals("PH000079", module.getCode());
		System.out.println(module.getCode());
		assertEquals("Philosophy", module.getCourse());
		System.out.println(module.getCourse());
		assertEquals("Bhuddist Philosophy", module.getStream());
		System.out.println(module.getStream());
		assertEquals(1, module.getYear());
		System.out.println(module.getYear());
		assertEquals("Philosophy in the Bhuddist World", module.getTitle());
		System.out.println(module.getTitle());
		assertEquals("bhud_phil.zip", module.getFileName());
		System.out.println(module.getFileName());
	}
	
	
	/**
	 * Test to assure that the client receives a list of modules from the server and
	 * parses them correctly
	 */
	@Test
	public void clientShouldReceieveRequestedModule() {
		try {
			client.checkPassword("Guest", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.sendData("Example_Presentation.zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File zippedPresentation = null;
		try {
			zippedPresentation = (File) client.receiveData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(zippedPresentation.getName().equals("Example_Presentation.zip"));
	}
}
