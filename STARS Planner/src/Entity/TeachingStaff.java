package Entity;

import java.util.ArrayList;
import java.util.HashMap;

enum StaffType {
	Professor, Lecturer, Tutor, LabSupervisor;
}

public class TeachingStaff extends Person {
	
	private static final long serialVersionUID = 1L;
	
	private StaffType type;
	private HashMap<Course, ArrayList<Lesson>> lessons;
	
	public TeachingStaff(String id, String name, String email, Password password, String gender, 
			String nationality, StaffType type) {
		super(id, name, email, password, gender, nationality);	
		this.type = type;
		this.lessons = new HashMap<>();
	}
	public void removeCourse(Course course, Lesson lesson) throws Exception{
		if(!lessons.containsKey(course))
			throw new Exception("Course not allocated to " + this.type);
		else 
			lessons.get(course).remove(lesson);
	}
	
	public void addLesson(Course course, Lesson lesson) throws Exception{
		if(!lessons.containsKey(course))
			lessons.put(course, new ArrayList<Lesson>());
		if(lessons.get(course).contains(lesson))
			throw new Exception("Lesson already allocated to " + this.type);
		else
			lessons.get(course).add(lesson);
	}
		
	public void removeLesson(Course course, Lesson lesson) throws Exception{
		if(!lessons.containsKey(course))
			throw new Exception("Course not allocated to " + this.type);
		else 
			lessons.get(course).remove(lesson);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof TeachingStaff)
			return ((TeachingStaff) o).getId()==this.id;
		else
			return false;
	}
}
