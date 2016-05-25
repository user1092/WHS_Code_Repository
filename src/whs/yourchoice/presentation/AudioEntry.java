/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */
package whs.yourchoice.presentation;


/**
* This class is responsible for storing an audio object contained in the XML file.
* A different instance of this class is created for each audio object in the XML file.
*
* @author ajff500 and sqk501
* @version v0.1 18/04/16
*/

public class AudioEntry 
{
	private int audioStartTime = -10;
	private int audioDuration = -10;
	private String audioSourceFile = null;
	private boolean audioLoop = false;

	
	/**
	* setAudioStartTime method. Sets the start time of the audio.
	* @param contents The start time of the audio.
	*/
	public void setAudioStartTime(String contents) 
	{
		audioStartTime = Integer.parseInt(contents);
	}

	/**
	* getAudioStartTime method. Gets the start time of the audio.
	* @return audioStartTime Returns the start time of the audio.
	*/
	public int getAudioStartTime() 
	{
		return audioStartTime;
	}
	
	/**
	* setAudioDuration method. Sets the duration of the audio.
	* @param contents The duration of the audio.
	*/
	public void setAudioDuration(String contents) 
	{
		audioDuration = Integer.parseInt(contents);
	}

	/**
	* getAudioDuration method. Gets the duration of the audio.
	* @return audioDuration Returns the duration of the audio.
	*/
	public int getAudioDuration() 
	{
		return audioDuration;
	}
	
	/**
	* setAudioSourceFile method. Sets the source file of the audio.
	* @param contents The source file of the audio.
	*/
	public void setAudioSourceFile(String contents) 
	{
		audioSourceFile = contents;		
	}

	/**
	* getAudioSourceFile method. Gets the source file of the audio.
	* @return audioSourceFile Returns the source file of the audio.
	*/
	public String getAudioSourceFile() 
	{
		return audioSourceFile;
	}
	
	/**
	* setAudioLoop method. Sets the loop flag of the audio.
	* @param contents The loop flag of the audio.
	*/
	public void setAudioLoop(String contents) 
	{
		audioLoop = Boolean.parseBoolean(contents);
	}

	/**
	* getAudioLoop method. Gets the loop flag of the audio.
	* @return audioLoop Returns the loop flag of the audio.
	*/
	public boolean getAudioLoop() 
	{
		return audioLoop;
	}
}
