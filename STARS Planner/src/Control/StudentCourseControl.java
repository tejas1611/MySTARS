package Control;

import java.util.ArrayList;
import java.util.HashMap;
import Entity.Course;
import Entity.IndexNumber;
import Entity.Lesson;
import Entity.Student;

public class StudentCourseControl {
	
    public static void addCourse(Student student, String courseID, int index) throws Exception {
    	boolean valid = false;
    	
    	Course course = CourseControl.findCourse(courseID);
    	if(course==null) {
    		throw new Exception("Course not found.");
    	}
    	// Check AU limit for student
    	int totalAU = student.getTotalAU();
		if(totalAU+course.getAu() > student.getAULimit() && !student.isOverloadPermission()) {
			throw new Exception("Student not allowed to overload.");
		}
		
		// Check if Course already registered, or invalid Index Number
		int i;
		ArrayList<IndexNumber> indices = course.getIndexes();
		
		if(student.getCourses().containsKey(course))
			throw new Exception("Student is already enrolled in course " + course.printName());
		else {
			for(i=0;i<indices.size();i++)
				if(indices.get(i).getIndexNum() == index) {
					valid = true;
					break;
				}
			if(!valid) throw new Exception("Index Number not found in course");
		}
		
		// Check for timetable clash
		if(timeClashBetweenModules(student, course, indices.get(i))) valid = false;
		
		// Check if vacancy available
		boolean vacancyAvailable = true;
		if(indices.get(i).getVacancy() <= 0) vacancyAvailable = false;
		
		if(vacancyAvailable && valid) {
			student.addCourse(course, indices.get(i));
			indices.get(i).addStudent(student);
			System.out.println("Course successfully registered");
		}
		else {
			if(student.getNumberWaitlist() < 5) {
				student.addWaitlist(course, indices.get(i));
				indices.get(i).putInWaitlist(student);
				System.out.println("No vacancy available. Added to waitlist.");
			}
			else {
				System.out.println("Unable to register course.");
				System.out.println("No vacancy available. Course waitlist limit (5) reached.");
			}
		}
		try {
			NotifyStudent.notifyEmail(student,course,0);
		} catch (Exception e) {
			System.out.print("Unable to send an E-mail.");
		}
		return;
    }

    public static void dropCourse(Student student, String courseID) throws Exception {
    	// Check if student enrolled in course
		Course course = CourseControl.findCourse(courseID);
    	if(course==null) {
    		throw new Exception("Course not found.");
    	}
    	if(!student.getCourses().containsKey(course))
			throw new Exception("Student is not enrolled in course " + course.getCourseCode() + ": " + course.getCourseName());
		else {
			IndexNumber index = student.getCourses().get(course);
			student.removeCourse(course);
			Student studentToReg = index.findNextInWaitlist();
			addCourse(studentToReg, courseID, index.getIndexNum());
		}
		try {
			NotifyStudent.notifyEmail(student,course,2);
		} catch (Exception e) {
			System.out.print("Unable to send an E-mail.");
		}
    }
    
    public static String printCourseRegistered(Student student){
    	String coursesString = "";
    	for(Course c: student.getCourses().keySet()) {
			String info = c.printName();
			String ind = String.valueOf(student.getCourses().get(c).getIndexNum());
			coursesString = coursesString + info + " " + ind + "\n";
		}
    	System.out.println(coursesString);
		return coursesString;
    }
    
    public static void changeIndexNumberOfCourse(Student student, String courseCode, int newIndex){
		Course course = null;
		course = CourseControl.findCourse(courseCode);
        for(IndexNumber i : course.getIndexes()) {
    		if (i.getIndexNum() == newIndex ) {
				if (i.getVacancy() > 0) {
					try {
						dropCourse(student, courseCode);
						addCourse(student, courseCode, newIndex);
					} catch (Exception e) {
						System.out.println("Error encountered");
					}
					
					return;
				}
				else {
					System.out.println("Vacancy is full.");
					return;
				}
			}
        }
	}
    
    public static void swapIndexNumberWithPeers(Student student, Student peer, String courseCode, int studentIndex, int peerIndex){
		Boolean studentValid = false;
		Boolean peerValid = false;
		HashMap<Course, IndexNumber> studentCourses = student.getCourses();
		HashMap<Course, IndexNumber> peerCourses = peer.getCourses();
		Course course = null;

		course = CourseControl.findCourse(courseCode);
		for (Course c: studentCourses.keySet()){
			if (c==course){
				studentValid = true;
			}
		}
		for (Course c: peerCourses.keySet()){
			if (c==course){
				peerValid = true;
			}
		}
		if (studentValid == true && peerValid==true) {
			try {
			StudentCourseControl.dropCourse(student, courseCode);
			StudentCourseControl.addCourse(student, courseCode, peerIndex);
			StudentCourseControl.dropCourse(peer, courseCode);
			StudentCourseControl.addCourse(peer, courseCode, studentIndex);
			} catch(Exception e) {
				System.out.println("Error encountered");
			}
		}
		else{
			System.out.println("Student is not enrolled in course.");
		}
	}
		
	
    
    
    public static Boolean timeClashBetweenModules(Student student, Course courseToReg, IndexNumber indexToReg) {
		Boolean clash = false;
    	HashMap<Course, IndexNumber> courses = student.getCourses();
		
    	Course courseClashing = null;
    	Lesson lessonClashing = null;
    	Lesson lessonToRegClash = null;
		for(Course courseRegistered : courses.keySet() ) {
			for(Lesson lessonRegistered : courses.get(courseRegistered).getLessons()) {
				for(Lesson lessonToReg : indexToReg.getLessons()) {
					if(lessonToReg.getDay()==lessonRegistered.getDay() && 
							lessonToReg.getWeek()==lessonRegistered.getWeek()) {
						if(lessonToReg.getStartTime().compareTo(lessonRegistered.getStartTime()) == 0) clash = true;
						if(lessonToReg.getEndTime().compareTo(lessonRegistered.getEndTime()) == 0) clash= true;
						if(lessonToReg.getStartTime().compareTo(lessonRegistered.getEndTime()) < 0 && 
								lessonToReg.getStartTime().compareTo(lessonRegistered.getStartTime()) > 0) clash = true;
						if(lessonToReg.getEndTime().compareTo(lessonRegistered.getStartTime()) > 0 &&
								lessonToReg.getEndTime().compareTo(lessonRegistered.getEndTime()) < 0 ) clash = true;
						if(clash) {
							courseClashing = courseRegistered;
							lessonClashing = lessonRegistered;
							lessonToRegClash = lessonToReg;
							break;
						}
					}
				}
				if(clash) break;
			}
			if(clash) break;
		}
		
		if(clash) {
			System.out.println("Clash found in timetable! Unable to register course.");
			System.out.println("Registered course: " + courseClashing.printName());
			System.out.println("Lesson clash:      " + lessonClashing.getLessonType());
			System.out.println("Timings: " + lessonClashing.getStartTime() + " - " + lessonClashing.getEndTime());
			System.out.println();
			System.out.println("Course to register: " + courseToReg.printName());
			System.out.println("Lesson to register: " + lessonToRegClash.getLessonType());
			System.out.println("Timings: " + lessonToRegClash.getStartTime() + " - " + lessonToRegClash.getEndTime());
		}
		return clash;
    }
    
}		