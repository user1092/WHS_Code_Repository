package whs.yourchoice.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import presentech.TextHandler;

import stammtisch.Objects.Images;
import stammtisch.handlers.ImageHandler;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import whs.yourchoice.audio.AudioPlayer;
import whs.yourchoice.graphics.PolygonGraphic;
import whs.yourchoice.graphics.ShapeGraphic;
import whs.yourchoice.presentation.AudioEntry;
import whs.yourchoice.presentation.ImageEntry;
import whs.yourchoice.presentation.PolygonEntry;
import whs.yourchoice.presentation.PresentationEntry;
import whs.yourchoice.presentation.ShapeEntry;
import whs.yourchoice.presentation.SlideEntry;
import whs.yourchoice.presentation.TextEntry;
import whs.yourchoice.presentation.TimingEntry;
import whs.yourchoice.presentation.VideoEntry;
import whs.yourchoice.utilities.SimpleTimer;
import whs.yourchoice.video.VideoPlayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
* Class for creation of the presentation window and adding functionality
*
* @author cd828 & cd1092
* @version v0.9 28/04/16
*/
public class PresentationGui extends Application {
	
	// Definitions for window, presentation and control area sizes
	final int WINDOW_MIN_WIDTH = 640;
	final int WINDOW_MIN_HEIGHT = 480;
	final int WINDOW_WIDTH = 640;
	final int WINDOW_HEIGHT = 480;
	final int CONTROL_BAR_WIDTH = WINDOW_WIDTH;
	final int CONTROL_BAR_HEIGHT = 60;
	final int PRESENTATION_WIDTH = WINDOW_WIDTH;
	final int PRESENTATION_HEIGHT = WINDOW_HEIGHT - (CONTROL_BAR_HEIGHT/2) - CONTROL_BAR_HEIGHT;
	// Values for the video player
	//TODO May consider implementing in the xml file
	final float VIDEO_WIDTH = 0.3f;
	final float VIDEO_HEIGHT = 0.3f;
	private static final int DEFAULT_START_VOLUME = 100;
	private static final int DEFAULT_HALFWAY_VOLUME = 50; 
	private static final int DEFAULT_VOLUMESLIDER_SCALE_DISPLAYED = 5; 
	private static final int DEFAULT_VOLUMESLIDER_INCREMENTING_SCALE = 10;
	
	private StackPane presentationLayout;
    private BorderPane subPresentationLayout;
	private BorderPane windowLayout;
	
	// Variables for the scaling of all the objects on the window
	private double scaleWidthRatio = 1;
    private double scaleHeightRatio = 1;
    private double stageInitialWidth = 0;
    private double stageInitialHeight = 0;
	
    private int currentSlideNumber = 0;
    
    private ToolBar controlBar;
    
    private Number masterVolume = DEFAULT_START_VOLUME;
    
    // Objects present on the control bar
    private Slider volumeSlider;
    
    private HBox slideButtonsHBox;
    private Button previousSlideButton;
    private Image previousSlideImage;
    private ImageView previousSlideView;
    private TextField slideNumberTextField;
    private Button nextSlideButton;
    private Image nextSlideImage;
    private ImageView nextSlideView;
        
	private ToggleButton modeButton;
	private ToggleButton muteButton;
	private Image muteImage;
	private ImageView muteView;
	private Image unmutedImage;
	private ImageView unmutedView;
	private ToggleButton playButton;
	private Image playImage;
	private ImageView playView;
	private Image pauseImage;
	private ImageView pauseView;
	private Button stopButton;
	private Image stopImage;
	private ImageView stopView;
	private ToggleButton fullScreenButton;
	private Image exitImage;
	private ImageView exitView;
	
	private PresentationEntry presentation;
	
	// Location of where VLC is installed
	private String vlcLibraryLocation;
	
	// Set VLC video output to a dummy, waveout used as bug with DX
	private final String[] VLC_ARGS = {"--vout", "dummy", "--aout", "waveout"};
	private MediaPlayerFactory mediaPlayerFactory;
	private List<AudioPlayer> audioPlayerList = new ArrayList<AudioPlayer>();
	
	private List<VideoPlayer> videoPlayerList = new ArrayList<VideoPlayer>();
	
	// Object for formatting the text fields to only accept integers
	private StringConverter<Integer> integerFormatter;
	
	// Variables for timing
	private boolean automaticMode = false;	
	private boolean mouseClicked = false;	
	private String presentationState = "Playing";	
	private List<TimingEntry> objectTimingList = new ArrayList<TimingEntry>();
	private ObjectTimingControl objectTimingControl;
	private boolean objectTimersDone = false;
	private int currentAutomaticNodeNumber = 0;
	private int currentManualNodeNumber = 0;
	private int storedCurrentNodeNumber = 0;
	private int numberOfNodes;
	private Thread objectAutomaticThread;
	private Thread objectManualThread;		
	private SimpleTimer slideTimer;
	private int storedCurrentSlideNumber = 0;
	private Thread slideAutomaticThread;
	private Thread slideManualThread;

	/**
	 * Constructor requires a presentation to display
	 * 
	 * @param passedPresentation	-	The presentation to be played
	 */
	public PresentationGui(PresentationEntry passedPresentation) {
		presentation = passedPresentation;
		
		// Find and load VLC libraries depending on the JVM
		if("32".equals(System.getProperty("sun.arch.data.model"))) {
			vlcLibraryLocation = new File("").getAbsolutePath() + "/vlc-2.1.0";
		}
		else {
			vlcLibraryLocation = new File("").getAbsolutePath() + "/vlc-2.1.0-win64";
		}
		System.out.println(vlcLibraryLocation);
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcLibraryLocation);
		System.setProperty("jna.library.path", vlcLibraryLocation);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		// Create a media player factory
		mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);
	}
	
	
	/**
	 * Main method to launch the GUI
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(PresentationGui.class, args);
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage slideStage) throws Exception {
		
		Scene scene = setupLayout(slideStage);
		
		//import and set background image
		String background = getClass().getResource("resources/SlideBackground.jpg").toExternalForm();
		windowLayout.setStyle("-fx-background-image: url('" + background + "'); " +
		           "-fx-background-position: center center; " +
		           "-fx-background-repeat: stretch;");
		
		// Scale the presentation for any window size
		scalePresentation(scene);
		
		// Mouse Handler for manual mode
		subPresentationLayout.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
				if ((!automaticMode) && (presentationState.equals("Playing"))) {
					mouseClicked = true;
				}
			}
		});
        
		// listen to the stage if it closes
        closeListener(slideStage);
		
        // Display slide 0 as default
        loadSlide(0);
     
        setupFullscreen(slideStage, scene);
	}


	/**
	 * Method to listen for an Esc key to exit fullscreen, and set to fullscreen
	 * 
	 * @param slideStage	-	The stage make fullscreen
	 * @param scene			-	The scene to listen to a Esc key
	 */
	private void setupFullscreen(Stage slideStage, Scene scene) {
		/* 
		 * Handler to change state of full screen toggle button
		 * when the user exits fullscreen using esc button
		 */ 
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        public void handle(KeyEvent ke) {
	            if (ke.getCode() == KeyCode.ESCAPE) {
	            	fullScreenButton.setSelected(true);
	            }
	        }
	    });
		
        // Initialise full screen
		slideStage.setFullScreen(true);
	}

	
	/**
	 * Method to setup a scene for the presentation 
	 * 
	 * @param slideStage	-	The stage to be setup
	 * @return scene		-	The populated scene
	 */
	private Scene setupLayout(Stage slideStage) {
		presentationLayout = new StackPane();
		subPresentationLayout = new BorderPane();
		windowLayout = new BorderPane();
		
		Scene scene = new Scene(windowLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		windowLayout.setTop(subPresentationLayout);
		subPresentationLayout.setLeft(presentationLayout);
		
		slideStage.setMinHeight(WINDOW_MIN_HEIGHT);
		slideStage.setMinWidth(WINDOW_MIN_WIDTH);
		
		//main stage set up with appropriate scene and size
		slideStage.setScene(scene);
		slideStage.setWidth(WINDOW_WIDTH);
		slideStage.setHeight(WINDOW_HEIGHT);
		slideStage.setTitle("Presentation Slide");
		slideStage.show();
		
		// call method for creating the control bar
		createControlBar(slideStage);
				
		// place the control bar at the bottom of the window
		windowLayout.setBottom(controlBar);
		
		return scene;
	}

	
	/**
	 * Method to listen for the stage closing to tidy up
	 * 
	 * @param slideStage	-	The stage to listen to
	 */
	private void closeListener(Stage slideStage) {
		// Listen for the stage closing to release all audio players
        slideStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	public void handle(WindowEvent we) {
        		// Set to exit for loop in objectTimingThread and objectManualThread
				exitTimingThreads();
				
        		// Stop any running timers if in automatic mode
				if (automaticMode) {
					stopAllTimers();
				}
        		
        		releaseMediaPlayers();
        		mediaPlayerFactory.release();
        		
        	}
        });
	}
	
	
	/**
	 * Method to exit timing threads by forcing for loop to exit
	 */
	private void exitTimingThreads() {
		System.out.println("Exiting Timing Threads");
		if (currentManualNodeNumber < numberOfNodes) {
			currentManualNodeNumber = numberOfNodes;
		}
		if (currentAutomaticNodeNumber < numberOfNodes) {
			currentAutomaticNodeNumber = numberOfNodes;
		}
	}
	
	/**
	 * Method to stop any running timers
	 */
	private void stopAllTimers() {
		System.out.println("Stopping Timers");
		// Check if running before stopping
		if (null != objectTimingControl) {
			if (objectTimingControl.isRunning()){
				objectTimingControl.stop();
			}
		}
		if (null != slideTimer) {
			if (slideTimer.isRunning()) {
				slideTimer.stopTimer();
			}
		}
		
	}
	
			
	/**
	 * Method to release all media players
	 */
	private void releaseMediaPlayers() {
		// Stop and release all audio players
		for(int audio = 0; audio < audioPlayerList.size(); audio++) {
			if (audioPlayerList.get(audio).wasPlaying()){
				audioPlayerList.get(audio).stopAudio();
			}
			audioPlayerList.get(audio).releasePlayer();
		}
		audioPlayerList.clear();
		
		System.out.println("Exit of audio players successful");
		
		// Stop and release all video players
		for(int video = 0; video < videoPlayerList.size(); video++) {
			if (videoPlayerList.get(video).wasPlaying()) {
				videoPlayerList.get(video).stopVideo();
			}
			videoPlayerList.get(video).releasePlayer();
		}
		videoPlayerList.clear();
		
		System.out.println("Exit of video players successful");
	}

	
	/**
	 * Method to scale the scene to keep the same positions of items
	 * 
	 * @param scene	-	The scene to scale
	 */
	private void scalePresentation(Scene scene) {
		stageInitialWidth = scene.getWidth();
        stageInitialHeight = scene.getHeight();

        /*
         *  Listener for getting the width of the window
         *  and scaling everything according to the change in the window size
         */
        windowLayout.getScene().widthProperty()
        .addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observable,
                       Number oldValue, Number newValue) {

            	 scaleWidthRatio = newValue.doubleValue() / stageInitialWidth;

                 presentationLayout.getTransforms().clear();
                 Scale scale = new Scale(scaleWidthRatio, scaleHeightRatio, 0, 0);
                 presentationLayout.getTransforms().add(scale);
                 controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);

             }
        });

        /* 
         * listener for getting the height of the window
         * and scaling everything according to the change in the window size
         */
        windowLayout.getScene().heightProperty()
        .addListener(new ChangeListener<Number>() {
             @Override
             public void changed(ObservableValue<? extends Number> observable,
                       Number oldValue, Number newValue) {

            	 scaleHeightRatio = (newValue.doubleValue())
                            / (stageInitialHeight);

                  presentationLayout.getTransforms().clear();
                  Scale scale = new Scale(scaleWidthRatio, scaleHeightRatio, 0, 0);
                  presentationLayout.getTransforms().add(scale);
                  controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);

             }
        });
	}
	
	
	/**
	 * Method for creating the control tool bar at the bottom of each slide
	 * 
	 * @param slideStage  -  window contains the control bar on
	 */
	private void createControlBar(Stage slideStage) {
		// Instantiation of the control bar
		controlBar = new ToolBar();
		controlBar.setMaxHeight(CONTROL_BAR_HEIGHT);
		controlBar.setMinHeight(CONTROL_BAR_HEIGHT);
		controlBar.setPrefHeight(CONTROL_BAR_HEIGHT);
		
		createVolumeSlider();
		
		createModeButton();
		
		createMuteButton();
		
		createPlayPauseButton();
		
		createStopButton();
		
		createFullScreenButton(slideStage);
		
		createSlideButtonsHBox();
		
		// Instantiation of the separator on the control bar
		Separator separator = new Separator();
		// Adding all the items on the control bar
		controlBar.getItems().addAll(slideButtonsHBox, separator, playButton, stopButton, modeButton, volumeSlider, 
													muteButton, fullScreenButton);
	}
	
	
	/**
	 * Method for the creation of the HBox that contains the 
	 * slide navigation controls, Previous/Next slide buttons 
	 * and the slide number text field.
	 */
	private void createSlideButtonsHBox() {
		slideButtonsHBox = new HBox();
		slideButtonsHBox.setPadding(new Insets(10, 10, 10, 10));
		slideButtonsHBox.setSpacing(5);
		slideNumberTextField = new TextField();
		slideNumberTextField.setPrefWidth(25);
		slideNumberTextField.setPrefHeight(30);
		// TODO fix so a slide number can be entered
		slideNumberTextField.setEditable(false);
		// Call method for setting the format of text fields
		integerFormatter = new IntRangeStringConverter(0, presentation.getTotalSlideNumber(), 1);
		slideNumberTextField.setTextFormatter(new TextFormatter<>(integerFormatter, currentSlideNumber));
		slideNumberTextField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				releaseMediaPlayers();
				presentationLayout.getChildren().clear();
				currentSlideNumber = Integer.parseInt(slideNumberTextField.getText());
				loadSlide(currentSlideNumber);
				slideNumberTextField.setText("" + currentSlideNumber);
			}
		});
		previousSlideButtonSetup();
		nextSlideButtonSetup();
		slideButtonsHBox.getChildren().addAll(previousSlideButton, slideNumberTextField, nextSlideButton);
		
	}
	
	
	/**
	 * Method for the setup of the next slide button
	 */
	private void nextSlideButtonSetup() {
		// Image for next slide button
		nextSlideImage = new Image(getClass().getResourceAsStream("resources/nextbutton.png"));
		nextSlideView = new ImageView(nextSlideImage);
		// Instantiation of next slide button
		nextSlideButton = new Button();
		nextSlideButton.setGraphic(nextSlideView);
		nextSlideButton.setMaxSize(35, 30);
		nextSlideButton.setPrefSize(35, 30);
		nextSlideButton.setMinSize(35, 30);
		nextSlideButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (currentSlideNumber < (presentation.getTotalSlideNumber() - 1)) {
					
					// Set to exit for loop in objectTimingThread and objectManualThread
					exitTimingThreads();
					
					// Stop any running timers if in automatic mode
					if (automaticMode) {
						stopAllTimers();
					}					
					
					// Clear the current slide
					presentationLayout.getChildren().clear();									
					//currentSlideNumber++;
					
					currentSlideNumber = presentation.getSlideList().get(currentSlideNumber).getSlideNext();
					
					if (!presentationState.equals("Stopped")) {
						storedCurrentNodeNumber = 0;
						releaseMediaPlayers();
						loadSlide(currentSlideNumber);
					}
					
					slideNumberTextField.setText("" + currentSlideNumber);
				}
			}
		});
	}
	
	
	/**
	 * Method for the setup of the previous slide button
	 */
	private void previousSlideButtonSetup() {
		// Image for previous slide button
		previousSlideImage = new Image(getClass().getResourceAsStream("resources/previousbutton.png"));
		previousSlideView = new ImageView(previousSlideImage);
		// Instantiation of previous slide button
		previousSlideButton = new Button();
		previousSlideButton.setGraphic(previousSlideView);
		previousSlideButton.setMaxSize(35, 30);
		previousSlideButton.setPrefSize(35, 30);
		previousSlideButton.setMinSize(35, 30);
		previousSlideButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (currentSlideNumber > 0) {
					
					// Set to exit for loop in objectTimingThread and objectManualThread
					exitTimingThreads();
					
					// Stop any running timers if in automatic mode
					if (automaticMode) {
						stopAllTimers();
					}
					
					presentationLayout.getChildren().clear();
					currentSlideNumber--;
					
					if (!presentationState.equals("Stopped")) {
						storedCurrentNodeNumber = 0;
						releaseMediaPlayers();
						loadSlide(currentSlideNumber);
					}
					
					slideNumberTextField.setText("" + currentSlideNumber);
				}
			}
		});
	}
	
	
	/**
	 * Method for creating the master volume slider with all its attributes
	 */
	private void createVolumeSlider() {
		// Instantiation of volume slider
		volumeSlider = new Slider();
		// Setting the attributes of the slider
		volumeSlider.setMin(0);
		volumeSlider.setMax(100);
		volumeSlider.setValue(DEFAULT_START_VOLUME);
		volumeSlider.setShowTickLabels(true);
		volumeSlider.setShowTickMarks(true);
		volumeSlider.setMajorTickUnit(DEFAULT_HALFWAY_VOLUME);
		volumeSlider.setMinorTickCount(DEFAULT_VOLUMESLIDER_SCALE_DISPLAYED);
		volumeSlider.setBlockIncrement(DEFAULT_VOLUMESLIDER_INCREMENTING_SCALE);
		// Listener to change the volume of the media when the slider is adjusted
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    masterVolume = new_val;
                    // Set volume on all audio players
                    for(int audio = 0; audio < audioPlayerList.size(); audio++) {
            			audioPlayerList.get(audio).setAudioVolume(new_val.intValue());
            		}
                    // Set volume on all video players
                    for(int video = 0; video < videoPlayerList.size(); video++) {
	        			videoPlayerList.get(video).setVolume(new_val.intValue());
	        		}
            }
        });
	}

	
	/**
	 * Method for creating the automatic/manual mode button
	 */
	private void createModeButton() {
		modeButton = new ToggleButton("Automatic");
		modeButton.setMaxSize(80, 30);
		modeButton.setPrefSize(80, 30);
		modeButton.setMinSize(80, 30);
		modeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
            	if (modeButton.isSelected() == true) {
            		
            		modeButton.setText("Manual");
            		
            		// Store the current node to be displayed
            		storedCurrentNodeNumber = currentManualNodeNumber;
            		
            		// Set to exit for loop in objectManualThread
            		exitTimingThreads();
					
					// Start the timingThread if the presentation is playing
					if (presentationState.equals("Playing")) {
						startAutomaticMode();
					}
					
					automaticMode = true;
					
					System.out.println("Automatic Mode Set");
            	}
            	else { 
            		modeButton.setText("Automatic");
            		
            		storedCurrentNodeNumber = currentAutomaticNodeNumber;
            		
            		// Set to exit for loop in objectTimingThread
            		exitTimingThreads();
					
					// Stop any running timers
					stopAllTimers();
					
            		startManualMode();
            		
            		automaticMode = false;
            		            		
            		System.out.println("Manual Mode Set");
            	}
            }
        });
	}

	
	/**
	 * Method for creating the mute button on the control bar
	 */
	private void createMuteButton() {
		// Image for mute button
		muteImage = new Image(getClass().getResourceAsStream("resources/mute.png"));
		muteView = new ImageView(muteImage);
		unmutedImage = new Image(getClass().getResourceAsStream("resources/unmute.png"));
		unmutedView = new ImageView(unmutedImage);
		// Instantiation of mute button
		muteButton = new ToggleButton();
		muteButton.setGraphic(unmutedView);
		muteButton.setMaxSize(50, 30);
		muteButton.setPrefSize(50, 30);
		muteButton.setMinSize(50, 30);
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (muteButton.isSelected() == true) {
					muteButton.setGraphic(muteView);
					// Mute all audio players
					for(int audio = 0; audio < audioPlayerList.size(); audio++) {
	        			audioPlayerList.get(audio).muteAudio(true);
	        		}
					// Mute all video players
					for(int video = 0; video < videoPlayerList.size(); video++) {
	        			videoPlayerList.get(video).muteAudio(true);
	        		}
				}
				else {
					muteButton.setGraphic(unmutedView);
					// Unmute all audio players
					for(int audio = 0; audio < audioPlayerList.size(); audio++) {
	        			audioPlayerList.get(audio).muteAudio(false);
	        		}
					// Unmute all video players
					for(int video = 0; video < videoPlayerList.size(); video++) {
	        			videoPlayerList.get(video).muteAudio(false);
	        		}
				}
			}
		});
	}
	
	
	/**
	 * Method for creating the play button on the control bar
	 */
	private void createPlayPauseButton() {
		// Image for the play button
		playImage = new Image(getClass().getResourceAsStream("resources/play.png"));
		playView = new ImageView(playImage);
		// Image for the pause button
		pauseImage = new Image(getClass().getResourceAsStream("resources/pause.png"));
		pauseView = new ImageView(pauseImage);
		// Instantiation of play button
		playButton = new ToggleButton();
		// Set the image on the button
		playButton.setSelected(true);
		playButton.setGraphic(pauseView);
		playButton.setMaxSize(40, 30);
		playButton.setPrefSize(40, 30);
		playButton.setMinSize(40, 30);
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (playButton.isSelected()) {
					playButton.setGraphic(pauseView);
					
					if (presentationState.equals("Paused")) {
						playPausedMedia();						
						presentationState = "Playing";
						// Check if displayed slide has changed
						if (automaticMode) {
							if (storedCurrentSlideNumber != currentSlideNumber) {
								startAutomaticMode();
							}
							else {
								if ((null != objectTimingControl) && (!objectTimersDone) && slideAutomaticThread.isAlive() && objectAutomaticThread.isAlive()) {
									if (objectTimingControl.wasRunning()) {
										objectTimingControl.resume();
										slideTimer.resumeTimer();
									}
								}
								else {
									startAutomaticMode();
								}
							}
						}
						else {
							if (storedCurrentSlideNumber != currentSlideNumber) {
								startManualMode();
							}
						}
					}
					
					if (presentationState.equals("Stopped")) {
						presentationState = "Playing";
						loadSlide(currentSlideNumber);
					}
					
				}
				else {
					playButton.setGraphic(playView);
					// Set all audio players to pause 
					for(int audio = 0; audio < audioPlayerList.size(); audio++) {
            			audioPlayerList.get(audio).pauseAudio();
            		}
					// Set all video players to pause 
					for(int video = 0; video < videoPlayerList.size(); video++) {
	        			videoPlayerList.get(video).pauseVideo();
	        		}
					
					if (automaticMode) {
						if ((false == objectTimersDone) && (objectTimingControl.isRunning())) {
							objectTimingControl.pause();
							slideTimer.pauseTimer();
						}
					}
					
					presentationState = "Paused";
					storedCurrentSlideNumber = currentSlideNumber;
				}
			}
		});
	}
	
	/**
	 * Method to continue playing any paused media
	 */
	private void playPausedMedia() {
		// Set all audio players to play 
		for(int audio = 0; audio < audioPlayerList.size(); audio++) {
			if (audioPlayerList.get(audio).wasPlaying()) {
				audioPlayerList.get(audio).playAudio();
				// Fixes "feature" with vlc where audio is not observed if not playing
				while(-1 == audioPlayerList.get(audio).getAudioVolume()) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(muteButton.isSelected()) {
					audioPlayerList.get(audio).muteAudio(true);
				}
				else {
					audioPlayerList.get(audio).setAudioVolume(masterVolume.intValue());
				}
			}
		}
		// Set all video players to play 
		for(int video = 0; video < videoPlayerList.size(); video++) {
			if (videoPlayerList.get(video).wasPlaying()) {
				videoPlayerList.get(video).playVideo();
			}
		}
	}
	
	
	/**
	 * Method for creating the stop button on the control bar
	 */
	private void createStopButton() {
		// Image for the stop button
		stopImage = new Image(getClass().getResourceAsStream("resources/stop.png"));
		stopView = new ImageView(stopImage);
		// Instantiation of play button
		stopButton = new Button();
		// Set the image on the button
		stopButton.setGraphic(stopView);
		stopButton.setMaxSize(40, 30);
		stopButton.setPrefSize(40, 30);
		stopButton.setMinSize(40, 30);
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {				
				exitTimingThreads();
				// Stop any running timers if in automatic mode
				if (automaticMode) {
					stopAllTimers();
				}
				
				storedCurrentNodeNumber = 0;
				
				presentationState = "Stopped";
				
				releaseMediaPlayers();
				
				presentationLayout.getChildren().clear();
				
				// Set playButton back to wait to play
				playButton.setSelected(false);
				playButton.setGraphic(playView);
			}
		});
	}
	
	
	/**
	 * Method for creating the full screen toggle button
	 * 
	 * @param slideStage  -  window that will switch from full screen to normal size
	 */
	private void createFullScreenButton(final Stage slideStage) {
		// Image for the full screen button
		exitImage = new Image(getClass().getResource("resources/exit.png").toExternalForm());
		exitView = new ImageView(exitImage);
		// Instantiation of full screen button
		fullScreenButton = new ToggleButton();
		// Set the image on the button
		fullScreenButton.setGraphic(exitView);
		
		fullScreenButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (fullScreenButton.isSelected() == true) {
					slideStage.setFullScreen(false);
				}
				else {
					slideStage.setFullScreen(true);
				}
			}
		});
	}
	
	
	/**
	 * Method for loading all objects on current slide within the current presentation
	 * 
	 * @param slideId - The slide number in the presentation to be displayed
	 */
	private void loadSlide(int slideId) {
		
		objectTimingList.clear();
		storedCurrentNodeNumber = 0;
		
		// An empty canvas to represent the size of the slide area
		Canvas duffCanvas = new Canvas(PRESENTATION_WIDTH,  PRESENTATION_HEIGHT);
		
		// Retrieve the slide information that was requested
		SlideEntry currentSlide = presentation.getSlideList().get(slideId);
		
		displayTexts(currentSlide, duffCanvas);				
		displayImages(currentSlide);		
		displayShapes(currentSlide);		
		displayPolygons(currentSlide);		
		displayVideos(currentSlide, duffCanvas);		
		displayAudios(currentSlide);
		
		numberOfNodes = objectTimingList.size();
		
		// Sort List by TimeInPresentation
		Collections.sort(objectTimingList, new TimeInPresentationComparator());
		
		populateTimeSinceLastNode();
				
		if (presentationState.equals("Playing")) {
			if (automaticMode) {
				startAutomaticMode();
			}
			else {
				startManualMode();
			}
			
		}
	}


	/**
	 * Method to start all tasks for manual mode
	 */
	private void startManualMode() {
		manualSlideMode();
		manualObjectMode();
	}


	/**
	 * Method to start all tasks for automatic mode
	 */
	private void startAutomaticMode() {
//		currentAutomaticNodeNumber = storedCurrentNodeNumber;
		automaticObjectMode();
		automaticSlideMode(currentSlideNumber);
	}


	/**
	 * Method to run task for changing slide in manual mode
	 */
	private void manualSlideMode() {
		Task<Void> manualSlideTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				storedCurrentSlideNumber = currentSlideNumber;
				
				while ((currentManualNodeNumber < numberOfNodes) || 
						((!mouseClicked) && (!automaticMode) && 
								(currentSlideNumber == storedCurrentSlideNumber)))	{
					Thread.sleep(20);
				}
								
				mouseClicked = false;
				
				// If in manual mode and on the same slide click the next slide button
				if ((!automaticMode) && (currentSlideNumber == storedCurrentSlideNumber)) {
					Platform.runLater(new Runnable() {
						@Override public void run() {
							nextSlideButton.fire();
						}
					});
				}
				return null;
			}
		};
		
		// Add task to a thread
		slideManualThread = new Thread(manualSlideTask);
		slideManualThread.setDaemon(true);
		slideManualThread.start();
	}


	/**
	 * Method to run task for displaying objects in manual mode
	 */
	private void manualObjectMode() {
		// Task to run both timers in, to not stall main program
		Task<Void> manualObjectTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				
				// Display all Nodes at correct time
				for(currentManualNodeNumber = storedCurrentNodeNumber; currentManualNodeNumber < numberOfNodes; currentManualNodeNumber++) {
					
					mouseClicked = false;
					// Get whether the object should appear
					boolean visible = objectTimingList.get(currentManualNodeNumber).getVisible();
					// Make a temporary copy of the node
					Node tempNode = objectTimingList.get(currentManualNodeNumber).getNode();
					/* 
					 * Check if the time is greater than zero
					 * if it isn't display or play the object immediately
					 */ 
					if (objectTimingList.get(currentManualNodeNumber).getTimeInPresentation() <= 0) {
						// Check if the object is on a node, if null it must be audio
						if (!(null == tempNode)) {
							if (!(objectTimingList.get(currentManualNodeNumber).getNode().isVisible())) {
								objectTimingList.get(currentManualNodeNumber).getNode().setVisible(true);
							}
						}
						else {
							objectTimingList.get(currentManualNodeNumber).getAudioPlayer().playAudio();
						}
					}
					else {
						/*
						 *  If time should elapse between the last object appearing 
						 *  and the current object, display the current object straight away.
						 */
						while((!mouseClicked) && (currentManualNodeNumber < numberOfNodes) && (!automaticMode)) {
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						mouseClicked = false;
						
						if ((currentManualNodeNumber >= numberOfNodes) || (automaticMode)) {
							break;
						}
												
						// Check if the object is on a node, if null it must be audio
						if (!(null == tempNode)) {
							objectTimingList.get(currentManualNodeNumber).getNode().setVisible(visible);
						}
						else {
							if (visible) {
								objectTimingList.get(currentManualNodeNumber).getAudioPlayer().playAudio();
							}
							else {
								objectTimingList.get(currentManualNodeNumber).getAudioPlayer().stopAudio();
							}
						}
					}
				}
				System.out.println("Exited manual object loop");
				return null;
			}
		};
		objectManualThread = new Thread(manualObjectTask);
		objectManualThread.setDaemon(true);
		objectManualThread.start();
	}


	/**
	 * Method for automatically changing slide within the current presentation
	 * 
	 * @param slideNumber - The slide to be timed
	 */
	private void automaticSlideMode(final int slideNumber) {
		
		// Task to time the slide and change once complete
		Task<Void> automaticSlideTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				boolean slideTimerDone = false;
				
				slideTimer = new SimpleTimer();
				slideTimerDone = slideTimer.startTimer(presentation.getSlideList().get(slideNumber).getSlideDuration());
				
				// If the timer finished click the next slide button
				if (slideTimerDone) {
					Platform.runLater(new Runnable() {
						@Override public void run() {
							nextSlideButton.fire();
						}
					});
				}
				return null;
			}
		};
		
		// Add task to a thread
		slideAutomaticThread = new Thread(automaticSlideTask);
		slideAutomaticThread.setDaemon(true);
		slideAutomaticThread.start();
	}


	/**
	 * Method to run task for displaying objects in automatic mode
	 */
	private void automaticObjectMode() {
		// Task to run both timers in, to not stall main program
		Task<Void> automaticObjectTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				objectTimersDone = false;
				// Display all Nodes at correct time
				for(currentAutomaticNodeNumber = storedCurrentNodeNumber; currentAutomaticNodeNumber < numberOfNodes; currentAutomaticNodeNumber++) {
									
					if("Node" == objectTimingList.get(currentAutomaticNodeNumber).getObjectType()) {
						objectTimingControl = new ObjectTimingControl(
										objectTimingList.get(currentAutomaticNodeNumber).getNode(),
										objectTimingList.get(currentAutomaticNodeNumber).getTimeSinceLastNode(),
										objectTimingList.get(currentAutomaticNodeNumber).getVisible());
					}
					else {
						objectTimingControl = new ObjectTimingControl(
								objectTimingList.get(currentAutomaticNodeNumber).getAudioPlayer(),
								objectTimingList.get(currentAutomaticNodeNumber).getTimeSinceLastNode(),
								objectTimingList.get(currentAutomaticNodeNumber).getVisible());
					}
					
					objectTimingControl.start();
					
					if (numberOfNodes == currentAutomaticNodeNumber) {
						objectTimersDone = true;
						break;
					}
				}
				System.out.println("Exited automatic object loop");
				return null;
			}
		};
		
		
		objectAutomaticThread = new Thread(automaticObjectTask);
		objectAutomaticThread.setDaemon(true);
		objectAutomaticThread.start();
	}


	/**
	 * Method to calculate and populate the timeSinceLastNode field in timing list
	 */
	private void populateTimeSinceLastNode() {
		
		// Populate timeSinceLastNode
		for(int node = 0; node < numberOfNodes; node++) {
			if((node > 0) && (objectTimingList.get(node).getTimeInPresentation() > 0)) {
				objectTimingList.get(node).setTimeSinceLastNode(
								objectTimingList.get(node).getTimeInPresentation() -
								objectTimingList.get(node - 1).getTimeInPresentation());
			} 
			else {
				if (0 >= objectTimingList.get(node).getTimeInPresentation()) {
					objectTimingList.get(node).setTimeSinceLastNode(0);
				}
				else {
					objectTimingList.get(node).setTimeSinceLastNode(
									objectTimingList.get(node).getTimeInPresentation());
				}
			}			
		}
	}


	/**
	 * Method to display all text on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 * @param duffCanvas	-	A reference canvas for getting the size of the presentation area
	 */
	private void displayTexts(SlideEntry currentSlide, Canvas duffCanvas) {
		// Get the total number of images to be displayed
		int numberOfTexts = currentSlide.getTextList().size();
		
		// Set sourceFile to null as not used by Wooly Hat Software 
		String sourceFile = null;
		
		// Display all text
		for(int text = 0; text < numberOfTexts; text++) {
			Pane tempPane = new Pane();
			TextEntry currentText = new TextEntry();
			
			currentText = currentSlide.getTextList().get(text);
			
			tempPane.getChildren().add(TextHandler.createText(duffCanvas, sourceFile,
					currentText.getTextContent(), currentText.getTextFont(),
					currentText.getTextFontColour(), currentText.getTextFontSize(),
					currentText.getTextXStart(), currentText.getTextYStart(),
					currentText.getTextStartTime(), currentText.getTextDuration()));
			
			presentationLayout.getChildren().add(tempPane);
			
			System.out.println("text entry: " + text + "\n");
			System.out.println(currentText.getTextContent() + "\n");
			
			tempPane.setVisible(false);
			
			// Add pane to objectTimingList 
			TimingEntry tempTextTimingEntryAppear = new TimingEntry(tempPane, currentText.getTextStartTime(), currentText.getTextDuration(), true);
			TimingEntry tempTextTimingEntryDisappear = new TimingEntry(tempPane, currentText.getTextStartTime(), currentText.getTextDuration(), false);
			objectTimingList.add(tempTextTimingEntryAppear);
			objectTimingList.add(tempTextTimingEntryDisappear);
		}
	}


	/**
	 * Method to display all images on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 */
	private void displayImages(SlideEntry currentSlide) {
		// Get the total number of images to be displayed
		int numberOfImages = currentSlide.getImageList().size();
		
		// Display all images
		for(int image = 0; image < numberOfImages; image++) {
			Canvas tempCanvas = new Canvas();
			ImageHandler imageHandler = new ImageHandler();
			Images tempImage = new Images();
			
			ImageEntry currentImage = currentSlide.getImageList().get(image);
			
			tempImage = new Images("file:" + presentation.getPath() + "/" +  currentImage.getImageSourceFile(),
													currentImage.getImageStartTime(),
													currentImage.getImageDuration(),
													currentImage.getImageXStart(),
													currentImage.getImageYStart(),
													currentImage.getImageHeight(),
													currentImage.getImageWidth());
			
			tempCanvas = imageHandler.drawCanvas(tempImage, PRESENTATION_WIDTH, PRESENTATION_HEIGHT);
			presentationLayout.getChildren().add(tempCanvas);
			
			System.out.println("image entry: " + image + "\n");
			
			tempCanvas.setVisible(false);
			
			// Add canvas to objectTimingList 
			TimingEntry tempImageTimingEntryAppear = new TimingEntry(tempCanvas, currentImage.getImageStartTime(), currentImage.getImageDuration(), true);
			TimingEntry tempImageTimingEntryDisappear = new TimingEntry(tempCanvas, currentImage.getImageStartTime(), currentImage.getImageDuration(), false);
			objectTimingList.add(tempImageTimingEntryAppear);
			objectTimingList.add(tempImageTimingEntryDisappear);
		}
	}


	/**
	 * Method to display all shapes on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 */
	private void displayShapes(SlideEntry currentSlide) {
		// Get the total number of shapes to be displayed
		int numberOfShapes = currentSlide.getShapeList().size();
		
		// Display all shapes
		for(int shape = 0; shape < numberOfShapes; shape++) {
			ShapeEntry currentShape = currentSlide.getShapeList().get(shape);
			
			ShapeGraphic tempShape = new ShapeGraphic(currentShape.getShapeStartTime(),
														currentShape.getShapeDuration(),
														currentShape.getShapeXStart(),
														currentShape.getShapeYStart(),
														currentShape.getShapeHeight(),
														currentShape.getShapeWidth(),
														currentShape.getShapeType(),
														currentShape.getShapeFillColour(),
														currentShape.getShapeLineColour());
			
			// Scale the shape to the resolution of the presentation area
			tempShape.setRes(PRESENTATION_WIDTH, PRESENTATION_HEIGHT);
			
			Pane tempPane = tempShape.drawShape();
			
			presentationLayout.getChildren().add(tempPane);
			
			System.out.println("shape entry: " + shape + "\n");
			
			tempPane.setVisible(false);
			
			// Add pane to objectTimingList 
			TimingEntry tempShapeTimingEntryAppear = new TimingEntry(tempPane, currentShape.getShapeStartTime(), currentShape.getShapeDuration(), true);
			TimingEntry tempShapeTimingEntryDisappear = new TimingEntry(tempPane, currentShape.getShapeStartTime(), currentShape.getShapeDuration(), false);
			objectTimingList.add(tempShapeTimingEntryAppear);
			objectTimingList.add(tempShapeTimingEntryDisappear);
		}
	}


	/**
	 * Method to display all polygons on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 */
	private void displayPolygons(SlideEntry currentSlide) {
		// Get the total number of polygons to be displayed
		int numberOfPolygons = currentSlide.getPolygonList().size();
		
		// Display all polygons
		for(int polygon = 0; polygon < numberOfPolygons; polygon++) {
			PolygonEntry currentPolygon = currentSlide.getPolygonList().get(polygon);
						
			PolygonGraphic tempPolygon = new PolygonGraphic(currentPolygon.getPolygonStartTime(),
															currentPolygon.getPolygonDuration(),
															presentation.getPath() + "/" +
															currentPolygon.getPolygonSourceFile());
			
			tempPolygon.setShading(currentPolygon.getPolygonShadeX1(), currentPolygon.getPolygonShadeX2(),
									currentPolygon.getPolygonShadeY1(), currentPolygon.getPolygonShadeY2(),
									currentPolygon.getPolygonShadeColour1(), currentPolygon.getPolygonShadeColour2());
			try {
				tempPolygon.parseCSV();
			}
			catch (FileNotFoundException e) {
				System.out.println(e);
			}
			
			tempPolygon.setRes(PRESENTATION_WIDTH, PRESENTATION_HEIGHT);
			
			Pane tempPane = tempPolygon.drawPolygon();
			
			presentationLayout.getChildren().add(tempPane);
			
			System.out.println("polygon entry: " + polygon + "\n");

			tempPane.setVisible(false);
			
			// Add pane to objectTimingList 
			TimingEntry tempPolygonTimingEntryAppear = new TimingEntry(tempPane, currentPolygon.getPolygonStartTime(), currentPolygon.getPolygonDuration(), true);
			TimingEntry tempPolygonTimingEntryDisappear = new TimingEntry(tempPane, currentPolygon.getPolygonStartTime(), currentPolygon.getPolygonDuration(), false);
			objectTimingList.add(tempPolygonTimingEntryAppear);
			objectTimingList.add(tempPolygonTimingEntryDisappear);
		}
	}


	/**
	 * Method to display all videos on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 * @param duffCanvas	-	A reference canvas for getting the size of the presentation area
	 */
	private void displayVideos(SlideEntry currentSlide, Canvas duffCanvas) {
		// Get the total number of videos to be displayed
		int numberOfVideos = currentSlide.getVideoList().size();
				
		// Display all videos
		for(int video = 0; video < numberOfVideos; video++) {
			VideoEntry currentVideo = currentSlide.getVideoList().get(video);
			
			File tempFile = new File(presentation.getPath() + "/" + currentVideo.getVideoSourceFile());
			String videoPath = tempFile.toURI().toASCIIString();
			
			VideoPlayer videoPlayer = new VideoPlayer();
			videoPlayerList.add(videoPlayer);
			
			Pane tempPane = videoPlayerList.get(video).videoPlayerWindow(
					videoPath,
					currentVideo.getVideoYStart(),
					currentVideo.getVideoXStart(),
					VIDEO_WIDTH, VIDEO_HEIGHT, duffCanvas);
			
			// Allow mouse clicks through the pane where the video is not located
			tempPane.setPickOnBounds(false);
			
			presentationLayout.getChildren().add(tempPane);
			
			System.out.println("video entry: " + video + "\n");
			
			tempPane.setVisible(false);
			
			// Add pane to objectTimingList 
			TimingEntry tempVideoTimingEntryAppear = new TimingEntry(tempPane, currentVideo.getVideoStartTime(), currentVideo.getVideoDuration(), true);
			TimingEntry tempVideoTimingEntryDisappear = new TimingEntry(tempPane, currentVideo.getVideoStartTime(), currentVideo.getVideoDuration(), false);
			objectTimingList.add(tempVideoTimingEntryAppear);
			objectTimingList.add(tempVideoTimingEntryDisappear);
		}
	}


	/**
	 * Method to display all audio on the current slide
	 * 
	 * @param currentSlide	-	The slide of which elements are to be displayed
	 */
	private void displayAudios(SlideEntry currentSlide) {
		// Get the total number of audio files to be played
		int numberOfAudios = currentSlide.getAudioList().size();
		
		Pane duffPane = new Pane();
		duffPane.setVisible(true);
		duffPane.setMinHeight(PRESENTATION_HEIGHT);
		duffPane.setMinWidth(PRESENTATION_WIDTH);
		// Allow mouse clicks through the pane
		duffPane.setPickOnBounds(false);
		presentationLayout.getChildren().add(duffPane);
		
		// Load all audio
		for(int audio = 0; audio < numberOfAudios; audio++) {
			// Create a audio player
			AudioPlayer audioPlayer = new AudioPlayer(mediaPlayerFactory);
			audioPlayerList.add(audioPlayer);
			
			AudioEntry currentAudio = currentSlide.getAudioList().get(audio);
			
			audioPlayerList.get(audio).loadLocalAudio(presentation.getPath() + "/" + currentAudio.getAudioSourceFile());
			audioPlayerList.get(audio).loopAudio(currentAudio.getAudioLoop());
			
			System.out.println("audio entry: " + audio + "\n");
			
			// Add audioPlayer to objectTimingList 
			TimingEntry tempAudioTimingEntryAppear = new TimingEntry(audioPlayerList.get(audio), currentAudio.getAudioStartTime(), currentAudio.getAudioDuration(), true);
			TimingEntry tempAudioTimingEntryDisappear = new TimingEntry(audioPlayerList.get(audio), currentAudio.getAudioStartTime(), currentAudio.getAudioDuration(), false);
			objectTimingList.add(tempAudioTimingEntryAppear);
			objectTimingList.add(tempAudioTimingEntryDisappear);
		}
	}
}
