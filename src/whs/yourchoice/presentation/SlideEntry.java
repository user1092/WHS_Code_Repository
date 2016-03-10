package whs.yourchoice.presentation;

/**
* Slide Entry
* Latest Update: 10/03/16
*
* Copyright and Licensing Information if applicable
*/

import java.util.ArrayList;
import java.util.List;

/**
* This class is responsible for storing a slide object contained in the XML file.
* It also stores lists of all instances of all other types of media contained in the XML file:
* 	(audio, image, polygon, shape, text and video).
*
* @author Antonio Figueiredo and Sabrina Quinn
* @version v1.0 10/03/16
*/

public class SlideEntry 
{
	private int slideID = -10;
	private int slideDuration = -10;
	private int slideNext = -10;
	
	public List<TextEntry> textList = new ArrayList<TextEntry>();
	public List<ShapeEntry> shapeList = new ArrayList<ShapeEntry>();
	public List<PolygonEntry> polygonList = new ArrayList<PolygonEntry>();
	public List<ImageEntry> imageList = new ArrayList<ImageEntry>();
	public List<VideoEntry> videoList = new ArrayList<VideoEntry>();
	public List<AudioEntry> audioList = new ArrayList<AudioEntry>();
	

	/**
	* setSlideID method. Sets the ID of the slide.
	* @param contents The ID of the slide.
	*/
	public void setSlideID(String contents) 
	{
		slideID = Integer.parseInt(contents);
	}
	
	/**
	* getSlideID method. Gets the ID of the slide.
	* @return slideID Returns the ID of the slide.
	*/
	public int getSlideID()
	{
		return slideID;
	}

	/**
	* setSlideDuration method. Sets the duration of the slide.
	* @param contents The duration of the slide.
	*/
	public void setSlideDuration(String contents) 
	{
		slideDuration = Integer.parseInt(contents);
	}
	
	/**
	* getSlideDuration method. Gets the duration of the slide.
	* @return slideDuration Returns the duration of the slide.
	*/
	public int getSlideDuration()
	{
		return slideDuration;
	}

	/**
	* setSlideNext method. Sets the next slide.
	* @param contents The next slide.
	*/
	public void setSlideNext(String contents) 
	{
		slideNext = Integer.parseInt(contents);
	}
	
	/**
	* getSlideNext method. Gets the next slide.
	* @return slideNext Returns the next slide.
	*/
	public int getSlideNext()
	{
		return slideNext;
	}
	
//-------------------------------------------------------------------------------------------------------

	/**
	* TextEntry method. Created a new instance of the TextEntry class.
	* @return newText Returns the new instance of the TextEntry created.
	*/
	public TextEntry CreateNewText() 
	{
		TextEntry newText = new TextEntry();
		
		textList.add(newText);
		
		return newText;
	}

	/**
	* ShapeEntry method. Created a new instance of the ShapeEntry class.
	* @return newShape Returns the new instance of the ShapeEntry created.
	*/
	public ShapeEntry CreateNewShape() 
	{
		ShapeEntry newShape = new ShapeEntry();
		
		shapeList.add(newShape);
		
		return newShape;
	}

	/**
	* PolygonEntry method. Created a new instance of the PolygonEntry class.
	* @return newPolygon Returns the new instance of the PolygonEntry created.
	*/
	public PolygonEntry CreateNewPolygon() 
	{
		PolygonEntry newPolygon = new PolygonEntry();
		
		polygonList.add(newPolygon);
		
		return newPolygon;
	}

	/**
	* ImageEntry method. Created a new instance of the ImageEntry class.
	* @return newImage Returns the new instance of the ImageEntry created.
	*/
	public ImageEntry CreateNewImage() 
	{
		ImageEntry newImage = new ImageEntry();
		
		imageList.add(newImage);
		
		return newImage;
	}

	/**
	* VideoEntry method. Created a new instance of the VideoEntry class.
	* @return newVideo Returns the new instance of the VideoEntry created.
	*/
	public VideoEntry CreateNewVideo() 
	{
		VideoEntry newVideo = new VideoEntry();
		
		videoList.add(newVideo);
		
		return newVideo;
	}

	/**
	* AudioEntry method. Created a new instance of the AudioEntry class.
	* @return newAudio Returns the new instance of the AudioEntry created.
	*/
	public AudioEntry CreateNewAudio() 
	{
		AudioEntry newAudio = new AudioEntry();
		
		audioList.add(newAudio);
		
		return newAudio;
	}	
}
