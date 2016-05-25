/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */
package whs.yourchoice.presentation;


/**
* This class is responsible for storing an image object contained in the XML file.
* A different instance of this class is created for each image object in the XML file.
*
* @author ajff500 and sqk501
* @version v0.1 18/04/16
*/

public class ImageEntry 
{
	private int imageStartTime = -10;
	private String imageSourceFile = null;
	private int imageDuration = -10;
	private float imageXStart = -10;
	private float imageYStart = -10;
	private float imageWidth = -10;
	private float imageHeight = -10;
	
	private boolean interactable = false;
	private int targetSlide = -10;
	
	
	/**
	* setImageTargetSlide method. Sets the target slide of the image.
	* @param contents The target slide of the image.
	*/
	public void setImageTargetSlide(String i)
	{
		targetSlide = Integer.parseInt(i);
	}
	
	/**
	* getImageTargetSlide method. Gets the target slide of the image.
	* @return targetSlide Returns the target slide of the image.
	*/
	public int getImageTargetSlide()
	{
		return targetSlide;
	}
	
	/**
	* setImageInteractable method. Sets the interactable flag of the image.
	* @param contents The interactable flag of the image.
	*/
	public void setImageInteractable(boolean b)
	{
		interactable = b;
	}
	
	/**
	* getImageInteractable method. Gets the interactable flag of the image.
	* @return interactable Returns the interactable flag of the image.
	*/
	public boolean getImageInteractable()
	{
		return interactable;
	}
	
	/**
	* setImageStartTime method. Sets the start time of the image.
	* @param contents The start time of the image.
	*/
	public void setImageStartTime(String contents) 
	{
		imageStartTime = Integer.parseInt(contents);
	}

	/**
	* getImageStartTime method. Gets the start time of the image.
	* @return imageStartTime Returns the start time of the image.
	*/
	public int getImageStartTime() 
	{
		return imageStartTime;
	}
	
	/**
	* setImageSourceFile method. Sets the source file of the image.
	* @param contents The source file of the image.
	*/
	public void setImageSourceFile(String contents) 
	{
		imageSourceFile = contents;		
	}

	/**
	* getImageSourceFile method. Gets the source file of the image.
	* @return imageSourceFile Returns the source file of the image.
	*/
	public String getImageSourceFile() 
	{
		return imageSourceFile;
	}
	
	/**
	* setImageDuration method. Sets the duration of the image.
	* @param contents The duration of the image.
	*/
	public void setImageDuration(String contents) 
	{
		imageDuration = Integer.parseInt(contents);	
	}

	/**
	* getImageDuration method. Gets the duration of the image.
	* @return imageDuration Returns the duration of the image.
	*/
	public int getImageDuration() 
	{
		return imageDuration;
	}
	
	/**
	* setImageXStart method. Sets the x start of the image.
	* @param contents The x start of the image.
	*/
	public void setImageXStart(String contents) 
	{
		imageXStart = Float.parseFloat(contents);
	}

	/**
	* getImageXStart method. Gets the x start of the image.
	* @return imageXStart Returns the x start of the image.
	*/
	public float getImageXStart() 
	{
		return imageXStart;
	}
	
	/**
	* setImageYStart method. Sets the y start of the image.
	* @param contents The y start of the image.
	*/
	public void setImageYStart(String contents) 
	{
		imageYStart = Float.parseFloat(contents);
	}

	/**
	* getImageYStart method. Gets the y start of the image.
	* @return imageYStart Returns the y start of the image.
	*/
	public float getImageYStart() 
	{
		return imageYStart;
	}
	
	/**
	* setImageWidth method. Sets the width of the image.
	* @param contents The width of the image.
	*/
	public void setImageWidth(String contents) 
	{
		imageWidth = Float.parseFloat(contents);
	}

	/**
	* getImageWidth method. Gets the width of the image.
	* @return imageWidth Returns the width of the image.
	*/
	public float getImageWidth() 
	{
		return imageWidth;
	}
	
	/**
	* setImageHeight method. Sets the height of the image.
	* @param contents The height of the image.
	*/
	public void setImageHeight(String contents) 
	{
		imageHeight = Float.parseFloat(contents);
	}

	/**
	* getImageHeight method. Gets the height of the image.
	* @return imageHeight Returns the height of the image.
	*/
	public float getImageHeight() 
	{
		return imageHeight;
	}
}
