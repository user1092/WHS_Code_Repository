package com.whs.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.junit.Test;

public class ClientTests {

	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	final int port = 1138;	
	ObjectInputStream inputStream = null;
	Object itemReceived = null;
	
	/**
	 * Test that client can open a socket and successfully close it.
	 */
	@Test 
	public void shouldConnectToServerSocketThenClose() {
				
		ObjectOutputStream outputStream = null;
				
		connectToClient();
		
		// Initialise new output stream for the connected client.
		try {
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Attempt to write to the connected client.
		try {
			outputStream.writeObject(1);
			System.out.println("Wrote to connected client");
		} catch (SocketException e) {
			System.out.println("failed to send to Client, were the sockets closed?");
			fail("failed to send to Client");
		} catch (IOException e) {
			System.out.println("Client never Connected to Server");
			fail("Client never Connected to Server");
		}
		
		Client.closeSocket();
		
		try {
			serverSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Attempt to write to the disconnected client. 
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
	 * Test that a client can send data on an open socket.
	 */
	@Test 
	public void clientShouldSendDataOnAnOpenSocket() {
		Object itemToSend = "HEYYY";
		
		connectToClient();
		
		// Thread for test to listen to Client
		Thread listenThread = new Thread("Listen") {
			public void run() {
				try {
					inputStream = new ObjectInputStream(clientSocket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		listenThread.start();
		
		Client.sendData(itemToSend);
		
		while(listenThread.isAlive());
		
		try {
			serverSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			assertEquals(itemToSend, inputStream.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test that a client can receive data from an open socket.
	 */
	@Test 
	public void clientShouldReceiveDataFromAnOpenSocket() {
		Object itemToSend = "HEYYY";
		ObjectOutputStream outputToClient = null;
		
		connectToClient();
			
		/* Thread for client to listen to test method
		 * check the client receives the string HEYYY
		 */
		Thread listenThread = new Thread("Listen") {
			public void run() {
				itemReceived = Client.receiveData();
			}
		};
		listenThread.start();
		
		// Create an object stream to send to client		
		try {
			outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Write the object to the client
		try {
			outputToClient.writeObject(itemToSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// wait until the thread is finished to ensure client has received the data
		while(listenThread.isAlive());
		
		try {
			serverSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		assertEquals(itemToSend, itemReceived);
		
	}

	/**
	 * 
	 */
	private void connectToClient() {
		// Opens socket and listens to the socket for the client to connect and then accepts.
		Thread socketThread = new Thread("socket") {
			public void run() {
				
				severSocketInitialise();
				
				clientSocketListener();
			}
		};
		socketThread.start();
		
		Client.openSocket();
		
		// wait until the thread is finished to ensure server has received the data
		while(socketThread.isAlive());
	}

	/**
	 * Method to initialise server socket to listen for clients.  
	 */
	private void severSocketInitialise() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Connected to port: " + port + " successfully" + "\n");
		}
		catch (IOException e) {
			System.out.println("Could not listen to port : " + port + "\n");
			System.exit(-1);
		}
	}

	/**
	 * Method to listen to client socket, and accept a connection if one is attempted. 
	 */
	private void clientSocketListener() {
		try {
			clientSocket = serverSocket.accept();
		}
		catch (IOException e) {
			System.out.println("Could not accept client");
			System.exit(-1);
		}
	}

}
