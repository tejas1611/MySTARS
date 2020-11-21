package Boundary;

import java.util.Scanner;
import java.util.ArrayList;

import Control.CourseControl;
import Control.PasswordControl;
import Entity.Admin;
import Entity.Course;
import Entity.IndexNumber;
import Entity.Password;
import Control.AdminControl;

public class AdminiMenu {
	
	public static void AdmMenu(Admin admin) {
			int adminChoice=0;
			while(adminChoice != 8) {
			    System.out.println("Select the following options: ");
			    System.out.println("(1): Edit student access period");
			    System.out.println("(2): Add a student");
			    System.out.println("(3): Add a course");
			    System.out.println("(4): Update a course");
			    System.out.println("(5): Check available slot for an index number");
			    System.out.println("(6): Print student list by index number");
			    System.out.println("(7): Print student list by course");
			    System.out.println("(8): Exit");   

			    Scanner sc = new Scanner(System.in);
			    adminChoice = sc.nextInt();
		        switch(adminChoice) {
		        	case 1: 
		        	   System.out.println("Enter the start of the access period YYYY/MM/DD HH/MM:");
		               System.out.print("Enter year YYYY ");
		               int startYear = sc.nextInt();
		               System.out.print("Enter month MM ");
		               int startMonth = sc.nextInt();
		               System.out.print("Enter day DD ");
		               int startDay = sc.nextInt();
		               System.out.print("Enter hours HH ");
		               int startHours = sc.nextInt();
		               System.out.print("Enter hours MM ");
		               int startMin = sc.nextInt();
		               System.out.println("Enter the end of the access period YYYY/MM/DD HH/MM ");
		               System.out.print("Enter year YYYY ");
		               int endYear = sc.nextInt();
		               System.out.print("Enter month MM ");
		               int endMonth = sc.nextInt();
		               System.out.print("Enter day DD ");
		               int endDay = sc.nextInt();
		               System.out.print("Enter hours HH ");
		               int endHours = sc.nextInt();
		               System.out.print("Enter hours MM ");
		               int endMin = sc.nextInt();
		               AdminControl.editStudentAccessPeriod(startYear, startMonth, startDay, startHours, startMin, endYear, endMonth, endDay, endHours, endMin);
		               break;
               
		        	case 2: 
		                System.out.print("Enter ID ");
		                String id = sc.nextLine();
		                System.out.print("Enter Student Name ");
		                String name = sc.nextLine();
		                System.out.print("Enter Email ");
		                String email = sc.nextLine();
		                System.out.print("Enter Password ");
		                String pass = sc.nextLine();
		                Password password = PasswordControl.generateHash(pass);
		                System.out.print("Enter Gender ");
		                String gender = sc.nextLine(); 
		                System.out.print("Enter Nationality ");
		                String nationality = sc.nextLine();
		                System.out.print("Enter Matric Number ");
		                int matricNo = sc.nextInt();
		                System.out.print("Enter School ");
		                String school = sc.nextLine();
		                System.out.print("Enter Program ");
		                String program = sc.nextLine();
		                System.out.print("Enter Year of Study ");
		                int yearOfStudy = sc.nextInt();
		                AdminControl.addStudent(id, name, email, password, gender, nationality, matricNo, school, program, yearOfStudy);
		                break;

		        	case 3: 
		                System.out.print("Enter course name ");
		                String courseName = sc.nextLine();
		                System.out.print("Enter course code ");
		                String courseCode = sc.nextLine();
		                System.out.print("Enter school ");
		                String school2 = sc.nextLine();
		                System.out.print("Enter school the type of course ");
		                String courseType = sc.nextLine();
		                System.out.print("Enter course au ");
		                int au = sc.nextInt();
		                AdminControl.addCourse(courseCode, courseName, school2, courseType, au);
		                break;
           
		        	case 4:
		        		System.out.print("Enter Course Code to modify: ");
		                String courseCode2 = sc.nextLine();
		                Course course = CourseControl.findCourse(courseCode2);
		                if(course==null) {
		                	System.out.println("Course code not found");
		                	break;
		                }
		                
		        		System.out.println("(1): Update Course Vacancy of Index");
		                System.out.println("(2): Update Course Index Number");
		                System.out.println("(3): Update Course Code");
		                System.out.println("(4): Update School of Course");
		                System.out.println("(5): Back to Main Menu");
		                                
		                int updates = sc.nextInt();
		                switch(updates) {
		                	case 1: 
		                		ArrayList<IndexNumber> indexNum = course.getIndexes();
				                System.out.print("Following index groups were found: ");
				                for (IndexNumber num : indexNum) { 		      
				                    System.out.print(num.getIndexNum() + " "); 		
				                }
				                System.out.print("\nEnter your required index number ");
				                int indexNumber = sc.nextInt();
				                System.out.println("Current Vacancy in Index: " + CourseControl.getVacancy(courseCode2, indexNumber));
				                
				                System.out.print("Enter the updated vacancy: ");
				                int vacancy = sc.nextInt();
								AdminControl.updateCourseVacancy(courseCode2, indexNumber, vacancy);
								break;

		                	case 2: 
		                		ArrayList<IndexNumber> indexNum2 = course.getIndexes();
				                System.out.print("Following index groups were found: ");
				                for (IndexNumber num : indexNum2) { 		      
				                    System.out.print(num.getIndexNum() + " "); 		
				                }
				                System.out.print("\nEnter index number to modify: ");
				                int indexNumber2 = sc.nextInt();
				                System.out.print("\nEnter new index number: ");
				                int newIndex = sc.nextInt();
		                        AdminControl.updateCourseIndex(courseCode2, indexNumber2, newIndex);
		                        break;

		                	case 3:
		                        System.out.print("Enter new course code");
		                        String newCourseCode= sc.nextLine();
		                        AdminControl.updateCourseCode(courseCode2, newCourseCode);
		                        break;

		                	case 4: 
		                		System.out.print("Enter School of Course");
		                		String newSchool = sc.nextLine();
		                		AdminControl.updateSchool(courseCode2, newSchool);
		                		break;
		                }
		                break;

		        	case 5:
		        		System.out.print("Enter Course Code: ");
		                String courseCode3 = sc.nextLine();
		                Course courseVacancy = CourseControl.findCourse(courseCode3);
		                if(courseVacancy==null) {
		                	System.out.println("Course code not found");
		                	break;
		                }
		                
		                ArrayList<IndexNumber> indexNum = courseVacancy.getIndexes();
		                System.out.print("Following index groups were found: ");
		                for (IndexNumber num : indexNum) { 		      
		                    System.out.print(num.getIndexNum() + " "); 		
		                }
		                System.out.print("\nEnter your required index number ");
		                int indexNumber = sc.nextInt();
		                System.out.println("Current Vacancy in Index: " + CourseControl.getVacancy(courseCode3, indexNumber));
		                break;
		                
	                case 6:
	                	System.out.print("Enter Course Code: ");
		                String courseCode4 = sc.nextLine();
		                Course coursePrint = CourseControl.findCourse(courseCode4);
		                if(coursePrint==null) {
		                	System.out.println("Course code not found");
		                	break;
		                }
		                ArrayList<IndexNumber> indexNum2 = coursePrint.getIndexes();
		                System.out.print("Following index groups were found: ");
		                for (IndexNumber num : indexNum2) { 		      
		                    System.out.print(num.getIndexNum() + " "); 		
		                }
		                System.out.print("\nEnter your required index number ");
		                int indexNumber2 = sc.nextInt();
		        	    AdminControl.printStudentListByIndexNumber(courseCode4, indexNumber2);
		                break;

	                case 7:                     	   
	                	System.out.print("Enter Course Code: ");
		                String courseCode5 = sc.nextLine();
		                Course coursePrint2 = CourseControl.findCourse(courseCode5);
		                if(coursePrint2==null) {
		                	System.out.println("Course code not found");
		                	break;
		                }
						AdminControl.printStudentListByCourse(courseCode5);
						break;

	                default:
	                	break; 
		        } 
		        sc.close();
			}
	}
}