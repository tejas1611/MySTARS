package Entity;

import java.io.Serializable;

public class Password implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String hashedPassword;
	private byte[] salt;
    
	public Password() {
		this.hashedPassword = " ";
	}
	public Password(String pass, byte[] salt) {
		this.hashedPassword = pass;
		this.salt=salt;
	}
	
	public String getPassword() { return hashedPassword; }
	
	public byte[] getSalt() { return salt; }

}
