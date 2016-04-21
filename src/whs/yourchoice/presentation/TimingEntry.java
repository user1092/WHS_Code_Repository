package whs.yourchoice.presentation;

import whs.yourchoice.audio.AudioPlayer;
import javafx.scene.Node;

public class TimingEntry {

	private int timeInPresentation;
	private Node tempNode;
	private AudioPlayer tempAudioPlayer;
	private boolean visible;
	private int timeSinceLastNode;
	
	private String objectType;
	
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

	public int getTimeInPresentation() {
		return timeInPresentation;
	}
	
	public String getObjectType() {
		return objectType;
	}
	
	public Node getNode() {
		return tempNode;
	}
	
	public AudioPlayer getAudioPlayer() {
		return tempAudioPlayer;
	}
	
	public boolean getVisible() {
		return visible;
	}

	/**
	 * @return the timeSinceLastNode
	 */
	public int getTimeSinceLastNode() {
		return timeSinceLastNode;
	}

	/**
	 * @param timeSinceLastNode the timeSinceLastNode to set
	 */
	public void setTimeSinceLastNode(int timeSinceLastNode) {
		this.timeSinceLastNode = timeSinceLastNode;
	}	
		
}
