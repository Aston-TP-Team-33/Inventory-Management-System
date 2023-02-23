package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @author Remy Thompson
 *
 */
public class DBUtils {
	
	/**
	 * Changes the scene to the fxmlFile passed in. If there is no username passed in then it will redirect to the login page.
	 * @param event - action event that caused the scene change
	 * @param title - title of the new scene
	 * @param fxmlFile - name of the fxmlFile to change to
	 * @param username - username of the user currently signed in
	 */
	public static void changeScene(ActionEvent event, String title, String fxmlFile) {
		Parent root = null;
		
		
		try {
			FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
			root = loader.load();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle(title);
		stage.setScene(new Scene(root, 1000, 500));
		stage.setResizable(false);
		stage.show();
	}

	
	/**
	 * Checks if user exists, if they are an admin and logs them into the program.
	 * @param event - Event that caused the method to run
	 * @param username - username entered into the text field
	 * @param password - password entered into the password field
	 */
	public static void loginUser(ActionEvent event, String username, String password) {
		Connection connection = null;
		PreparedStatement checkLoginCredentials = null;
		ResultSet resultSet = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		try {
			// Connect to database
			//TODO connect to host database
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Query user
			checkLoginCredentials = connection.prepareStatement("SELECT password, role FROM user WHERE username = ?;");
			checkLoginCredentials.setString(1, username);
			
			resultSet = checkLoginCredentials.executeQuery();
			
			// If user does not exist then display an error
			if(!resultSet.isBeforeFirst()) {
				alert.setContentText("Incorrect login credentials please try again.");
				alert.show();
				return;
			}
			
			resultSet.next();
			String retrievedPassword = resultSet.getString("password");
			int retrievedRole = resultSet.getInt("role");
						
			// Check password
			if(!password.equals(retrievedPassword)) {
				alert.setContentText("Incorrect login credentials please try again.");
				alert.show();
				return;
			}
			
			// Check role
			if(retrievedRole != 1) {
				alert.setContentText("You are not an admin. Please contact an administrator to upgrade your role.");
				alert.show();
				return;
			}
			
			DBUtils.changeScene(event, "Test", "../application/SideNavigation.fxml");
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		} catch(Exception e) {
			e.printStackTrace();
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}
	
	/**
	 * Adds a new user to the database
	 * @param name - name entered into the text field
	 * @param email - email entered into the text field
	 * @param username - username entered into the email field
	 * @param password - password entered into the password field
	 * @param role - role selected 
	 */
	public static void addUserToDatabase(String name, String email, String username, String password, int role) {
		Connection connection = null;
		PreparedStatement addUser = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Prepare statement to add user
			addUser = connection.prepareStatement("INSERT INTO `user` (`user_id`, `full_name`, `email`, `username`, `password`, `role`) VALUES (NULL, ?, ?, ?, ?, ?);");
			addUser.setString(1, name);
			addUser.setString(2, email);
			addUser.setString(3, username);
			addUser.setString(4, password);
			addUser.setString(5, Integer.toString(role));
			
			// Add user to database
			addUser.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}
		catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}
	
	/**
	 * Returns all the users currently in the database
	 * @return an ObervableList of users
	 */
	public static ObservableList<User> getUsers(){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		Statement getUsers = null;
			
		ObservableList<User> users = FXCollections.observableArrayList();
		
		try {
			// Connect to database
			//TODO connect to host database
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");	
			
			// Query users
			getUsers = connection.createStatement();
			ResultSet rs = getUsers.executeQuery("SELECT * FROM user");
			
			// Add users to list
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setFullName(rs.getString("full_name"));
				user.setEmail(rs.getString("email"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getInt("role"));
				users.add(user);
			}			
		} catch(SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		} catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}

		return users;
	}
}
