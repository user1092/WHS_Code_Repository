package com.whs.server;

public class TextClass 
{
	private String textStartTime = null;
	private String textDuration = null;
	private String textXStart = null;
	private String textYStart = null;
	private String textFont = null;
	private String textFontSize = null;
	private String textFontColour = null;
	private String textContent = null;
	
	
	protected void setTextStartTime(String contents) 
	{
		textStartTime = contents;		
	}
	
	protected String getTextStartTime()
	{
		return textStartTime;
	}

	protected void setTextDuration(String contents) 
	{
		textDuration = contents;
	}
	
	protected String getTextDuration()
	{
		return textDuration;
	}

	protected void setTextXStart(String contents) 
	{
		textXStart = contents;		
	}

	protected String getTextXStart() 
	{
		return textXStart;
	}
	
	protected void setTextYStart(String contents) 
	{
		textYStart = contents;		
	}

	protected String getTextYStart() 
	{
		return textYStart;
	}
	
	protected void setTextFont(String contents) 
	{
		textFont = contents;		
	}

	protected String getTextFont() 
	{
		return textFont;
	}
	
	protected void setTextFontSize(String contents) 
	{
		textFontSize = contents;		
	}

	protected String getTextFontSize() 
	{
		return textFontSize;
	}
	
	protected void setTextFontColour(String contents) 
	{
		textFontColour = contents;		
	}

	protected String getTextFontColour() 
	{
		return textFontColour;
	}
	
	protected void setTextContent(String contents) 
	{
		textContent = contents;		
	}

	protected String getTextContent() 
	{
		return textContent;
	}
}