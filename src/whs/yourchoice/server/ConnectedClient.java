package whs.yourchoice.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class for storing information about the clients that are connected to the server.
 * 
 * @author		user1092, guest501
 * @version		v0.3 06/04/2016
 */
public class ConnectedClient {
	
	private int iD = -1; 
	private Socket clientSocket = new Socket();
	private ObjectOutputStream outputToClient = null;
	private ObjectInputStream inputFromClient = null;

	
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

	
}
