/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import whs.yourchoice.client.ViewFeedbackGui.Feedback;


/**
* Class for parsing a text file and extracting user comments
*
* @author jcl513, gw679, ch1092
* @version v0.3 28/05/16
*/
public class CommentParser {
	
	public final ObservableList<Feedback> feedback = FXCollections.observableArrayList();
	private float avgRating = 0;
		
	/**
	 * Function that reads a text file and parses it to extract names, comments and ratings
	 * @param textFilePath - Location of text file to be parsed
	 * @throws FileNotFoundException
	 */
	public void parseTextFile(String textFilePath) throws FileNotFoundException {
		
		// List of strings used to store parsed tokens
		final List<String> tokenList = new ArrayList<String>();
		int newLinesNeeded = 0;
		
		// Character limit to dictate line wrapping
		int characterLimit = 70;
		int location = 0;
		String tempString = "";
		
		// These variables need to be floats to allow floating point division
		// when calculating average
		float ratingTotal = 0;
		float ratingCounter = 0;
		
		int remainder = 0;
		
		BufferedReader br = null;
		try {
			
			// Parse text file line by line
			br = new BufferedReader(new FileReader(textFilePath));
			String line;
			while ((line = br.readLine()) != null) {

			    if (line.isEmpty() == false)
			    {
//			    	System.out.println("Full line detected");
			    	// Regex code to split a line of the text file by commas, only if the comma
					// is followed by an even number of quotation marks
					// This means that commas inside quotation marks won't cause a line split
					String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);;
					
					// Each line should generate three tokens which are then appended to the list
					tokenList.add(tokens[0]);
					tokenList.add(tokens[1]);
					tokenList.add(tokens[2]);
					tokenList.add(tokens[3]);
					tokenList.add(tokens[4]);
			    }
			    // Parser ignores empty lines at the start of a text file
			    else
			    {
			    	System.out.println("Empty line detected");
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
		
		// Loop through list of tokens and re-format them accordingly
		for (int i=0; i<tokenList.size(); i++)
		{
			// Store the current item in tempString for re-formatting
			tempString = tokenList.get(i);
			
			remainder = i%5;

			switch(remainder)
			{
				// Current item is a name
				case 0:
					// Remove quotation marks surrounding name
					tempString = tempString.substring(1, tempString.length()-1);
					break;
					
				// Current item is a comment
				case 1:
					// Remove quotation marks surrounding comment
					tempString = tempString.substring(1, tempString.length()-1);
					
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
					}
					break;
					
				// Current item is a rating
				case 2:
					// Parse the current rating as an integer for averaging purposes
					ratingTotal += Integer.parseInt(tempString);
					ratingCounter++;
					break;
					
				case 3:
					// Do nowt
					
					break;
					
				// Current item is approved therefore a new instance of Feedback can be
				// created
				case 4:
					feedback.add(new Feedback(tokenList.get(i-4), tokenList.get(i-3), 
							tokenList.get(i-2), tokenList.get(i-1), tokenList.get(i)));
					break;	
					
				default:
					break;
					
			}
			// Set formatted item into list
			tokenList.set(i,tempString);
		}
		
		// Calculates average rating
		avgRating = ratingTotal/ratingCounter;			
	}
	
	/**
	 * Function to return average of ratings found in feedback text file
	 * @return avgRating - Float, the average module rating
	 */
	public float getAverageRating()
	{
		return avgRating;
	}
	
	/**
	 * Function to return list of comments
	 * @return comments - List of instances of Feedback class, containing information
	 * 						from text file
	 */
	public ObservableList<Feedback> getComments()
	{
		return feedback;
	}

}


