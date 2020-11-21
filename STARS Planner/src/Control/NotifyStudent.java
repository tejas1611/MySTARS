package Control;
import Entity.Student;

public class NotifyStudent {
	
    private String email;
    
    public NotifyStudent() {
		 email = " ";
     }
     
     public NotifyStudent(Student student) {
		 email = student.getEmail();
     }
     
     public void notifyEmail(Student student) throws Exception {
		try {
			SendMailTLS.sendMail(email, student);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
     }

}
