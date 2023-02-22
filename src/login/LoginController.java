package login;

import db.DBUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	TextField usernameField;
	@FXML
	PasswordField passwordField;
	@FXML
	Button loginButton;
	
	public void login(ActionEvent event) {
		try {			
			DBUtils.loginUser(event, usernameField.getText(), passwordField.getText());
		}catch(Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.show();
			e.printStackTrace();
		}
		
	}
}
