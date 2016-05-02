/**
 * Author: Alex Butcher & Noor Mansoor 
 * Company: PesenTech. 
 * Date Created: 2 March 2016
 * 
 **/

package presentech;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class TextHandler {

	//This class can return the following variables
	static int tStartTime;
	static int tDuration;

	
	//Variables used by handler
	static Text prevText;
	static StringBuilder textFromFile = new StringBuilder();
	static String string;
	
	//Adjust this according to your desired right side margin.
	static float margin = 0.9f;

	
	/*The methods are static because it makes calling them easier. 
	 **You can change that if you prefer a different method of accessing methods.
	 **This method accepts all the parameters to do with the parsed text and creates 
	a text object to display on a JavaFX canvas.*/
	public static TextFlow createText(Canvas canvas, String filePath, String text, String font, String fontColor, int fontSize, double xStart, double yStart, int startTime, int duration){
		TextFlow textFlow = new TextFlow();
		//Position and wrapping calculations.
		// Note: if you would like to use floats instead of doubles change 
		// the doubles to floats in the parameters above. Change the doubles below to floats
		// and add a cast to float. e.g float xtStart = (float) (canvas.getWidth()*xStart);
		double xtStart = (canvas.getWidth()*xStart);
		double ytStart = (canvas.getHeight()*yStart);
		double wrappingWidth = ((margin - xStart)*canvas.getWidth());

		//Read and display text from a source file
		if (filePath != null){
			try {
				FileReader fr = new FileReader(filePath);
				BufferedReader br = new BufferedReader(fr);
				String aLine;
				while ((aLine = br.readLine()) != null) {
					textFromFile.append(aLine).append("\n");
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			string = textFromFile.toString();
		} else {
			// If no input file then get text from method parameter
			string = text;
		}
		
		// Adds text to JavaFX Text objects then adds them to a TextFlow. 
		// Each time a bold or italics tag is encountered a new Text object is created.
		int tagTracker = 0; // Where the next Text object should begin from in the String
		boolean bold = false;
		boolean italic = false;
		for (int i = 0; i<string.length(); i++){
			if (string.charAt(i) == '<'){
				// If there are not 2 consecutive bold/italic tags, create a new text 
				// object containing the string up to the point of the tag.
				if (tagTracker != i) {
					prevText = new Text(string.substring(tagTracker, i));
					prevText.setFont(Font.font(font, fontSize));
					prevText.setFill(Color.web(fontColor));
					
					// Set the bold and italic attributes if we are inside bold and/or italic tags
					if (bold && italic) {
						prevText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.ITALIC, fontSize));
					} else if (bold) {
						prevText.setFont(Font.font(font, FontWeight.BOLD, fontSize));
					} else if (italic) {
						prevText.setFont(Font.font(font, FontPosture.ITALIC, fontSize));
					}
					//Add the new text object to the TextFlow object
					textFlow.getChildren().add(prevText);
				}
				
				//Set or reset the bold and italic status depending on the type of tag encountered.
				//Move tagTracker to the character after the currently encountered tag.
				if (string.charAt(i+1) == 'b'){
					bold = true;
					tagTracker = i+3;
				} else if (string.charAt(i+1) == 'i') {
					italic = true;
					tagTracker = i+3;
				} else if (string.charAt(i+1) == '/') {
					if (string.charAt(i+2) == 'b') {
						bold = false;
						tagTracker = i+4;
					} else if (string.charAt(i+2) == 'i') {
						italic = false;
						tagTracker = i+4;
					}
				}
			}
		}
		// Add whatever text still remains to a new text object.
		prevText = new Text(string.substring(tagTracker, string.length()));
		prevText.setFont(Font.font(font, fontSize));
		prevText.setFill(Color.web(fontColor));
		textFlow.getChildren().add(prevText);

		textFlow.setMaxWidth(wrappingWidth);
		textFlow.setLayoutX(xtStart);
		textFlow.setLayoutY(ytStart);
		/*
		 * Text alignment was not mentioned in the contract but it is possible so we've added
		 * it in case you'd like to use it. You can change the TextAlignment property to 
		 * center, left, right or justify. 
		 */
		textFlow.setTextAlignment(TextAlignment.JUSTIFY);
		
		 /*
		  * Removed as not implementing these timings 
		  */
		
		//Start time and Duration animation
//		if (duration == -1) {
//			Timeline tl = new Timeline(
//					  new KeyFrame(
//			                  Duration.millis(0),
//			                  new KeyValue(
//			                          textFlow.opacityProperty(), 
//			                          0, 
//			                          Interpolator.DISCRETE
//			                  )
//			          ),
//			          new KeyFrame(
//			                  Duration.millis(startTime),
//			                  new KeyValue(
//			                          textFlow.opacityProperty(), 
//			                          
//			                          1, 
//			                          Interpolator.DISCRETE
//			                  )
//			          )
//			 );
//			tl.play();
//		} else {
//			Timeline tl = new Timeline(
//					  new KeyFrame(
//			                  Duration.millis(0),
//			                  new KeyValue(
//			                          textFlow.opacityProperty(), 
//			                          0, 
//			                          Interpolator.DISCRETE
//			                  )
//			          ),
//			          new KeyFrame(
//			                  Duration.millis(startTime),
//			                  new KeyValue(
//			                          textFlow.opacityProperty(), 
//			                          
//			                          1, 
//			                          Interpolator.DISCRETE
//			                  )
//			          ),
//			          new KeyFrame(
//			                  Duration.millis(duration),
//			                  new KeyValue(
//			                          textFlow.opacityProperty(), 
//			                          0, 
//			                          Interpolator.DISCRETE
//			                  )
//			                  )
//			          );
//			tl.play();			
//		}	
		
		return textFlow;
	}


}

