/**
 * 
 * 
 * 
 */

package whs.yourchoice.audio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * Class provided as an example for streaming an audio file
 * 
 * This file has been tested with the following configuration:
 * JDK 1.8 32-bit
 * VLC 2.1.0 32-bit
 * vlcj-3.8.0
 * an acc format audio file
 * 
 * @author		user1092, guest501
 * @version		v0.2 04/03/2016
 * 
 * NOT FOR RELEASE!
 */
public class AudioServerExample {
	
	// Declarations for the GUI window
	private JFrame serverFrame;
	private JPanel buttonPanel;
	private JPanel upperPanel;
	
	/*
	 * Start of Audio specific declarations
	 */
	
	// Location of where VLC is installed
	private final String VLC_LIBRARY_LOCATION = "M:/vlc-2.1.0";
	// Set VLC video output to a dummy
	private final String[] VLC_ARGS = {"--vout", "dummy"};
	
	private static MediaPlayerFactory mediaPlayerFactory;
	
	private static AudioStreamer audioStreamer;
	
	private String audioRequested = "jetairplane16sec.aac";
	private String iP = "127.0.0.1";
	private int rtpPort = 5555;
	
	/*
	 * End of Audio specific declarations
	 */

	/**
	 * Constructor
	 * 
	 * The constructor will automatically create an audio server as well as 
	 * creating and populating a server window.
	 */
	public AudioServerExample() {
		
		// Find and load VLC libraries
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_LIBRARY_LOCATION);
		System.setProperty("jna.library.path", VLC_LIBRARY_LOCATION);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		// Create a media player factory
		mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);
		
		// Create a new audio streamer
		audioStreamer = new AudioStreamer(mediaPlayerFactory);
		
		setupGui();
		
		setupButtons();
		
		serverFrame.validate();
		serverFrame.addWindowListener(listener);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Main Method to start a new ServerGui. 
	 * @param args
	 */
	public static void main(String[] args) {
		new AudioServerExample();
	}
	
	/**
	 * Method to create a new JFrame and set the relevant options
	 */
	private void setupGui() {
		//Create a new frame
		serverFrame = new JFrame(); 
		
		// Setup window title and size
		serverFrame.setTitle("Audio Server Example");
		serverFrame.setSize(600, 400);
		serverFrame.setVisible(true);
		serverFrame.setResizable(false);
	}

	/**
	 * Method to setup all buttons on the server window
	 */
	private void setupButtons() {
		// Create button panel for the open and close socket buttons
		upperPanel = new JPanel();
		buttonPanel = new JPanel();
		serverFrame.add(upperPanel, BorderLayout.NORTH);
		upperPanel.add(buttonPanel, BorderLayout.CENTER);
		buttonPanel.setLayout(new BorderLayout());
		
		setupStreamButton();
	}

	/**
	 * This method will setup a button streaming an audio file
	 */
	private void setupStreamButton() {
		JButton streamButton;
		streamButton = new JButton("Stream");
		buttonPanel.add(streamButton, BorderLayout.SOUTH);
		
		streamButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				audioStreamer.streamAudio(audioRequested, iP, rtpPort);
			}          
	    });
	}
	
	 /**
	 * Listen for the window closing to correctly shutdown the media player 
	 */
	private static final WindowAdapter listener = new WindowAdapter() {

	        @Override
	        public void windowClosing(WindowEvent e) {
	        	audioStreamer.releasePlayer();
	    		mediaPlayerFactory.release();
	        }
	        
	    };

		
}
