/**
 * 
 */
package com.whs.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author ch1092
 *
 */
public class Client {
	
	static Socket serverSocket = null;
	
	/**
	 * Method to open socket, in order to connect to the server. 
	 */
	protected static void openSocket() {
	
		String host = "127.0.0.1";
		int port = 1138;

		
		// Connect to the server	
		try {			
			serverSocket = new Socket(host, port);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
	}

	/**
	 * Method to close socket, in order to disconnect from the server. 
	 */
	protected static void closeSocket() {
		
		try {
			serverSocket.close();
			System.out.println("Sockets successfully closed \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Method to send an object to the server. 
	 * A connection must be made prior to using this method.  
	 * @param itemToSend		The object that is to be sent to the server.
	 */
	protected static void sendData(Object itemToSend) {
	
		ObjectOutputStream outputToServer = null;

		// Create an object stream to send to server
		try {
			outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Write the object to the server
		try {
			outputToServer.writeObject(itemToSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Method to receive data from the server.
	 * A connection must be made prior to using this method.
	 * @return itemReceived		This is the object that the client should have received from the client
	 */
	protected static Object receiveData() {
		ObjectInputStream inputFromServer = null;
				
		// Create an input stream from the connected server
		try {
			inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Return the object received from the server
		try {
			return inputFromServer.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



}
