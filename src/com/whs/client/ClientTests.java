package com.whs.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whs.server.ServerInterfacer;

public class ClientTests {

	Client client;
	
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	final int port = 1138;	
	ObjectInputStream inputStream = null;
	Object itemReceived = null;
	ServerInterfacer server;
	
	@Before
	public void setup() {
		server = new ServerInterfacer();
		server.openSocket();
		server.checkAndAcceptClientConnections();
		
		client = new Client();
		client.openSocket();
	}
	
	@After
	public void closeSockets() {
		server.closeSocket();
		
		client.closeSocket();
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
				itemReceived = server.receiveData(0);
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
	
	/*
	 * 
	 */
//	@Test
//	public void clientShouldReceieveModuleList() {
//		fail("Not Implemented");
//	}
	
	/*
	 * 
	 */
	@Test
	public void clientShouldReceieveAnID() {
		System.out.println("Client ID: " + client.getID());
		assertEquals(0, client.getID());
	}

}
