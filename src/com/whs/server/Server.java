/**
 * 
 */
package com.whs.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ch1092
 *
 */
public class Server {
	
	private static ServerSocket serverSocket;
	private static Socket clientSocket;

	/**
	 * Method to open socket to allow communications with a client.
	 * 
	 */
	protected static void openSockets() {
		int port = 1138;
		
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
	 * Method to close socket to stop communications with a client.
	 * 
	 */
	protected static void closeSockets() {
		try {
			serverSocket.close();
			System.out.println("Sockets successfully closed \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to send data to a client.
	 * A connection must be made prior to using this method.
	 * @param itemToSend 	This is the object that the server should send to the client
	 */
	protected static void sendData(Object itemToSend) {
		ObjectOutputStream outputToClient = null;
		
		// Accept connection request from client
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create an object stream to send to client
		try {
			outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Write the object to the client
		try {
			outputToClient.writeObject(itemToSend);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method to receive data from a client.
	 * A connection must be made prior to using this method.
	 * @return itemReceived		This is the object that the server should have received from the client
	 */
	public static Object receiveData() {
		ObjectInputStream inputFromClient = null;
		
		// Accept connection request from client
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create an input stream from the connected client
		try {
			inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Return the object received from the client
		try {
			return inputFromClient.readObject();
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
