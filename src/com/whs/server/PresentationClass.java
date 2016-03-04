package com.whs.server;

import java.util.ArrayList;
import java.util.List;

public class PresentationClass 
{
	private String titleName = null;
	private String authorName = null;
	private String versionNumber = null;
	
	private String defaultBackgroundColour = null;
	private String defaultFont = null;
	private int defaultFontSize = -10;
	private String defaultFontColour = null;
	private String defaultLineColour = null;
	private String defaultFillColour = null;
	
	protected List<SlideClass> slideList = new ArrayList<SlideClass>();
	
	
	protected void setPresentationTitle(String contents) 
	{
		titleName = contents;		
	}
	
	protected String getPresentationTitle()
	{
		return titleName;
	}
	
	protected void setPresentationAuthor(String contents) 
	{
		authorName = contents;
	}
	
	protected String getPresentationAuthor() 
	{
		return authorName;
	}
	
	protected void setPresentationVersion(String contents) 
	{
		versionNumber = contents;
	}
	
	protected String getPresentationVersion() 
	{
		return versionNumber;
	}

	protected void setDefaultBackgroundColour(String contents) 
	{
		defaultBackgroundColour = contents;	
	}

	protected String getDefaultBackgroundColour() 
	{	
		return defaultBackgroundColour;
	}

	protected void setDefaultFont(String contents) 
	{
		defaultFont = contents;	
	}

	protected String getDefaultFont() 
	{
		return defaultFont;
	}
	
	protected void setDefaultFontSize(String contents) 
	{
		defaultFontSize = Integer.parseInt(contents);
	}

	protected int getDefaultFontSize() 
	{
		return defaultFontSize;
	}
	
	protected void setDefaultFontColour(String contents) 
	{
		defaultFontColour = contents;	
	}

	protected String getDefaultFontColour() 
	{
		return defaultFontColour;
	}
	
	protected void setDefaultLineColour(String contents) 
	{
		defaultLineColour = contents;	
	}

	protected String getDefaultLineColour() 
	{
		return defaultLineColour;
	}
	
	protected void setDefaultFillColour(String contents) 
	{
		defaultFillColour = contents;	
	}

	protected String getDefaultFillColour() 
	{
		return defaultFillColour;
	}
	
//-------------------------------------------------------------------------------------------------------
	
	protected SlideClass CreateNewSlide() 
	{
		SlideClass slide = new SlideClass();
		
		slideList.add(slide);
		
		return slide;
	}
	
	
	
	
	
	
	






}
