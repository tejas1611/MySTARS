package Control;
import Entity.Student;
import Entity.Course;

public class NotifyStudent {
	
    private static String email;
    
    public NotifyStudent() {
		 email = " ";
     }
     
     public NotifyStudent(Student student) {
		 email = student.getEmail();
     }
     
     public static void notifyEmail(Student student, Course course, int flag) throws Exception {
		try {
			SendMailTLS.sendMail(email, student, course, flag);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
     }

}
