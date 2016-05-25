/**
 * Licensing information
 * 
 * Copyright Woolly Hat Software
 */

package whs.yourchoice.presentation;

import java.util.ArrayList;
import java.util.List;

/**
* This class is responsible for storing a slide object contained in the XML file.
* It also stores lists of all instances of all other types of media contained in the XML file:
* 	(audio, image, polygon, shape, text and video).
*
* @author ajff500, sqk501 and ch1092
* @version v0.3 02/05/16
*/

public class SlideEntry 
{
	private int slideID = -10;
	private int slideDuration = -10;
	private int slideNext = -10;
	
	private String slideBackgroundColour = null;
	
	private List<TextEntry> textList = new ArrayList<TextEntry>();
	private List<ShapeEntry> shapeList = new ArrayList<ShapeEntry>();
	private List<PolygonEntry> polygonList = new ArrayList<PolygonEntry>();
	private List<ImageEntry> imageList = new ArrayList<ImageEntry>();
	private List<VideoEntry> videoList = new ArrayList<VideoEntry>();
	private List<AudioEntry> audioList = new ArrayList<AudioEntry>();
	
	
	/**
	* setSlideBackgroundColour method. Sets the background colour of the slide.
	* @param contents The background colour of the slide.
	*/
	public void setSlideBackgroundColour(String contents) 
	{
		slideBackgroundColour = contents;	
	}

	/**
	* getSlideBackgroundColour method. Gets the background colour of the slide.
	* @return slideBackgroundColour Returns the background colour of the slide.
	*/
	public String getSlideBackgroundColour() 
	{	
		return slideBackgroundColour;
	}
	
	/**
	* getTextEntry method. Gets the TextList.
	* @return textList Returns the text array.
	*/
	public List<TextEntry> getTextList()
	{
		return textList;
	}
	
	/**
	* getShapeEntry method. Gets the ShapeList.
	* @return shapeList Returns the shape array.
	*/
	public List<ShapeEntry> getShapeList()
	{
		return shapeList;
	}
	
	/**
	* getPolygonEntry method. Gets the PolygonList.
	* @return polygonList Returns the polygon array.
	*/
	public List<PolygonEntry> getPolygonList()
	{
		return polygonList;
	}
	
	/**
	* getImageEntry method. Gets the ImageList.
	* @return imageList Returns the image array.
	*/
	public List<ImageEntry> getImageList()
	{
		return imageList;
	}
	
	/**
	* getVideoEntry method. Gets the VideoList.
	* @return videoList Returns the video array.
	*/
	public List<VideoEntry> getVideoList()
	{
		return videoList;
	}
	
	/**
	* getAudioEntry method. Gets the AudioList.
	* @return audioList.get(i) Returns the audio array.
	*/
	public List<AudioEntry> getAudioList()
	{
		return audioList;
	}
	
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
