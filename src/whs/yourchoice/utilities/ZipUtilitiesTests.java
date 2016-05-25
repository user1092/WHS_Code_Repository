/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.utilities;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

/**
 * Method to test the ZipUtilities Class
 * 
 * @author ch1092
 * @version v0.1 25/05/16
 */
public class ZipUtilitiesTests {

	/**
	 * Method to ensure that a file is extracted from a zip
	 * deletes the folder afterwards
	 */
	@Test
	public void ensureFileIsExtracted() {
		ZipUtilities unzipper = new ZipUtilities();
		
		File zippedFile = new File("Test_Zip.zip");
		
		boolean fileFound = false;
		
		try {
			unzipper.unzip(zippedFile, "Test_Folder");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File folder = new File("Test_Folder");
		
		File f = new File("Test_Folder/File.txt");
		if(f.exists() && !f.isDirectory()) { 
		    fileFound = true;
		}
				
		try {
			delete(folder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(fileFound);
	}
	
	
	/**
	 * Method to delete a file or folder
	 * 
	 * @param f	-	The file/folder to be deleted 
	 * @throws IOException
	 */
	void delete(File f) throws IOException {
	  if (f.isDirectory()) {
	    for (File c : f.listFiles())
	      delete(c);
	  }
	  if (!f.delete())
	    throw new FileNotFoundException("Failed to delete file: " + f);
	}

}
