package com.whs.server;

public class AudioClass 
{
	protected String audioStartTime = null;
	protected String audioDuration = null;
	protected String audioSourceFile = null;
	protected String audioLoop = null;

	
	protected void setAudioStartTime(String contents) 
	{
		audioStartTime = contents;		
	}

	protected String getAudioStartTime() 
	{
		return audioStartTime;
	}
	
	protected void setAudioDuration(String contents) 
	{
		audioDuration = contents;		
	}

	protected String getAudioDuration() 
	{
		return audioDuration;
	}
	
	protected void setAudioSourceFile(String contents) 
	{
		audioSourceFile = contents;		
	}

	protected String getAudioSourceFile() 
	{
		return audioSourceFile;
	}
	
	protected void setAudioLoop(String contents) 
	{
		audioLoop = contents;		
	}

	protected String getAudioLoop() 
	{
		return audioLoop;
	}
}
