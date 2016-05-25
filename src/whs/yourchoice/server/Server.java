/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import whs.yourchoice.utilities.encryption.ClientDetails;
import whs.yourchoice.utilities.encryption.ClientPasswordHandler;
import whs.yourchoice.utilities.encryption.RsaEncryption;
import whs.yourchoice.utilities.encryption.ServerPasswordHandler;


/**
 * Class for the server's back end handling communications to the clients. 
 * 
 * @author		ch1092, skq501, cd828
 * @version		v0.9 24/05/2016
 */
public class Server {
	
	protected ServerSocket serverSocket;
	
	// Variable to store information about the clients connected.
	private ConnectedClient[] clients;
	private int currentClientNumber = 0;
	private final int connectedClientsMaxNumber = 2;
	
	//registered modules variables
	private final String rmPath = new File("").getAbsolutePath() + "/src/registered_modules.xml";
	private final String clientDetailsLocation = "AdminDetails.txt";
	private String serverPrivKeyFileName = "serverPrivKeyFileName.key";
	
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
					System.out.println("Socket of client is open?: " + clients[currentClientNumber].socketIsConnected());
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
						System.out.println("Checking for disconnected clients");
						// Cycle through clients array to see if one has disconnected
						for(int clientNumber = 0; clientNumber < connectedClientsMaxNumber; clientNumber++) {
							if(!clients[clientNumber].socketIsConnected()) {
								currentClientNumber = clientNumber;
								System.out.println("client number: " + clientNumber + "isn't connected");
								break;
							}
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
			try {
				currentClient.createOutputToClient();
				currentClient.createInputFromClient();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 *  If entry in clients array doesn't exist create it 
			 *  and accept then listen to client
			 */
			if(currentClient.socketIsConnected() && (null == clients[clientNumber])) {
				clients[clientNumber] = currentClient;
				System.out.println("(null)Current Client" + currentClient.getID());
				sendData(currentClient.getID(), currentClient.getID());
				try {
					System.out.println("Receiving public key");
					currentClient.setPublicKey((Key) receiveData(currentClient.getID()));
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					sendRMFile(currentClient.getID());
				}
				catch (IOException e) {
					System.out.println("Error sending module list");
					System.out.println(e);
				}
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
					try {
						currentClient.setPublicKey((Key) receiveData(currentClient.getID()));
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						sendRMFile(currentClient.getID());
					}
					catch (IOException e) {
						System.out.println("Error sending module list");
						System.out.println(e);
					}
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
		try {
			clients[clientID].getOutputToClient().writeObject(itemToSend);
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
		return clients[clientID].getInputFromClient().readObject();
	}
	
	
	/**
	 * Method to inform client that server is full.
	 * 
	 * @param client - This is the client to be informed
	 */
	private void informThatServerFull(ConnectedClient client) {
		try {
			client.getOutputToClient().writeObject(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			client.closeClientSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method to listen to client for data requests
	 * 
	 * @param client - This is the client to be listened to
	 */
	private void listenToClient(final ConnectedClient client) {
		Thread[] listenToClientThread;
		listenToClientThread = new Thread[connectedClientsMaxNumber];
		
		listenToClientThread[client.getID()] = new Thread("Listen to connected clients") {
			public void run() {
				boolean validPassword = false;
				Object object = null;
				while (!serverSocket.isClosed() && client.socketIsConnected()) {
										
					try {
						// Check the client's password or if a guest login						
						while ((!validPassword) && client.socketIsConnected())  {
							validPassword = validate(client);
							if (client.socketIsConnected()) {
								sendData(validPassword, client.getID());
							}
						}
						
						object = receiveData(client.getID());
						if (((String) object).equals("Example_Presentation")) {
							sendData(new File("Example_Presentation.zip"), client.getID());
						}
						
						// Wait for the client to send some data, below is for tests
						object = receiveData(client.getID());
						sendData(object, client.getID());
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
	
	// not currently happy with variety of Exceptions that can be thrown
	private void sendRMFile(int clientID) throws IOException {
		File moduleFile = new File(rmPath);
		byte[] mybytearray = new byte[(int) moduleFile.length()];
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(moduleFile));
		bis.read(mybytearray, 0, mybytearray.length);
		OutputStream os = clients[clientID].getOutputStream();
		os.write(mybytearray, 0, mybytearray.length);
		os.flush();
		bis.close();
	}

	/**
	 * Method to set the administrators password.
	 * 
	 * @param password	-	The administrators password to be set.
	 */
	protected void setAdminPassword(String password) {
				
		ClientDetails adminDetails = new ClientDetails();
		
		ServerPasswordHandler serverPasswordHandler = new ServerPasswordHandler();
		
		// Clear the current password
		try {
			serverPasswordHandler.clearClientDetailsFile(clientDetailsLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ClientPasswordHandler clientPasswordHandler = new ClientPasswordHandler();
		try {
			adminDetails = clientPasswordHandler.encryptNewPassword("Admin", password);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serverPasswordHandler.storeDetails(adminDetails, clientDetailsLocation);
	}
	
	/**
	 * Method to ensure the user logs in with a valid account
	 * 
	 * @param client	-	The Client attempting to login
	 * @return valid	-	Boolean that returns true if a valid login is used
	 * @throws ClassNotFoundException
	 */
	private boolean validate(final ConnectedClient client)
			throws ClassNotFoundException {
		
		boolean validPassword = false;
		Object encryptedObject = null;
		try {
			System.out.println("Getting username, client:" + client.getID());
			encryptedObject = receiveData(client.getID());
			System.out.println("Got username, client:" + client.getID());
		} catch (IOException e1) {
			//e1.printStackTrace();
			// Client Disconnected so close the socket.
			try {
				System.out.println("Didn't Get username, client:" + client.getID());
				client.closeClientSocket();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Object object;
		ClientDetails clientDetails = null;
		RsaEncryption rsaHandler = new RsaEncryption();
		
		if (client.socketIsConnected()) {
			object = rsaHandler.rsaDecryptDataToObject((byte[]) encryptedObject, serverPrivKeyFileName);
		
			clientDetails = (ClientDetails) object;
					
			if (clientDetails.getUserName().equals("Admin")) {
				ServerPasswordHandler serverPasswordHandler = new ServerPasswordHandler();
				ClientDetails retrievedClientDetails = serverPasswordHandler.getDetails(clientDetails.getUserName(), clientDetailsLocation);
				
				sendData(encryptData(retrievedClientDetails.getSalt(), client), client.getID());
				
				try {
					encryptedObject = receiveData(client.getID());
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
				object = rsaHandler.rsaDecryptDataToObject((byte[]) encryptedObject, serverPrivKeyFileName);
				
				ClientDetails currentClientDetails = (ClientDetails) object;
				
				validPassword = serverPasswordHandler.validateHash(currentClientDetails.getHash(), retrievedClientDetails);
			}
			else {
				validPassword = true;
			}
		}
		return validPassword;
	}
	
	
	
	/**
	 * Method to encrypt an object for a specific client using RSA 
	 * 
	 * @param objectToEncrypt	-	The object to encrypt
	 * @param client	-	The client whose key is to be used
	 * @return encryptData	-	The data that has been encrypted
	 */
	private byte[] encryptData(Object objectToEncrypt, ConnectedClient client) {
		RsaEncryption rsaHandler = new RsaEncryption();
		Key publicKey = client.getPublicKey();
		
		return rsaHandler.rsaEncryptObject(objectToEncrypt, publicKey);
	}
}
