package Control;

import java.util.ArrayList;
import java.util.List;

import Entity.Course;
import Entity.IndexNumber;
import Entity.Student;

public class CourseControl {

	public static Course findCourse(String courseCode) {
		Course course = null;
		
		// Read courses from database
		@SuppressWarnings("unchecked")
		List<Course> courseDB = (List<Course>) DatabaseControl.readSerializedObject("courseDB");
		
		// Compare course code
		for(Course c : courseDB) {
    		if(c.getCourseCode().equals(courseCode)) {
    			course = c;
    		}
    	}
		
		return course;
	}	
	
    public static int getVacancy(String courseCode, int indexNumber) {
    	Course course;
		try {
			course = CourseControl.findCourse(courseCode);
		} catch (Exception e) {
			return -1;
		}
		
        for (IndexNumber i: course.getIndexes()){
            if (i.getIndexNum()==indexNumber){
                return i.getVacancy();
            }
        }
        return -1;
    }
    
    public static ArrayList<Student> getStudents(Object o) {
    	ArrayList<Integer> studentsID = null;
    	if(o instanceof Course)
    		studentsID = ((Course) o).getStudents();
    	else if(o instanceof IndexNumber)
    		studentsID = ((IndexNumber) o).getStudents();
    	ArrayList<Student> students = new ArrayList<Student>();
    	
    	// Read students from database
		@SuppressWarnings("unchecked")
		List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
		
		// Create students list
		for(Student s : studentDB) {
			if(studentsID.contains(s.getMatricNo())) {
				students.add(s);
			}
		}
		return students;
    }
}
