package Boundary;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Calendar;
import Control.CourseControl;
import Control.StudentCourseControl;
import Control.PasswordControl;
import Control.DatabaseControl;
import Entity.*;

public class StudentMenu {
	
	public static void stdMenu(Student student)  {
        Calendar startDate = Student.getAccessStart();
        Calendar endDate = Student.getAccessEnd();
		int studentChoice=0;
		while(studentChoice != 7) {
			StarsPlanner.clearScreen();
			Calendar currentDate = Calendar.getInstance();
			if (currentDate.before(startDate) || endDate.before(currentDate)){
				System.out.println("Student is not allowed to access menu outside access period");
				System.out.println("Access period: " + Student.printaccessStart() + " to " + Student.printaccessEnd());
				System.out.println("Current date and time: " + currentDate.getTime().toString());
				break;
			}
			
			System.out.println("\n\n\n***** STUDENT PANEL *****");
			System.out.println("(1): Add Course");
			System.out.println("(2): Drop Course");
			System.out.println("(3): Check/Print Courses Registered ");
			System.out.println("(4): Check Vacancies Available");
			System.out.println("(5): Change Index Number of Course");
			System.out.println("(6): Swap Index Number with Another Student");
			System.out.println("(7): Exit");
			System.out.print("Select an action: ");
			Scanner sc = new Scanner(System.in);
			studentChoice = sc.nextInt();
			sc.nextLine(); // Consume newline character
			switch(studentChoice) {
		     	case 1:
		                System.out.print("Enter Course Code: ");
		                String courseID = sc.nextLine();
		                Course course = CourseControl.findCourse(courseID);
		                if(course==null) {
		                	System.out.println("Course code not found");
		                	break;
		                }
		                ArrayList<IndexNumber> indexNum =course.getIndexes();
		                System.out.print("Following index groups were found: ");
		                for (IndexNumber num : indexNum) { 		      
		                    System.out.print(num.getIndexNum() + " "); 		
		                }
		                System.out.print("\nEnter your required index number ");
		                int index = sc.nextInt();   
		                sc.nextLine(); // Consume newline character
		                try {
		                	StudentCourseControl.addCourse(student, courseID, index);
		                } catch (Exception e) {
		                	System.out.print("Error encountered");
						}
	                break;   
	                
	            case 2:
		            	System.out.println("Enter Course Code");
		                String courseID2 = sc.nextLine();    
		                try {
							StudentCourseControl.dropCourse(student, courseID2);
						} catch (Exception e) {
							System.out.print("Error Encountered");
						}
	                break;
	                
	            case 3: 
	            	StudentCourseControl.printCourseRegistered(student);
	                break;
	                
	            case 4: 
	            	int indexnumber=0;
	                System.out.println("Enter Course Code: ");
	                String courseCode3 = sc.nextLine();
	                Course course3=CourseControl.findCourse(courseCode3);
	                ArrayList<IndexNumber> indexNumber =course3.getIndexes();
	                for (IndexNumber inum : indexNumber)
	                { 		      
	                    System.out.println(inum.getIndexNum()); 		
	                }
	                System.out.println("Enter your required index number to check for vacancy:");
	                indexnumber=sc.nextInt(); 
	                sc.nextLine(); // Consume newline character
	                CourseControl.getVacancy(courseCode3, indexnumber);          
	                break;
	                
	            case 5:
	            	System.out.println("Enter Course Code: ");
	                String courseCode4 = sc.nextLine();
	                Course course4=CourseControl.findCourse(courseCode4);
	                ArrayList<IndexNumber> indexNumber2 =course4.getIndexes();
	                for (IndexNumber indnum : indexNumber2)
	                { 		      
	                    System.out.println(indnum.getIndexNum()); 		
	                }
	                System.out.println("Enter index number to swap: ");
	                int index2 = sc.nextInt();
	                sc.nextLine(); // Consume newline character
	                StudentCourseControl.changeIndexNumberOfCourse(student, courseCode4, index2);
	                break;
	                
	            case 6:
                	@SuppressWarnings("unchecked") 
                	List<Student> list = (List<Student>) DatabaseControl.readSerializedObject("studentDB");
                	String username="";
                	String pass="";
                	int tries=3;
                	System.out.print("Enter user name of peer: ");
                	Student peer = null;
                	username = sc.nextLine();
                	for(int i = 0 ; i < list.size() ; i++) {
                		if(username.equals(((Student)list.get(i)).getId())) {
                			tries=3;
                			while(tries>0) {
                				System.out.print("Enter password of peer: ");
                				pass=LoginMenu.enterPassword();
                				if(PasswordControl.comparePassword(pass, ((Student)list.get(i)).getPassw())) {
	                            	//password correct
                                    peer = (Student) list.get(i);
	                            	System.out.println("Login Successful!");
                				}
                				else {
	                            	System.out.println("Incorrect Password. Kindly re-enter!");
	                            	tries-=1;
	                            	System.out.println("Tries left- "+tries);
	                            	if(tries==0) {
	                            		System.out.println("Out of tries! Login again.");
	                            	}
                				}
                			}
                		}	        		
                		else {
                			System.out.println("Username not found in records! Login again.");
                		}
	                    System.out.println("Enter Course Code:");
	                    String courseCode5=sc.nextLine();
	                    System.out.println("Enter your Index Number:");
	                    int studentIndex = sc.nextInt();
	                    sc.nextLine(); // Consume newline character
	                    System.out.println("Enter peer's Index Number");
	                    int peerIndex = sc.nextInt();
	                    sc.nextLine(); // Consume newline character
	                    StudentCourseControl.swapIndexNumberWithPeers(student,peer,courseCode5,studentIndex,peerIndex);
					}                  
                	break;
                	
                  default: 
                        break;
			}
		}
	}
}

    