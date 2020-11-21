package Boundary;

import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Console;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;

import Control.*;
import Entity.*;

public class LoginMenu {
	static String userNAME="";
	
    @SuppressWarnings("unchecked") 
	public static int userLogin(int domain) { 
        Scanner sc = new Scanner(System.in);
        List list = null;
        if(domain==1){
            list = (List<Student>) (DatabaseControl.readSerializedObject("studentDB"));
        }
        else if(domain==2){
            list = (List<Admin>) (DatabaseControl.readSerializedObject("adminDB"));
        }
        else{
            System.out.println("Wrong Domain Chosen! Login again.");
            return 0;
        }  
        String username="";
        String pass="";
        int tries=3;

        System.out.print("Enter your user name: ");
        username = sc.next();

        for(int i = 0 ; i < list.size() ; i++) {
            if(username.equals(((Person)list.get(i)).getId())) {
                // Username found
                tries=3;
                while(tries>0) {
	                System.out.print("Enter your password: ");
	                pass = enterPassword();
	                if(PasswordControl.comparePassword(pass, ((Person)list.get(i)).getPassw())) {
                        // Password correct
                        userNAME=username;
                        System.out.println("Login Successful!");
                        return 1;
                    }
                    else {
                        System.out.println("Incorrect Password. Kindly re-enter!");
                        tries-=1;
                        System.out.println(tries + " attempts remaining...");
                        if(tries==0) {
                            System.out.println("Out of tries! Login again.");
                            return 0;
                        }
                    }
                }
            }
        } 

        // If username not found, then proceed here
       	System.out.println("Username not found in records! Login again.");
       	return 0;
    }
    
    public static String enterPassword(){
		java.io.Console c = System.console();
		char[] passString;
		passString = c.readPassword();
		String password = new String(passString);
		return password;
	}

    @SuppressWarnings("unchecked")
	public static Student getStudentObject(){
        List<Student> studentList = (DatabaseControl.readSerializedObject("studentDB"));
        Student st=null;
        for(int i = 0 ; i < studentList.size() ; i++) {
            if(userNAME.equals((studentList.get(i)).getId())) {
                return (studentList.get(i));
            }
        }
        return st;
    }
    
    @SuppressWarnings("unchecked")
	public static Admin getAdminObject(){
        List<Admin> adminList = (DatabaseControl.readSerializedObject("adminDB"));
        Admin ad=null;
        for(int i = 0 ; i < adminList.size() ; i++) {
            if(userNAME.equals((adminList.get(i)).getId())) {
                return (adminList.get(i));
            }
        }
        return ad;
    }
}