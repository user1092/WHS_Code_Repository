/**
 * Licensing information
 */
package whs.yourchoice.utilities.encryption;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Class for encrypting and decrypting data using RSA 
 * 
 * @author 		ch1092, cd828
 * @version		v0.1 24/05/2016
 */
public class RsaEncryption {
	
	private final int KEY_SIZE = 3072;
	
	
	/**
	 * Create some new keys to be stored in  file
	 * 
	 * @param privKeyFileName	-	The filename of the private key to be saved
	 * @param pubKeyFileName	-	The filename of the public key to be saved
	 * @throws IOException
	 */
	public void createSavedKeys(String privKeyFileName, String pubKeyFileName) throws IOException {
		KeyPairGenerator kpg = null;
		
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		kpg.initialize(KEY_SIZE);
		KeyPair kp = kpg.genKeyPair();
		KeyFactory fact = null;
		
		try {
			fact = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RSAPublicKeySpec pub = null;
		
		try {
			pub = fact.getKeySpec(kp.getPublic(),
			  RSAPublicKeySpec.class);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RSAPrivateKeySpec priv = null;
		
		try {
			priv = fact.getKeySpec(kp.getPrivate(),
			  RSAPrivateKeySpec.class);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		saveKey(pubKeyFileName, pub.getModulus(),
		  pub.getPublicExponent());
		saveKey(privKeyFileName, priv.getModulus(),
		  priv.getPrivateExponent());
	}
	

	/**
	 * Method to save the key to the specified files
	 * 
	 * @param fileName	-	The filename to save to.
	 * @param modulus	-	The modulus used by RSA
	 * @param exponent	-	The exponent used by RSA
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveKey(String fileName, BigInteger modulus, BigInteger exponent) 
									throws FileNotFoundException, IOException {
		
		ObjectOutputStream oout = new ObjectOutputStream(
			    new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
		    oout.writeObject(modulus);
		    oout.writeObject(exponent);
		  } catch (Exception e) {
		    throw new IOException("Unexpected error", e);
		  } finally {
		    oout.close();
		  }
	}
	
	
	/**
	 * Method to create keys that are not to be saved
	 * 
	 * @return	keys	-	An array of Key, first entry is the public key second is the private key
	 */
	public Key[] createKeys() {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kpg.initialize(KEY_SIZE);
		KeyPair kp = kpg.genKeyPair();
		Key publicKey = kp.getPublic();
		Key privateKey = kp.getPrivate();
		Key[] keys = {publicKey, privateKey};
		return keys;
	}

	
	/**
	 * Method for encrypting raw data using RSA
	 * 
	 * @param data	-	The data to be encrypted
	 * @param pubKeyFileName	-	The file that holds the public key to be used to encrypt
	 * @return encrytedData	-	The data encrypted using the keys
	 * @throws IOException
	 */
	public byte[] rsaEncrypt(byte[] data, String pubKeyFileName) throws IOException {
		PublicKey pubKey = readPublicKeyFromFile(pubKeyFileName);
		
		return rsaEncrypt(data, pubKey);
	}
	
	
	/**
	 * Method for encrypting raw data using RSA
	 * 
	 * @param data	-	The data to be encrypted
	 * @param pubKey	-	The public key used to encrypt the data
	 * @return	encrytedData	-	The data encrypted using the keys
	 * @throws IOException
	 */
	public byte[] rsaEncrypt(byte[] data, Key pubKey) throws IOException {
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] cipherData = null;
		
		try {
			cipherData = cipher.doFinal(data);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipherData;
	}
	

	/**
	 * Method to read the public key from a file
	 * 
	 * @param keyFileName	-	The filename of the file that holds the public key
	 * @return publicKey	-	The public key extracted from the file
	 * @throws IOException
	 */
	private PublicKey readPublicKeyFromFile(String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		
		ObjectInputStream oin =
			    new ObjectInputStream(new BufferedInputStream(in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
		    throw new RuntimeException("Spurious serialisation error", e);
		} finally {
		    oin.close();
		}
	}
	
	
	/**
	 * Method to decrypt raw data encoded using RSA
	 * 
	 * @param data	-	The data to be decrypted
	 * @param privKeyFileName	-	The file that holds the private key
	 * @return decryptedData	-	The data decrypted using the private key
	 * @throws IOException
	 */
	public byte[] rsaDecrypt(byte[] data, String privKeyFileName) throws IOException {
		PrivateKey privKey = readPrivateKeyFromFile(privKeyFileName);
		
		return rsaDecrypt(data, privKey);
	}
	
	
	/**
	 * Method to decrypt raw data encoded using RSA
	 * 
	 * @param data	-	The data to be decrypted
	 * @param privKey	-	The private key
	 * @return decryptedData	-	The data decrypted using the private key
	 * @throws IOException
	 */
	public byte[] rsaDecrypt(byte[] data, Key privKey) throws IOException {
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, privKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] cipherData = null;
		
		try {
			cipherData = cipher.doFinal(data);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipherData;
	}

	
	/**
	 * Method to read the read the private key from a file
	 * 
	 * @param keyFileName	-	The filename of the file that holds the private key
	 * @return privateKey	-	The private key extracted from the file
	 * @throws IOException
	 */
	private PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		
		ObjectInputStream oin =
			    new ObjectInputStream(new BufferedInputStream(in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privKey = fact.generatePrivate(keySpec);
			return privKey;
		} catch (Exception e) {
		    throw new RuntimeException("Spurious serialisation error", e);
		} finally {
		    oin.close();
		}
	}
	
	
	/**
	 * Method to decrypt data as an object
	 * 
	 * @param dataToDecrypt	-	The data to be decrypted
	 * @param privateKey	-	The filename that contains the key used to decrypt
	 * @return decrytedObject	-	The object decrypted
	 */
	public Object rsaDecryptDataToObject(byte[] dataToDecrypt, String privateKeyFileName) {
		byte[] decryptedData = null;
				
		try {
			decryptedData = rsaDecrypt(dataToDecrypt, privateKeyFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
		ObjectInput in = null;
		Object objectDecrypted = new Object();
		try {
			try {
				in = new ObjectInputStream(bis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				objectDecrypted = (Object) in.readObject();
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
		return objectDecrypted;
	}
	
		
	/**
	 * Method to decrypt data as an object
	 * 
	 * @param dataToDecrypt	-	The data to be decrypted
	 * @param privateKey	-	The key used to decrypt
	 * @return decrytedObject	-	The object decrypted
	 */
	public Object rsaDecryptDataToObject(byte[] dataToDecrypt, Key privateKey) {
		byte[] decryptedData = null;
				
		try {
			decryptedData = rsaDecrypt(dataToDecrypt, privateKey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
		ObjectInput in = null;
		Object objectDecrypted = new Object();
		try {
			try {
				in = new ObjectInputStream(bis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				objectDecrypted = (Object) in.readObject();
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
		return objectDecrypted;
	}
	
	
	/**
	 * Method to encrypt an object using RSA
	 * 
	 * @param objectToEncrypt	-	The object to be encrypted
	 * @param publicKey	-	The key to use to encrypt
	 * @return encryptedData	-	The data encrypted
	 */
	public byte[] rsaEncryptObject(Object objectToEncrypt, Key publicKey) {
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
			out.writeObject(objectToEncrypt);
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
			encryptedData = rsaEncrypt(dataToEncrypt, publicKey);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return encryptedData;
	}
	
	
	/**
	 * Method to encrypt an object using RSA
	 * 
	 * @param objectToEncrypt	-	The object to be encrypted
	 * @param publicKeyFileName	-	The filename that contains the key to use to encrypt
	 * @return encryptedData	-	The data encrypted
	 */
	public byte[] rsaEncryptObject(Object objectToEncrypt, String publicKeyFileName) {
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
			out.writeObject(objectToEncrypt);
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
			encryptedData = rsaEncrypt(dataToEncrypt, publicKeyFileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return encryptedData;
	}
}
