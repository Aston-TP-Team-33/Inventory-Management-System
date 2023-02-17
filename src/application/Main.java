package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		// Open application
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/login.fxml"));
	        Parent root = loader.load();
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		// DB Info
		//TODO environmental variables
		String url = "jdbc:mysql://localhost:3306/team33";
		String uname = "root";
		String password = "";
		String query = "SELECT * FROM user WHERE user_id = 1";
		
		// Test if package is imported correctly 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 			
		} catch(ClassNotFoundException e) {
			e.getStackTrace();
		}
		
		
		// Connect to DB
		try {
			Connection con = DriverManager.getConnection(url, uname, password);
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(query);
			ResultSetMetaData rsmd = result.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			// Each row 
			while(result.next()) {
				String userData = "";
				
				// Iterate each column in row
				for(int i = 1; i <= columnCount; i++) {
					userData += result.getString(i) + " : ";
				}
				
				System.out.println(userData);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}