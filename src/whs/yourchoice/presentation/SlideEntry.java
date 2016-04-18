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
* @author ajff500 and sqk501
* @version v0.1 18/04/16
*/

public class SlideEntry 
{
	private int slideID = -10;
	private int slideDuration = -10;
	private int slideNext = -10;
	
	private List<TextEntry> textList = new ArrayList<TextEntry>();
	private List<ShapeEntry> shapeList = new ArrayList<ShapeEntry>();
	private List<PolygonEntry> polygonList = new ArrayList<PolygonEntry>();
	private List<ImageEntry> imageList = new ArrayList<ImageEntry>();
	private List<VideoEntry> videoList = new ArrayList<VideoEntry>();
	private List<AudioEntry> audioList = new ArrayList<AudioEntry>();
	
	/**
	* getTextEntry method. Gets the specific element of the TextList.
	* @param i The index of the text
	* @return textList.get(i) Returns the element text of the text array.
	*/
	public TextEntry getTextListEntry(int i)
	{
		return textList.get(i);
	}
	
	/**
	* getShapeEntry method. Gets the specific element of the ShapeList.
	* @param i The index of the shape
	* @return shapeList.get(i) Returns the element shape of the shape array.
	*/
	public ShapeEntry getShapeListEntry(int i)
	{
		return shapeList.get(i);
	}
	
	/**
	* getPolygonEntry method. Gets the specific element of the PolygonList.
	* @param i The index of the polygon
	* @return polygonList.get(i) Returns the element polygon of the polygon array.
	*/
	public PolygonEntry getPolygonListEntry(int i)
	{
		return polygonList.get(i);
	}
	
	/**
	* getImageEntry method. Gets the specific element of the ImageList.
	* @param i The index of the image
	* @return imageList.get(i) Returns the element image of the image array.
	*/
	public ImageEntry getImageListEntry(int i)
	{
		return imageList.get(i);
	}
	
	/**
	* getVideoEntry method. Gets the specific element of the VideoList.
	* @param i The index of the video
	* @return videoList.get(i) Returns the element video of the video array.
	*/
	public VideoEntry getVideoListEntry(int i)
	{
		return videoList.get(i);
	}
	
	/**
	* getAudioEntry method. Gets the specific element of the AudioList.
	* @param i The index of the audio
	* @return audioList.get(i) Returns the element audio of the audio array.
	*/
	public AudioEntry getAudioListEntry(int i)
	{
		return audioList.get(i);
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
