package Boundary;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import Entity.*;;

public class StarsPlanner{
    List<Student> listOfStudents = new ArrayList<Student>();
    
    public static void main(String args[]) {
        int domain=0, loginAgain=0;

        Scanner sc = new Scanner(System.in);
        while(loginAgain==0) {
	        System.out.println("============Welcome To STARS Planner===========");
	        System.out.println("=====================LOG IN====================");
	        System.out.println("            CHOOSE DOMAIN:");
	        System.out.println("		1. STUDENT");
	        System.out.println("		2. ADMIN");
	        System.out.print("ENTER YOUR CHOICE: ");
	        domain = sc.nextInt();
	        loginAgain = LoginMenu.userLogin(domain);
	        System.out.println("===============================================");
        }
        
        switch(domain){
            case 1: 
                Student student= LoginMenu.getStudentObject();
                StudentMenu.stdMenu(student);
            case 2:
                Admin admin= LoginMenu.getAdminObject();
                AdminiMenu.AdmMenu(admin);
        }
        sc.close();
    }
}
