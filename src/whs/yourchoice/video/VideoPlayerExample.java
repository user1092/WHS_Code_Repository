package whs.yourchoice.video;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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
	
	// when setting the video file, you need to make sure you set the media type, so always have "file:" (or the media type) in front of the directory...
	private static final String VIDEO_FILE = "file:///M:/Thank_You_Video.mp4";
	private VideoPlayer videoPlayer;
	
	final int PRESENTATION_WIDTH = 640;
	final int PRESENTATION_HEIGHT = 390;
	
	
	public static void main(final String[] args){
		launch(VideoPlayerExample.class, args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Canvas canvas = new Canvas(PRESENTATION_WIDTH,  PRESENTATION_HEIGHT);
		
		videoPlayer = new VideoPlayer();
			
		Pane videoPlayerAndControls = videoPlayer.videoPlayerWindow(VIDEO_FILE, 0.1f, 0.3f, 0.4f, 0.6f, canvas);
	  
		primaryStage.setTitle("MediaPlayer");	

		Scene scene = new Scene(videoPlayerAndControls, 1000 , 1000, Color.RED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
