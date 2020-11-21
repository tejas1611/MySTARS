import java.util.ArrayList;
import java.util.List;

import Control.DatabaseControl;
import Control.PasswordControl;
import Entity.Admin;
import Entity.Password;
import Entity.Student;

public class test {

	public static void main(String[] args) {
		
		Password passwordToStore = PasswordControl.generateHash("Qwertyu123");
		Admin admin = new Admin("1", "Tang K", "tang_k@gmail.com", passwordToStore, "Male", "Singapore");
		List<Admin> list = new ArrayList<Admin>();
		list.add(admin);
		DatabaseControl.writeSerializedObject("adminDB", list);
		
		Password passwordToStore1 = PasswordControl.generateHash("Asdfg123");
		Student stud = new Student("2", "Tejas G", "tejas@gmail.com", passwordToStore1, "Male", "India", 123, "SCSE", "CE", 2);
		List<Student> list1 = new ArrayList<Student>();
		list1.add(stud);
		DatabaseControl.writeSerializedObject("studentDB", list1);

	}

}
