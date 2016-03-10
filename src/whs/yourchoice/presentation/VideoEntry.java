package whs.yourchoice.presentation;

/**
* Video Entry
* Latest Update: 10/03/16
*
* Copyright and Licensing Information if applicable
*/


/**
* This class is responsible for storing an video object contained in the XML file.
* A different instance of this class is created for each video object in the XML file.
*
* @author Antonio Figueiredo and Sabrina Quinn
* @version v1.0 10/03/16
*/

public class VideoEntry 
{
	public int videoStartTime = -10;
	public int videoDuration = -10;
	public float videoXStart = -10;
	public float videoYStart = -10;
	public String videoSourceFile = null;
	public boolean videoLoop = false;
	
	private boolean interactable = false;
	private int targetSlide = -10;
	
	
	/**
	* setVideoTargetSlide method. Sets the target slide of the video.
	* @param contents The target slide of the video.
	*/
	public void setVideoTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	/**
	* getVideoTargetSlide method. Gets the target slide of the video.
	* @return targetSlide Returns the target slide of the video.
	*/
	public int getVideoTargetSlide()
	{
		return targetSlide;
	}
	
	/**
	* setVideoInteractable method. Sets the interactable flag of the video.
	* @param contents The interactable flag of the video.
	*/
	public void setVideoInteractable(boolean b)
	{
		interactable = b;
	}
	
	/**
	* getVideoInteractable method. Gets the interactable flag of the video.
	* @return interactable Returns the interactable flag of the video.
	*/
	public boolean getVideoInteractable()
	{
		return interactable;
	}
	
	/**
	* setVideoStartTime method. Sets the start time of the video.
	* @param contents The start time of the video.
	*/
	public void setVideoStartTime(String contents) 
	{
		videoStartTime = Integer.parseInt(contents);
	}

	/**
	* getVideoStartTime method. Gets the start time of the video.
	* @return videoStartTime Returns the start time of the video.
	*/
	public int getVideoStartTime() 
	{
		return videoStartTime;
	}
	
	/**
	* setVideoDuration method. Sets the duration of the video.
	* @param contents The duration of the video.
	*/
	public void setVideoDuration(String contents) 
	{
		videoDuration = Integer.parseInt(contents);
	}

	/**
	* getVideoDuration method. Gets the duration of the video.
	* @return videoDuration Returns the duration of the video.
	*/
	public int getVideoDuration() 
	{
		return videoDuration;
	}
	
	/**
	* setVideoXStart method. Sets the x start of the video.
	* @param contents The x start of the video.
	*/
	public void setVideoXStart(String contents) 
	{
		videoXStart = Float.parseFloat(contents);
	}

	/**
	* getVideoXStart method. Gets the x start of the video.
	* @return videoXStart Returns the x start of the video.
	*/
	public float getVideoXStart() 
	{
		return videoXStart;
	}
	
	/**
	* setVideoYStart method. Sets the y start of the video.
	* @param contents The y start of the video.
	*/
	public void setVideoYStart(String contents) 
	{
		videoYStart = Float.parseFloat(contents);
	}

	/**
	* getVideoYStart method. Gets the y start of the video.
	* @return videoYStart Returns the y start of the video.
	*/
	public float getVideoYStart() 
	{
		return videoYStart;
	}
	
	/**
	* setVideoSourceFile method. Sets the source file of the video.
	* @param contents The source file of the video.
	*/
	public void setVideoSourceFile(String contents) 
	{
		videoSourceFile = contents;		
	}

	/**
	* getVideoSourceFile method. Gets the source file of the video.
	* @return videoSourceFile Returns the source file of the video.
	*/
	public String getVideoSourceFile() 
	{
		return videoSourceFile;
	}
	
	/**
	* setVideoLoop method. Sets the loop flag of the video.
	* @param contents The loop flag of the video.
	*/
	public void setVideoLoop(String contents) 
	{
		videoLoop = Boolean.parseBoolean(contents);
	}

	/**
	* getVideoLoop method. Gets the loop flag of the video.
	* @return videoLoop Returns the loop flag of the video.
	*/
	public boolean getVideoLoop() 
	{
		return videoLoop;
	}
}