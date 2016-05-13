package whs.yourchoice.presentation;

import javafx.scene.Node;
import whs.yourchoice.audio.AudioPlayer;
import whs.yourchoice.video.VideoPlayer;

/**
* Class for creation of TimingEntry object
*
* @author cd828 & ch1092
* @version v0.1 28/04/16
*/
public class TimingEntry {

	private int timeInPresentation;
	private Node tempNode;
	private AudioPlayer tempAudioPlayer;
	private VideoPlayer tempVideoPlayer;
	private boolean visible;
	private int timeSinceLastNode;
	
	private String objectType;
	
	/**
	 * Constructor to create a Node based TimingEntry
	 * 
	 * @param tempNode
	 * @param startTime
	 * @param duration
	 * @param visible
	 */
	public TimingEntry(Node tempNode, int startTime, int duration, boolean visible) {
		if(true == visible) {
			timeInPresentation = startTime;
		}
		else {
			timeInPresentation = startTime + duration;
		}
		this.tempNode = tempNode;
		this.visible = visible;
		
		objectType = "Node";
	}
	
	/**
	 * Constructor to create a AudioPlayer based TimingEntry
	 * 
	 * @param tempAudioPlayer
	 * @param startTime
	 * @param duration
	 * @param visible
	 */
	public TimingEntry(AudioPlayer tempAudioPlayer, int startTime,	int duration, boolean visible) {
		if(true == visible) {
			timeInPresentation = startTime;
		}
		else {
			timeInPresentation = startTime + duration;
		}
		this.tempAudioPlayer = tempAudioPlayer;
		this.visible = visible;
		
		objectType = "AudioPlayer";
	}
	
	
	public TimingEntry(Node tempNode, int startTime, int duration, boolean visible, VideoPlayer tempVideoPlayer) {
		if(true == visible) {
			timeInPresentation = startTime;
		}
		else {
			timeInPresentation = startTime + duration;
		}
		this.tempNode = tempNode;
		this.visible = visible;
		this.tempVideoPlayer = tempVideoPlayer;
		
		objectType = "VideoPlayer";
	}

	/**
	 * Method for returning the time of the object in the presentation
	 * 
	 * @return timeInPresentation - the time in presentation
	 */
	public int getTimeInPresentation() {
		return timeInPresentation;
	}
	
	/**
	 * Method for returning the type of object
	 * 
	 * @return objectType - the type of object
	 */
	public String getObjectType() {
		return objectType;
	}
	
	/**
	 * Method to return a specific node in the presentation
	 * 
	 * @return tempNode - temporary node being timed
	 */
	public Node getNode() {
		return tempNode;
	}
	
	/**
	 * Method to return the audio player of the presentation
	 * 
	 * @return tempAudioPlayer - temporary audio player being timed
	 */
	public AudioPlayer getAudioPlayer() {
		return tempAudioPlayer;
	}
	
	/**
	 * Method to return whether object is visible or not
	 * 
	 * @return visible - object is visible
	 */
	public boolean getVisible() {
		return visible;
	}

	/**
	 * Method to return the time of the current node since the previous node
	 * 
	 * @return the timeSinceLastNode
	 */
	public int getTimeSinceLastNode() {
		return timeSinceLastNode;
	}

	/**
	 * Method to set the time of a particular node depending on the time of the previous node
	 * 
	 * @param timeSinceLastNode the timeSinceLastNode to set
	 */
	public void setTimeSinceLastNode(int timeSinceLastNode) {
		this.timeSinceLastNode = timeSinceLastNode;
	}

	/**
	 * Method to return the video player of the presentation
	 * 
	 * @return tempVideoPlayer - temporary video player being timed
	 */
	public VideoPlayer getVideoPlayer() {
		return tempVideoPlayer;
	}	
		
}
