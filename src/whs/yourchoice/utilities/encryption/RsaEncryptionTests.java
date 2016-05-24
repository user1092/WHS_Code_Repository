/**
 * Licensing information
 */
package whs.yourchoice.utilities.encryption;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.Test;

/**
 * Class for testing the RsaEncrytption class
 * 
 * @author 		ch1092, cd828
 * @version		v0.1 24/05/2016
 */
public class RsaEncryptionTests {	
	
	/**
	 * Test that the clientDetails can be encrypted and decrypted without loss of data.
	 */
	@Test
	public void checkClientDetailsEncryptsAndDecrypts() {
		RsaEncryption rsaHandler = new RsaEncryption();
		
		try {
			rsaHandler.createSavedKeys("serverPrivKeyFileName.key", "serverPubKeyFileName.key");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ClientDetails clientDetails = new ClientDetails();
		
		clientDetails.setHash("hash");
		clientDetails.setUserName("userName");
		clientDetails.setSalt(clientDetails.getHash().getBytes());
		
		byte[] encryptedData = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] dataToEncrypt = null;
		try {
		  try {
			out = new ObjectOutputStream(bos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		  try {
			out.writeObject(clientDetails);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  dataToEncrypt = bos.toByteArray();
		}
		finally {
			try {
			  if (out != null) {
			    out.close();
			  }
			} catch (IOException ex) {
			  // ignore close exception
			}
			try {
			  bos.close();
			} catch (IOException ex) {
			  // ignore close exception
			}
		}
		try {
			encryptedData = rsaHandler.rsaEncrypt(dataToEncrypt, "serverPubKeyFileName.key");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		byte[] decryptedData = null;
		
		try {
			decryptedData = rsaHandler.rsaDecrypt(encryptedData, "serverPrivKeyFileName.key");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
		ObjectInput in = null;
		ClientDetails clientDetailsDecrypted = new ClientDetails();
		try {
			try {
				in = new ObjectInputStream(bis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				clientDetailsDecrypted = (ClientDetails) in.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			try {
				bis.close();
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		
		assertEquals(clientDetails.getHash(), clientDetailsDecrypted.getHash());
		assertEquals(clientDetails.getUserName(), clientDetailsDecrypted.getUserName());
		
		byte[] a = clientDetails.getSalt();
		byte[] b = clientDetailsDecrypted.getSalt();
		assertTrue(Arrays.equals(b,a));
	}

}
