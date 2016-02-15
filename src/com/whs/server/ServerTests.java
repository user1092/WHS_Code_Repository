/**
 * 
 */
package com.whs.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author ch1092
 *
 */
public class ServerTests {

	Socket serverSocket = null;
	String host = "127.0.0.1";
	int port = 1138;
	ObjectInputStream inputStream = null;
	Object itemReceived = null;
		
	/**
	 * Method run before every test to open the server socket.
	 */
	@Before
	public void openSockets() {
		Server.openSockets();
	}

	/**
	 * Method run after every test to close the server socket.
	 */
	@After
	public void closeSockets() {
		Server.closeSockets();
	}

	/**
	 * Test that a server can open a socket and successfully close.
	 */
	@Test 
	public void shouldConnectToServersSocketThenClose() {
		ObjectOutputStream outputStream = null;
		
		connectToServer();
		
		outputStream = initialiseOutputStream(outputStream);
		
		Server.closeSockets();
		
		try {
			outputStream.writeObject(1);
			System.out.println("Wrote to disconnected client, were the sockets closed?");
			fail("Wrote to disconnected client");
		} catch (SocketException e) {
			System.out.println("Successfully failed to send to Server");
		} catch (IOException e) {
			System.out.println("Client never Connected to Server");
			fail("Client never Connected to Server");
		}
	}

	/**
	 * Test that a server can send data on an open socket.
	 */
	@Test 
	public void serverShouldSendDataOnAnOpenSocket() {
		
		Thread listenThread;
		
		connectToServer();
		
		// Thread for test to listen to Server
		listenThread = new Thread("Listen") {
			public void run() {
				try {
					inputStream = new ObjectInputStream(serverSocket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		listenThread.start();
		
		// Send the string hello from the server
		Object itemToSend = "hello";
		Server.sendData(itemToSend);
		
		while(listenThread.isAlive());
		
		// Check the item received is the same as what was sent
		try {
			assertEquals(itemToSend, inputStream.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		connectToServer();
		
		final Object itemToSend = "hello";
		ObjectOutputStream outputToServer = null;
		
		/* Thread for server to listen to test method
		 * check the server receives the string hello
		 */
		Thread listenThread = new Thread("Listen") {
			public void run() {
				itemReceived = Server.receiveData();
			}
		};
		listenThread.start();
		
		// Create an object stream to send to server		
		outputToServer = initialiseOutputStream(outputToServer);
		
		// Write the object to the client
		try {
			outputToServer.writeObject(itemToSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// wait until the thread is finished to ensure server has received the data
		while(listenThread.isAlive());
		
		assertEquals(itemToSend, itemReceived);
		
		
	}
	

	/**
	 * Method used to connect to server. 
	 */
	public void connectToServer() {
		// Connect to the server	
		try {
			serverSocket = new Socket(host, port);
			assertTrue(serverSocket.isConnected());
		} catch (IOException e) {
			System.out.println("Client never Connected to Server, are the sockets open?");
			fail("Client never Connected to Server");
		}
	}

	/**
	 * Method to initialise a new output stream.
	 * @param outputStream 		The object output stream to be initialised. 
	 * @return outputStream 	The initialised object output stream. 
	 */
	private ObjectOutputStream initialiseOutputStream(ObjectOutputStream outputStream) {
		try {
			outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return outputStream;
	}

}
