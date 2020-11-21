package Entity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Student extends Person {

	private static final long serialVersionUID = 1L;
	private static Calendar accessStart = new GregorianCalendar(2020, 11, 15, 0, 0);
	private static Calendar accessEnd = new GregorianCalendar(2020, 11, 30, 23, 59);
	
	private int matricNo;
	private int AULimit;
	private String school;
	private int yearOfStudy;
	private HashMap<Course, IndexNumber> courses;
	private HashMap<Course, IndexNumber> waitlist;
	private boolean overloadPermission;
	
	private void setDefaultAULimit() {
		if(school.equals("SCSE"))
			AULimit = 22;
		else if(school.equals("NBS"))
			AULimit = 19;
		else if(school.equals("SPMS"))
			AULimit = 21;
		else
			AULimit = 22;
	}
	
	public Student(String id, String name, String email, Password password, String gender, String nationality,
			int matricNo, String school, String program, int yearOfStudy) {
		
		super(id, name, email, password, gender, nationality);
		this.matricNo = matricNo;
		this.school = school;
		this.yearOfStudy = yearOfStudy;
		this.courses = new HashMap<Course, IndexNumber>();
		this.waitlist = new HashMap<Course, IndexNumber>();
		this.overloadPermission = false;
		setDefaultAULimit();
	}
	
	public String getNationality() { return nationality; }
	public String getGender() { return gender; }
	
	public void addCourse(Course course, IndexNumber index) { courses.put(course, index); }
	public void removeCourse(Course course)  { courses.remove(course); }
	
	public void addWaitlist(Course course, IndexNumber index) { waitlist.put(course, index); }
	public void removeWaitlist(Course course) { waitlist.remove(course); }
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Student)
			return ((Student) o).getId()==this.id;
		else
			return false;
	}
	
	public int getTotalAU() {
		int totalAU = 0;
		for(Course c : this.courses.keySet())
			totalAU = totalAU + c.getAu();
		return totalAU;
	}
	
	public int getAULimit() { return AULimit; }

	public int getNumberWaitlist() { return this.waitlist.size(); }
	
	public HashMap<Course, IndexNumber> getCourses() { return courses; }
	
	public IndexNumber getIndexofCourse(Course courseReq) { return courses.get(courseReq); }
	public void setIndexofCourse(Course course, int index){
		courses.get(course).setIndexNum(index);
	}
	
	public int getMatricNo() { return matricNo; }
	public void setMatricNo(int matricNo) { this.matricNo = matricNo; }

	public int getYearOfStudy() { return yearOfStudy; }
	public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
	
	public void setOverloadPermission(boolean overload) { this.overloadPermission = overload; }
	public boolean isOverloadPermission() { return overloadPermission; }
	
	public static Calendar getAccessStart() { return accessStart; }
	public static void setAccessStart(Calendar accessStart) { Student.accessStart = accessStart; }

	public static Calendar getAccessEnd() {	return accessEnd; }
	public static void setAccessEnd(Calendar accessEnd) { Student.accessEnd = accessEnd; }

	public String getSchool() { return school; }
	
}
