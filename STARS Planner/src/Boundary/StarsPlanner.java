package Boundary;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import Entity.*;;

public class StarsPlanner{
    List<Student> listOfStudents = new ArrayList<Student>();
    
    public static void main(String args[]) {
    	clearScreen();
        int domain=0, loginAgain=0;

        Scanner sc = new Scanner(System.in);
        while(loginAgain==0) {
	        System.out.println("\n\n============ Welcome To STARS Planner ===========");
	        System.out.println("===================== LOG IN ====================");
	        System.out.println("CHOOSE DOMAIN:");
	        System.out.println("1. STUDENT");
	        System.out.println("2. ADMIN");
	        System.out.print("ENTER YOUR CHOICE: ");
	        domain = sc.nextInt();
	        sc.nextLine(); // Consume newline character
	        loginAgain = LoginMenu.userLogin(domain);
	        System.out.println("=================================================");
        }
        
        switch(domain){
            case 1: 
                StudentMenu.stdMenu();
                StarsPlanner.main(null);
                break;
                
            case 2:
                Admin admin= LoginMenu.getAdminObject();
                AdminiMenu.AdmMenu(admin);
                StarsPlanner.main(null);
                break;
        }
        sc.close();
    }
    
    public static void clearScreen() {  
    	java.io.Console c = System.console();
		if(c==null) return;
    	try {
    		if (System.getProperty("os.name").contains("Windows"))
    			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    		else
    			Runtime.getRuntime().exec("clear");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
    } 
}
