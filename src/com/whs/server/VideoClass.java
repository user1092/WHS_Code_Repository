package com.whs.server;

public class VideoClass 
{
	protected String videoStartTime = null;
	protected String videoDuration = null;
	protected String videoXStart = null;
	protected String videoYStart = null;
	protected String videoSourceFile = null;
	protected String videoLoop = null;
	
	private boolean interactable = false;
	
	
	protected void setVideoInteractable(boolean b)
	{
		interactable = b;
	}
	
	protected boolean getVideoInteractable()
	{
		return interactable;
	}
	
	protected void setVideoStartTime(String contents) 
	{
		videoStartTime = contents;		
	}

	protected String getVideoStartTime() 
	{
		return videoStartTime;
	}
	
	protected void setVideoDuration(String contents) 
	{
		videoDuration = contents;		
	}

	protected String getVideoDuration() 
	{
		return videoDuration;
	}
	
	protected void setVideoXStart(String contents) 
	{
		videoXStart = contents;		
	}

	protected String getVideoXStart() 
	{
		return videoXStart;
	}
	
	protected void setVideoYStart(String contents) 
	{
		videoYStart = contents;		
	}

	protected String getVideoYStart() 
	{
		return videoYStart;
	}
	
	protected void setVideoSourceFile(String contents) 
	{
		videoSourceFile = contents;		
	}

	protected String getVideoSourceFile() 
	{
		return videoSourceFile;
	}
	
	protected void setVideoLoop(String contents) 
	{
		videoLoop = contents;		
	}

	protected String getVideoLoop() 
	{
		return videoLoop;
	}
}