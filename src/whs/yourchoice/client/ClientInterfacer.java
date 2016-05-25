/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Class for the test classes to interface with the client's back end
 * in order to handling communications to the server from a external package.
 * 
 * NOT FOR RELEASE!
 * 
 * @author		ch1092, skq501
 * @version		v0.5 28/02/2016
 */
public class ClientInterfacer {

	Client client;
	
	/**
	 * Constructor
	 * 
	 * Creates a new client object to interface to. 
	 */
	public ClientInterfacer() {
		client = new Client();
	}
	
	/**
	 * Method to open socket, in order to connect to the server. 
	 */
	public void openSocket(String host, int port) {
		try {
			client.openSocket(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method to close socket, in order to disconnect from the server. 
	 */
	public void closeSocket() {
		client.closeSocket();
	}
		
	/**
	 * Method to send an object to the server. 
	 * A connection must be made prior to using this method. 
	 * 
	 * @param itemToSend	-	The object that is to be sent to the server.
	 * @throws IOException		Throws an exception if not connected to the server.
	 */
	public void sendData(Object itemToSend) throws IOException {
		client.sendData(itemToSend);
	}
		
	/**
	 * Method to receive data from the server.
	 * A connection must be made prior to using this method.
	 * 
	 * @return itemReceived	-	This is the object that the client should have received from the client, returns null if invalid object or disconnected.
	 * @throws IOException		Throws an exception if not connected to the server.
	 */
	public Object receiveData() throws IOException{
		return client.receiveData();
	}
	
	/**
	 * Method to retrieve the ID that has been assigned by the server.
	 * 
	 * @return iD		The ID assigned to the client by the server.
	 */
	public int getID() {
		return client.getID();
	}
}
