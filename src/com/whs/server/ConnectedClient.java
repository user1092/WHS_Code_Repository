/**
 * ConnectedClient.java		v0.2 23/02/2016
 * 
 * 
 */

package com.whs.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Class for storing information about the clients that are connected to the server.
 * 
 * @author		user1092, guest501
 * @version		v0.2 23/02/2016
 */
public class ConnectedClient {
	
	private int iD = -1; 
	private Socket clientSocket = new Socket();
	

	/**
	 * Method to determine if the client's socket is connected
	 * 
	 * @return clientSocket.isConnected()	-	Returns true if connected, else returns false.
	 */
	protected boolean socketIsConnected() {
		//return clientSocket.isConnected();
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
		clientSocket.close();
	}

}
