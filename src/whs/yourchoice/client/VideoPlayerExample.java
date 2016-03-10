package whs.yourchoice.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class creating a example of using the video player.
 * 
 * @author		user1092, guest501
 * @version		v0.2 10/03/2016
 */
public class VideoPlayerExample extends Application { 

	private VideoPlayer videoPlayer;
	
	public static void main(final String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		videoPlayer = new VideoPlayer();
			
		BorderPane videoPlayerAndControls = videoPlayer.videoPlayerWindow();
	  
		primaryStage.setTitle("MediaPlayer");	
			
		videoPlayerAndControls.setLayoutY(400);
		videoPlayerAndControls.setLayoutX(300);
		
		Pane baseBox = new Pane();		
		baseBox.getChildren().add(videoPlayerAndControls);
		baseBox.autosize();
		baseBox.setBackground(null);
	
		Scene scene = new Scene(baseBox, 1000 , 1000, Color.RED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
