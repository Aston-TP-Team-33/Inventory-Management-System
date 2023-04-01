package application.models;

/**
 * @author Remy Thompson
 *
 */
public class UserQuery {
	private String name, email, query;
	private int id;

	public UserQuery(String name, String email, String query, int id) {
		this.name = name;
		this.email = email;
		this.id = id;
		this.query = query;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getId() {
		return id;
	}
	
	public String getQuery() {
		return query;
	}
}
