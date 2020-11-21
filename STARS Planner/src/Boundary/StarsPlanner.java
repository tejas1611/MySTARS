package Boundary;

import java.util.Date;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.HashMap;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;

import Control.*;
import Boundary.LoginMenu;
import Entity.*;;

public class StarsPlanner{
    List<Student> listOfStudents = new ArrayList<Student>();
    
    public static void main(String args[]) {
        int domain=0, loginAgain=0;

        HashMap<Course, IndexNumber> courses;
        Scanner sc = new Scanner(System.in);
        while(loginAgain==0) {
	        System.out.println("============Welcome To STARS Planner===========");
	        System.out.println("=====================LOG IN====================");
	        System.out.println("			    CHOOSE DOMAIN:");
	        System.out.println("				   1. STUDENT");
	        System.out.println("				   2. ADMIN");
	        System.out.print("              ENTER YOUR CHOICE: ");
	        domain = sc.nextInt();
	        loginAgain=LoginMenu.userLogin(domain);
	        System.out.println("===============================================");
        }
        
        switch(domain){
            case 1: 
                Student student= LoginMenu.getStudentObject();
                StudentMenu.stdMenu(student);  //edit accordingly
            case 2:
                Admin admin= LoginMenu.getAdminObject();
                AdminiMenu.admMenu(admin);  //edit accordingly
        }
    }
    


}


