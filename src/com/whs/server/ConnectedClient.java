/**
 * 
 */
package com.whs.server;

import java.io.IOException;
import java.net.Socket;

/**
 * @author ch1092
 *
 */
public class ConnectedClient {
	
	private int iD = -1; 
	private Socket clientSocket = new Socket();
//	private boolean socketIsConnected = false;
	

	protected boolean socketIsConnected() {
//		if(clientSocket.isClosed()) {
//			socketIsConnected = clientSocket.isConnected()
//		}
//		return socketIsConnected;
		return clientSocket.isConnected();
	}

	/**
	 * @return the iD
	 */
	protected int getID() {
		return iD;
	}

	/**
	 * @param iD the iD to set
	 */
	protected void setID(int iD) {
		this.iD = iD;
	}

	/**
	 * @return the clientSocket
	 */
	protected Socket getClientSocket() {
		return clientSocket;
	}

	/**
	 * @param clientSocket the clientSocket to set
	 */
	protected void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	protected void closeClientSocket() throws IOException {
		clientSocket.close();
	}

}
