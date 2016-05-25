/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */
package whs.yourchoice.parsers;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.Test;

import whs.yourchoice.client.ViewFeedbackGui.Feedback;

/**
* Test class for CommentParser
* 
* NOT FOR RELEASE!
*
* @author jcl513, gw679
* @version v0.1 03/05/16
*/
public class CommentParserTests {
	
	public ObservableList<Feedback> testContents = FXCollections.observableArrayList();
	
	/**
	* Test - checkCommentsContents 
	* Parses the text file and then checks the content of each created comment
	*/
	@Test
	public void checkCommentsContents() {
		CommentParser testParser = new CommentParser();
		
		// Parse test text file
		try {
			testParser.parseTextFile("src/CommentsTestFile.txt");
		} catch (FileNotFoundException e) {
			System.out.println("Comments test file not found");
			e.printStackTrace();
		}
		
		// Retrieve list of comments and check contents
		testContents = testParser.getComments();
		
		assertEquals("Jason", testContents.get(0).getName());
		assertEquals("This is the first comment", testContents.get(0).getComment());
		assertEquals("4", testContents.get(0).getRating());
		
		assertEquals("George", testContents.get(1).getName());
		assertEquals("This is the second comment", testContents.get(1).getComment());
		assertEquals("5", testContents.get(1).getRating());
		
		assertEquals("Chris", testContents.get(2).getName());
		assertEquals("This is, the third comment, and it demonstrates, that commas in the comment, don't \ncause issues.", testContents.get(2).getComment());
		assertEquals("0", testContents.get(2).getRating());
		
		assertEquals("Cos", testContents.get(3).getName());
		assertEquals("This is the \"fourth\" comment !?£$%^&*()", testContents.get(3).getComment());
		assertEquals("5", testContents.get(3).getRating());
	}
}
