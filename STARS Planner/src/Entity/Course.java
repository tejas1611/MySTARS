package Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String courseCode;
	private String courseName;
	private String school;
	private String courseType;
	private int au;
	private ArrayList<IndexNumber> indexes = new ArrayList<IndexNumber>();
	
	public Course(String courseCode, String courseName, String school, String courseType, int au) {
		this.courseCode=courseCode;
		this.courseName=courseName;
		this.school=school;
		this.courseType=courseType;

		this.au=au;
		this.indexes = null;
	}
	
	public Course(Course another) {
		this.au = another.getAu();
		this.courseCode = another.getCourseCode();
		this.courseName = another.getCourseName();
		this.courseType = another.courseType;
		this.indexes = another.getIndexes();
		this.school = another.getSchool();
	}
	
	public String printName() { return (courseCode + ": " + courseName); }
	
	public ArrayList<Integer> getStudents() {
		ArrayList<Integer> studentID = new ArrayList<Integer>();
    	for(IndexNumber i : indexes) {
    		for(Integer id : i.getStudents())
    			studentID.add(id);
    	}
    	return studentID;
	}
	
	public int getAu() { return au; }
	public void setAu(int au) { this.au = au; }
	
	public void addIndex (IndexNumber ind) { indexes.add(ind); }
	public ArrayList<IndexNumber> getIndexes() { return indexes; }
	public void setIndexes(ArrayList<IndexNumber> indexes) { this.indexes = indexes; }
	
	public String getSchool() { return school; }
	public void setSchool(String school) { this.school = school;}
	
	public String getCourseType() { return courseType; }
	public void setCourseType(String courseType) { this.courseType = courseType; }
	
	public String getCourseCode() { return courseCode; }	public void setCourseCode(String courseCode) { this.courseCode=courseCode; }
	
	public String getCourseName() { return courseName; }
	public void setCourseName(String courseName) { this.courseName=courseName; }
	
	public int getAU(){ return au; }
	
	public void printInfo() {
		System.out.println(courseCode + ": " + courseName + " - " + courseType + ", AU=" + au);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Course)
			return ((Course) o).getCourseCode()==this.courseCode;
		else
			return false;
	}
}
