package whs.yourchoice.presentation;
/**
 * Registered Modules Object
 * 
 * Copyright and Licensing Information if applicable
 */

import java.util.LinkedList;
import java.util.List;

/**
 * Class that stores, searches and removes modules in a list
 * 
 * @author ws659 skq501
 * 
 * @version 0.1 20/04/16
 *
 */
public class RegisteredModules {
	private List<RegisteredModuleEntry> moduleList = new LinkedList<RegisteredModuleEntry>();
	
	/**
	 * Adds a module to module list
	 * @param module Module to be added to list
	 */
	public void addModule(RegisteredModuleEntry module) {
		moduleList.add(module);
	}
	
	/**
	 * Searches and returns a module by module code
	 * @param code String module code name
	 * @return module Returns module found, if none is found returns null
	 */
	public RegisteredModuleEntry searchModuleCode(String code) {
		RegisteredModuleEntry returnModule = null;
		
		//search each module individually until module found
		//if no module is found returns a null object
		for (int i=0; i<moduleList.size(); i++) {
			if(moduleList.get(i).getCode().equals(code)) {
				returnModule = moduleList.get(i);
				break;
			}
		}
		return returnModule;
	}
	
	/**
	 * Method to return size of list
	 * @return int moduleList size
	 */
	public int getListSize(){
		return moduleList.size();
	}
	
	/**
	 * Returns list of modules
	 * @return List moduleList
	 */
	public List<String> getAllModules() {
		List<String> moduleNames = new LinkedList<String>();
		for (int i=0; i<moduleList.size(); i++) {
			moduleNames.add(moduleList.get(i).getTitle());
		}
		
		return moduleNames;
	}
	
	/**
	 * Returns list of streams
	 * @return List streamList
	 */
	public List<String> getAllStreams() {
		List<String> streamList = new LinkedList<String>();
		String stream;
		Boolean found = false;
		
		for (int i=0; i<moduleList.size(); i++) {
			stream = moduleList.get(i).getStream();
			if (streamList.size() < 1) {
				streamList.add(stream);
			}
			for (int j=0; j<streamList.size(); j++) {
				if (stream.equals(streamList.get(j))) {
					found = true;
				}
			}
			if (found == false) {
				streamList.add(stream);
			}
			found = false;
		}
		return streamList;
	}
	
	/**
	 * Returns list of academic year
	 * @return List yearList
	 */
	public List<String> getAllYears() {
		List<String> yearList = new LinkedList<String>();
		String year;
		Boolean found = false;
		
		for (int i=0; i<moduleList.size(); i++) {
			year = Integer.toString(moduleList.get(i).getYear());
			if (yearList.size() < 1) {
				yearList.add(year);
			}
			for (int j=0; j<yearList.size(); j++) {
				if (year.equals(yearList.get(j))) {
					found = true;
				}
			}
			if (found == false) {
				yearList.add(year);
			}
			found = false;
		}
		return yearList;
	}
	
	/**
	 * Returns list of academic courses
	 * @return List courseList
	 */
	public List<String> getAllCourses() {
		List<String> courseList = new LinkedList<String>();
		String course;
		Boolean found = false;
		
		for (int i=0; i<moduleList.size(); i++) {
			course = moduleList.get(i).getCourse();
			if (courseList.size() < 1) {
				courseList.add(course);
			}
			for (int j=0; j<courseList.size(); j++) {
				if (course.equals(courseList.get(j))) {
					found = true;
				}
			}
			if (found == false) {
				courseList.add(course);
			}
			found = false;
		}
		return courseList;
	}
}
