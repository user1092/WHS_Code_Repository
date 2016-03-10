package whs.yourchoice.client;


import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	
	// when setting the video file, you need to make sure you set the media type, so always have "file:" (or the media type) in front of the directory...
	private static final String VIDEO_FILE = "file:///H:/Desktop/GitPortable/Data/WHS_Code_Repository/yourChoice/bin/com/whs/client/Thank_You_Video.mp4";
	private static final int DEFAULT_START_VOLUME = 50;
	private static final int DEFAULT_VOLUMESLIDER_WIDTH = 100;
	private static final int DEFAULT_HALFWAY_VOLUME = 50; 
	private static final int DEFAULT_VOLUMESLIDER_SCALE_DISPLAYED = 5; 
	private static final int DEFAULT_VOLUMESLIDER_INCREMENTING_SCALE = 10;
	public Canvas canvas;
	public MediaPlayer player;
	public AnchorPane anchorPane;
	public int CurrentCycleCount;

	/**
	 * Method to create the video player window, this contains the controls to operate the video player
	 * @return mediaViewer - This displays the video
	 */
	protected BorderPane videoPlayerWindow() {
		
		MediaView mediaViewer = setUpVideo();
		
		HBox controls = createControlBar();
		
		BorderPane videoPlayerAndControls = new BorderPane();
		videoPlayerAndControls.setBottom(controls);
		videoPlayerAndControls.setCenter(mediaViewer);	
		videoPlayerAndControls.setMaxSize(50, 50);
		
		return  videoPlayerAndControls;
	}

	/**
	 * Method to create the video player and displays it
	 * @return mediaViewer - This displays the video
	 */
	protected MediaView setUpVideo() {
		//media resource
		Media mediaToPlay = new Media(VIDEO_FILE);
			
		//media player
		player = new MediaPlayer(mediaToPlay);
		
		//view media player
		MediaView mediaViewer = new MediaView(player);
		
		//set the size you want the video to be re-scaled to. 
		mediaViewer.setFitHeight(300);
		mediaViewer.setFitWidth(600);
		
		return mediaViewer;
	}

	/**
	 * Method to create the control bar for the video player.
	 * @return controlBar - This is the control bar controlling the video player
	 */
	private HBox createControlBar() {
		
		// image for the play button
		Image playImage = new Image(getClass().getResourceAsStream("play.png"));
		ImageView playView = new ImageView(playImage);
		// image for the pause button
		Image pauseImage = new Image(getClass().getResourceAsStream("pause.png"));
		ImageView pauseView = new ImageView(pauseImage);
		// image for the exit button
		Image exitImage = new Image(getClass().getResourceAsStream("exit.png"));
		ImageView exitView = new ImageView(exitImage);
		
		Image loopImage = new Image(getClass().getResourceAsStream("loop.png"));
		ImageView loopView = new ImageView(loopImage);
		
		HBox controlBar = new HBox();
		
		Button playButton = new Button();
		playButton.setGraphic(playView);
		
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				player.play();
			}
		});
		
		// set the image on the button
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
		
		//volume slider 
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
					player.setCycleCount(MediaPlayer.INDEFINITE);
				}else{
					player.setCycleCount(1);
				}
			}
		});
		
		controlBar.getChildren().addAll(playButton,pauseButton,exitButton,volumeSlider,loopButton);
				
				return controlBar;
			}

	}