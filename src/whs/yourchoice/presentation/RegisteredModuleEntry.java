package whs.yourchoice.presentation;

public class RegisteredModuleEntry {
	private String code;
	private String course;
	private String stream;
	private int year;
	private String title;
	private String fileName;
	
	public RegisteredModuleEntry(String code,
								String course,
								String stream,
								int year,
								String title,
								String fileName) {
		
		this.code = code;
		this.course = course;
		this.stream = stream;
		this.year = year;
		this.title = title;
		this.fileName = fileName;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
	
	public String getCourse() {
		return this.course;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	
	public String getStream() {
		return this.stream;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getYear() {
		return this.year;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return this.fileName;
	}

}
