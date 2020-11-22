package Control;

import Entity.Student;
import Entity.Course;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {
	public static void sendMail(String recepient, Student student, Course course, int flag) throws Exception{

		final String username = "stars.planner.01@gmail.com";  //do-not-reply@blackboard.com "; 		final String username = "kartikeyavedula@gmail.com";  //do-not-reply@blackboard.com "; 
        String password = "hello_java@123";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
		new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient)); // to be added an email addr
			message.setSubject("COURSE(S) RGISTERED");
			if(flag==1){
				message.setText(printWaitlist(student, course));
			}
			else if(flag==0){
				message.setText(printAddCourse(student, course));
			}
			else if(flag==2){
				message.setText(printDropCourse(student, course));
			}
			else{
				System.out.println("Error in the type of email to be sent!");
			}
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	public static String printWaitlist(Student student, Course course){
		String dummy="";
		dummy+= ("You have been added to the following course:\nCourse Name:  "+course.getCourseName());
		dummy+=StudentCourseControl.printCourseRegistered(student);
		return dummy;
	}
	public static String printAddCourse(Student student, Course course){
		String dummy="";
		dummy+= ("You have added the following course:\nCourse Name:  "+course.getCourseName());
		dummy+=StudentCourseControl.printCourseRegistered(student);
		return dummy;
	}
	public static String printDropCourse(Student student, Course course){
		String dummy="";
		dummy+= ("You have dropped the following course:\nCourse Name:  "+course.getCourseName());
		dummy+=StudentCourseControl.printCourseRegistered(student);
		return dummy;
	}
}