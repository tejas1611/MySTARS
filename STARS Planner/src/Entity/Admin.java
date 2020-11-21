package Entity;
import java.io.Serializable;

public class Admin extends Person implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Admin(String id, String name, String email, Password password, String gender, String nationality) {
		super(id, name, email, password, gender, nationality);
	}

	
 
}


