/**
 * 
 */
package com.whs.client;

import java.io.IOException;

/**
 * @author ch1092
 *
 */
public class ClientInterfacer {

	Client client;
	
	/**
	 * 
	 */
	public ClientInterfacer() {
		client = new Client();
	}
	
	public void openSocket() {
		client.openSocket();
	}

	public void closeSocket() {
		client.closeSocket();
	}
	
	public void sendData(Object itemToSend) throws IOException {
		client.sendData(itemToSend);
	}
	
	public Object receiveData() throws IOException{
		return client.receiveData();
	}
	
}
