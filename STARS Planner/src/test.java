import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Control.CourseControl;
import Control.DatabaseControl;
import Control.PasswordControl;
import Entity.Admin;
import Entity.Course;
import Entity.Day;
import Entity.IndexNumber;
import Entity.Lesson;
import Entity.LessonType;
import Entity.Password;
import Entity.Student;

public class test {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		Password passwordToStore = PasswordControl.generateHash("Qwertyu123");
		Admin admin = new Admin("tang023", "Tang K", "tang_k@gmail.com", passwordToStore, "Male", "Singapore");
		List<Admin> list = new ArrayList<Admin>();
		list.add(admin);
		DatabaseControl.writeSerializedObject("adminDB", list);
		
		Password passwordToStore1 = PasswordControl.generateHash("Asdfg123");
		Student stud1 = new Student("tejas002", "Tejas G", "tejas@gmail.com", passwordToStore1, "Male", "Indian", 123, "SCSE", "CE", 2);
		Password passwordToStore2 = PasswordControl.generateHash("Sdfghj234");
		Student stud2 = new Student("gavin123", "Gavin N", "gavin@gmail.com", passwordToStore2, "Male", "Singaporean", 234, "SCSE", "CE", 1);
		Password passwordToStore3 = PasswordControl.generateHash("Dfghjk345");
		Student stud3 = new Student("max004", "Max N", "max@gmail.com", passwordToStore3, "Male", "Singaporean", 345, "SPMS", "MATH", 2);
		Password passwordToStore4 = PasswordControl.generateHash("Fghjkl456");
		Student stud4 = new Student("kartikeya03", "Kartikeya V", "kartikeya@gmail.com", passwordToStore4, "Male", "Indian", 456, "SPMS", "CHEM", 1);
		Password passwordToStore5 = PasswordControl.generateHash("Qwerty567");
		Student stud5 = new Student("rachita007", "Rachita A", "rachita@gmail.com", passwordToStore5, "Female", "Indian", 567, "SCSE", "CE", 2);
		Password passwordToStore6 = PasswordControl.generateHash("Wertyu678");
		Student stud6 = new Student("amy345", "Amy W", "amy@gmail.com", passwordToStore6, "Female", "Singaporean", 678, "SCSE", "BA", 1);
		Password passwordToStore7 = PasswordControl.generateHash("Ertyui789");
		Student stud7 = new Student("joel001", "Joel C", "joel@gmail.com", passwordToStore7, "Female", "Singaporean", 789, "NBS", "BB", 3);
		
		List<Student> list1 = new ArrayList<Student>();
		list1.add(stud1);
		list1.add(stud2);
		list1.add(stud3);
		list1.add(stud4);
		list1.add(stud5);
		list1.add(stud6);
		list1.add(stud7);
		DatabaseControl.writeSerializedObject("studentDB", list1);
		
		
		Course course = new Course("CE2002", "Object Oriented", "SCSE", "Core", 3);
		
		IndexNumber ind = new IndexNumber(1039, "SEP1", 2);
		Lesson lesson = new Lesson("TKL", new Time(10, 0, 0), new Time(12, 0, 0), LessonType.LEC, Day.TUESDAY, "all", "LT1A");
		Lesson lesson2 = new Lesson("TH", new Time(12, 30, 0), new Time(2, 30, 0), LessonType.LAB, Day.THURSDAY, "even", "SPL");
		try {
			ind.addLesson(lesson);
			ind.addLesson(lesson2);
		} catch (Exception e) {		}
		
		
		IndexNumber ind2 = new IndexNumber(1025, "SE2", 3);
		Lesson lesson3 = new Lesson("TKL", new Time(10, 0, 0), new Time(12, 0, 0), LessonType.LEC, Day.TUESDAY, "all", "LT1A");
		Lesson lesson4 = new Lesson("SR", new Time(1, 30, 0), new Time(3, 30, 0), LessonType.LAB, Day.THURSDAY, "odd", "SPL");
		try {
			ind2.addLesson(lesson3);
			ind2.addLesson(lesson4);
		} catch (Exception e) {}
		
		course.addIndex(ind);
		course.addIndex(ind2);
		
		List<Course> list2 = new ArrayList<Course>();
		list2.add(course);
		DatabaseControl.writeSerializedObject("courseDB", list2);

	}

}
