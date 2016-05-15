package whs.yourchoice.utilities.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author ch1092
 *
 */
public class ClientPasswordHandler {

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
	 * @param password
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
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
	}
	
}
