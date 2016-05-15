package whs.yourchoice.utilities.encryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

/**
 * @author ch1092
 *
 */
public class ServerPasswordHandler {
	
	String clientDetailsLocation = "src/DemoClientDetails.txt";
	
	/**
	 * @param clientDetails
	 */
	public void storeDetails(ClientDetails clientDetails) {
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
	 * @param clientDetails
	 * @return
	 */
	private String formatClientDetails(ClientDetails clientDetails) {
			
		String formattedClientDetails = null;
		
		String formattedSalt = null;
		formattedSalt = Base64.encodeBase64String(clientDetails.getSalt());
		
		// Concatenate feedback to form a single string ready for insertion
		// into text file by inserting a comma between each field
		formattedClientDetails = clientDetails.getUserName() + "," + clientDetails.getHash() 
			+ "," + formattedSalt + ",";
		
		return formattedClientDetails;
	}
	
	/**
	 * @param userName
	 * @return
	 */
	public ClientDetails getDetails(String userName) {
		BufferedReader br = null;
		
		ClientDetails clientDetails = new ClientDetails();
		
		
		try {
			
			// Parse text file line by line
			br = new BufferedReader(new FileReader(clientDetailsLocation));
			String line;
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
	 * @param hash
	 * @param clientDetails
	 * @return
	 */
	public boolean validateHash(String hash, ClientDetails clientDetails) {
		if (hash == clientDetails.getHash()) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * @throws IOException
	 */
	public void clearClientDetailsFile() throws IOException {
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(clientDetailsLocation));
		bw.close();
	}
}
