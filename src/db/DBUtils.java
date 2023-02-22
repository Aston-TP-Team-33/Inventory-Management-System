package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
		stage.setScene(new Scene(root, 600, 400));
		stage.show();
	}
	
	/**
	 * Checks if user exists, if they are an admin and logs them into the program.
	 * @param event
	 * @param username
	 * @param password
	 */
	public static void loginUser(ActionEvent event, String username, String password) {
		Connection connection = null;
		PreparedStatement checkLoginCredentials = null;
		ResultSet resultSet = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		try {
			// Connect to database
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
			
			DBUtils.changeScene(event, "Test", "../application/test.fxml");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
