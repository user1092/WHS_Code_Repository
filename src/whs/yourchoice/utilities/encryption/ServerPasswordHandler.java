/**
 * Licensing information
 */
package whs.yourchoice.utilities.encryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

/**
 * Class for handling the passwords on the server side
 * 
 * @author 		ch1092, cd828
 * @version		v0.1 20/05/2016
 */
public class ServerPasswordHandler {
	
	
	/**
	 * Method to store the user details in a txt file.
	 * 
	 * @param clientDetails		-	The client details to be stored
	 * @param clientDetailsLocation	-	The file location where the details are stored
	 */
	public void storeDetails(ClientDetails clientDetails, String clientDetailsLocation) {
		String formattedClientDetails = formatClientDetails(clientDetails);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(clientDetailsLocation, true))){
			bw.newLine();
			bw.write(formattedClientDetails);
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	/**
	 * Method to format the client details into a csv format.
	 * 
	 * @param clientDetails		-	The client details to be formatted
	 * @return formattedClientDetails	-	The formatted client details
	 */
	private String formatClientDetails(ClientDetails clientDetails) {
			
		String formattedClientDetails = null;
		
		String formattedSalt = null;
		formattedSalt = Base64.encodeBase64String(clientDetails.getSalt());
		
		/*
		 *  Concatenate client details to form a single string ready for insertion
		 *  into text file by inserting a comma between each field
		 */		
		formattedClientDetails = clientDetails.getUserName() + "," + clientDetails.getHash() 
			+ "," + formattedSalt + ",";
		
		return formattedClientDetails;
	}
	
	/**
	 * Method to retrieve the client details from the txt file.
	 * 
	 * @param userName	-	The user name of the details to be retrieved
	 * @param clientDetailsLocation	-	The file location where the details are stored
	 * @return clientDetails	-	The details relating to the user name passed in.
	 */
	public ClientDetails getDetails(String userName, String clientDetailsLocation) {
		BufferedReader br = null;
		
		ClientDetails clientDetails = new ClientDetails();
		
		
		try {
			
			// Parse text file line by line
			br = new BufferedReader(new FileReader(clientDetailsLocation));
			String line;
			// Search line by line till the username is found
			while ((line = br.readLine()) != null) {

			    // Regex code to split a line of the text file by commas, only if the comma
				// is followed by an even number of quotation marks
				// This means that commas inside quotation marks won't cause a line split
				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);;
				
				if (tokens[0].equals(userName)) {
					clientDetails.setUserName(tokens[0]);
					clientDetails.setHash(tokens[1]);
					clientDetails.setSalt(Base64.decodeBase64(tokens[2]));
					break;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return clientDetails;
	}
	
	/**
	 * Method to check whether the hash entered matches that of what is stored.
	 * 
	 * @param hash	-	The hash created by the user entering the password
	 * @param clientDetails	-	The client details containing the hash that should match the passed in hash 
	 * @return valid	-	Boolean that states whether the hashes match
	 */
	public boolean validateHash(String hash, ClientDetails clientDetails) {
		if (clientDetails.getHash().equals(hash)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Method to clear all stored user entries in the txt file
	 * 
	 * DANGEROUS ONLY CALL IF NEEDED!
	 * 
	 * @param clientDetailsLocation	-	The file location where the details are stored
	 * @throws IOException
	 */
	public void clearClientDetailsFile(String clientDetailsLocation) throws IOException {
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(clientDetailsLocation));
		bw.close();
	}
}
