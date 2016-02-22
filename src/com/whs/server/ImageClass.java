package com.whs.server;

public class ImageClass 
{
	protected String imageStartTime = null;
	protected String imageSourceFile = null;
	protected String imageDuration = null;
	protected String imageXStart = null;
	protected String imageYStart = null;
	protected String imageWidth = null;
	protected String imageHeight = null;
	
	
	protected void setImageStartTime(String contents) 
	{
		imageStartTime = contents;		
	}

	protected String getImageStartTime() 
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
		imageDuration = contents;		
	}

	protected String getImageDuration() 
	{
		return imageDuration;
	}
	
	protected void setImageXStart(String contents) 
	{
		imageXStart = contents;		
	}

	protected String getImageXStart() 
	{
		return imageXStart;
	}
	
	protected void setImageYStart(String contents) 
	{
		imageYStart = contents;		
	}

	protected String getImageYStart() 
	{
		return imageYStart;
	}
	
	protected void setImageWidth(String contents) 
	{
		imageWidth = contents;		
	}

	protected String getImageWidth() 
	{
		return imageWidth;
	}
	
	protected void setImageHeight(String contents) 
	{
		imageHeight = contents;		
	}

	protected String getImageHeight() 
	{
		return imageHeight;
	}

}
