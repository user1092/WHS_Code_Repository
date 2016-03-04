package com.whs.server;

public class ImageClass 
{
	protected int imageStartTime = -10;
	protected String imageSourceFile = null;
	protected int imageDuration = -10;
	protected float imageXStart = -10;
	protected float imageYStart = -10;
	protected float imageWidth = -10;
	protected float imageHeight = -10;
	
	private boolean interactable = false;
	private int targetSlide = -10;
	
	protected void setImageTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	protected int getImageTargetSlide()
	{
		return targetSlide;
	}
	
	
	protected void setImageInteractable(boolean b)
	{
		interactable = b;
	}
	
	protected boolean getImageInteractable()
	{
		return interactable;
	}
	
	protected void setImageStartTime(String contents) 
	{
		imageStartTime = Integer.parseInt(contents);
	}

	protected int getImageStartTime() 
	{
		return imageStartTime;
	}
	
	protected void setImageSourceFile(String contents) 
	{
		imageSourceFile = contents;		
	}

	protected String getImageSourceFile() 
	{
		return imageSourceFile;
	}
	
	protected void setImageDuration(String contents) 
	{
		imageDuration = Integer.parseInt(contents);	
	}

	protected int getImageDuration() 
	{
		return imageDuration;
	}
	
	protected void setImageXStart(String contents) 
	{
		imageXStart = Float.parseFloat(contents);
	}

	protected float getImageXStart() 
	{
		return imageXStart;
	}
	
	protected void setImageYStart(String contents) 
	{
		imageYStart = Float.parseFloat(contents);
	}

	protected float getImageYStart() 
	{
		return imageYStart;
	}
	
	protected void setImageWidth(String contents) 
	{
		imageWidth = Float.parseFloat(contents);
	}

	protected float getImageWidth() 
	{
		return imageWidth;
	}
	
	protected void setImageHeight(String contents) 
	{
		imageHeight = Float.parseFloat(contents);
	}

	protected float getImageHeight() 
	{
		return imageHeight;
	}
}
