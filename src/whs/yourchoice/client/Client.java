/**
 * Client.java		v0.7 28/02/2016
 * 
 * 
 */
package whs.yourchoice.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class for the client's back end handling communications to the server 
 * 
 * @author		user1092, guest501
 * @version		v0.7 28/02/2016
 */
public class Client {
	
	// Variables storing information about the servers socket
	Socket serverSocket;
	String host = "127.0.0.1";
	int port = 1138;
	
	// Variable assigned by the server to identify the client
	int iD = -1;
	
	
	/**
	 * Method to open socket, in order to connect to the server. 
	 * @param host - The socket's host number
	 * @param port - The socket's port number
	 */
	protected void openSocket(String host, int port) {
	
		// Connect to the server	
		try {			
			serverSocket = new Socket(host, port);
			System.out.println("Connected to Server, host: " + host + ",port: " + port);
			receiveID();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Could not connected to Server, host: " + host + ",port: " + port);
		}
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
	 * 
	 * @param itemToSend	-	The object that is to be sent to the server.
	 * @throws IOException	-	Throws an exception if not connected to the server.
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
	 * 
	 * @return itemReceived	-	This is the object that the client should have received from the client, returns null if invalid object or disconnected.
	 * @throws IOException	-	Throws an exception if not connected to the server.
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
	
	/**
	 * Method to receive an ID should be called once the connection to the server has been made.
	 */
	private void receiveID() {
		
		try {
			System.out.println("Waiting for ID");
			iD = (int) receiveData();
			System.out.println("received ID: " + iD);
			if(-1 == iD) {
				System.out.println("Server Full, Try Again later");
				closeSocket();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method to retrieve the ID that has been assigned by the server.
	 * 
	 * @return iD	-	The ID assigned to the client by the server.
	 */
	protected int getID() {
		return iD;
	}
}
