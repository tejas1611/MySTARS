package Boundary;

import java.util.Date;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Console;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;

import Control.CourseControl;
import Control.PasswordControl;
import Control.StudentCourseControl;
import Entity.Admin;
import Entity.Course;
import Entity.IndexNumber;
import Entity.Student;
import Control.AdminControl;
public class AdminiMenu{
public static void AdmMenu(Admin admin) {
int adminChoice=0;
while(adminChoice != 7)
{
    System.out.println("Select the following options: ");
    System.out.println("(1): Edit student access period");
    System.out.println("(2): Add a student");
    System.out.println("(3): Add/Update a course");
    System.out.println("(4): Check available slot for an index number");
    System.out.println("(5): Print student list by index number");
    System.out.println("(6): Print student list by course");
    System.out.println("(7): Exit");   

    Scanner sc = new Scanner(System.in);
    adminChoice = sc.nextInt();
        switch(adminChoice)
       {
           case 1: System.out.println("Enter the start of the access period:");
                   System.out.println("Enter year:");
                   int startYear = sc.nextInt();
                   System.out.println("Enter month");
                   
			startStr = sc.nextLine();
			System.out.println("Enter the end of the access period: (YY/MM/DD HH:mm)");
			endStr = sc.nextLine();
            
            

           case 2: AdminControl.addStudent();
                   Student newStudent = new Student(id, studentName, email, password, gender, nationality, matricNo, school, program, yearOfStudy);
                    System.out.println("Enter Student ID");
                    System.out.println("Enter Student Name");
                    System.out.println("Enter Email"):
                    
                    break;

           case 3:  
                    CourseControl(sc);
                    
                    break;

           case 4: StudentCourseControl(sc);
                    break;

           case 5: StudentCourseControl.printCourseRegistered(student);
                    break;


           case 6: System.out.println(coursesString);
                    break;


           
       } 
       sc.close();
}
}
}