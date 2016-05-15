package whs.yourchoice.utilities.encryption;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author ch1092
 *
 */
public class PasswordHandlerTests {

	/**
	 * 
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
	 * 
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
		serverPasswordHandler.storeDetails(enteredClientDetails);
		
		retrievedClientDetails = serverPasswordHandler.getDetails(userName);
		
		try {
			serverPasswordHandler.clearClientDetailsFile();
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
	 * 
	 */
	@Test
	public void checkPasswordValidates() {
		ClientDetails enteredClientDetails = new ClientDetails();
		
		ClientDetails retrievedClientDetails = new ClientDetails();
		
		String userName = "bob";
		String rightPassword = "ABCDEF";
		String wrongPassword = "bollocks";
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
		serverPasswordHandler.storeDetails(enteredClientDetails);
		
		retrievedClientDetails = serverPasswordHandler.getDetails(userName);
				
		try {
			serverPasswordHandler.clearClientDetailsFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wrongHash = clientPasswordHandler.generateHash(wrongPassword, retrievedClientDetails.getSalt());
		
		assertFalse(enteredClientDetails.getHash().equals(wrongHash));
		
		rightHash = clientPasswordHandler.generateHash(rightPassword, retrievedClientDetails.getSalt());
		
		assertTrue(enteredClientDetails.getHash().equals(rightHash));
	}
}
