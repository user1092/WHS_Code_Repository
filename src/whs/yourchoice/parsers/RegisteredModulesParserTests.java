package whs.yourchoice.parsers;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import whs.yourchoice.presentation.RegisteredModuleEntry;


public class RegisteredModulesParserTests 
{
	/**
	* ExtractPresentationInfo Test. Parses the XML file and asserts if the both the "default" and "documentInfo" tags were parsed and stored successfully.
	*/
	@Test
	public void ExtractPresentationInfo()
	{		
		Path path = Paths.get("/src/registered_modules.xml");
		String filePath = path.toString();
		RegisteredModulesParser parser = new RegisteredModulesParser();		
		RegisteredModuleEntry module = parser.parseModules(filePath);
		
		assertEquals("ELE000034", module.getCode());
		assertEquals("Electronics", module.getCourse());
		assertEquals("Electronics and Computer Engineering", module.getStream());
		assertEquals(2016, module.getYear());
		assertEquals("Checking Title", module.getTitle());
		assertEquals("ele000034.zip", module.getFileName());
	}
	
}

