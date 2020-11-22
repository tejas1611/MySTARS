package Control;

import Entity.*;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.GregorianCalendar;


@SuppressWarnings("resource")
public class AdminControl {
	public static void editStudentAccessPeriod(int startYear, int startMonth, int startDay, int startHours, int startMin,
												int endYear, int endMonth, int endDay, int endHours, int endMin) {
	   	Student.setAccessStart(new GregorianCalendar(startYear, startMonth-1, startDay, startHours, startMin));
	   	Student.setAccessEnd(new GregorianCalendar(endYear, endMonth-1, endDay, endHours, endMin));
	   	
	   	System.out.println("Access Time have been changed to: " + Student.printaccessStart() + " to " + Student.printaccessEnd());
	   	System.out.print("\n\nPress enter to continue....");
    	new Scanner(System.in).nextLine();
	}
	
    public static void addStudent(String id, String studentName, String email, Password password, String gender, String nationality,
			int matricNo, String school, String program, int yearOfStudy) {
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
		
		// Write to file and print all students
		System.out.println("\nStudents currently in database:");
		studentDB.add(newStudent);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    	for(Student s : studentDB) {
    		s.printInfo();
    	}
    	System.out.print("Total: " + studentDB.size());
    	
    	System.out.print("\n\nPress enter to continue....");
    	new Scanner(System.in).nextLine();
    }
    
    @SuppressWarnings("unchecked")
    public static void addCourse(String courseCode, String courseName, String school, String courseType, int au) {
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
    	System.out.print("Total: " + courseDB.size());
    	
    	System.out.print("\n\nPress enter to continue....");
    	new Scanner(System.in).nextLine();
    }
       
    public static boolean checkCourseIndex(String courseCode, int indexNumber) {
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
	public static void updateCourseVacancy(String courseCode, int indexNumber, int vacancy) {
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
					try {
						System.out.println("Sending Confirmation...");
						SendMailTLS.sendMail(student.getEmail(), student, newCourse, 1);
					} catch (Exception e) {
						System.out.print("Unable to send an E-mail.");
					}
					try {
						i.addStudent(student);
					} catch (Exception e) {
						System.out.print("Error Encountered");
					}
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
	    		s.addCourse(newCourse, s.findIndex(oldCourse));
	    		s.removeCourse(oldCourse);
	    		studentDB.set(index, s);
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("courseDB", courseDB);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }
    
    @SuppressWarnings("unchecked")
	public static void updateCourseIndex(String courseCode, int indexNumber, int newIndex) {
        Course course = CourseControl.findCourse(courseCode);
		if (course == null) {
			System.out.println("Error: Course not found");
            return;
		}
        if(!checkCourseIndex(courseCode, indexNumber)){
			System.out.println("This index number does not exist!");
			return;
		}
        
        // Search correct index and update inside newCourse object
        ArrayList<IndexNumber> indexNumbers = course.getIndexes();
        IndexNumber oldIndexNumber = indexNumbers.get(0);
        for(int i=0; i<indexNumbers.size(); i++) {
        	oldIndexNumber = indexNumbers.get(i);
        	if(oldIndexNumber.getIndexNum() == indexNumber) {
        		oldIndexNumber.setIndexNum(newIndex);
        		indexNumbers.set(i, oldIndexNumber);
        		break;
        	}
        }
        course.setIndexes(indexNumbers);
        DatabaseControl.updateInFile(course);
		
		// Update students and database
    	List<Integer> studentsInIndex = oldIndexNumber.getStudents();
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(studentsInIndex.contains(Integer.valueOf(s.getMatricNo()))) {
    			s.removeCourse(course);
	    		s.addCourse(course, oldIndexNumber);
	    		studentDB.set(index, s);
    		}
    	}
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }

    @SuppressWarnings("unchecked")
	public static void updateCourseCode(String courseCode, String newCourseCode) {
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
    		if(c.getCourseName().equals(oldCourse.getCourseName())) {
    			courseDB.set(index, newCourse);
    			break;
    		}
    	}
    	
    	// Update students and database
    	ArrayList<Integer> studentsInCourse = newCourse.getStudents();
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(studentsInCourse.contains(Integer.valueOf(s.getMatricNo()))) {
	    		IndexNumber toAdd = s.findIndex(oldCourse);
	    		s.removeCourse(oldCourse);
    			s.addCourse(newCourse, toAdd);
	    		studentDB.set(index, s);
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("courseDB", courseDB);
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }

    @SuppressWarnings("unchecked")
	public static void updateSchool(String courseCode, String setSchool) {
        Course course = CourseControl.findCourse(courseCode);
		if (course == null) {
			System.out.println("Error: Course not found");
            return;
		}
		course.setSchool(setSchool);

		// Update in course database
		DatabaseControl.updateInFile(course);
		
		// Update students and database
    	ArrayList<Integer> studentsInCourse = course.getStudents();
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(studentsInCourse.contains(Integer.valueOf(s.getMatricNo()))) {
	    		IndexNumber toAdd = s.findIndex(course);
	    		s.removeCourse(course);
    			s.addCourse(course, toAdd);
	    		studentDB.set(index, s);
    		}
    	}
    	
    	DatabaseControl.writeSerializedObject("studentDB", studentDB);
    }

    @SuppressWarnings("unchecked")
	public static void addCourseIndex(String courseCode, int indexNumber, String tutorialGroup, int capacity) {
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

    
    public static void printStudentListByIndexNumber(String courseCode, int IndexNumber)
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
    
    public static void printStudentListByCourse(String courseCode)
    {
    	Course course = CourseControl.findCourse(courseCode);
		if (course == null) {
			System.out.println("Error: Course not found");
			return;
		}
    	ArrayList<Student> students = null;
    	students = CourseControl.getStudents(course);
    	
    	if(students==null) {
    		System.out.println("There are no students in this course!");
    		return;
    	}
    	
        System.out.println("List of students in the course " + courseCode + ":\n"); 
        System.out.println("========================================================");
        System.out.println("|         Name         |  Gender  |     Nationality    |");
        System.out.println("========================================================");           
        for (int i = 0; i < students.size(); i++) {
            Student s = (Student) students.get(i);
            String name = s.getName();
            String gender = s.getGender();
            String nationality = s.getNationality();
            System.out.format("| %-21s| %-9s| %-19s|\n",name,gender,nationality);
        }
        System.out.println("========================================================");  
        System.out.println("Number of students in this course: " + students.size());
    }
    
    @SuppressWarnings("unchecked")
	public static void grantPermissionForOverloading(int matricNumber) {
    	boolean flag = false;
    	List<Student> studentDB = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
    	for(int index=0; index<studentDB.size(); index++) {
    		Student s = studentDB.get(index);
    		if(s.getMatricNo() == matricNumber) {
    			flag=true;
    			System.out.print("Student Found: "); s.printInfo();
    			System.out.print("\nPress Y to confirm ");
    			Scanner scanner = new Scanner(System.in);
    			if(!scanner.nextLine().toLowerCase().equals("y"))
    				return;
    			s.setOverloadPermission(true);
    			DatabaseControl.updateInFile(s);
    			return;
    		}
    	}
    	
    	if(flag==false) System.out.println("Matric number not found.");
    }

}