package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class IndexNumber implements Serializable {

	private static final long serialVersionUID = 1L;
	private int indexNum;
	private String tutorialGroup;
	private int vacancy;
	Queue<Student> waitlist;
	private ArrayList<Integer> students;
	private ArrayList<Lesson> lessons;
	
	
	public IndexNumber(int indexNum, String tutorialGroup, int capacity) {
		this.indexNum = indexNum;
		this.tutorialGroup = tutorialGroup;
		this.vacancy = capacity;
		this.students = new ArrayList<Integer>();
		this.lessons = new ArrayList<Lesson>();
		this.waitlist = new LinkedList<Student>();
	}
	
	public IndexNumber(IndexNumber another) {
		this.indexNum = another.indexNum;
		this.tutorialGroup = another.tutorialGroup;
		this.waitlist = another.waitlist;
		this.vacancy = another.vacancy;
		this.students = another.students;
		this.lessons = another.lessons;
	}
	
    public int getIndexNum() { return indexNum; }
	public void setIndexNum(int indexNum) { this.indexNum=indexNum; }

	public void printTimeTable(){
		for(Lesson l:lessons){
			LessonType lessonType = l.getLessonType();
			String venue = l.getVenue();
			String week = l.getWeek();
			Day day = l.getDay();
			System.out.println(lessonType + " " + venue + " " + week + " " + day.toString() + " " + l.printStartTime() + "-" + l.printEndTime());
		}
	}

	public String getTutorialGroup() {return tutorialGroup;}
	public void setTutorialGroup(String tutorialGroup) {this.tutorialGroup= tutorialGroup;}

	public int getVacancy() { return vacancy; }
	public void setVacancy(int vacancy) { this.vacancy= vacancy; }
	

	public void addStudent(Student student) throws Exception {
		students.add(student.getMatricNo());
		vacancy--;
	}
	public void removeStudent(Student student) throws Exception { students.remove(student.getMatricNo()); }
	
	public ArrayList<Integer> getStudents() { return students; }

	public void addLesson(Lesson lesson) throws Exception {
		if(lessons.contains(lesson))
			throw new Exception("Lesson already exists in this index number " + lesson.getLessonType());
		else{
			lessons.add(lesson);
		}
	}
	public void removeLesson(Lesson lesson) throws Exception {
		if(!lessons.contains(lesson))
			throw new Exception("Lesson does not exist in this index number ");
		else{
			lessons.remove(lesson);
		}
	}
	public ArrayList<Lesson> getLessons() { return lessons; }
	
	public void putInWaitlist(Student student) {
		waitlist.add(student);
	}
	public Student findNextInWaitlist() throws Exception {
		return waitlist.remove();
	}

	public int getWaitlistLength(){
		return waitlist.size();
	}
}
