/**
 * ClientTests.java		v0.6 23/02/2016
 * 
 * 
 */

package com.whs.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whs.server.ServerInterfacer;

/**
 * Class for testing the clients back end handling communications to the server.
 * 
 * NOT FOR RELEASE!
 * 
 * @author		user1092, guest501
 * @version		v0.6 23/02/2016
 */
public class ClientTests {

	// Declare a client to be used
	Client client;
	
	//Declare the host and port of the server
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
		client.openSocket(host, port);
	}
	
	/**
	 * After every test, the client's sockets should close
	 * then the server's sockets should close. 
	 */
	@After
	public void closeSockets() {
		client.closeSocket();
		
		server.closeSocket();
	}
		
	
	/**
	 * Test that a client can send data on an open socket.
	 */
	@Test 
	public void clientShouldSendDataOnAnOpenSocket() {
		Object itemToSend = "HEYYY";
		
		// Thread for test to listen to Client
		Thread listenThread = new Thread("Listen") {
			public void run() {
				try {
					itemReceived = server.receiveData(client.getID());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		
		// wait until the server has received the data.
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
	}

	/**
	 * Test that a client can receive data from an open socket.
	 */
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
				
		// wait until the thread is finished to ensure client has received the data
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
		
	}
	
	/**
	 * 
	 */
//	@Test
//	public void clientShouldReceieveModuleList() {
//		fail("Not Implemented");
//	}
	
	/**
	 * Test that the client gets assigned the ID 0 as it should be the only client connected.
	 */
	@Test
	public void clientShouldReceieveAnID() {
		System.out.println("Client ID: " + client.getID());
		assertEquals(0, client.getID());
	}

}
