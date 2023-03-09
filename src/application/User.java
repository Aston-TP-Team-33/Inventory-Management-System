package application;

/**
 * @author Remy Thompson
 *
 */
public class User {
	public User(String name, String email, String password, int type, int id) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.type = type;
		this.id = id;
	}

	private String name, email, password;
	private int type, id;
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getType() {
		return type;
	}
	
	public int getId() {
		return id;
	}
}
