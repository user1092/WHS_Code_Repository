package whs.yourchoice.presentation;

import java.util.LinkedList;
import java.util.List;

/**
 * Class that stores, searches and removes modules in a list
 * 
 * @author Will Sharrard & Sabrina Quinn
 * 
 * @version 0.1 20/04/16
 *
 */
public class RegisteredModules {
	// debating - protected or private?
	//private ArrayList<RegisteredModuleEntry> moduleList = new ArrayList<RegisteredModuleEntry>();
	private List<RegisteredModuleEntry> moduleList = new LinkedList<RegisteredModuleEntry>();
	
	/**
	 * Adds a module to module list
	 * @param module Module to be added to list
	 */
	public void addModule(RegisteredModuleEntry module) {
		RegisteredModuleEntry addModule = new RegisteredModuleEntry();
		addModule = module;
		System.out.println("Adding: " + addModule.getCode());
		moduleList.add(addModule);
	}
	
	/**
	 * Searches and returns a module by module code
	 * @param code String module code name
	 * @return module Returns module found, if none is found returns null
	 */
	public RegisteredModuleEntry searchModuleCode(String code) {
		RegisteredModuleEntry returnModule = null;
		
		//currently can't handle modules with same code
		//probably will just return the first one with same code
		for (int i=0; i<moduleList.size(); i++) {
			System.out.println(moduleList.get(i).getCode());
			if(moduleList.get(i).getCode().equals(code)) {
				returnModule = moduleList.get(i);
				break;
			}
		}
		return returnModule;
	}
}
