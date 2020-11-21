package Control;

import java.util.ArrayList;
import Entity.*;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;

public class AdminControl {
    
	public void editStudentAccessPeriod(int startYear, int startMonth, int startDay, int startHours, int startMin,
												int endYear, int endMonth, int endDay, int endHours, int endMin) {
	   	Student.setAccessStart(new GregorianCalendar(startYear, startMonth, startDay, startHours, startMonth));
	   	Student.setAccessEnd(new GregorianCalendar(endYear, endMonth, endDay, endHours, endMonth));
	}
	
    public void addStudent(String id, String studentName, String email, Password password, String gender, String nationality,
			int matricNo, School school, String program, int yearOfStudy) {
        Student newStudent = new Student(id, studentName, email, password, gender, nationality, matricNo, school, program, yearOfStudy);
        
        // Read student from database		
		@SuppressWarnings("unchecked")
		List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
		
		// Check if student already exists
		for(Student s : studentDB) {
    		if(newStudent.equals(s)) {
    			System.out.println("Student already exists.");
    			return;
    		}
    	}
		
		// Write to file and print all courses
		studentDB.add(newStudent);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    	for(Student s : studentDB) {
    		s.printInfo();
    	}
    }
    
    @SuppressWarnings("unchecked")
    public void addCourse(String courseCode, String courseName, String school, String courseType, int au) {
    	Course newCourse = new Course(courseCode, courseName, school, courseType, au);
    	
    	// Read from file
		List<Course> courseDB = (List<Course>) DatabaseControl.readSerializedObject("courseDB");
    	
    	// Check if course already exists
    	Course course = CourseControl.findCourse(courseCode);
		if (course != null) {
			System.out.println("Course already exists.");
			return;
		}
		
    	// Write to file and print all courses
		courseDB.add(newCourse);
    	DatabaseControl.writeSerializedObject("courseDB", courseDB);
    	for(Course c : courseDB) {
    		c.printInfo();
    	}
    }
       
    public boolean checkCourseIndex(String courseCode, int indexNumber) {
    	Course course = CourseControl.findCourse(courseCode);
		if (course == null) {
			System.out.println("Error: Course not found");
			return false;
		}
        for (IndexNumber i: course.getIndexes()){
            if (i.getIndexNum()==indexNumber) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
	public void updateCourseVacancy(String courseCode, int indexNumber, int vacancy) throws Exception {
    	// TODO update course vacancy
        int waitlistLength=0;
		int availableSpots=0;
		Course oldCourse = CourseControl.findCourse(courseCode);
		if (oldCourse == null) {
			System.out.println("Error: Course not found");
            return;
		}
		Course newCourse= new Course(oldCourse);
        if(!checkCourseIndex(courseCode, indexNumber)){
			System.out.println("This index number does not exist!");
			return;
        }
		for(IndexNumber i:newCourse.getIndexes()) {
			if(i.getIndexNum()==indexNumber) {
				i.setVacancy(vacancy);

				availableSpots=vacancy-(i.getVacancy());
				waitlistLength=i.getWaitlistLength();

				while(availableSpots!=0 && waitlistLength!=0){

					Student student =null;
					try {
						student = i.findNextInWaitlist();
					} catch (Exception e) {
						System.out.println("No students on waitlist.");
					}
					student.removeWaitlist(newCourse);
					i.addStudent(student);
					availableSpots-=1;
					waitlistLength-=1;
				}
			}
		}

		List<Course> courseDB = (List<Course>) DatabaseControl.readSerializedObject("courseDB");
		for(int index=0; index<courseDB.size(); index++) {
			Course c = courseDB.get(index);
			if(c.equals(oldCourse)) {
				courseDB.remove(index);
				courseDB.add(index, newCourse);
				break;
			}
        }
		// Update students and database
    	List<Student> studentsInCourse = CourseControl.getStudents(newCourse);
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(studentsInCourse.contains(s)) {
	    		s.addCourse(newCourse, s.getIndexofCourse(oldCourse));
	    		s.removeCourse(oldCourse);
	    		studentDB.set(index, s);
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("courseDB", courseDB);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }
    
    @SuppressWarnings("unchecked")
	public void updateCourseIndex(String courseCode, int indexNumber, int newIndex) {
        Course oldCourse = CourseControl.findCourse(courseCode);
		if (oldCourse == null) {
			System.out.println("Error: Course not found");
            return;
		}
		Course newCourse = new Course(oldCourse);
        if(!checkCourseIndex(courseCode, indexNumber)){
			System.out.println("This index number does not exist!");
			return;
		}
        
        // Search correct index and update inside newCourse object
        IndexNumber newIndexNumber = null;
        ArrayList<IndexNumber> indexNumbers = newCourse.getIndexes();
        for(int i=0; i<indexNumbers.size(); i++) {
        	IndexNumber oldIndexNumber = indexNumbers.get(i);
        	if(oldIndexNumber.getIndexNum() == indexNumber) {
        		newIndexNumber = new IndexNumber(oldIndexNumber);
        		newIndexNumber.setIndexNum(newIndex);
        		indexNumbers.set(i, newIndexNumber);
        		break;
        	}
        }
        newCourse.setIndexes(indexNumbers);
        
        // Update in course database
		List<Course> courseDB = (List<Course>) DatabaseControl.readSerializedObject("courseDB");
		for(int index=0; index<courseDB.size(); index++) {
			Course c = courseDB.get(index);
			if(c.equals(oldCourse)) {
				courseDB.remove(index);
				courseDB.add(index, newCourse);
				break;
			}
        }
		
		// Update students and database
    	List<Student> studentsInIndex = CourseControl.getStudents(newIndexNumber);
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(studentsInIndex.contains(s)) {
	    		s.addCourse(newCourse, s.getIndexofCourse(oldCourse));
	    		s.removeCourse(oldCourse);
	    		studentDB.set(index, s);
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("courseDB", courseDB);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }

    @SuppressWarnings("unchecked")
	public void updateCourseCode(String courseCode, String newCourseCode) {
        Course oldCourse = CourseControl.findCourse(courseCode);
		if (oldCourse == null) {
			System.out.println("Error: Course not found");
            return;
		}
        Course newCourse = new Course(oldCourse);
		newCourse.setCourseCode(newCourseCode);
        
		// Update in course database
		List<Course> courseDB = (List<Course>) DatabaseControl.readSerializedObject("courseDB");
    	for(int index=0; index<courseDB.size(); index++) {
    		Course c = courseDB.get(index);
    		if(c.equals(oldCourse)) {
    			courseDB.remove(index);
    			courseDB.add(index, newCourse);
    			break;
    		}
    	}
    	
    	// Update students and database
    	List<Student> studentsInCourse = CourseControl.getStudents(newCourse);
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(studentsInCourse.contains(s)) {
	    		s.addCourse(newCourse, s.getIndexofCourse(oldCourse));
	    		s.removeCourse(oldCourse);
	    		studentDB.set(index, s);
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("courseDB", courseDB);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }

    @SuppressWarnings("unchecked")
	public void updateSchool(String courseCode, String setSchool) {
        Course oldCourse = CourseControl.findCourse(courseCode);
		if (oldCourse == null) {
			System.out.println("Error: Course not found");
            return;
		}
		Course newCourse = new Course(oldCourse);
		newCourse.setSchool(setSchool);

		// Update in course database
		List<Course> courseDB = (List<Course>) DatabaseControl.readSerializedObject("courseDB");
		for(int index=0; index<courseDB.size(); index++) {
			Course c = courseDB.get(index);
			if(c.equals(oldCourse)) {
				courseDB.remove(index);
				courseDB.add(index, newCourse);
				break;
			}
        }
		
		// Update students and database
    	List<Student> studentsInCourse = CourseControl.getStudents(newCourse);
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(studentsInCourse.contains(s)) {
	    		s.addCourse(newCourse, s.getIndexofCourse(oldCourse));
	    		s.removeCourse(oldCourse);
	    		studentDB.set(index, s);
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("courseDB", courseDB);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }

    @SuppressWarnings("unchecked")
	public void addCourseIndex(String courseCode, int indexNumber, String tutorialGroup, int capacity) {
        Course oldCourse = CourseControl.findCourse(courseCode);
		if (oldCourse == null) {
			System.out.println("Error: Course not found");
            return;
		}
		Course newCourse = new Course(oldCourse);
        if(checkCourseIndex(courseCode, indexNumber)){
			System.out.println("Index already exists!");
			return;
		}
		
		IndexNumber newIndex = new IndexNumber(indexNumber,tutorialGroup,capacity);
		newCourse.addIndex(newIndex);

		List<Course> courseDB = (List<Course>) DatabaseControl.readSerializedObject("courseDB");
		for(int index=0; index<courseDB.size(); index++) {
			Course c = courseDB.get(index);
			if(c.equals(oldCourse)) {
				courseDB.remove(index);
				courseDB.add(index, newCourse);
				break;
			}
        }

		DatabaseControl.writeSerializedObject("courseDB", courseDB);
    }

    
    public void printStudentListByIndexNumber(String courseCode, int IndexNumber)
     {
    	Course course = CourseControl.findCourse(courseCode);
		if (course == null) {
			System.out.println("Error: Course not found");
			return;
		}
    	ArrayList<Student> students = new ArrayList<Student>();
    	
    	for(IndexNumber ind : course.getIndexes()) {
    		if(ind.getIndexNum() == IndexNumber)
    			students = CourseControl.getStudents(ind);
    	}
    	
    	if(students.size()<=0) {
    		System.out.println("There are no students in this index number!");
    		return;
    	}
    	
        System.out.println("List of students in this Index Number " + IndexNumber + ":\n"); 
        System.out.println("========================================================");
        System.out.println("|         Name         |  Gender  |     Nationality    |");
        System.out.println("========================================================");           
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            String name = student.getName();
            String gender = student.getGender();
            String nationality = student.getNationality();
            System.out.format("| %-21s| %-9s| %-19s|\n",name,gender,nationality);
        }
        System.out.println("========================================================");
        System.out.println("Number of students in this index group: " + students.size());
    }
    
    public void printStudentListByCourse(String courseCode)
    {
    	Course course = CourseControl.findCourse(courseCode);
		if (course == null) {
			System.out.println("Error: Course not found");
			return;
		}
    	ArrayList<Student> students = new ArrayList<Student>();
    	students = CourseControl.getStudents(course);
    	
    	if(students.size()<=0) {
    		System.out.println("There are no students in this course!");
    		return;
    	}
    	
        System.out.println("List of students in the course " + courseCode + ":\n"); 
        System.out.println("========================================================");
        System.out.println("|         Name         |  Gender  |     Nationality    |");
        System.out.println("========================================================");           
        for (int i = 0; i < students.size(); i++) {
            Student student = (Student) students.get(i);
            String name = student.getName();
            String gender = student.getGender();
            String nationality = student.getNationality();
            System.out.format("| %-21s| %-9s| %-19s|\n",name,gender,nationality);
        }
        System.out.println("========================================================");  
        System.out.println("Number of students in this course: " + students.size());
    }
    
    @SuppressWarnings("unchecked")
	public void grantPermissionForOverloading(String studentName, int matricNumber) {
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(s.getMatricNo() == matricNumber) {
    			s.setOverloadPermission(true);
    			studentDB.set(index, s);
    			break;
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }

}