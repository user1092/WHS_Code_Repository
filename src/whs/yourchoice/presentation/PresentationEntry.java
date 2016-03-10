package whs.yourchoice.presentation;

/**
* Presentation Entry
* Latest Update: 10/03/16
*
* Copyright and Licensing Information if applicable
*/

import java.util.ArrayList;
import java.util.List;

/**
* This class is responsible for storing the presentation object contained in the XML file.
* It also stores a list of all instances of all slides contained in the XML file.
*
* @author Antonio Figueiredo and Sabrina Quinn
* @version v1.0 10/03/16
*/

public class PresentationEntry 
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
	
	public List<SlideEntry> slideList = new ArrayList<SlideEntry>();
	
	
	/**
	* setPresentationTitle method. Sets the title of the presentation.
	* @param contents The title of the presentation.
	*/
	public void setPresentationTitle(String contents) 
	{
		titleName = contents;		
	}
	
	/**
	* getPresentationTitle method. Gets the title of the presentation.
	* @return titleName Returns the title of the presentation.
	*/
	public String getPresentationTitle()
	{
		return titleName;
	}
	
	/**
	* setPresentationAuthor method. Sets the author of the presentation.
	* @param contents The author of the presentation.
	*/
	public void setPresentationAuthor(String contents) 
	{
		authorName = contents;
	}
	
	/**
	* getPresentationAuthor method. Gets the author of the presentation.
	* @return authorName Returns the author of the presentation.
	*/
	public String getPresentationAuthor() 
	{
		return authorName;
	}
	
	/**
	* setPresentationVersion method. Sets the version of the presentation.
	* @param contents The version of the presentation.
	*/
	public void setPresentationVersion(String contents) 
	{
		versionNumber = contents;
	}
	
	/**
	* getPresentationVersion method. Gets the version of the presentation.
	* @return versionNumber Returns the version of the presentation.
	*/
	public String getPresentationVersion() 
	{
		return versionNumber;
	}

	/**
	* setDefaultBackgroundColour method. Sets the background colour of the presentation.
	* @param contents The background colour of the presentation.
	*/
	public void setDefaultBackgroundColour(String contents) 
	{
		defaultBackgroundColour = contents;	
	}

	/**
	* getDefaultBackgroundColour method. Gets the background colour of the presentation.
	* @return defaultBackgroundColour Returns the background colour of the presentation.
	*/
	public String getDefaultBackgroundColour() 
	{	
		return defaultBackgroundColour;
	}

	/**
	* setDefaultFont method. Sets the font of the presentation.
	* @param contents The font of the presentation.
	*/
	public void setDefaultFont(String contents) 
	{
		defaultFont = contents;	
	}

	/**
	* getDefaultFont method. Gets the font of the presentation.
	* @return defaultFont Returns the font of the presentation.
	*/
	public String getDefaultFont() 
	{
		return defaultFont;
	}
	
	/**
	* setDefaultFontSize method. Sets the font size of the presentation.
	* @param contents The font size of the presentation.
	*/
	public void setDefaultFontSize(String contents) 
	{
		defaultFontSize = Integer.parseInt(contents);
	}

	/**
	* getDefaultFontSize method. Gets the font size of the presentation.
	* @return defaultFontSize Returns the font size of the presentation.
	*/
	public int getDefaultFontSize() 
	{
		return defaultFontSize;
	}
	
	/**
	* setDefaultFontColour method. Sets the font colour of the presentation.
	* @param contents The font colour of the presentation.
	*/
	public void setDefaultFontColour(String contents) 
	{
		defaultFontColour = contents;	
	}

	/**
	* getDefaultFontColour method. Gets the font colour of the presentation.
	* @return defaultFontColour Returns the font colour of the presentation.
	*/
	public String getDefaultFontColour() 
	{
		return defaultFontColour;
	}
	
	/**
	* setDefaultLineColour method. Sets the line colour of the presentation.
	* @param contents The line colour of the presentation.
	*/
	public void setDefaultLineColour(String contents) 
	{
		defaultLineColour = contents;	
	}

	/**
	* getDefaultLineColour method. Gets the line colour of the presentation.
	* @return defaultLineColour Returns the line colour of the presentation.
	*/
	public String getDefaultLineColour() 
	{
		return defaultLineColour;
	}
	
	/**
	* setDefaultFillColour method. Sets the fill colour of the presentation.
	* @param contents The fill colour of the presentation.
	*/
	public void setDefaultFillColour(String contents) 
	{
		defaultFillColour = contents;	
	}

	/**
	* getDefaultFillColour method. Gets the fill colour of the presentation.
	* @return defaultFillColour Returns the fill colour of the presentation.
	*/
	public String getDefaultFillColour() 
	{
		return defaultFillColour;
	}
	
	/**
	* SlideEntry method. Created a new instance of the SlideEntry class.
	* @return slide Returns the new instance of the SlideEntry created.
	*/
	public SlideEntry CreateNewSlide() 
	{
		SlideEntry slide = new SlideEntry();
		
		slideList.add(slide);
		
		return slide;
	}
	
	
	
	
	
	
	






}
