package Control;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import Entity.*;

import java.util.ArrayList;

// add import java.io.Serializable;
//add 'implements Serializable' after any class whose object is to be written into the file

// Note : When structure of the Object type (the class file) in the list changed
// the Serialized file may fail.
public class DatabaseControl
{
	public static List readSerializedObject(String filename) {
		List objectList = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			objectList = (ArrayList) in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		return objectList;
	}

	public static void writeSerializedObject(String filename, List list) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void updateInFile(Object o) {
		if(o instanceof Student) {
			List<Student> studList = (List<Student>) readSerializedObject("studentDB");
			for(int index=0; index<studList.size(); index++) {
				Student s = studList.get(index);
				if(s.getMatricNo()==((Student)o).getMatricNo()) {
					studList.set(index, (Student)o);
					break;
				}
			}
			writeSerializedObject("studentDB", studList);
		}
		else if(o instanceof Course) {
			List<Course> courseList = (List<Course>) readSerializedObject("courseDB");
			for(int index=0; index<courseList.size(); index++) {
				Course c = courseList.get(index);
				if(c.getCourseCode().equals(((Course)o).getCourseCode())) {
					courseList.set(index, (Course)o);
					break;
				}
			}
			writeSerializedObject("courseDB", courseList);
		}
		
			
	}
}