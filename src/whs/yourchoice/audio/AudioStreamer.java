/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.audio;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

/**
 * Class for the client's back end handling playing audio files 
 * 
 * @author		ch1092, skq501
 * @version		v0.1 01/03/2016
 */
public class AudioStreamer {
	
	private HeadlessMediaPlayer headlessPlayer;
	
	/**
	 * Constructor
	 * 
	 * Create a new player from the mediaPlayerFactory.
	 * 
	 * @param mediaPlayerFactory
	 */
	public AudioStreamer(MediaPlayerFactory mediaPlayerFactory) {
		headlessPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
	}
	
	/**
	 * Method to start streaming the selected audio
	 * 
	 * @param audioRequested	-	The audio file that is to be streamed
	 * @param iP	-	The IP address of the client
	 * @param rtpPort	-	The port to stream the audio
	 */
	public void streamAudio(String audioRequested, String iP, int rtpPort) {
	
		String options = formatRtpStream(iP, rtpPort);
		
		System.out.println(audioRequested + " attempting to play");
		headlessPlayer.playMedia(audioRequested,
									options, 
									":no-sout-rtp-sap", 
									":no-sout-standard-sap", 
									":sout-all", 
									":sout-keep"
									);
	}

	/**
	 * Method to format the audio steam to the RTP standard
	 * 
	 * @param clientAddress	-	IP address of the client
	 * @param clientPort	-	RTP port to be used
	 * 
	 * @return options	-	Correctly formatted string for the playMedia method
	 */
	private String formatRtpStream(String clientAddress, int clientPort) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(clientAddress);
		sb.append(",port=");
		sb.append(clientPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}
	
	/**
	 * Method to clean up and release all players
	 */
	public void releasePlayer() {
		headlessPlayer.stop();
		headlessPlayer.release();
	}
}
