/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Key;

/**
 * Class for storing information about the clients that are connected to the server.
 * 
 * @author		ch1092, skq501
 * @version		v0.5 24/05/2016
 */
public class ConnectedClient {
	
	private int iD = -1; 
	private Socket clientSocket = new Socket();
	private ObjectOutputStream outputToClient = null;
	private ObjectInputStream inputFromClient = null;
	private Key publicKey = null;


	/**
	 * Method to determine if the client's socket is connected
	 * 
	 * @return clientSocket.isConnected()	-	Returns true if connected, else returns false.
	 */
	protected boolean socketIsConnected() {
		return !clientSocket.isClosed();
	}

	
	/**
	 * Method to get the ID assigned to the client.
	 * 
	 * @return iD	-	The ID of client connected. 
	 */
	protected int getID() {
		return iD;
	}

	
	/**
	 * Method to set the ID of the client.
	 * 
	 * @param iD	-	The ID of the client connected.
	 */
	protected void setID(int iD) {
		this.iD = iD;
	}

	
	/**
	 * Method to get the socket assigned to the client.
	 * 
	 * @return clientSocket	-	The socket used to communicate to the client.
	 */
	protected Socket getClientSocket() {
		return clientSocket;
	}

	
	/**
	 * Method to set the socket assigned to the client.
	 * 
	 * @param clientSocket	-	The socket used to communicate to the client.
	 */
	protected void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	
	/**
	 * Method to close the socket used to communicate to the connected client.
	 * 
	 * @throws IOException		Throws if the socket is already closed.
	 */
	protected void closeClientSocket() throws IOException {
		inputFromClient.close();
		outputToClient.close();
		clientSocket.close();
	}

	
	/**
	 * Method to get the output stream
	 * 
	 * @return the outputToClient
	 */
	protected ObjectOutputStream getOutputToClient() {
		return outputToClient;
	}

	
	/**
	 * Method to create an output stream, to send data.
	 * 
	 * @throws IOException
	 */
	protected void createOutputToClient() throws IOException {
		System.out.println("(SERVER) Creating ouput stream for client: " + iD);
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		outputToClient.flush();
		System.out.println("(SERVER) Created ouput stream for client: " + iD);
	}
	
	/**
	 * Method to return outputstream for file sending
	 * @return getOutputStream
	 * @throws IOException
	 */
	protected OutputStream getOutputStream() throws IOException {
		return clientSocket.getOutputStream();
	}
	
	/**
	 * Method to get the input stream
	 * 
	 * @return the inputFromClient
	 */
	protected ObjectInputStream getInputFromClient() {
		return inputFromClient;
	}

	
	/**
	 * Method to create an input stream, to receive data.
	 * 
	 * @throws IOException
	 */
	protected void createInputFromClient() throws IOException {
		System.out.println("(SERVER) Creating input stream for client: " + iD);
		inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		System.out.println("(SERVER) Created input stream for client: " + iD);
	}


	/**
	 * Method to set the public key associated with the client
	 * 
	 * @param publicKey	-	The public key received from the client
	 */
	public void setPublicKey(Key publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * Method to get the public key associated with the client
	 * 
	 * @return the publicKey
	 */
	public Key getPublicKey() {
		return publicKey;
	}
}
