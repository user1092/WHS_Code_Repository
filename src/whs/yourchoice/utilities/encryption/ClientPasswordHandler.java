/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.utilities.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Class for handling the passwords on the client side
 * 
 * @author 		ch1092, cd828
 * @version		v0.1 20/05/2016
 */
public class ClientPasswordHandler {

	/**
	 * Method to encrypt a password and return the user name, hash and salt in a object
	 * 
	 * @param userName	-	The user name of the client
	 * @param password	-	The password entered by the client
	 * @return clientDetails	-	An object that contains the user name, hash and salt
	 * @throws NoSuchAlgorithmException
	 */
	public ClientDetails encryptNewPassword(String userName, String password) throws NoSuchAlgorithmException {
		byte[] salt = null;
		String hash = null;
		salt = getSalt();
		hash = generateHash(password, salt);
		
		ClientDetails currentClient = new ClientDetails();
		
		currentClient.setUserName(userName);
		currentClient.setHash(hash);
		currentClient.setSalt(salt);
		
        return currentClient;
	}

	/**
	 * Method to create a hash from the password and salt
	 * 
	 * @param password	-	The password to be encrypted
	 * @param salt		-	The salt to be hashed with the password
	 * @return hash		-	The encrypted password
	 */
	public String generateHash(String password, byte[] salt) {
		String hash = null;
		try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
		return hash;
	}

	/**
	 * Method to randomly create a salt.
	 * 
	 * @return salt	-	The salt to be hashed with the password
	 * @throws NoSuchAlgorithmException
	 */
	private byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
	}
	
}
