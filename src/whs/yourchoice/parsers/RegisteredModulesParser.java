package whs.yourchoice.parsers;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import whs.yourchoice.presentation.RegisteredModuleEntry;
import whs.yourchoice.presentation.RegisteredModules;

/**
* Registered Modules Parser
* Latest Update: 18/04/16
*
* Copyright and Licensing Information if applicable
*/

/**
 * A class to parse a registered_modules.xml file
 * 
 * @author Will Sharrard & Sabrina Quinn
 * 
 * @version 0.1 18/04/16
 */
public class RegisteredModulesParser {
	private RegisteredModuleEntry module = new RegisteredModuleEntry();
	private RegisteredModules moduleList  = new RegisteredModules();
	
	/**
	 * Extracts information from a module list XML file
	 * @param filePath Path to XML file
	 * @return RegisteredModuleEntry object containing all extracted information
	 */
	public RegisteredModules parseModules(String filePath) {
		try 
		{
			File file = new File(filePath);

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = dBuilder.parse(file);
			
			doc.getDocumentElement().normalize();

			System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
			
			continueParsingForChildren(doc.getChildNodes());
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		module = null;
		return moduleList;
	}
	
	/**
	 * Parses XML file to extract all expected fields
	 * @param currentNodeList Extracted node list from file
	 */
	private void continueParsingForChildren(NodeList currentNodeList) 
	{
		RegisteredModuleEntry addModule = new RegisteredModuleEntry();
		for (int count = 0; count < currentNodeList.getLength(); count++) 
		{
			Node currentNodeElement = currentNodeList.item(count);
			
			if (currentNodeElement.getNodeType() == Node.ELEMENT_NODE) 
			{
				CheckModuleContent(currentNodeElement);

				if (currentNodeElement.hasChildNodes()) 
				{
					continueParsingForChildren(currentNodeElement.getChildNodes());
				}
			}
			
			if ((currentNodeElement.getNodeName() == "module") && (module != null)) {
				addModule = module;
				addToModuleList(addModule);
			}
		}
	}
	
	/**
	 * Takes node and checks for relevent module information and stores it in
	 * a RegisteredModuleEntry object
	 * @param temporaryNode Node to extract information from
	 */
	private void CheckModuleContent(Node temporaryNode)
	{
		String temporaryNodeName = temporaryNode.getNodeName();
		String temporaryNodeTextContent = temporaryNode.getTextContent();
		
		switch(temporaryNodeName)
		{
			case "code" :
				if (temporaryNode.getParentNode().getNodeName() == "module")
				{
					module.setCode(temporaryNodeTextContent);				
				}			
			break;
				
			case "course" :
				if (temporaryNode.getParentNode().getNodeName() == "module")
				{
					module.setCourse(temporaryNodeTextContent);				
				}			
			break;
				
			case "stream" :
				if (temporaryNode.getParentNode().getNodeName() == "module")
				{
					module.setStream(temporaryNodeTextContent);					
				}			
			break;
			
			case "year" :
				if (temporaryNode.getParentNode().getNodeName() == "module")
				{
					module.setYear(Integer.parseInt(temporaryNodeTextContent));					
				}			
			break;
			
			case "title" :
				if (temporaryNode.getParentNode().getNodeName() == "module")
				{
					module.setTitle(temporaryNodeTextContent);				
				}			
			break;
			
			case "filename" :
				if (temporaryNode.getParentNode().getNodeName() == "module")
				{
					module.setFileName(temporaryNodeTextContent);					
				}			
			break;
			
			default:
				
				// Some sort of error message here.
				
			break;		
		}
	}
	
	private void addToModuleList(RegisteredModuleEntry addModule) {
		//RegisteredModuleEntry addModule = new RegisteredModuleEntry();
		//addModule = module;
		moduleList.addModule(addModule);
		module = null;
	}

}
