/**
 * 
 */
package com.whs.server;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Class for the client's back end handling playing audio files 
 * 
 * @author		user1092, guest501
 * @version		v0.1 01/03/2016
 */
public class AudioStreamer {

	// Location of where VLC is installed
	private final String VLC_LIBRARY_LOCATION = "M:/vlc-2.2.2";
	// Set VLC video output to a dummy
	private final String[] VLC_ARGS = {"--vout", "dummy", "-vvv"};
	
	private MediaPlayerFactory mediaPlayerFactory;
	private HeadlessMediaPlayer headlessPlayer;
		
	public AudioStreamer() {
		// Find and load VLC libraries
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_LIBRARY_LOCATION);
		System.setProperty("jna.library.path", VLC_LIBRARY_LOCATION);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);		
		headlessPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
	}
	
	/**
	 * Method to start streaming the selected audio
	 * 
	 * @param currentClient
	 */
	protected void streamAudio() {
		String audioRequested = "jetairplane16sec.wav";
		String iP = "127.0.0.1";
		int rtpPort = 5555; 
		
		
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
		StringBuilder sb = new StringBuilder(200);
		sb.append(":sout=#transcode{acodec=mp3,ab=192,channels=2,samplerate=48000}:rtp{dst=");
		//sb.append("::sout=#transcode{vcodec=mp4v,vb=3000,fps=30,scale=1,acodec=mp4a,ab=128,channels=2,samplerate=48000,width=800,height=600}:rtp{dst=");
		
		//sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}
}
