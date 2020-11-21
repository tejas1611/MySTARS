package Entity;

import java.sql.Time;

enum LessonType {
	LEC, TUT, LAB, SEM, ONL;
}

@SuppressWarnings("deprecation")
public class Lesson {
	Time startTime;
	Time endTime;
	private LessonType lessonType;
	private String day;
	private String week; 		// {"even", "odd", "all"}
	private String venue;
	private TeachingStaff teacher;
	
	public Lesson(TeachingStaff teacher, Time startTime, Time endTime, LessonType lessonType, 
			String day, String week, String venue) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.lessonType = lessonType;
		this.day= day;
		this.week= week;
		this.venue= venue;
		this.teacher= teacher;
	}

	public String getVenue() { return venue; }
	public void setVenue(String venue) { this.venue=venue; }

	public String getWeek() { return week; }
	public void getWeek(String week) {this.week=week;}

	public LessonType getLessonType() { return lessonType; }
	public void setLessonType(LessonType lessonType) { this.lessonType= lessonType; }

	public String getDay() { return day; }
	public void setDay(String day) { this.day= day; }

	public Time getStartTime() { return startTime; }
	public void setStartTime(Time startTime) { this.startTime= startTime; }
	public String printStartTime() { 
		return String.valueOf(startTime.getHours()) + ":" + String.valueOf(startTime.getMinutes()); 
	}

	public Time getEndTime() { return endTime; }
	public void setEndTime(Time endTime) { this.endTime= endTime; }
	public String printEndTime() { 
		return String.valueOf(endTime.getHours()) + ":" + String.valueOf(endTime.getMinutes()); 
	}

	public TeachingStaff getTeacher() { return teacher; }
	public void setTeacher(TeachingStaff teacher) { this.teacher= teacher; }

}
