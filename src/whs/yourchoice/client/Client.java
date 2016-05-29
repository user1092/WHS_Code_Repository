/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.Arrays;
import java.util.List;

import whs.yourchoice.parsers.RegisteredModulesParser;
import whs.yourchoice.presentation.RegisteredModules;
import whs.yourchoice.utilities.encryption.ClientDetails;
import whs.yourchoice.utilities.encryption.ClientPasswordHandler;
import whs.yourchoice.utilities.encryption.RsaEncryption;

/**
 * Class for the client's back end handling communications to the server 
 * 
 * @author		ch1092, skq501, cd828
 * @version		v0.13 29/05/2016
 */
public class Client {
	
	// Variables storing information about the servers socket
	protected Socket serverSocket;
	private ObjectOutputStream outputToServer = null;
	private ObjectInputStream inputFromServer = null;
	
	// Variable assigned by the server to identify the client
	private int iD = -1;
	private boolean serverState = false;
	// Variables for registered modules
	protected RegisteredModules moduleList;
	private String modulePath = "src/registered_modules2.xml";
	private final int BUFFER_SIZE = 1024;
	
	private String serverPubKeyFileName = "serverPubKeyFileName.key";
	private Key privKey;
	private boolean adminMode = false;
	
	
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
		System.out.println("ID Received" + iD);
		if (iD > -1) {
			handleKeys();
			receiveRequestedFile(modulePath);
			parseModules();
		}
	}

	
	/**
	 * Method to close socket, in order to disconnect from the server. 
	 */
	protected void closeSocket() {
		try {
			outputToServer.close();
			inputFromServer.close();
			serverSocket.close();
			serverState = false;
			System.out.println("Socket successfully closed \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Method to create an output stream
	 *  
	 * @throws IOException
	 */
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
	 * Method to create and input stream
	 * 
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
			else {
				serverState = true;
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
	 * Method to send a requested file
	 * 
	 * @param clientID
	 * @throws IOException
	 */
	protected void sendRequestedFile(String requestedFile) throws IOException {
		
        File file = new File(requestedFile);
 
        FileInputStream fis = new FileInputStream(file);
        byte [] buffer = new byte[BUFFER_SIZE];
        Integer bytesRead = 0;
 
        while ((bytesRead = fis.read(buffer)) > 0) {
        	outputToServer.writeObject(bytesRead);
        	outputToServer.writeObject(Arrays.copyOf(buffer, buffer.length));
        }
        
        fis.close();
	}
	
	/**
	 * Method to get a byte array and save as file
	 * @throws IOException
	 */
	protected void receiveRequestedFile(String saveLocation) throws IOException {
	    
		Object o = null;
		FileOutputStream fos = new FileOutputStream(saveLocation);
		
		byte[] mybytearray = new byte[BUFFER_SIZE];
		Integer bytesRead = 0;
		
		do {
            try {
            	System.out.println("Reading requested file chunk size");
				o = inputFromServer.readObject();
				System.out.println("Read requested file chunk size");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  
            bytesRead = (Integer)o;
            
            if (bytesRead != 0) {
            	try {
	            	System.out.println("Reading requested file chunk");
					o = inputFromServer.readObject();
					System.out.println("Read requested file chunk");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	mybytearray = (byte[])o;
            
            	fos.write(mybytearray, 0, bytesRead);
            }
            else {
            	File f = new File(saveLocation);
            	f.createNewFile();
            }
 
            
  
            
        } while (bytesRead == BUFFER_SIZE);
	    
		fos.close();
	}
	
	
	/**
	 * Method to parse the modules available
	 */
	private void parseModules() {
		RegisteredModulesParser parser = new RegisteredModulesParser();
		moduleList = parser.parseModules(modulePath);
	}
	
	/**
	 * Method to check with the server if the password is valid
	 * 
	 * @param userName	-	The username of the account trying to login
	 * @param password	-	The password entered by the user
	 * @return valid	-	Boolean that states true if the login was successful
	 * @throws IOException
	 */
	protected boolean checkPassword(String userName, String password) throws IOException {
		ClientDetails enteredClientDetails = new ClientDetails();
		RsaEncryption rsaHandler = new RsaEncryption();
		
		enteredClientDetails.setUserName(userName);
		
		boolean valid = false;
		
		// Only check if connection was successful.
		if (iD > -1) {
			sendData(rsaHandler.rsaEncryptObject(enteredClientDetails, serverPubKeyFileName));
		
			if (userName.equals("Admin")) {
				
				enteredClientDetails.setSalt((byte[]) rsaHandler.rsaDecryptDataToObject((byte[]) receiveData(), privKey));
				
				ClientPasswordHandler clientPasswordHandler = new ClientPasswordHandler();
				
				enteredClientDetails.setHash(clientPasswordHandler.generateHash(password, 
															enteredClientDetails.getSalt()));
							
				sendData(rsaHandler.rsaEncryptObject(enteredClientDetails, serverPubKeyFileName));
			}
			
			valid = (boolean) receiveData();
		}
		
		
		return valid;
	}
	
	
	/**
	 * Method to create keys for RSA encryption, sending the public key to the server
	 */
	private void handleKeys(){
		RsaEncryption rsaHandler = new RsaEncryption();
		Key[] keys = rsaHandler.createKeys();
		Key pubKey = keys[0];
		privKey = keys[1];
		try {
			System.out.println("Sending public key");
			sendData(pubKey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to add combo boxes to allow the user to select modules
	 * 
	 */
	protected List<String> getRevievedModules(){
		return moduleList.getAllModules();
	}
	
	/**
	 * Method to add combo boxes to allow the user to select streams
	 * 
	 */
	protected List<String> getRevievedStreams(){
		return moduleList.getAllStreams();
	}
	
	/**
	 * Method to add combo boxes to allow the user to select years
	 * 
	 */
	protected List<String> getRevievedYears(){
		return moduleList.getAllYears();
	}
	
	/**
	 * Method to add combo boxes to allow the user to select course
	 * 
	 */
	protected List<String> getRevievedCourses(){
		return moduleList.getAllCourses();
	}
	
	/**
	 * Method that lists streams by associated course
	 * @param String course
	 * @return List<String> List of streams
	 */
	protected List<String> getStreamByCourse(String course) {
		return moduleList.searchStreamByCourse(course);
	}
	
	/**
	 * Method that lists years by associated streams
	 * @param String stream
	 * @return List<String> List of years
	 */
	protected List<String> getYearsByStream(String stream) {
		return moduleList.searchYearsByStream(stream);
	}
	
	protected List<String> getModulesByCourse(String course) {
		return moduleList.searchModuleByCourse(course);
	}
	
	/**
	 * Method that lists modules by associated years
	 * @param String year
	 * @return List<String> List of years
	 */
	protected List<String> getModulesByYear(String year) {
		return moduleList.searchModulesByYear(year);
	}
	
	/**
	 * Method that lists modules by associated years
	 * @param String year
	 * @return List<String> List of years
	 */
	protected List<String> getYearsByCourse(String course) {
		return moduleList.searchYearsByCourse(course);
	}
	
	/**
	 * Method to return the filename of a zip file from the title
	 * 
	 * @param title			-	The title of the desired presentation
	 * @return zippedFile 	-	The filename of the associated zip file
	 */
	protected String getFilenameFromTitle(String title) {
		return moduleList.getFilenameFromTitle(title);
	}
	
	
	
	/**
	 * returns all relevant modules when all combo boxes selected
	 * @param String course
	 * @param String stream
	 * @param String year
	 * @return List<String> results
	 */
	protected List<String> getResultModules(String course, String stream, String year) {
		List<String> resultModules;
		
		if (year.equals("")) {
			resultModules = moduleList.getModulesByCourseStream(course, stream);
		}
		else if (stream.equals("")) {
			resultModules = moduleList.getModulesByCourseYear(course, year);
		}
		else {
			resultModules = moduleList.searchResultModules(course, stream, year);
		}
		return resultModules;	
	}
		
	public boolean isServerConnected() {
		return serverState;
	}


	/**
	 * @return the adminMode
	 */
	public boolean isAdminMode() {
		return adminMode;
	}


	/**
	 * @param adminMode the adminMode to set
	 */
	public void setAdminMode(boolean adminMode) {
		this.adminMode = adminMode;
	}
}
		

