package whs.yourchoice.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import whs.yourchoice.client.ViewFeedbackGui.Comment;


/**
* Class for parsing a text file and extracting user comments
*
* @author user513, user679
* @version v0.1 03/05/16
*/
public class CommentParser {
	
	public final ObservableList<Comment> comments = FXCollections.observableArrayList();
	private float avgRating = 0;
		
	/**
	 * Function that reads a text file and parses it to extract names, comments and ratings
	 * @param textFilePath - Location of text file to be parsed
	 * @throws FileNotFoundException
	 */
	public void parseTextFile(String textFilePath) throws FileNotFoundException {
		
		// List of strings used to store parsed tokens
		final List<String> commentsList = new ArrayList<String>();
		int newLinesNeeded = 0;
		
		// Character limit to dictate line wrapping
		int characterLimit = 85;
		int location = 0;
		String tempString = "";
		
		// These variables need to be floats to allow floating point division
		// when calculating average
		float ratingTotal = 0;
		float ratingCounter = 0;
		
		BufferedReader br = null;
		try {
			
			// Parse text file line by line
			br = new BufferedReader(new FileReader(textFilePath));
			String line;
			while ((line = br.readLine()) != null) {

			    // Regex code to split a line of the text file by commas, only if the comma
				// is followed by an even number of quotation marks
				// This means that commas inside quotation marks won't cause a line split
				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);;
				
				// Each line should generate three tokens which are then appended to the list
				commentsList.add(tokens[0]);
				commentsList.add(tokens[1]);
				commentsList.add(tokens[2]);
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
		
		// Loop through list of tokens and re-format them accordingly
		for (int i=0; i<commentsList.size(); i++)
		{
			// Store the current item in tempString for re-formatting
			tempString = commentsList.get(i);			
			
			if (i%3 != 2)
			{
				// If i is not 2 more than a multiple of three, the current item is not a rating
				// Therefore the first and last characters, which will be quotation marks, can be removed
				tempString = tempString.substring(1, tempString.length()-1);
				commentsList.set(i,tempString);
				
				// If i is 1 more than a multiple of 3, the current item will be a comment
				if (i%3 == 1)
				{
					// If the comment is longer than the character limit new lines 
					// need to be inserted
					// This is essentially manual text wrapping
					if (tempString.length() > characterLimit)
					{
						// Determine how many new lines will need to be inserted
						newLinesNeeded = tempString.length()/characterLimit;

						// Cycle through and add as many new lines as required
						for (int j = 0; j < newLinesNeeded;j++)
						{
							// Location is initially set to the point at which the character limit
							// is reached
							location = characterLimit*(j+1);

							// Location is the decremented until a space is found, 
							// to avoid inserting a new line in the middle of a word
							while (tempString.charAt(location)  != ' ')
							{
								location--;
							}
							// A new line is then inserted after the space found
							tempString = tempString.substring(0, location + 1) + "\n" 
										+ tempString.substring(location + 1, tempString.length());	
						}
						
						// The reformatted string then replaces the original in the list
						commentsList.set(i,tempString);
					}
				}
			}
			
			// If i is 2 more than a multiple of 3, the current item is a rating and 
			// so a new instance of the comment class can be created
			else
			{
				comments.add(new Comment(commentsList.get(i-2), commentsList.get(i-1), 
													commentsList.get(i)));
				
				// Parses the current rating as an integer for averaging purposes
				ratingTotal += Integer.parseInt(tempString);
				ratingCounter++;
			}
		}
		
		// Calculates average rating
		avgRating = ratingTotal/ratingCounter;			
	}
	
	/**
	 * Function to return average of ratings found in comments text file
	 * @return avgRating - Float, the average module rating
	 */
	public float getAverageRating()
	{
		return avgRating;
	}
	
	/**
	 * Function to return list of comments
	 * @return comments - List of instances of Comment class, containing information
	 * 						from text file
	 */
	public ObservableList<Comment> getComments()
	{
		return comments;
	}

}


