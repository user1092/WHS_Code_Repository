/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import whs.yourchoice.client.ClientInterfacer;


/**
 * Class for testing the server's back end handling communications to the clients. 
 * 
 * NOT FOR RELEASE!
 * 
 * @author		ch1092, skq501
 * @version		v0.7 06/04/2016
 */
public class ServerTests {

	// Declare a server to be used.
	Server server;
	
	/*
	 *  Declare a client to be used, must use the ClientInterfacer class
	 *  in order to access the internal methods. 
	 */
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
		client.openSocket(host, port);
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
	@Ignore
	@Test 
	public void serverShouldReceiveDataFromAnOpenSocket() {
				
		final Object itemToSend = "hello";
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		/* Thread for server to listen to client
		 * check the server receives the string hello
		 */
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
				System.out.println("itemReceived: " + itemReceived);
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
		
		final ClientInterfacer client2 = new ClientInterfacer();
		client2.openSocket(host, port);

		server.checkAndAcceptClientConnections(); 
		
		/* Thread for server to listen to client
		 * check the server receives the string hello
		 */
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
		
		// Send object to server from the client.
		server.sendData(itemToSend, 0);
//		try {
//			client.sendData(itemToSend);
//		} catch (IOException e1) {
//			fail("Could not send data to server.");
//			e1.printStackTrace();
//		}

		// Wait until the thread is finished to ensure server has received the data
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
		
		/* Thread for server to listen to client
		 * check the server receives the string hello
		 */
		Thread listenThread2 = new Thread("Listen") {
			public void run() {
				try {
					// Can no longer do as server listens to client as soon as it connects
					//itemReceived = server.receiveData(client.getID());
					// Now Server will pass back the object as soon as it receives it
					itemReceived = client2.receiveData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		listenThread2.start();
		
		server.sendData(itemToSend, 1);
		
		// Send object to server from the client.
//		try {
//			client2.sendData(itemToSend);
//		} catch (IOException e1) {
//			fail("Could not send data to server.");
//			e1.printStackTrace();
//		}

		// Wait until the thread is finished to ensure server has received the data
		while(listenThread2.isAlive());
		
		client2.closeSocket();
		
		assertEquals(itemToSend, itemReceived);
						
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
