/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */
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
import javafx.scene.control.Tooltip;
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
* @author cd828 & ch1092
* @version v0.13 29/05/16
*/
public class PresentationGui extends Application {
	
	// Definitions for window, presentation and control area sizes
	private final int WINDOW_MIN_WIDTH = 640;
	private final int WINDOW_MIN_HEIGHT = 480;
	private final int WINDOW_WIDTH = 640;
	private final int WINDOW_HEIGHT = 480;
	private final int CONTROL_BAR_WIDTH = WINDOW_WIDTH;
	private final int CONTROL_BAR_HEIGHT = 60;
	private final int PRESENTATION_WIDTH = WINDOW_WIDTH;
	private final int PRESENTATION_HEIGHT = WINDOW_HEIGHT - (CONTROL_BAR_HEIGHT/2) - CONTROL_BAR_HEIGHT;
	// Definitions for button sizes
	private final int BUTTON_HEIGHT = 30;
	private final int BUTTON_WIDTH = 35;
	// Values for the video player
	//TODO May consider implementing in the xml file
	private final float VIDEO_WIDTH = 0.3f;
	private final float VIDEO_HEIGHT = 0.3f;
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
    
    private boolean masterMute = false;
    
    // Objects present on the control bar
    private Button previousSlideButton;
    private TextField slideNumberTextField;
    private Button nextSlideButton;
        
	private ToggleButton playButton;
	private Image playImage;
	private ImageView playView;
	private ToggleButton fullScreenButton;
	private Image fullscreenImage;
	private ImageView fullscreenView;
	
	private PresentationEntry presentation = new PresentationEntry();
	private Client client;
	
	// Location of where VLC is installed
	private String vlcLibraryLocation;

	
	// Set VLC video output to a dummy, waveout used as bug with DX
	private final String[] VLC_ARGS = {"--vout", "dummy", "--aout", "waveout"};
	private MediaPlayerFactory mediaPlayerFactory;
	private List<AudioPlayer> audioPlayerList = new ArrayList<AudioPlayer>();
	
	private List<VideoPlayer> videoPlayerList = new ArrayList<VideoPlayer>();
	private VideoPlayer tempVideoPlayer;
	
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
	
	private boolean interactableClicked = false;
	private boolean ignoredClick = false;

	/**
	 * Constructor requires a presentation to display
	 * 
	 * @param passedPresentation	-	The presentation to be played
	 */
	public PresentationGui(PresentationEntry passedPresentation, Client client) {
		presentation = passedPresentation;
		this.client = client;
		
		// Find and load VLC libraries depending on the JVM
		if("32".equals(System.getProperty("sun.arch.data.model"))) {
			vlcLibraryLocation = new File("").getAbsolutePath() + "/vlc-2.1.0";
		}
		else {
			vlcLibraryLocation = new File("").getAbsolutePath() + "/vlc-2.1.0-win64";
			//vlcLibraryLocation = "/Applications/VLC.app/Contents/MacOS/lib";
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
		
		// Scale the presentation for any window size
		scalePresentation(scene);
		
		// Mouse Handler for manual mode
		subPresentationLayout.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
				if ((!automaticMode) && (presentationState.equals("Playing")) && !(interactableClicked)) {
					mouseClicked = true;
				}
				else {
					ignoredClick = true;
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
	            	fullScreenButton.setGraphic(fullscreenView);
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
		
		presentationLayout.setMinHeight(PRESENTATION_HEIGHT);
		presentationLayout.setMinWidth(PRESENTATION_WIDTH);
		presentationLayout.setMaxHeight(PRESENTATION_HEIGHT);
		presentationLayout.setMaxWidth(PRESENTATION_WIDTH);
		subPresentationLayout.setMinHeight(PRESENTATION_HEIGHT);
		subPresentationLayout.setMinWidth(PRESENTATION_WIDTH);
		subPresentationLayout.setMaxHeight(PRESENTATION_HEIGHT);
		subPresentationLayout.setMaxWidth(PRESENTATION_WIDTH);
		
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
		currentManualNodeNumber = numberOfNodes;
		currentAutomaticNodeNumber = numberOfNodes;
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
		controlBar.setPrefWidth(CONTROL_BAR_WIDTH);
		
		Slider volumeSlider = createVolumeSlider();
		
		ToggleButton modeButton = createModeButton();
		
		ToggleButton muteButton = createMuteButton();
		
		createPlayPauseButton();
		
		Button stopButton = createStopButton();
		
		createFullScreenButton(slideStage);
		
		HBox slideButtonsHBox = createSlideButtonsHBox();
		
		Button aboutPresentationButton = createAboutPresentationButton();
		
		Button viewFeedbackButton = createViewFeedbackButton();
		
		// Instantiation of the separator on the control bar
		Separator separator = new Separator();
		// Adding all the items on the control bar
		controlBar.getItems().addAll(slideButtonsHBox, separator, playButton, stopButton, modeButton, volumeSlider, 
													muteButton, fullScreenButton, aboutPresentationButton, viewFeedbackButton);
	}
	
	
	/**
	 * Method for the creation of the HBox that contains the 
	 * slide navigation controls, Previous/Next slide buttons 
	 * and the slide number text field.
	 * 
	 * @return HBox  -  HBox that contains the slide buttons
	 */
	private HBox createSlideButtonsHBox() {
		HBox slideButtonsHBox = new HBox();
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
		return slideButtonsHBox;
	}
	
	
	/**
	 * Method for the setup of the next slide button
	 */
	private void nextSlideButtonSetup() {
		// Image for next slide button
		Image nextSlideImage = new Image(getClass().getResourceAsStream("resources/nextSlide.png"));
		ImageView nextSlideView = new ImageView(nextSlideImage);
		// Instantiation of next slide button
		nextSlideButton = new Button();
		nextSlideButton.setTooltip(new Tooltip("Next Slide"));
		nextSlideButton.setGraphic(nextSlideView);
		nextSlideButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		nextSlideButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		nextSlideButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		nextSlideButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (currentSlideNumber < (presentation.getTotalSlideNumber() - 1)) {
					changeSlide(presentation.getSlideList().get(currentSlideNumber).getSlideNext());
				}
			}
		});
	}
	
	
	/**
	 * Method for the setup of the previous slide button
	 */
	private void previousSlideButtonSetup() {
		// Image for previous slide button
		Image previousSlideImage = new Image(getClass().getResourceAsStream("resources/previousSlide.png"));
		ImageView previousSlideView = new ImageView(previousSlideImage);
		// Instantiation of previous slide button
		previousSlideButton = new Button();
		previousSlideButton.setTooltip(new Tooltip("Previous Slide"));
		previousSlideButton.setGraphic(previousSlideView);
		previousSlideButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		previousSlideButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		previousSlideButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		previousSlideButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (currentSlideNumber > 0) {
					int slideNumber = currentSlideNumber - 1;
					changeSlide(slideNumber);
				}
			}
		});
	}
	
	
	/**
	 * Method for creating the master volume slider with all its attributes
	 * 
	 * @return Slider  -  Slider that controls the volume
	 */
	private Slider createVolumeSlider() {
		// Instantiation of volume slider
		Slider volumeSlider = new Slider();
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
		return volumeSlider;
	}

	
	/**
	 * Method for creating the automatic/manual mode button
	 * 
	 * @return ToggleButton  -  ToggleButton that changes the presentation mode
	 */
	private ToggleButton createModeButton() {
		final ToggleButton modeButton = new ToggleButton("Automatic");
		modeButton.setMaxSize(80, BUTTON_HEIGHT);
		modeButton.setPrefSize(80, BUTTON_HEIGHT);
		modeButton.setMinSize(80, BUTTON_HEIGHT);
		modeButton.setTooltip(new Tooltip("Toggle Automatic Mode"));
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
		return modeButton;
	}

	
	/**
	 * Method for creating the mute button on the control bar
	 * 
	 * @return ToggleButton  -  Button that mutes and unmutes the media
	 */
	private ToggleButton createMuteButton() {
		// Image for mute button
		Image muteImage = new Image(getClass().getResourceAsStream("resources/mute.png"));
		final ImageView muteView = new ImageView(muteImage);
		Image unmutedImage = new Image(getClass().getResourceAsStream("resources/unmute.png"));
		final ImageView unmutedView = new ImageView(unmutedImage);
		// Instantiation of mute button
		final ToggleButton muteButton = new ToggleButton();
		muteButton.setTooltip(new Tooltip("Mute Media"));
		muteButton.setGraphic(unmutedView);
		muteButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		muteButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		muteButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (muteButton.isSelected() == true) {
					muteButton.setTooltip(new Tooltip("Unmute Media"));
					muteButton.setGraphic(muteView);
					masterMute = true;
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
					muteButton.setTooltip(new Tooltip("Mute Media"));
					muteButton.setGraphic(unmutedView);
					masterMute = false;
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
		return muteButton;
	}
	
	
	/**
	 * Method for creating the play button on the control bar
	 */
	private void createPlayPauseButton() {
		// Image for the play button
		playImage = new Image(getClass().getResourceAsStream("resources/play.png"));
		playView = new ImageView(playImage);
		// Image for the pause button
		Image pauseImage = new Image(getClass().getResourceAsStream("resources/pause.png"));
		final ImageView pauseView = new ImageView(pauseImage);
		// Instantiation of play button
		playButton = new ToggleButton();
		playButton.setTooltip(new Tooltip("Play Presentation"));
		// Set the image on the button
		playButton.setSelected(true);
		playButton.setGraphic(pauseView);
		playButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		playButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		playButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (playButton.isSelected()) {
					playButton.setTooltip(new Tooltip("Pause Presentation"));
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
					playButton.setTooltip(new Tooltip("Play Presentation"));
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
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				audioPlayerList.get(audio).setAudioVolume(masterVolume.intValue());
				audioPlayerList.get(audio).muteAudio(masterMute);
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
	 * 
	 * @return Button  -  Stop button
	 */
	private Button createStopButton() {
		// Image for the stop button
		Image stopImage = new Image(getClass().getResourceAsStream("resources/stop.png"));
		ImageView stopView = new ImageView(stopImage);
		// Instantiation of play button
		Button stopButton = new Button();
		stopButton.setTooltip(new Tooltip("Stop Presentation"));
		// Set the image on the button
		stopButton.setGraphic(stopView);
		stopButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		stopButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		stopButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
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

				playButton.setTooltip(new Tooltip("Play Presentation"));
				playButton.setGraphic(playView);
			}
		});
		return stopButton;
	}
	
	
	/**
	 * Method for creating the full screen toggle button
	 * 
	 * @param slideStage  -  window that will switch from full screen to normal size
	 */
	private void createFullScreenButton(final Stage slideStage) {
		// Image for the full screen button
		Image exitFullscreenImage = new Image(getClass().getResource("resources/exitFullscreen.png").toExternalForm());
		final ImageView exitFullscreenView = new ImageView(exitFullscreenImage);
		fullscreenImage = new Image(getClass().getResource("resources/fullscreen.png").toExternalForm());
		fullscreenView = new ImageView(fullscreenImage);
		// Instantiation of full screen button
		fullScreenButton = new ToggleButton();
		fullScreenButton.setTooltip(new Tooltip("Exit Fullscreen"));
		// Set the image on the button
		fullScreenButton.setGraphic(exitFullscreenView);
		fullScreenButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		fullScreenButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		fullScreenButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		
		fullScreenButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (fullScreenButton.isSelected() == true) {
					fullScreenButton.setTooltip(new Tooltip("Fullscreen"));
					fullScreenButton.setGraphic(fullscreenView);
					slideStage.setFullScreen(false);
				}
				else {
					fullScreenButton.setTooltip(new Tooltip("Exit Fullscreen"));
					fullScreenButton.setGraphic(exitFullscreenView);
					slideStage.setFullScreen(true);
				}
			}
		});
	}
	
	
	/**
	 * Method for the setup of the about presentation button
	 */
	private Button createAboutPresentationButton() {
		// Image for about presentation button
		Image aboutPresentationImage = new Image(getClass().getResourceAsStream("resources/about.png"));
		ImageView aboutPresentationView = new ImageView(aboutPresentationImage);
		// Instantiation of next slide button
		Button aboutPresentationButton = new Button();
		aboutPresentationButton.setTooltip(new Tooltip("About Presentation"));
		aboutPresentationButton.setGraphic(aboutPresentationView);
		aboutPresentationButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		aboutPresentationButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		aboutPresentationButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		aboutPresentationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Platform.runLater(new Runnable() {
	 	    		 public void run() {             
	 	    			try {				 	        	  
	 	     				new MetaDataGui(presentation).start(new Stage());
	 	     			}
	 	    			catch (Exception e) {
	 	     				// TODO Auto-generated catch block
	 	     				e.printStackTrace();
	 	     			}
	 	    		 }
	 	    	 });
			}
		});
		return aboutPresentationButton;
	}

	
	/**
	 * Method for the setup of the about presentation button
	 */
	private Button createViewFeedbackButton() {
		// Image for view feedback button
		Image viewFeedbackImage = new Image(getClass().getResourceAsStream("resources/feedback.png"));
		ImageView viewFeedbackView = new ImageView(viewFeedbackImage);
		// Instantiation of next slide button
		Button viewFeedbackButton = new Button();
		viewFeedbackButton.setTooltip(new Tooltip("View Module Feedback"));
		viewFeedbackButton.setGraphic(viewFeedbackView);
		viewFeedbackButton.setMaxSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		viewFeedbackButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		viewFeedbackButton.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		viewFeedbackButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Platform.runLater(new Runnable() {
	 	    		 public void run() {             
	 	    			try {				 	        	  
//	 	     				new MetaDataGui(presentation).start(new Stage());
	 	    				// TODO CHANGE THE FILENAME
	 	    				new ViewFeedbackGui(presentation.getPresentationTitle(), 
	 	    								presentation.getFeedbackFilename(), 
	 	    								client).start(new Stage());
	 	     			}
	 	    			catch (Exception e) {
	 	     				// TODO Auto-generated catch block
	 	     				e.printStackTrace();
	 	     			}
	 	    		 }
	 	    	 });
			}
		});
		return viewFeedbackButton;
	}

	
	/**
	 * Method to change Slide according to the passed number
	 * 
	 * @param targetSlide	-	The slide that the presentation should change to
	 */
	private void changeSlide(final int targetSlide) {
		// Set to exit for loop in objectTimingThread and objectManualThread
		exitTimingThreads();
		
		// Stop any running timers if in automatic mode
		if (automaticMode) {
			stopAllTimers();
		}					
		
		// Clear the current slide
		presentationLayout.getChildren().clear();
		
		currentSlideNumber = targetSlide;
		
		// Ensure all Threads are dead before continuing
		if (automaticMode) {
			while (objectAutomaticThread.isAlive() || slideAutomaticThread.isAlive()) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			while (objectManualThread.isAlive() || slideManualThread.isAlive()) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		storedCurrentSlideNumber = currentSlideNumber;
		
		if (!presentationState.equals("Stopped")) {
			storedCurrentNodeNumber = 0;
			releaseMediaPlayers();
			loadSlide(currentSlideNumber);
		}
		
		slideNumberTextField.setText("" + currentSlideNumber);
		
		// Wait until the mouse click is registered by the mouse listener
		Task<Void> stopMouseTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				while (!(ignoredClick)) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ignoredClick = false;
				interactableClicked = false;
				return null;
			}
		};
		
		// Add task to a thread
		Thread stopMouseThread = new Thread(stopMouseTask);
		stopMouseThread.setDaemon(true);
		if (interactableClicked) {
			stopMouseThread.start();
		}
		
		
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
		
		windowLayout.setStyle("-fx-background-color: #" + currentSlide.getSlideBackgroundColour() + ";");
				
		System.out.println("Background colour: " + currentSlide.getSlideBackgroundColour());
		
		displayImages(currentSlide);
		displayTexts(currentSlide, duffCanvas);
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
		automaticObjectMode();
		automaticSlideMode(currentSlideNumber);
	}


	/**
	 * Method to run task for changing slide in manual mode
	 */
	private void manualSlideMode() {
		Task<Void> manualSlideTask = new Task<Void>() {
			@Override protected Void call() throws Exception {
				
				mouseClicked = false;
				
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
				
				System.out.println("Exiting manual slide control");
				
				mouseClicked = false;
				
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
							if (!(null == objectTimingList.get(currentManualNodeNumber).getVideoPlayer())) {
								tempVideoPlayer = objectTimingList.get(currentManualNodeNumber).getVideoPlayer();
								Platform.runLater(new Runnable() {
									@Override public void run() {
										tempVideoPlayer.playVideo();
									}
								});
							}
						}
						else {
							objectTimingList.get(currentManualNodeNumber).getAudioPlayer().playAudio();
							// Fixes "feature" with vlc where audio is not observed if not playing
							while(-1 == objectTimingList.get(currentManualNodeNumber).getAudioPlayer().getAudioVolume()) {
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							objectTimingList.get(currentManualNodeNumber).getAudioPlayer().setAudioVolume(masterVolume.intValue());
							objectTimingList.get(currentManualNodeNumber).getAudioPlayer().muteAudio(masterMute);
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
							if (!(null == objectTimingList.get(currentManualNodeNumber).getVideoPlayer())) {
								tempVideoPlayer = objectTimingList.get(currentManualNodeNumber).getVideoPlayer();
								if (visible) {
									Platform.runLater(new Runnable() {
										@Override public void run() {
											tempVideoPlayer.playVideo();
										}
									});
								}
								else {
									Platform.runLater(new Runnable() {
										@Override public void run() {
											tempVideoPlayer.stopVideo();
										}
									});
								}
							}
						}
						else {
							if (visible) {
								objectTimingList.get(currentManualNodeNumber).getAudioPlayer().playAudio();
								// Fixes "feature" with vlc where audio is not observed if not playing
								while(-1 == objectTimingList.get(currentManualNodeNumber).getAudioPlayer().getAudioVolume()) {
									try {
										Thread.sleep(50);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								objectTimingList.get(currentManualNodeNumber).getAudioPlayer().setAudioVolume(masterVolume.intValue());
								objectTimingList.get(currentManualNodeNumber).getAudioPlayer().muteAudio(masterMute);
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
				slideTimerDone = slideTimer.startTimer(presentation.getSlideList().get(slideNumber).getSlideDuration()
										- objectTimingList.get(currentAutomaticNodeNumber).getTimeInPresentation());
				
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
									
					if ("Node" == objectTimingList.get(currentAutomaticNodeNumber).getObjectType()) {
						objectTimingControl = new ObjectTimingControl(
										objectTimingList.get(currentAutomaticNodeNumber).getNode(),
										objectTimingList.get(currentAutomaticNodeNumber).getTimeSinceLastNode(),
										objectTimingList.get(currentAutomaticNodeNumber).getVisible());
					}
					else {
						if ("VideoPlayer" == objectTimingList.get(currentAutomaticNodeNumber).getObjectType()) {
							objectTimingControl = new ObjectTimingControl(
									objectTimingList.get(currentAutomaticNodeNumber).getNode(),
									objectTimingList.get(currentAutomaticNodeNumber).getTimeSinceLastNode(),
									objectTimingList.get(currentAutomaticNodeNumber).getVisible(),
									objectTimingList.get(currentAutomaticNodeNumber).getVideoPlayer());
						}
						else {
							objectTimingControl = new ObjectTimingControl(
											objectTimingList.get(currentAutomaticNodeNumber).getAudioPlayer(),
											objectTimingList.get(currentAutomaticNodeNumber).getTimeSinceLastNode(),
											objectTimingList.get(currentAutomaticNodeNumber).getVisible(),
											masterVolume.intValue(), masterMute);
						}
						
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
			
			final TextEntry currentText = currentSlide.getTextList().get(text);
			
			tempPane.getChildren().add(TextHandler.createText(duffCanvas, sourceFile,
					currentText.getTextContent(), currentText.getTextFont(),
					currentText.getTextFontColour(), currentText.getTextFontSize(),
					currentText.getTextXStart(), currentText.getTextYStart(),
					currentText.getTextStartTime(), currentText.getTextDuration()));
			
			presentationLayout.getChildren().add(tempPane);
			
			System.out.println("text entry: " + text + "\n");
			System.out.println(currentText.getTextContent() + "\n");
			
			tempPane.setVisible(false);
			
			// Allow mouse clicks through the pane where the video is not located
			tempPane.setPickOnBounds(false);
			
			tempPane.setOnMouseClicked(new EventHandler<MouseEvent>(){				 
				@Override
				public void handle(MouseEvent arg0) {
					if (currentText.getTextTargetSlide() >= 0) {
						interactableClicked = true;
						changeSlide(currentText.getTextTargetSlide());
					}
				}
			});
			
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
			
			final ImageEntry currentImage = currentSlide.getImageList().get(image);
			
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
			
			// Allow mouse clicks through the pane where the video is not located
			tempCanvas.setPickOnBounds(false);
			
			tempCanvas.setOnMouseClicked(new EventHandler<MouseEvent>(){				 
				@Override
				public void handle(MouseEvent arg0) {
					if (currentImage.getImageTargetSlide() >= 0) {
						interactableClicked = true;
						changeSlide(currentImage.getImageTargetSlide());
					}
				}
			});
			
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
			final ShapeEntry currentShape = currentSlide.getShapeList().get(shape);
			
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
			
			// Allow mouse clicks through the pane where the video is not located
			tempPane.setPickOnBounds(false);
			
			tempPane.setOnMouseClicked(new EventHandler<MouseEvent>(){				 
				@Override
				public void handle(MouseEvent arg0) {
					if (currentShape.getShapeTargetSlide() >= 0) {
						interactableClicked = true;
						changeSlide(currentShape.getShapeTargetSlide());
					}
				}
			});
			
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
			final PolygonEntry currentPolygon = currentSlide.getPolygonList().get(polygon);
						
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
			
			// Allow mouse clicks through the pane where the video is not located
			tempPane.setPickOnBounds(false);
			
			tempPane.setOnMouseClicked(new EventHandler<MouseEvent>(){				 
				@Override
				public void handle(MouseEvent arg0) {
					if (currentPolygon.getPolygonTargetSlide() >= 0) {
						interactableClicked = true;
						changeSlide(currentPolygon.getPolygonTargetSlide());
					}
				}
			});
			
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
			final VideoEntry currentVideo = currentSlide.getVideoList().get(video);
			
			File tempFile = new File(presentation.getPath() + "/" + currentVideo.getVideoSourceFile());
			String videoPath = tempFile.toURI().toASCIIString();
			
			VideoPlayer videoPlayer = new VideoPlayer();
			
			
			videoPlayerList.add(videoPlayer);
			
			Pane tempPane = videoPlayerList.get(video).videoPlayerWindow(
					videoPath,
					currentVideo.getVideoYStart(),
					currentVideo.getVideoXStart(),
					VIDEO_WIDTH, VIDEO_HEIGHT, duffCanvas);
			
			videoPlayerList.get(video).setVolume(masterVolume.intValue());
			videoPlayerList.get(video).muteAudio(masterMute);
			
			// Allow mouse clicks through the pane where the video is not located
			tempPane.setPickOnBounds(false);
			
			tempPane.setOnMouseClicked(new EventHandler<MouseEvent>(){				 
				@Override
				public void handle(MouseEvent arg0) {
					if (currentVideo.getVideoTargetSlide() >= 0) {
						interactableClicked = true;
						changeSlide(currentVideo.getVideoTargetSlide());
					}
				}
			});
			
			presentationLayout.getChildren().add(tempPane);
			
			System.out.println("video entry: " + video + "\n");
			
			tempPane.setVisible(false);
			
			// Add pane to objectTimingList 
			TimingEntry tempVideoTimingEntryAppear = new TimingEntry(tempPane, currentVideo.getVideoStartTime(), currentVideo.getVideoDuration(), true, videoPlayer);
			TimingEntry tempVideoTimingEntryDisappear = new TimingEntry(tempPane, currentVideo.getVideoStartTime(), currentVideo.getVideoDuration(), false, videoPlayer);
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
