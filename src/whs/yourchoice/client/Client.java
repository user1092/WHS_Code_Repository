package whs.yourchoice.client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import whs.yourchoice.parsers.RegisteredModulesParser;
import whs.yourchoice.presentation.RegisteredModules;
import whs.yourchoice.utilities.encryption.ClientDetails;
import whs.yourchoice.utilities.encryption.ClientPasswordHandler;

/**
 * Class for the client's back end handling communications to the server 
 * 
 * @author		ch1092, skq501, cd828
 * @version		v0.9 20/05/2016
 */
public class Client {
	
	// Variables storing information about the servers socket
	protected Socket serverSocket;
	private ObjectOutputStream outputToServer = null;
	private ObjectInputStream inputFromServer = null;
	
	// Variable assigned by the server to identify the client
	private int iD = -1;
	
	// Variables for registered modules
	protected RegisteredModules moduleList;
	private String modulePath = "src/registered_modules2.xml";
	
	
	
	/**
	 * Method to open socket, in order to connect to the server. 
	 * 
	 * @param host - The socket's host number
	 * @param port - The socket's port number
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	protected void openSocket(String host, int port) throws UnknownHostException, IOException {
		// Connect to the server	
			
			serverSocket = new Socket(host, port);
			System.out.println("Connected to Server, host: " + host + ",port: " + port);
			createOutputStream();
			createInputStream();
			receiveID();
			getModules();
			parseModules();
	}

	
	/**
	 * Method to close socket, in order to disconnect from the server. 
	 */
	protected void closeSocket() {
		try {
			outputToServer.close();
			inputFromServer.close();
			serverSocket.close();
			System.out.println("Socket successfully closed \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	private void createOutputStream() throws IOException {
		// Create an object stream to send to server
		System.out.println("(CLIENT) Creating output stream");
		outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
		System.out.println("(CLIENT) Created output stream");
		outputToServer.flush();
	}
	
	
	/**
	 * Method to send an object to the server. 
	 * A connection must be made prior to using this method. 
	 * 
	 * @param itemToSend	-	The object that is to be sent to the server.
	 * @throws IOException	-	Throws an exception if not connected to the server.
	 */
	protected void sendData(Object itemToSend) throws IOException {
		// Write the object to the server
		outputToServer.writeObject(itemToSend);
	}

	
	/**
	 * @throws IOException
	 */
	private void createInputStream() throws IOException {
		System.out.println("(CLIENT) Creating input stream");
		// Create an input stream from the connected server		
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
		System.out.println("(CLIENT) Created input stream");
	}
	
	
	/**
	 * Method to receive data from the server.
	 * A connection must be made prior to using this method.
	 * 
	 * @return itemReceived	-	This is the object that the client should have received from the client, returns null if invalid object or disconnected.
	 * @throws IOException	-	Throws an exception if not connected to the server.
	 */
	protected Object receiveData() throws IOException {
		// Return the object received from the server
		try {
			return inputFromServer.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Method to receive an ID should be called once the connection to the server has been made.
	 */
	private void receiveID() {
		
		try {
			System.out.println("Waiting for ID");
			iD = (int) receiveData();
			System.out.println("received ID: " + iD);
			if(-1 == iD) {
				System.out.println("Server Full, Try Again later");
				closeSocket();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Method to retrieve the ID that has been assigned by the server.
	 * 
	 * @return iD	-	The ID assigned to the client by the server.
	 */
	protected int getID() {
		return iD;
	}
	
	/**
	 * Method to get module list and save as file
	 * @throws IOException
	 */
	private void getModules() throws IOException {
		byte[] mybytearray = new byte[1024];
		InputStream is = serverSocket.getInputStream();
		FileOutputStream fos = new FileOutputStream(modulePath);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		int bytesRead = is.read(mybytearray, 0, mybytearray.length);
	    bos.write(mybytearray, 0, bytesRead);
	    bos.close();
	}
	
	private void parseModules() {
		RegisteredModulesParser parser = new RegisteredModulesParser();
		moduleList = parser.parseModules(modulePath);
	}
	
	protected boolean checkPassword(String userName, String password) throws IOException {
		ClientDetails enteredClientDetails = new ClientDetails();
		
		enteredClientDetails.setUserName(userName);
		
		sendData(userName);
		
		if (userName.equals("Admin")) {
			
			enteredClientDetails.setSalt((byte[]) receiveData());
			
			ClientPasswordHandler clientPasswordHandler = new ClientPasswordHandler();
			
			enteredClientDetails.setHash(clientPasswordHandler.generateHash(password, 
														enteredClientDetails.getSalt()));
			
			sendData(enteredClientDetails);
		}
		
		return (boolean) receiveData();
	}
}
