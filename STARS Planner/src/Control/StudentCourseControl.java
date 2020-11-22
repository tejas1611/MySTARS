package Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import Entity.Course;
import Entity.IndexNumber;
import Entity.Lesson;
import Entity.Student;

public class StudentCourseControl {
	
    public static void addCourse(Student student, Course course, int index) throws Exception {
    	boolean valid = false;
    	
    	// Check AU limit for student
    	int totalAU = student.getTotalAU();
		if(totalAU+course.getAu() > student.getAULimit() && !student.isOverloadPermission()) {
			System.out.println("Student not allowed to overload.");
			throw new Exception();
		}
		
		// Check for invalid Index Number
		int i;
		ArrayList<IndexNumber> indices = course.getIndexes();
		
		for(i=0;i<indices.size();i++) {
			if(indices.get(i).getIndexNum() == index) {
				valid = true;
				break;
			}
		}
		if(!valid) {
			System.out.println("Index Number not found in course");
		}
		
		// Check for timetable clash
		if(timeClashBetweenModules(student, course, indices.get(i))) valid = false;
		
		// Check if vacancy available
		boolean vacancyAvailable = true;
		if(indices.get(i).getVacancy() <= 0) vacancyAvailable = false;
		
		if(vacancyAvailable && valid) {
			student.addCourse(course, indices.get(i));
			IndexNumber i_temp = indices.get(i);
			i_temp.addStudent(student);
			indices.set(i, i_temp);
			System.out.println("Course successfully registered");
			try {
				NotifyStudent.notifyEmail(student,course,0);
			} catch (Exception e) {
				System.out.print("Unable to send an E-mail.");
			}
		}
		else {
			if(student.getNumberWaitlist() < 5) {
				student.addWaitlist(course, indices.get(i));
				IndexNumber i_temp = indices.get(i);
				i_temp.putInWaitlist(student);
				indices.set(i, i_temp);
				System.out.println("No vacancy available. Added to waitlist.");
			}
			else {
				System.out.println("Unable to register course.");
				System.out.println("No vacancy available. Course waitlist limit (5) reached.");
			}
		}
		course.setIndexes(indices);
		
		DatabaseControl.updateInFile(course);
		DatabaseControl.updateInFile(student);
		return;
    }

    public static void dropCourse(Student student, Course course) throws Exception {
    	// Remove student from course
    	IndexNumber index = student.getCourses().get(course);
    	student.removeCourse(course);
    	index.removeStudent(student);
//    	try {
//			NotifyStudent.notifyEmail(student,course,2);
//		} catch (Exception e) {
//			System.out.print("Unable to send an E-mail.");
//		}
    	ArrayList<IndexNumber> indices = course.getIndexes();
    	int i;
    	for(i=0;i<indices.size();i++) {
			if(indices.get(i).getIndexNum() == index.getIndexNum()) {
				indices.set(i, index);
				break;
			}
		}
    	course.setIndexes(indices);
		DatabaseControl.updateInFile(student);
		DatabaseControl.updateInFile(course);
		
		// Check for Waitlist
		try { 
			Student studentToReg = index.findNextInWaitlist();
			addCourse(studentToReg, course, index.getIndexNum());
		} catch (NoSuchElementException e) {
			return;
		}
    }
    
    public static String printCourseRegistered(Student student){
    	String coursesString = "";
    	for(Course c: student.getCourses().keySet()) {
			String info = c.printName();
			String ind = String.valueOf(student.getCourses().get(c).getIndexNum());
			coursesString = coursesString + info + " " + ind + "\n";
		}
    	if(coursesString.equals(""))
    		System.out.println("\n\nNo courses have been registered.");
    	else 
    		System.out.println("\n\n" + coursesString);
		return coursesString;
    }
    
    public static void changeIndexNumberOfCourse(Student student, String courseCode, int newIndex) {
    	
    	// TODO update index number
		Course course = null;
		course = CourseControl.findCourse(courseCode);
        for(IndexNumber i : course.getIndexes()) {
    		if (i.getIndexNum() == newIndex ) {
				if (i.getVacancy() > 0) {
					try {
						dropCourse(student, course);
						addCourse(student, course, newIndex);
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
			StudentCourseControl.dropCourse(student, course);
			StudentCourseControl.addCourse(student, course, peerIndex);
			StudentCourseControl.dropCourse(peer, course);
			StudentCourseControl.addCourse(peer, course, studentIndex);
			} catch(Exception e) {
				System.out.println("Error encountered");
			}
		}
		else{
			System.out.println("Student is not enrolled in course.");
		}
	}
		
	
    public static Boolean checkCourseRegistered(Student student, Course course) {
    	for(Course c : student.getCourses().keySet()) {
			if(c.getCourseCode().equals(course.getCourseCode())) {
				return false;
			}
		}
    	return true;
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