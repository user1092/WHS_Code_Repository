/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
* Class for handling zip files
* 
* @author cd828 & ch1092
* @version v0.1 25/05/16
*/
public class ZipUtilities {
	

	private static final int BUFFER_SIZE = 4096;

	
	/**
	 * Method to unzip the entire zip file
	 * 
	 * @param zippedFile		-	The zip file to extract
	 * @param unzipDirectory	-	The directory to unzip the file to
	 * @throws IOException
	 */
	public void unzip(File zippedFile, String unzipDirectory) throws IOException {
		File destDir = new File(unzipDirectory);
		if (!destDir.exists()) {
            destDir.mkdir();
        }
		FileInputStream inputStream = new FileInputStream(zippedFile);
        ZipInputStream zipIn = new ZipInputStream(inputStream);
        ZipEntry entry = zipIn.getNextEntry();
        // extract all entries in the zip file
        while (entry != null) {
            String filePath = unzipDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
	}
	
	
	/**
	 * Method to extract a file from a zip
	 * 
	 * @param zipIn		-	The zip stream to extract a file from
	 * @param filePath	-	The location to extract the file to
	 * @throws IOException
	 */
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
	
}
