package whs.yourchoice.client;

import javafx.application.Application;
import javafx.scene.Scene;
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
	private static final String VIDEO_FILE = "file:///H:/Desktop/GitPortable/Data/WHS_Code_Repository/yourchoice/bin/whs/yourchoice/audio/Thank_You_Video.mp4";
	private VideoPlayer videoPlayer;
	
	public static void main(final String[] args){
		launch(VideoPlayerExample.class, args);
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		videoPlayer = new VideoPlayer();
			
		Pane videoPlayerAndControls = videoPlayer.videoPlayerWindow(VIDEO_FILE, 10, 300, 400, 600);
	  
		primaryStage.setTitle("MediaPlayer");	

		Scene scene = new Scene(videoPlayerAndControls, 1000 , 1000, Color.RED);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
