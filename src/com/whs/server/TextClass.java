package com.whs.server;

public class TextClass 
{
	private int textStartTime = -10;
	private int textDuration = -10;
	private float textXStart = -10;
	private float textYStart = -10;
	private String textFont = null;
	private int textFontSize = -10;
	private String textFontColour = null;
	private String textContent = null;
	
	private boolean interactable = false;
	private int targetSlide = -10;
	
	protected void setTextTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	protected int getTextTargetSlide()
	{
		return targetSlide;
	}
	
	protected void setTextInteractable(boolean b)
	{
		interactable = b;
	}
	
	protected boolean getTextInteractable()
	{
		return interactable;
	}
	
	protected void setTextStartTime(String contents) 
	{
		textStartTime = Integer.parseInt(contents);		
	}
	
	protected int getTextStartTime()
	{
		return textStartTime;
	}

	protected void setTextDuration(String contents) 
	{
		textDuration = Integer.parseInt(contents);
	}
	
	protected int getTextDuration()
	{
		return textDuration;
	}

	protected void setTextXStart(String contents) 
	{
		textXStart = Float.parseFloat(contents);
	}

	protected float getTextXStart() 
	{
		return textXStart;
	}
	
	protected void setTextYStart(String contents) 
	{
		textYStart = Float.parseFloat(contents);
	}

	protected float getTextYStart() 
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
		textFontSize = Integer.parseInt(contents);	
	}

	protected int getTextFontSize() 
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
