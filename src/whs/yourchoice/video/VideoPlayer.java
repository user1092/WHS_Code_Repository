/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.video;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
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
 * Ensure that releasePlayer is called upon exit of a window.
 * 
 * @author		ch1092, sqk501, cd828
 * @version		v0.5 28/04/2016
 */
public class VideoPlayer{
	
	private double guiHeight = 600;
	private double guiWidth = 600;
	
	private static final int DEFAULT_START_VOLUME = 100;
	private static final int DEFAULT_VOLUMESLIDER_WIDTH = 100;
	private static final int DEFAULT_HALFWAY_VOLUME = 50; 
	private static final int DEFAULT_VOLUMESLIDER_SCALE_DISPLAYED = 5; 
	private static final int DEFAULT_VOLUMESLIDER_INCREMENTING_SCALE = 10;
	public MediaPlayer player;
	private boolean wasPlaying = false;
	
	// objects which are present on the control bar
	private Slider volumeSlider;
	private Image playImage;
	private ImageView playView;
	private Image pauseImage;
	private ImageView pauseView;
	private ToggleButton playButton;
	private Image stopImage;
	private ImageView stopView;
	private Button stopButton;
	private Image loopImage;
	private ImageView loopView;
	private Button loopButton;
	private Image muteImage;
	private ImageView muteView;
	private Image unmutedImage;
	private ImageView unmutedView;
	private ToggleButton muteButton;
	private double maxVolume = DEFAULT_START_VOLUME;
		
	/**
	 * Method to create the video player window, this contains the controls to operate the video player and the video viewer.
	 * 
	 * @param videoFile		-	The path of the file to be played
	 * @param locationY		-	The location along the Y-axis for the video to appear, scaled from 0-1
	 * @param locationX		-	The location along the X-axis for the video to appear, scaled from 0-1
	 * @param videoWidth	-	The width of the video to appear, scaled from 0-1
	 * @param videoHeight	-	The height of the video to appear, scaled from 0-1
	 * @param duffCanvas	-	The size of the area which the video will be placed
	 * @return				-	Returns a pane with the video and controls embedded
	 */
	public Pane videoPlayerWindow(String videoFile, float locationY, float locationX, float videoWidth, float videoHeight, Canvas duffCanvas) {
		
		guiHeight = duffCanvas.getHeight();
		guiWidth = duffCanvas.getWidth();
		
		MediaView mediaViewer = setUpVideo(videoFile, getPixel("X", videoWidth), getPixel("Y", videoHeight));
		
		HBox controls = createControlBar();
		
		// Adding the media Viewer and the controls together  
		BorderPane videoPlayerAndControls = new BorderPane();
		videoPlayerAndControls.setBottom(controls);
		videoPlayerAndControls.setCenter(mediaViewer);	
		videoPlayerAndControls.setMaxSize(50, 50);			
     	videoPlayerAndControls.setLayoutY(getPixel("Y", locationY));
 		videoPlayerAndControls.setLayoutX(getPixel("X", locationX));
		
		Pane baseBox = new Pane();		
		baseBox.getChildren().add(videoPlayerAndControls);
		baseBox.autosize();
		baseBox.setBackground(null);
	
		return baseBox;
	}

	
	/**
	 * Method to create the video player and displays it
	 * 
	 * @param videoFile		-	The path of the file to be played
	 * @param videoWidth	-	The width of the video to appear, scaled from 0-1
	 * @param videoHeight	-	The height of the video to appear, scaled from 0-1
	 * @return				-	Returns a mediaView with the video
	 */
	private MediaView setUpVideo(String videoFile, float videoWidth, float videoHeight) {
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
	 * 
	 * @return controlBar - This is the control bar controlling the video player
	 */
	private HBox createControlBar() {
		HBox controlBar = new HBox();
		controlBar.setSpacing(3);
		createPlayButton();
		createStopButton();
		createVolumeSlider();
		createLoopButton();
		createMuteButton();
		
		controlBar.getChildren().addAll(playButton, stopButton, volumeSlider, loopButton, muteButton);
				
		return controlBar;
	}
	
	
	/**
	 * Method for the creation of the stop button on the control bar
	 */
	private void createStopButton() {
		stopImage = new Image(getClass().getResourceAsStream("resources/stop.png"));
		stopView = new ImageView(stopImage);
		// stop button on tool bar
		stopButton = new Button();
		stopButton.setGraphic(stopView);
		stopButton.setMaxSize(20, 20);
		stopButton.setPrefSize(20, 20);
		stopButton.setMinSize(20, 20);
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				player.stop();
				wasPlaying = false;
				playButton.setSelected(false);
				playButton.setGraphic(playView);
			}
		});
	}
	
	
	/**
	 * Method for the creation of the loop button on the control bar
	 */
	private void createLoopButton() {
		// Loop Button 
		loopImage = new Image(getClass().getResourceAsStream("resources/loop.png"));
		loopView = new ImageView(loopImage);
		loopButton = new Button();
		loopButton.setGraphic(loopView);
		loopButton.setMaxSize(20, 20);
		loopButton.setPrefSize(20, 20);
		loopButton.setMinSize(20, 20);
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
	}
	
	
	/**
	 * Method for the creation of the volume slider on the control bar
	 */
	private void createVolumeSlider() {
		// Volume slider 
		volumeSlider = new Slider();
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
		    	   maxVolume = volumeSlider.getValue();
		           player.setVolume(volumeSlider.getValue() / 100.0);
		       }		     
		    }
		});
	}
	
	
	/**
	 * Method for the creation of the mute button on the control bar
	 */
	private void createMuteButton() {
		// image for mute button
		muteImage = new Image(getClass().getResourceAsStream("resources/mute.png"));
		muteView = new ImageView(muteImage);
		unmutedImage = new Image(getClass().getResourceAsStream("resources/unmute.png"));
		unmutedView = new ImageView(unmutedImage);
		// instantiation of mute button
		muteButton = new ToggleButton();
		muteButton.setGraphic(unmutedView);
		muteButton.setMaxSize(20, 20);
		muteButton.setPrefSize(20, 20);
		muteButton.setMinSize(20, 20);
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (muteButton.isSelected() == true) {
					muteButton.setGraphic(muteView);
					player.setMute(true);
				}
				else {
					muteButton.setGraphic(unmutedView);
					player.setMute(false);
				}
			}
		});
	}
	
	
	/**
	 * Method for creating the play button on the control bar
	 */
	private void createPlayButton() {
		// image for the play button
		playImage = new Image(getClass().getResourceAsStream("resources/play.png"));
		playView = new ImageView(playImage);
		// image for the pause button
		pauseImage = new Image(getClass().getResourceAsStream("resources/pause.png"));
		pauseView = new ImageView(pauseImage);
		// instantiation of play button
		playButton = new ToggleButton();
		// set the image on the button
		playButton.setGraphic(playView);
		playButton.setMaxSize(20, 20);
		playButton.setPrefSize(20, 20);
		playButton.setMinSize(20, 20);
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (playButton.isSelected() == true) {
					playButton.setGraphic(pauseView);
					player.play();
					wasPlaying = true;
				}
				else {
					playButton.setGraphic(playView);
					player.pause();
				}
			}
		});
	}
	
	
	/**
	 * Converts a float representing a percentage of each axis into a
	 * float pixel amount.
	 * 
	 * @param axis	A string representation of an axis
	 * @param percentage	A float representation of percentage.
	 * @return pixel	An float pixel position.
	 */
	private float getPixel(String axis, float percentage) {
		float pixel;
		
		if (axis.equals("Y")) {
			pixel = (float) (guiHeight * percentage);
		}
		else if (axis.equals("X")) {
			pixel = (float) (guiWidth * percentage);
		}
		else {
			pixel = 0;
			throw new IllegalArgumentException("Invalid axis type, expected on of: 'X' or 'Y'");
		}
		return pixel;
	}


	/**
	 * Method to mute the audio of the video
	 * 
	 * @param mute	-	true to mute, false to unmute
	 */
	public void muteAudio(boolean mute) {
		
		if (mute) {
			muteButton.setGraphic(muteView);
			player.setMute(true);
			muteButton.setSelected(true);
		}
		else {
			muteButton.setGraphic(unmutedView);
			player.setMute(false);
			muteButton.setSelected(false);
		}
		
	}


	/**
	 * Method to set the volume for the video
	 * 
	 * @param volume	-	the desired volume of the video, must be from 0 - 100
	 */
	public void setVolume(int volume) {
		if(volume <= maxVolume) {
			player.setVolume(volume / 100.0);
		}
		
	}


	/**
	 * Method to play the video that is loaded 
	 */
	public void playVideo() {
		playButton.setGraphic(pauseView);
		player.play();
		wasPlaying = true;
		playButton.setSelected(true);
	}


	/**
	 * Method to pause the loaded video
	 */
	public void pauseVideo() {
		playButton.setGraphic(playView);
		player.pause();
		playButton.setSelected(false);
	}


	/**
	 * Method to stop the loaded video
	 */
	public void stopVideo() {
		playButton.setGraphic(playView);
		player.stop();
		wasPlaying = false;
		playButton.setSelected(false);
	}

	/**
	 * Method to see if media play was playing
	 * 
	 * @return
	 */
	public boolean wasPlaying() {
		return wasPlaying;
	}

	/**
	 * Method to release the video player.
	 * 
	 * This must be called upon closing of the window
	 */
	public void releasePlayer() {
		player.dispose();
	}
	
	
}