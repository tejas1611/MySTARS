package Entity;

import java.io.Serializable;

public abstract class Person implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	protected String email;
	protected Password password; 
	protected String gender;
	protected String nationality;
	
	Person(String id, String name, String email, Password password, String gender, String nationality) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.nationality = nationality;
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }

	public Password getPassw(){return password;}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public void printInfo() {
		System.out.println( id + " - " + name + ", Email:" + email);
	}
}
