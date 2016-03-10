/**
 * Author: Alex Butcher & Noor Mansoor 
 * Company: PesenTech. 
 * Date Created: 2 March 2016
 * 
 * This class is written simply to provide an example of using the 
 * TextHanlder class and its methods. It is not otherwise required.
 **/

package presentech;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Main extends Application {

	//these variables represent the parameters that would have been parsed by your software;
	//you can uncomment the following and comment out the next line. 
	//static String sourceFile = "C:/Users/Alex/Documents/TextHandler/src/sourceFile.txt";
	static String sourceFile = null;
	static String text = "Test text <b>bold test</b> <i>italic test</i> <i><b>bold italic test</b></i> normal <b><i>bold italic test</i></b> <b><i>test</b> of text</i> back to normal";
	static String font = "Verdana";
	static String fontColor = "354297";
	static int fontSize = 15;
	static int startTime = 0;
	static int duration = -1;
	static float xStart = 0.1f;
	static float yStart = 0.1f;
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage theStage) {
		final Group rootGroup = new Group();
	    final Scene scene = new Scene(rootGroup, 800, 600, Color.BEIGE);
	    
	    //Canvas is used only for size, no text is added to the canvas.
	    final Canvas canvas = new Canvas(800,600);
	    
	    TextFlow t;

	    //An example of using the TextHandler
	    t = TextHandler.createText(canvas, sourceFile, text, font, fontColor, fontSize, xStart, yStart, startTime, duration);	    

	    rootGroup.getChildren().add(t);
	   
		theStage.setTitle("Text Handler Example Window");
		theStage.setScene(scene);
        theStage.show();	
	}
}
