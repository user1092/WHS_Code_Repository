/**
 * 
 */
package com.whs.server;

/**
 * @author ch1092
 *
 */
public class ServerInterfacer {

	Server server;
	
	public ServerInterfacer() {
		server = new Server();
	}
	
	public void openSocket() {
		server.openSocket();
	}
	
	public void closeSocket() {
		server.closeSocket();
	}
	
//	public void acceptClientConnection() {
//		server.acceptClientConnection(0);
//	}
	public void checkAndAcceptClientConnections() {
		server.checkAndAcceptClientConnections();
	}
	
	public void sendData(Object itemToSend, int clientSocketNumber) {
		server.sendData(itemToSend, clientSocketNumber);
	}
	
	public Object receiveData(int clientSocketNumber) {
		return server.receiveData(clientSocketNumber);
	}
	
}
