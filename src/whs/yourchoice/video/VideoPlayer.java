package whs.yourchoice.video;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Class for the client's back end handling playing video files 
 * 
 * @author		user1092, guest501
 * @version		v0.2 03/03/2016
 */
public class VideoPlayer{
	
	private static final int DEFAULT_START_VOLUME = 50;
	private static final int DEFAULT_VOLUMESLIDER_WIDTH = 100;
	private static final int DEFAULT_HALFWAY_VOLUME = 50; 
	private static final int DEFAULT_VOLUMESLIDER_SCALE_DISPLAYED = 5; 
	private static final int DEFAULT_VOLUMESLIDER_INCREMENTING_SCALE = 10;
	public MediaPlayer player;
	

	/**
	 * Method to create the video player window, this contains the controls to operate the video player and the video viewer.
	 * @return baseBox - This is returning a pane in which the video displays
	 */
	public Pane videoPlayerWindow(String videoFile, float locationY, float locationX, int videoWidth, int videoHeight) {
		
		MediaView mediaViewer = setUpVideo(videoFile, videoWidth, videoHeight);
		
		HBox controls = createControlBar();
		
		// Adding the media Viewer and the controls together  
		BorderPane videoPlayerAndControls = new BorderPane();
		videoPlayerAndControls.setBottom(controls);
		videoPlayerAndControls.setCenter(mediaViewer);	
		videoPlayerAndControls.setMaxSize(50, 50);			
     	videoPlayerAndControls.setLayoutY(locationY);
 		videoPlayerAndControls.setLayoutX(locationX);
		
		Pane baseBox = new Pane();		
		baseBox.getChildren().add(videoPlayerAndControls);
		baseBox.autosize();
		baseBox.setBackground(null);
	
		return  baseBox;
	}

	/**
	 * Method to create the video player and displays it
	 * @return mediaViewer - This displays the video
	 */
	protected MediaView setUpVideo(String videoFile, int videoWidth, int videoHeight) {
		//media resource
		Media mediaToPlay = new Media(videoFile);
			
		//media player
		player = new MediaPlayer(mediaToPlay);
		
		//view media player
		MediaView mediaViewer = new MediaView(player);
		
		//set the size you want the video to be re-scaled to. 
		mediaViewer.setFitHeight(videoHeight);
		mediaViewer.setFitWidth(videoWidth);
		
		return mediaViewer;
	}

	/**
	 * Method to create the control bar for the video player.
	 * @return controlBar - This is the control bar controlling the video player
	 */
	private HBox createControlBar() {
		
		// Adding the images required for their relevant buttons, in the control bar. 
		Image playImage = new Image(getClass().getResourceAsStream("resources/play.png"));
		ImageView playView = new ImageView(playImage);

		Image pauseImage = new Image(getClass().getResourceAsStream("resources/pause.png"));
		ImageView pauseView = new ImageView(pauseImage);
	
		Image exitImage = new Image(getClass().getResourceAsStream("resources/exit.png"));
		ImageView exitView = new ImageView(exitImage);
		
		Image loopImage = new Image(getClass().getResourceAsStream("resources/loop.png"));
		ImageView loopView = new ImageView(loopImage);
		
		HBox controlBar = new HBox();
		
		// Adding all the buttons with the relevant images and setting all the action listeners for the buttons on the control bar. 
		Button playButton = new Button();
		playButton.setGraphic(playView);
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				player.play();
			}
		});
		playButton.setGraphic(playView);
		
		// pause button on tool bar
		Button pauseButton = new Button();
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				player.pause();
			}
		});
		pauseButton.setGraphic(pauseView);
				
		// exit button on tool bar
		Button exitButton = new Button();
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				player.stop();
			}
		});
		exitButton.setGraphic(exitView);
		
		// Volume slider 
		final Slider volumeSlider = new Slider();
		volumeSlider.setValue(DEFAULT_START_VOLUME);
		volumeSlider.setPrefWidth(DEFAULT_VOLUMESLIDER_WIDTH);
		volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
		volumeSlider.setShowTickLabels(true);
		volumeSlider.setShowTickMarks(true);
		volumeSlider.setMajorTickUnit(DEFAULT_HALFWAY_VOLUME);
		volumeSlider.setMinorTickCount(DEFAULT_VOLUMESLIDER_SCALE_DISPLAYED);
		volumeSlider.setBlockIncrement(DEFAULT_VOLUMESLIDER_INCREMENTING_SCALE);
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
		    public void invalidated(Observable ov) {
		       if (volumeSlider.isValueChanging()) {
		           player.setVolume(volumeSlider.getValue() / 100.0);
		           System.out.println("Volume " + player.getVolume());
		       }		     
		    }
		});

		// Loop Button 
		Button loopButton = new Button();
		loopButton.setGraphic(loopView);
		
		loopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {				
				if(player.getCycleCount() == 1){
					player.setCycleCount(1000);
				}else{
					player.setCycleCount(1);
				}
			}
		});
		
		controlBar.getChildren().addAll(playButton,pauseButton,exitButton,volumeSlider,loopButton);
				
		return controlBar;
	}
}