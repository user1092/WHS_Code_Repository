/**
 * 
 */
package com.whs.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whs.client.ClientInterfacer;


/**
 * @author ch1092
 *
 */
public class ServerTests {

	Server server;
	ClientInterfacer client;
	Socket serverSocket;
	String host = "127.0.0.1";
	int port = 1138;
	ObjectInputStream inputStream = null;
	Object itemReceived = null;
	Object recievedObject = null;
	
	/**
	 * Method run before every test to create a server, open its socket, and listen for a client.
	 * Then a client is setup and its socket is opened. 
	 */
	@Before
	public void setup() {
		server = new Server();
		server.openSocket();
		server.checkAndAcceptClientConnections();
		
		client = new ClientInterfacer();
		client.openSocket();
	}
	
	/**
	 * Method run after every test to close the client and server socket.
	 */
	@After
	public void closeSockets() {
		server.closeSocket();
		
		client.closeSocket();
	}

	/**
	 * Test that a server can send data on an open socket.
	 */
	@Test 
	public void serverShouldSendDataOnAnOpenSocket() {
				
		Thread listenThread;
				
		// Thread for client to listen to Server
		listenThread = new Thread("Listen") {
			public void run() {
				try {
					itemReceived = client.receiveData();
				} catch (IOException e) {
					fail("Client could not recieve data");
					e.printStackTrace();
				}
			}
		};
		listenThread.start();
		
		// Send the string hello from the server
		Object itemToSend = "hello";
		server.sendData(itemToSend, 0);
		
		// Wait till client has returned the item received
		while(listenThread.isAlive());
		
		// Check the item received is the same as what was sent
		try {
			assertEquals(itemToSend, itemReceived);
		} catch (NullPointerException e) {
			System.out.println("Server never sent item, was sendData() called?");
			fail("Server never sent item");
		}				
	}
	
	/**
	 * Test that a server can receive data from an open socket.
	 */
	@Test 
	public void serverShouldReceiveDataFromAnOpenSocket() {
				
		final Object itemToSend = "hello";
				
		/* Thread for server to listen to client
		 * check the server receives the string hello
		 */
		Thread listenThread = new Thread("Listen") {
			public void run() {
				itemReceived = server.receiveData(0);
				System.out.println("BLUGHHH");
			}
		};
		listenThread.start();
		
		// Send object to server from the client.
		try {
			client.sendData(itemToSend);
		} catch (IOException e1) {
			fail("Could not send data to server.");
			e1.printStackTrace();
		}

		// Wait until the thread is finished to ensure server has received the data
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
	}
	
	/**
	 * Test that a server can connect to multiple clients.
	 */
	@Test 
	public void serverShouldConnectToMultipleClients() {
		Object itemToSend = "STRING" ;
		
		ClientInterfacer client2 = new ClientInterfacer();
		client2.openSocket();

		server.checkAndAcceptClientConnections(); 
		
		/* Thread for server to listen to client
		 * check the server receives the string hello
		 */
		Thread listenThread = new Thread("Listen") {
			public void run() {
				itemReceived = server.receiveData(0);
			}
		};
		listenThread.start();
		
		// Send object to server from the client.
		try {
			client.sendData(itemToSend);
		} catch (IOException e1) {
			fail("Could not send data to server.");
			e1.printStackTrace();
		}

		// Wait until the thread is finished to ensure server has received the data
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
		
		/* Thread for server to listen to client
		 * check the server receives the string hello
		 */
		Thread listenThread2 = new Thread("Listen") {
			public void run() {
				itemReceived = server.receiveData(1);
			}
		};
		listenThread2.start();
		
		// Send object to server from the client.
		try {
			client2.sendData(itemToSend);
		} catch (IOException e1) {
			fail("Could not send data to server.");
			e1.printStackTrace();
		}

		// Wait until the thread is finished to ensure server has received the data
		while(listenThread2.isAlive());
		
		client2.closeSocket();
		
		assertEquals(itemToSend, itemReceived);
						
	}
		

}
