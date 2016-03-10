package whs.yourchoice.presentation;

/**
* Text Entry
* Latest Update: 10/03/16
*
* Copyright and Licensing Information if applicable
*/


/**
* This class is responsible for storing an text object contained in the XML file.
* A different instance of this class is created for each text object in the XML file.
*
* @author Antonio Figueiredo and Sabrina Quinn
* @version v1.0 10/03/16
*/

public class TextEntry 
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
	
	
	/**
	* setTextTargetSlide method. Sets the target slide of the text.
	* @param contents The target slide of the text.
	*/
	public void setTextTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	/**
	* getTextTargetSlide method. Gets the target slide of the text.
	* @return targetSlide Returns the target slide of the text.
	*/
	public int getTextTargetSlide()
	{
		return targetSlide;
	}
	
	/**
	* setTextInteractable method. Sets the interactable flag of the text.
	* @param contents The interactable flag of the text.
	*/
	public void setTextInteractable(boolean b)
	{
		interactable = b;
	}
	
	/**
	* getTextInteractable method. Gets the interactable flag of the text.
	* @return interactable Returns the interactable flag of the text.
	*/
	public boolean getTextInteractable()
	{
		return interactable;
	}
	
	/**
	* setTextStartTime method. Sets the start time of the text.
	* @param contents The start time of the text.
	*/
	public void setTextStartTime(String contents) 
	{
		textStartTime = Integer.parseInt(contents);		
	}
	
	/**
	* getTextStartTime method. Gets the start time of the text.
	* @return textStartTime Returns the start time of the text.
	*/
	public int getTextStartTime()
	{
		return textStartTime;
	}

	/**
	* setTextDuration method. Sets the duration of the text.
	* @param contents The duration of the text.
	*/
	public void setTextDuration(String contents) 
	{
		textDuration = Integer.parseInt(contents);
	}
	
	/**
	* getTextDuration method. Gets the duration of the text.
	* @return textDuration Returns the duration of the text.
	*/
	public int getTextDuration()
	{
		return textDuration;
	}

	/**
	* setTextXStart method. Sets the x start of the text.
	* @param contents The x start of the text.
	*/
	public void setTextXStart(String contents) 
	{
		textXStart = Float.parseFloat(contents);
	}

	/**
	* getTextXStart method. Gets the x start of the text.
	* @return textXStart Returns the x start of the text.
	*/
	public float getTextXStart() 
	{
		return textXStart;
	}
	
	/**
	* setTextYStart method. Sets the y start of the text.
	* @param contents The y start of the text.
	*/
	public void setTextYStart(String contents) 
	{
		textYStart = Float.parseFloat(contents);
	}

	/**
	* getTextYStart method. Gets the y start of the text.
	* @return textYStart Returns the y start of the text.
	*/
	public float getTextYStart() 
	{
		return textYStart;
	}
	
	/**
	* setTextFont method. Sets the font of the text.
	* @param contents The font of the text.
	*/
	public void setTextFont(String contents) 
	{
		textFont = contents;		
	}

	/**
	* getTextFont method. Gets the font of the text.
	* @return textFont Returns the font of the text.
	*/
	public String getTextFont() 
	{
		return textFont;
	}
	
	/**
	* setTextFontSize method. Sets the font size of the text.
	* @param contents The font size of the text.
	*/
	public void setTextFontSize(String contents) 
	{
		textFontSize = Integer.parseInt(contents);	
	}

	/**
	* getTextFontSize method. Gets the font size of the text.
	* @return textFontSize Returns the font size of the text.
	*/
	public int getTextFontSize() 
	{
		return textFontSize;
	}
	
	/**
	* setTextFontColour method. Sets the font colour of the text.
	* @param contents The font colour of the text.
	*/
	public void setTextFontColour(String contents) 
	{
		textFontColour = contents;		
	}

	/**
	* getTextFontColour method. Gets the font colour of the text.
	* @return textFontColour Returns the font colour of the text.
	*/
	public String getTextFontColour() 
	{
		return textFontColour;
	}
	
	/**
	* setTextContent method. Sets the content of the text.
	* @param contents The content of the text.
	*/
	public void setTextContent(String contents) 
	{
		textContent = contents;		
	}

	/**
	* getTextContent method. Gets the content of the text.
	* @return textContent Returns the content of the text.
	*/
	public String getTextContent() 
	{
		return textContent;
	}
}
