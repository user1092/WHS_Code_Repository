/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.utilities.encryption;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.Test;


/**
 * Class for testing the password handlers
 * 
 * NOT FOR RELEASE!
 * 
 * @author 		ch1092, cd828
 * @version		v0.1 20/05/2016
 */
public class PasswordHandlerTests {

	String clientDetailsLocation = "src/DemoClientDetails.txt";
	
	/**
	 * Test to check the user details can be stored in an object
	 */
	@Test
	public void checkClientDetailsClassStoresCorrectData() {
		ClientDetails clientDetails = new ClientDetails();
		String userName = "bob";
		String hash = "ABCDEF";
		byte[] salt = hash.getBytes();
		
		clientDetails.setUserName(userName);
		clientDetails.setHash(hash);
		clientDetails.setSalt(salt);
		
		assertEquals(userName, clientDetails.getUserName());
		assertEquals(hash, clientDetails.getHash());
		assertEquals(salt, clientDetails.getSalt());
	}
	
	/**
	 * Test to check the user details can be stored in a file
	 */
	@Test
	public void checkClientDetailsFileStoresCorrectData() {
		ClientDetails enteredClientDetails = new ClientDetails();
		
		ClientDetails retrievedClientDetails = new ClientDetails();
		
		String userName = "bob";
		String password = "ABCDEF";
		
		ClientPasswordHandler clientPasswordHandler = new ClientPasswordHandler();
		try {
			enteredClientDetails = clientPasswordHandler.encryptNewPassword(userName, password);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServerPasswordHandler serverPasswordHandler = new ServerPasswordHandler();
		serverPasswordHandler.storeDetails(enteredClientDetails, clientDetailsLocation);
		
		retrievedClientDetails = serverPasswordHandler.getDetails(userName, clientDetailsLocation);
		
		try {
			serverPasswordHandler.clearClientDetailsFile(clientDetailsLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		assertEquals(enteredClientDetails.getUserName(), retrievedClientDetails.getUserName());
		assertEquals(enteredClientDetails.getHash(), retrievedClientDetails.getHash());
		
		byte[] a = enteredClientDetails.getSalt();
		byte[] b = retrievedClientDetails.getSalt();
		assertTrue(Arrays.equals(b,a));
		
	}
	
	/**
	 * Test to check that the password can be verified once stored
	 */
	@Test
	public void checkPasswordValidates() {
		ClientDetails enteredClientDetails = new ClientDetails();
		
		ClientDetails retrievedClientDetails = new ClientDetails();
		
		String userName = "bob";
		String rightPassword = "ABCDEF";
		String wrongPassword = "badPassword";
		String wrongHash = null;
		String rightHash = null;
		
		ClientPasswordHandler clientPasswordHandler = new ClientPasswordHandler();
		try {
			enteredClientDetails = clientPasswordHandler.encryptNewPassword(userName, rightPassword);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServerPasswordHandler serverPasswordHandler = new ServerPasswordHandler();
		serverPasswordHandler.storeDetails(enteredClientDetails, clientDetailsLocation);
		
		retrievedClientDetails = serverPasswordHandler.getDetails(userName, clientDetailsLocation);
				
		try {
			serverPasswordHandler.clearClientDetailsFile(clientDetailsLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wrongHash = clientPasswordHandler.generateHash(wrongPassword, retrievedClientDetails.getSalt());
		
		assertFalse(enteredClientDetails.getHash().equals(wrongHash));
		
		rightHash = clientPasswordHandler.generateHash(rightPassword, retrievedClientDetails.getSalt());
		
		assertTrue(enteredClientDetails.getHash().equals(rightHash));
		
		assertFalse(serverPasswordHandler.validateHash(wrongHash, enteredClientDetails));
		
		assertTrue(serverPasswordHandler.validateHash(rightHash, enteredClientDetails));
	}
}
