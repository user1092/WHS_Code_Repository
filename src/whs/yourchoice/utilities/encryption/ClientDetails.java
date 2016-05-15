package whs.yourchoice.utilities.encryption;

/**
 * @author ch1092
 *
 */
public class ClientDetails {

	private String userName = null;
	private String hash = null;
	private byte[] salt = null;
	
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	
	
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	
	
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	
	/**
	 * @return the salt
	 */
	public byte[] getSalt() {
		return salt;
	}
	
	
	/**
	 * @param salt the salt to set
	 */
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
}
