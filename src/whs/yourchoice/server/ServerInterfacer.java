/**
 * ServerInterfacer.java		v0.4 23/02/2016
 * 
 * 
 */
package whs.yourchoice.server;

import java.io.IOException;

/**
 * Class for the test classes to interface with the server's back end
 * in order to handling communications to the server from a external package.
 * 
 * NOT FOR RELEASE!
 * 
 * @author		user1092, guest501
 * @version		v0.4 23/02/2016
 */
public class ServerInterfacer {

	Server server;
	
	/**
	 * Constructor
	 * 
	 * Creates a new server object to interface to. 
	 */
	public ServerInterfacer() {
		server = new Server();
	}
	
	/**
	 * Method to open socket to allow communications with a client.
	 */
	public void openSocket() {
		server.openSocket();
	}
	
	/**
	 * Method to close socket to stop communications with a client.
	 */
	public void closeSocket() {
		server.closeSocket();
	}
	
	/**
	 * Method to listen for new clients and accept their connection 
	 * then store their details in a connectedClients array.
	 */
	public void checkAndAcceptClientConnections() {
		server.checkAndAcceptClientConnections();
	}
	
	/**
	 * Method to send data to a client.
	 * A connection must be made prior to using this method.
	 * 
	 * @param itemToSend -	This is the object that the server should send to the client
	 * @param clientID	- This is the ID of the client the data should be sent to.
	 */
	public void sendData(Object itemToSend, int clientSocketNumber) {
		server.sendData(itemToSend, clientSocketNumber);
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
	public Object receiveData(int clientSocketNumber) throws ClassNotFoundException, IOException {
		return server.receiveData(clientSocketNumber);
	}
	
}
