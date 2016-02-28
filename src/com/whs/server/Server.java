/**
 * Server.java		v0.6 23/02/2016
 * 
 * 
 */

package com.whs.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

/**
 * Class for the server's back end handling communications to the clients. 
 * 
 * @author		user1092, guest501
 * @version		v0.6 23/02/2016
 */
public class Server {
	
	protected ServerSocket serverSocket;
	
	// Variable to store information about the clients connected.
	private ConnectedClient[] clients;
	private int currentClientNumber = 0;
	private final int connectedClientsMaxNumber = 2;
		
	/**
	 * Constructor to create a new array of connected clients.
	 */
	public Server() {
		clients = new ConnectedClient[connectedClientsMaxNumber];
	}
	
	
	/**
	 * Method to open socket to allow communications with a client.
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
	 */
	protected void closeSocket() {
		try {
			//Check the connection state of all clients and close the sockets if required
			for(currentClientNumber = 0; 
					currentClientNumber < connectedClientsMaxNumber - 1; 
													currentClientNumber++) {
				// Check if the client entry exists
				if(null != clients[currentClientNumber]) {
					// Check if the socket is connected 
					if(clients[currentClientNumber].socketIsConnected()){
						System.out.println("Closing socket of client: " + currentClientNumber);
						clients[currentClientNumber].closeClientSocket();
					}
				}
			}
			serverSocket.close();
			System.out.println("Server Socket successfully closed \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to listen for new clients and accept their connection 
	 * then store their details in a connectedClients array.
	 */
	protected void checkAndAcceptClientConnections() {	
		
		// Thread to check if a client disconnects so a new one can be added
		final Thread checkThread = new Thread("Check for closed sockets for new clients") {
			public void run() {
				while(!serverSocket.isClosed()) {
					// Only check if current entry is connected
					if(clients[currentClientNumber].socketIsConnected()) {
						// Cycle through clients array to see if one has disconnected
						for(int clientNumber = 0; clientNumber < connectedClientsMaxNumber; clientNumber++) {
							if(!clients[clientNumber].socketIsConnected()) {
								currentClientNumber = clientNumber;
								System.out.println("client number: " + clientNumber + "isn't connected");
								break;
							}
						}
					}
				}
			}
		};
		
		// Thread to listen for new clients and accept if there is space or reject if connections are full
		Thread listenThread = new Thread("Listen for new clients") {
			public void run() {
				while(!serverSocket.isClosed()) {
					ConnectedClient currentClient = new ConnectedClient();
					// Initial Population of clients array
					if(null == clients[currentClientNumber]) {
						if((currentClientNumber < connectedClientsMaxNumber)) {
							
							currentClient = acceptClientConnection(currentClient);
							currentClient.setID(currentClientNumber);
							manageClient(currentClient, currentClientNumber);
							
							if(currentClientNumber < connectedClientsMaxNumber - 1){
								System.out.println("Incresing client number");
								currentClientNumber++;
							}
						}
					} else {
						// Start looking for disconnected clients 
						if(!checkThread.isAlive()){
							checkThread.start();
						}
						// Attempt to accept client
						currentClient = acceptClientConnection(currentClient);
						currentClient.setID(currentClientNumber);	
						manageClient(currentClient, currentClientNumber);
					}
				}
			}
		};	
		listenThread.start();
	}
	
	/**
	 * Method to accept a clients connection.
	 * 
	 * @param currentClient - The client whose connection is to be accepted.
	 * @return currentClient - The Client who is now connected.
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
	 * Method to accept or refuse clients depending on if there is space
	 * if accepted the client is then listened to for data requests.
	 * 
	 * @param currentClient
	 * @param clientNumber
	 */
	private void manageClient(ConnectedClient currentClient, int clientNumber) {
		try {
			/*
			 *  If entry in clients array doesn't exist create it 
			 *  and accept then listen to client
			 */
			if(currentClient.socketIsConnected() && (null == clients[clientNumber])) {
				clients[clientNumber] = currentClient;
				System.out.println("(null)Current Client" + currentClient.getID());
				sendData(currentClient.getID(), currentClient.getID());
				listenToClient(currentClient);
			} else {
				/*
				 *  If entry in clients array does exist and is not connected 
				 *  accept then listen to client
				 */
				if(currentClient.socketIsConnected() && (!clients[clientNumber].socketIsConnected())) {
					clients[clientNumber] = currentClient;
					System.out.println("(not connected)Current Client" + currentClient.getID());
					sendData(currentClient.getID(), currentClient.getID());
					listenToClient(currentClient);
				} else {
					/*
					 *  If entry in clients array does exist and is connected
					 *  inform client the server is full
					 */
					informThatServerFull(currentClient);
				}
			}
		} catch(NullPointerException e) {
			//TODO
			// Comes here if sockets closed.
			e.printStackTrace();
		}
	}

	/**
	 * Method to send data to a client.
	 * A connection must be made prior to using this method.
	 * 
	 * @param itemToSend -	This is the object that the server should send to the client
	 * @param clientID	- This is the ID of the client the data should be sent to.
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
	 * 
	 * @param clientID	- This is the ID of the client the server should be listening to to receive data.
	 * @return itemReceived	-	This is the object that the server should have received from the client, returns null if invalid object or disconnected.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	protected Object receiveData(int clientID) throws IOException, ClassNotFoundException {
		ObjectInputStream inputFromClient = null;
				
		// Create an input stream from the connected client
		inputFromClient = new ObjectInputStream(clients[clientID].getClientSocket().getInputStream());
		
		// Return the object received from the client
		return inputFromClient.readObject();
	}
	
	private void informThatServerFull(ConnectedClient client) {
		ObjectOutputStream outputToClient = null;
		
		// Create an object stream to send to client
		try {
			outputToClient = new ObjectOutputStream(client.getClientSocket().getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Write the object to the client
		try {
			outputToClient.writeObject(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to listen to client for data requests
	 * 
	 * @param client
	 */
	private void listenToClient(final ConnectedClient client) {
		Thread[] listenToClientThread;
		listenToClientThread = new Thread[connectedClientsMaxNumber];
		listenToClientThread[client.getID()] = new Thread("Listen to connected clients") {
			public void run() {
				while (!serverSocket.isClosed() && client.socketIsConnected()) {
					Object object;
					try {
						// Wait for the client to send some data
						object = receiveData(client.getID());
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						//e1.printStackTrace();
						// Client Disconnected so close the socket.
						try {
							client.closeClientSocket();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		listenToClientThread[client.getID()].start();
	}

}
