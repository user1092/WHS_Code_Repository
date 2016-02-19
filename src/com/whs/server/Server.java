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
	
	/**
	 * @author ch1092
	 *
	 */
	public interface ServerInterfacer {

	}

	protected ServerSocket serverSocket;
	
	private int currentClientNumber = 0;
	private final int connectedClientsMaxNumber = 3;
	private ConnectedClient[] clients;
	
	

	/**
	 * 
	 */
	public Server() {
		clients = new ConnectedClient[connectedClientsMaxNumber];
	}
	
	
	/**
	 * Method to open socket to allow communications with a client.
	 * 
	 */
	protected void openSocket() {
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
	protected void closeSocket() {
		try {
			for(currentClientNumber = 0; currentClientNumber < connectedClientsMaxNumber - 1; currentClientNumber++) {
				System.out.println("attempt to close client: " + currentClientNumber);
				System.out.println("Null?: " + (null == clients[currentClientNumber]));
				//System.out.println(-1 != clients[currentClientNumber].getID());
				
				if(null != clients[currentClientNumber]) {
					System.out.println("closing clients: " + currentClientNumber);
					clients[currentClientNumber].closeClientSocket();
				}
			}
			serverSocket.close();
			System.out.println("Server Socket successfully closed \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void checkAndAcceptClientConnections() {
		
		System.out.println("Server socket is open? " + !serverSocket.isClosed());
		
		Thread listenThread = new Thread("Listen") {
			public void run() {
				while(!serverSocket.isClosed()) {
					ConnectedClient currentClient = new ConnectedClient();
					System.out.println("currentClient: " + currentClientNumber);
					
					currentClient.setID(currentClientNumber);
					
					currentClient = acceptClientConnection(currentClient);
					try {
							if(currentClient.socketIsConnected()) {
							clients[currentClientNumber] = currentClient;
							System.out.println("Current Client" + currentClient.getID());
							sendData(currentClient.getID(), currentClient.getID());
						}
					} catch(NullPointerException e) {
						//TODO
						// Comes here if sockets closed.
						e.printStackTrace();
					}
					
					if(currentClientNumber < connectedClientsMaxNumber - 1) {
						currentClientNumber++;
					}
				}
			}
		};
					
		listenThread.start();	
		
	}
	
	/**
	 * @return 
	 * 
	 */
	private ConnectedClient acceptClientConnection(ConnectedClient currentClient) {
		try {
			currentClient.setClientSocket(serverSocket.accept());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Comes here if sockets closed.
			//e.printStackTrace();
		}
		return currentClient;		
	}

	/**
	 * Method to send data to a client.
	 * A connection must be made prior to using this method.
	 * @param itemToSend 	This is the object that the server should send to the client
	 */
	protected void sendData(Object itemToSend, int clientID) {
		ObjectOutputStream outputToClient = null;
		
		// Create an object stream to send to client
		try {
			outputToClient = new ObjectOutputStream(clients[clientID].getClientSocket().getOutputStream());
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
	protected Object receiveData(int clientID) {
		ObjectInputStream inputFromClient = null;
				
		// Create an input stream from the connected client
		try {
			inputFromClient = new ObjectInputStream(clients[clientID].getClientSocket().getInputStream());
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
