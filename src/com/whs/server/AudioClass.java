package com.whs.server;

public class AudioClass 
{
	protected int audioStartTime = -10;
	protected int audioDuration = -10;
	protected String audioSourceFile = null;
	protected boolean audioLoop = false;

	
	protected void setAudioStartTime(String contents) 
	{
		audioStartTime = Integer.parseInt(contents);
	}

	protected int getAudioStartTime() 
	{
		return audioStartTime;
	}
	
	protected void setAudioDuration(String contents) 
	{
		audioDuration = Integer.parseInt(contents);
	}

	protected int getAudioDuration() 
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
		audioLoop = Boolean.parseBoolean(contents);
	}

	protected boolean getAudioLoop() 
	{
		return audioLoop;
	}
}
