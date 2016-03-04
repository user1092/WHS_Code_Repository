package com.whs.server;

public class VideoClass 
{
	protected int videoStartTime = -10;
	protected int videoDuration = -10;
	protected float videoXStart = -10;
	protected float videoYStart = -10;
	protected String videoSourceFile = null;
	protected boolean videoLoop = false;
	
	private boolean interactable = false;
	private int targetSlide = -10;
	
	protected void setVideoTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	protected int getVideoTargetSlide()
	{
		return targetSlide;
	}
	
	
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
		videoStartTime = Integer.parseInt(contents);
	}

	protected int getVideoStartTime() 
	{
		return videoStartTime;
	}
	
	protected void setVideoDuration(String contents) 
	{
		videoDuration = Integer.parseInt(contents);
	}

	protected int getVideoDuration() 
	{
		return videoDuration;
	}
	
	protected void setVideoXStart(String contents) 
	{
		videoXStart = Float.parseFloat(contents);
	}

	protected float getVideoXStart() 
	{
		return videoXStart;
	}
	
	protected void setVideoYStart(String contents) 
	{
		videoYStart = Float.parseFloat(contents);
	}

	protected float getVideoYStart() 
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
		videoLoop = Boolean.parseBoolean(contents);
	}

	protected boolean getVideoLoop() 
	{
		return videoLoop;
	}
}