package com.whs.server;

//	Company : 				Woolly Software
//	Programmer :			Antonio Figueiredo
//	Latest Update Date :	25 / 02 / 2016

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// This class controls the main parsing process. It extracts the XML File content and saves it in a hierarchy structure which is easily accessible.
public class XMLParser 
{	
	PresentationClass presentation = new PresentationClass();
	boolean interactable = false;
	
	// Main method. It initialises the parsing process.
	public static void main(String[] args)
	{
		XMLParser newMain = new XMLParser();
		newMain.parsePresention("src/test_file.xml");
	}
	
	// This method sets up the parser and gets the correct document to parse.
	protected PresentationClass parsePresention(String filePath) 
	{
		try 
		{
			File file = new File(filePath);

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = dBuilder.parse(file);
			
			doc.getDocumentElement().normalize();

			System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
			
			ContinueParsingForChildren(doc.getChildNodes());			
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		return presentation;
	}
	
	// The main parsing loop. It gets all the main children:
	// In our specific PWS Standard, the main children are: documentInfo, Defaults and Slide.
	// Both DocumentInfo and Defaults were merged into one class called PresentationClass.
	// This method will loop for each slide encountered.
	protected void ContinueParsingForChildren(NodeList nodeList) 
	{
		for (int count = 0; count < nodeList.getLength(); count++) 
		{
			Node outerNode = nodeList.item(count);
			
			if (outerNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				CheckPresentationContent(outerNode);
				CheckSlideContent(outerNode);

				if (outerNode.hasChildNodes()) 
				{
					ContinueParsingForChildren(outerNode.getChildNodes());
				}
			}			
		}
	}
	
	// Small method to retrieve the presentation stuff (DocumentInfo + Defaults).
	protected void CheckPresentationContent(Node temp)
	{
		String nodeName = temp.getNodeName();
		String nodeContents = temp.getTextContent();
		
		switch(nodeName)
		{
			case "Title" :
				if (temp.getParentNode().getNodeName() == "documentInfo")
				{
					presentation.setPresentationTitle(nodeContents);					
				}			
				System.out.println("Title: " + presentation.getPresentationTitle());
			break;
				
			case "Author" :
				if (temp.getParentNode().getNodeName() == "documentInfo")
				{
					presentation.setPresentationAuthor(nodeContents);					
				}			
				System.out.println("Author: " + presentation.getPresentationAuthor());
			break;
				
			case "Version" :
				if (temp.getParentNode().getNodeName() == "documentInfo")
				{
					presentation.setPresentationVersion(nodeContents);					
				}			
				System.out.println("Version: " + presentation.getPresentationVersion());
			break;
			
			case "backgroundColour" :
				if (temp.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultBackgroundColour(nodeContents);					
				}			
				System.out.println("Background Colour: " + presentation.getDefaultBackgroundColour());
			break;
			
			case "font" :
				if (temp.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFont(nodeContents);					
				}			
				System.out.println("Font: " + presentation.getDefaultBackgroundColour());
			break;
			
			case "fontsize" :
				if (temp.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFontSize(nodeContents);					
				}			
				System.out.println("Font Size: " + presentation.getDefaultBackgroundColour());
			break;
			
			case "fontColour" :
				if (temp.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFontColour(nodeContents);					
				}			
				System.out.println("Font Colour: " + presentation.getDefaultBackgroundColour());
			break;
			
			case "lineColour" :
				if (temp.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultLineColour(nodeContents);					
				}			
				System.out.println("Line Colour: " + presentation.getDefaultBackgroundColour());
			break;
			
			case "fillColour" :
				if (temp.getParentNode().getNodeName() == "defaults")
				{
					presentation.setDefaultFillColour(nodeContents);					
				}			
				System.out.println("Fill Colour: " + presentation.getDefaultBackgroundColour());
			break;
			
			default:
				
				// Some sort of error message here.
				
			break;		
		}
	}
	
	// Method to parse a Text node.
	// It takes the node itself and the current slide it belongs to, and adds the contents of the node to the TextClass inside the current Slide.
	protected void ParseText (SlideClass currentSlide, Node currentNode, boolean interactable)
	{
		String currentNodeContents = currentNode.getTextContent();
		NamedNodeMap currentNodeAttributes = currentNode.getAttributes();
		
		TextClass tempText = currentSlide.CreateNewText();
		
		// These Strings are temporary and will be refactored out.
		String tagABody = "";
		String tagBBody = "";
		String tagIBody = "";
		
		for (int i = 0; i < currentNodeAttributes.getLength(); i++) 
		{
			Node reallyTempNode = currentNodeAttributes.item(i);
				
			if (reallyTempNode.getNodeName() == "starttime")
			{
				tempText.setTextStartTime(reallyTempNode.getNodeValue());
				System.out.println("Text Start Time : " + tempText.getTextStartTime());
			}
			
			if (reallyTempNode.getNodeName() == "duration")
			{
				tempText.setTextDuration(reallyTempNode.getNodeValue());
				System.out.println("Text Duration : " + tempText.getTextDuration());
			}
			
			if (reallyTempNode.getNodeName() == "xstart")
			{
				tempText.setTextXStart(reallyTempNode.getNodeValue());
				System.out.println("Text X Start : " + tempText.getTextXStart());
			}
			
			if (reallyTempNode.getNodeName() == "ystart")
			{
				tempText.setTextYStart(reallyTempNode.getNodeValue());
				System.out.println("Text Y Start : " + tempText.getTextYStart());
			}
			
			if (reallyTempNode.getNodeName() == "font")
			{
				tempText.setTextFont(reallyTempNode.getNodeValue());
				System.out.println("Text Font : " + tempText.getTextFont());
			}
			
			if (reallyTempNode.getNodeName() == "fontsize")
			{
				tempText.setTextFontSize(reallyTempNode.getNodeValue());
				System.out.println("Text Font Size : " + tempText.getTextFontSize());
			}
			
			if (reallyTempNode.getNodeName() == "fontColour")
			{
				tempText.setTextFontColour(reallyTempNode.getNodeValue());
				System.out.println("Text Font Colour : " + tempText.getTextFontColour());
			}
		}
		
		// The bit that deals with <b> and <i> tags. It is not functional but it is a skeleton.
		if (currentNode.hasChildNodes())
		{
	        NodeList childList = currentNode.getChildNodes();
	        for (int i = 0; i < childList.getLength(); i++) 
	        {
	            // get the child
	            Node child = childList.item(i);
	            
	            // parse normal text
	            if(child.getNodeType() == Node.TEXT_NODE) 
	            {
	            	tagABody = child.getTextContent();
	            	System.out.println("Normal Text: " + tagABody);
	            } 
	            	
	            // parse <b> tag
	            if (child != null && "b".equals(child.getNodeName())) 
	            {
	            	tagBBody = child.getTextContent();
	            	System.out.println("Bold Text: " + tagBBody);
	            }
	            
	            // parse <i> tag
	            if (child != null && "i".equals(child.getNodeName())) 
	            {
	            	tagIBody = child.getTextContent();
	            	System.out.println("Italic Text: " + tagIBody);
	            }
	        }		
		}
		
		// Ignore the above <b> and <i> parser and just save the whole thing.
		tempText.setTextContent(currentNodeContents);
		
		System.out.println("Text Content : " + tempText.getTextContent());
	}
	
	// Method to parse a Shape node.
	// It takes the node itself and the current slide it belongs to, and adds the contents of the node to the ShapeClass inside the current Slide.
	protected void ParseShape (SlideClass currentSlide, Node currentNode, boolean interactable)
	{
		NamedNodeMap currentNodeAttributes = currentNode.getAttributes();
		
		ShapeClass tempShape = currentSlide.CreateNewShape();
		
		for (int i = 0; i < currentNodeAttributes.getLength(); i++) 
		{
			Node reallyTempNode = currentNodeAttributes.item(i);
				
			if (reallyTempNode.getNodeName() == "starttime")
			{
				tempShape.setShapeStartTime(reallyTempNode.getNodeValue());
				System.out.println("Shape Start Time : " + tempShape.getShapeStartTime());
			}
			
			if (reallyTempNode.getNodeName() == "duration")
			{
				tempShape.setShapeDuration(reallyTempNode.getNodeValue());
				System.out.println("Shape Duration : " + tempShape.getShapeDuration());
			}
			
			if (reallyTempNode.getNodeName() == "xstart")
			{
				tempShape.setShapeXStart(reallyTempNode.getNodeValue());
				System.out.println("Shape X Start : " + tempShape.getShapeXStart());
			}
			
			if (reallyTempNode.getNodeName() == "ystart")
			{
				tempShape.setShapeYStart(reallyTempNode.getNodeValue());
				System.out.println("Shape Y Start : " + tempShape.getShapeYStart());
			}
			
			if (reallyTempNode.getNodeName() == "type")
			{
				tempShape.setShapeType(reallyTempNode.getNodeValue());
				System.out.println("Shape Type : " + tempShape.getShapeType());
			}
			
			if (reallyTempNode.getNodeName() == "width")
			{
				tempShape.setShapeWidth(reallyTempNode.getNodeValue());
				System.out.println("Shape Width : " + tempShape.getShapeWidth());
			}
			
			if (reallyTempNode.getNodeName() == "height")
			{
				tempShape.setShapeHeight(reallyTempNode.getNodeValue());
				System.out.println("Shape Height : " + tempShape.getShapeHeight());
			}
			
			if (reallyTempNode.getNodeName() == "lineColour")
			{
				tempShape.setShapeLineColour(reallyTempNode.getNodeValue());
				System.out.println("Shape Line Colour : " + tempShape.getShapeLineColour());
			}
			
			if (reallyTempNode.getNodeName() == "fillColour")
			{
				tempShape.setShapeFillColour(reallyTempNode.getNodeValue());
				System.out.println("Shape Fill Colour : " + tempShape.getShapeFillColour());
			}
		}	
	}
	
	// Method to parse a Polygon node.
	// It takes the node itself and the current slide it belongs to, and adds the contents of the node to the PolygonClass inside the current Slide.
	protected void ParsePolygon (SlideClass currentSlide, Node currentNode, boolean interactable)
	{
		NamedNodeMap currentNodeAttributes = currentNode.getAttributes();
		
		PolygonClass tempPolygon = currentSlide.CreateNewPolygon();
		
		for (int i = 0; i < currentNodeAttributes.getLength(); i++) 
		{
			Node reallyTempNode = currentNodeAttributes.item(i);
				
			if (reallyTempNode.getNodeName() == "starttime")
			{
				tempPolygon.setPolygonStartTime(reallyTempNode.getNodeValue());
				//System.out.println("Polygon Start Time : " + tempPolygon.getPolygonStartTime());
			}
			
			if (reallyTempNode.getNodeName() == "sourceFile")
			{
				tempPolygon.setPolygonSourceFile(reallyTempNode.getNodeValue());
				//System.out.println("Polygon Source File : " + tempPolygon.getPolygonSourceFile());
			}
			
			if (reallyTempNode.getNodeName() == "duration")
			{
				tempPolygon.setPolygonDuration(reallyTempNode.getNodeValue());
				//System.out.println("Polygon Duration : " + tempPolygon.getPolygonDuration());
			}
			
			if (reallyTempNode.getNodeName() == "lineColour")
			{
				tempPolygon.setPolygonLineColour(reallyTempNode.getNodeValue());
				//System.out.println("Polygon Line Colour : " + tempPolygon.getPolygonLineColour());
			}
			
			if (reallyTempNode.getNodeName() == "fillColour")
			{
				tempPolygon.setPolygonFillColour(reallyTempNode.getNodeValue());
				//System.out.println("Polygon Fill Colour : " + tempPolygon.getPolygonFillColour());
			}
		}
	}
	
	// Method to parse a Image node.
	// It takes the node itself and the current slide it belongs to, and adds the contents of the node to the ImageClass inside the current Slide.
	protected void ParseImage (SlideClass currentSlide, Node currentNode, boolean interactable)
	{
		NamedNodeMap currentNodeAttributes = currentNode.getAttributes();
		
		ImageClass tempImage = currentSlide.CreateNewImage();
		
		for (int i = 0; i < currentNodeAttributes.getLength(); i++) 
		{
			Node reallyTempNode = currentNodeAttributes.item(i);
				
			if (reallyTempNode.getNodeName() == "starttime")
			{
				tempImage.setImageStartTime(reallyTempNode.getNodeValue());
				//System.out.println("Image Start Time : " + tempImage.getImageStartTime());
			}
			
			if (reallyTempNode.getNodeName() == "sourceFile")
			{
				tempImage.setImageSourceFile(reallyTempNode.getNodeValue());
				//System.out.println("Image Source File : " + tempImage.getImageSourceFile());
			}
			
			if (reallyTempNode.getNodeName() == "duration")
			{
				tempImage.setImageDuration(reallyTempNode.getNodeValue());
				//System.out.println("Image Duration : " + tempImage.getImageDuration());
			}
			
			if (reallyTempNode.getNodeName() == "xstart")
			{
				tempImage.setImageXStart(reallyTempNode.getNodeValue());
				//System.out.println("Image X Start : " + tempImage.getImageXStart());
			}
			
			if (reallyTempNode.getNodeName() == "ystart")
			{
				tempImage.setImageYStart(reallyTempNode.getNodeValue());
				//System.out.println("Image Y Start : " + tempImage.getImageYStart());
			}
			
			if (reallyTempNode.getNodeName() == "width")
			{
				tempImage.setImageWidth(reallyTempNode.getNodeValue());
				//System.out.println("Image Width : " + tempImage.getImageWidth());
			}
			
			if (reallyTempNode.getNodeName() == "height")
			{
				tempImage.setImageHeight(reallyTempNode.getNodeValue());
				//System.out.println("Image Height : " + tempImage.getImageHeight());
			}
		}
	}
	
	// Method to parse a Video node.
	// It takes the node itself and the current slide it belongs to, and adds the contents of the node to the VideoClass inside the current Slide.
	protected void ParseVideo (SlideClass currentSlide, Node currentNode, boolean interactable)
	{
		NamedNodeMap currentNodeAttributes = currentNode.getAttributes();
		
		VideoClass tempVideo = currentSlide.CreateNewVideo();
		
		for (int i = 0; i < currentNodeAttributes.getLength(); i++) 
		{
			Node reallyTempNode = currentNodeAttributes.item(i);
				
			if (reallyTempNode.getNodeName() == "starttime")
			{
				tempVideo.setVideoStartTime(reallyTempNode.getNodeValue());
				//System.out.println("Video Start Time : " + tempVideo.getVideoStartTime());
			}
			
			if (reallyTempNode.getNodeName() == "duration")
			{
				tempVideo.setVideoDuration(reallyTempNode.getNodeValue());
				//System.out.println("Video Duration : " + tempVideo.getVideoDuration());
			}
			
			if (reallyTempNode.getNodeName() == "xstart")
			{
				tempVideo.setVideoXStart(reallyTempNode.getNodeValue());
				//System.out.println("Video X Start : " + tempVideo.getVideoXStart());
			}
			
			if (reallyTempNode.getNodeName() == "ystart")
			{
				tempVideo.setVideoYStart(reallyTempNode.getNodeValue());
				//System.out.println("Video Y Start : " + tempVideo.getVideoYStart());
			}
			
			if (reallyTempNode.getNodeName() == "sourceFile")
			{
				tempVideo.setVideoSourceFile(reallyTempNode.getNodeValue());
				//System.out.println("Video Source File : " + tempVideo.getVideoSourceFile());
			}
			
			if (reallyTempNode.getNodeName() == "loop")
			{
				tempVideo.setVideoLoop(reallyTempNode.getNodeValue());
				//System.out.println("Video Loop : " + tempVideo.getVideoLoop());
			}
		}	
	}
	
	// Method to parse a Audio node.
	// It takes the node itself and the current slide it belongs to, and adds the contents of the node to the AudioClass inside the current Slide.
	protected void ParseAudio (SlideClass currentSlide, Node currentNode, boolean interactable)
	{
		NamedNodeMap currentNodeAttributes = currentNode.getAttributes();
		
		AudioClass tempAudio = currentSlide.CreateNewAudio();
		
		for (int i = 0; i < currentNodeAttributes.getLength(); i++) 
		{
			Node reallyTempNode = currentNodeAttributes.item(i);
				
			if (reallyTempNode.getNodeName() == "starttime")
			{
				tempAudio.setAudioStartTime(reallyTempNode.getNodeValue());
				System.out.println("Audio Start Time : " + tempAudio.getAudioStartTime());
			}
			
			if (reallyTempNode.getNodeName() == "duration")
			{
				tempAudio.setAudioDuration(reallyTempNode.getNodeValue());
				System.out.println("Audio Duration : " + tempAudio.getAudioDuration());
			}
			
			if (reallyTempNode.getNodeName() == "sourceFile")
			{
				tempAudio.setAudioSourceFile(reallyTempNode.getNodeValue());
				System.out.println("Audio Source File : " + tempAudio.getAudioSourceFile());
			}
			
			if (reallyTempNode.getNodeName() == "loop")
			{
				tempAudio.setAudioLoop(reallyTempNode.getNodeValue());
				System.out.println("Audio Loop : " + tempAudio.getAudioLoop());
			}
		}
	}

	// Main method to loop within each slide and extracting everything they have.
	protected void CheckSlideContent(Node temp) 
	{
		String nodeName = temp.getNodeName();
		NamedNodeMap nodeAttributes = temp.getAttributes();
		
		switch(nodeName)
		{
			case "slide" :
				SlideClass currentSlide = presentation.CreateNewSlide();
				
				for (int i = 0; i < nodeAttributes.getLength(); i++) 
				{
					Node reallyTempNode = nodeAttributes.item(i);
						
					if (reallyTempNode.getNodeName() == "slideID")
					{
						currentSlide.setSlideID(reallyTempNode.getNodeValue());
						System.out.println("Slide no. : " + currentSlide.getSlideID());
					}
					
					if (reallyTempNode.getNodeName() == "duration")
					{
						currentSlide.setSlideDuration(reallyTempNode.getNodeValue());
						System.out.println("Slide Duration : " + currentSlide.getSlideDuration());
					}
					
					if (reallyTempNode.getNodeName() == "nextSlide")
					{
						currentSlide.setSlideNext(reallyTempNode.getNodeValue());
						System.out.println("Next Slide : " + currentSlide.getSlideNext());
					}
				}
				
				for (int count = 0; count < temp.getChildNodes().getLength(); count++) 
				{
					Node currentNode = temp.getChildNodes().item(count);
					String currentNodeName = temp.getChildNodes().item(count).getNodeName();
					String currentNodeContents = temp.getChildNodes().item(count).getTextContent();
					NamedNodeMap currentNodeAttributes = temp.getChildNodes().item(count).getAttributes();
					
					switch(currentNodeName)
					{
						case "text" :
							
							ParseText(currentSlide, currentNode, interactable);
							
						break;
						
						case "shape" :
							
							ParseShape(currentSlide, currentNode, interactable);
							
						break;
						
						case "polygon" :
							
							ParsePolygon(currentSlide, currentNode, interactable);
							
						break;
						
						case "image" :
							
							ParseImage(currentSlide, currentNode, interactable);
							
						break;
						
						case "video" :
							
							ParseVideo(currentSlide, currentNode, interactable);
							
						break;
						
						case "audio" :
							
							ParseAudio(currentSlide, currentNode, interactable);
							
						break;
						
						case "interactable" :
							
							Node currentNode2 = currentNode.getFirstChild();
							String currentNodeName2 = currentNode.getFirstChild().getNodeName();
							
							
							
							String currentNodeContents2 = currentNode.getChildNodes().item(count).getTextContent();
							NamedNodeMap currentNodeAttributes2 = currentNode.getChildNodes().item(count).getAttributes();
							
							System.out.println("THIS IS A TEST: " + currentNodeName2);
							
							switch(currentNodeName2)
							{
								case "text" :
									
									interactable = true;
									ParseText(currentSlide, currentNode2, interactable);
									interactable = false;
									
								break;
								
								case "shape" :
									
									interactable = true;
									ParseShape(currentSlide, currentNode2, interactable);
									interactable = false;
									
								break;
								
								case "polygon" :
									
									interactable = true;
									ParsePolygon(currentSlide, currentNode2, interactable);
									interactable = false;
									
								break;
								
								case "image" :
									
									interactable = true;
									ParseImage(currentSlide, currentNode2, interactable);
									interactable = false;
									
								break;
								
								case "video" :
									
									interactable = true;
									ParseVideo(currentSlide, currentNode2, interactable);
									interactable = false;
									
								break;
								
								case "audio" :
									
									interactable = true;
									ParseAudio(currentSlide, currentNode2, interactable);
									interactable = false;
									
								break;
							}
								
						break;
					}	
				}
			break;
			
			default:
				//System.out.println("-----ERROR! Something has not been parsed correctly!");
			break;
		}
	}
}