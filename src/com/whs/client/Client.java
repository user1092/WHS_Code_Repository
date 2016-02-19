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
	
	Socket serverSocket;
	String host = "127.0.0.1";
	int port = 1138;
	
	int iD;
	
	
	/**
	 * Method to open socket, in order to connect to the server. 
	 */
	protected void openSocket() {
	
		// Connect to the server	
		try {			
			serverSocket = new Socket(host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		receiveID();
		
	}

	/**
	 * Method to close socket, in order to disconnect from the server. 
	 */
	protected void closeSocket() {
		
		try {
			serverSocket.close();
			System.out.println("Socket successfully closed \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Method to send an object to the server. 
	 * A connection must be made prior to using this method.  
	 * @param itemToSend		The object that is to be sent to the server.
	 * @throws IOException 
	 */
	protected void sendData(Object itemToSend) throws IOException {
	
		ObjectOutputStream outputToServer = null;

		// Create an object stream to send to server
		outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
		
		// Write the object to the server
		outputToServer.writeObject(itemToSend);
		
	}

	/**
	 * Method to receive data from the server.
	 * A connection must be made prior to using this method.
	 * @return itemReceived		This is the object that the client should have received from the client
	 * @throws IOException 
	 */
	protected Object receiveData() throws IOException {
		ObjectInputStream inputFromServer = null;
				
		// Create an input stream from the connected server
		
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
		
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
	
	private void receiveID() {
		try {
			iD = (int) receiveData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the iD
	 */
	protected int getID() {
		return iD;
	}
}
