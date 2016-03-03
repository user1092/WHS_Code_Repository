/**
 * 
 */
package com.whs.server;

import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

/**
 * Class for the client's back end handling playing audio files 
 * 
 * @author		user1092, guest501
 * @version		v0.1 01/03/2016
 */
public class AudioStreamer {
	
	/**
	 * Method to start streaming the selected audio
	 * 
	 * @param audioRequested	-	The audio file that is to be streamed
	 * @param iP	-	The IP address of the client
	 * @param rtpPort	-	The port to stream the audio
	 * @param headlessPlayer	-	The player that should handle the streaming
	 */
	protected void streamAudio(String audioRequested, String iP, int rtpPort, HeadlessMediaPlayer headlessPlayer) {
	
		String options = formatRtpStream(iP, rtpPort);
		
		System.out.println(audioRequested + " attempting to play");
		headlessPlayer.playMedia(audioRequested,
									":file-caching=0",
									":network-caching=300",
									options, 
									":no-sout-rtp-sap", 
									":no-sout-standard-sap", 
									":sout-all", 
									":sout-keep",
									":sout-mux-caching=1000"
									);
	}

	/**
	 * Method to format the audio steam to the RTP standard
	 * 
	 * @param serverAddress
	 * @param serverPort
	 * @return
	 */
	private String formatRtpStream(String serverAddress, int serverPort) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}
}
