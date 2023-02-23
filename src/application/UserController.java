package application;
import java.net.URL;
import java.util.ResourceBundle;

import db.DBUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

/**
 * @author Remy Thompson
 *
 */
public class UserController implements Initializable {

    @FXML
    private TextField fullNameField;

    @FXML
    private ChoiceBox<Integer> roleField;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private TableColumn<User, String> fullNameColumn;

    @FXML
    private TableColumn<User, Integer> roleColumn;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAdd;

    @FXML
    private TableView<User> table;

    @FXML
    private TextField passwordField;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    /**
     * Takes the users input and adds it to the database
     * @param event
     */
    @FXML
    void add(ActionEvent event) {
    	String fullName = fullNameField.getText();
    	String username = usernameField.getText();
    	String password = passwordField.getText();
    	String email = emailField.getText();
    	Integer role = roleField.getValue();
    	
    	// Make sure form was filled
    	if(fullName.equals("") || username.equals("") || password.equals("") || email.equals("") || role == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setContentText("Please fill the form.");
			alert.show();
    		return;
    	}
    	
    	DBUtils.addUserToDatabase(fullName, email, username, password, role);
    	
    	fullNameField.setText("");
    	usernameField.setText("");
    	passwordField.setText("");
    	emailField.setText("");
    	roleField.setValue(null);
    	
    	updateTable();
    }

    @FXML
    void update(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }
    
    /**
     * Loads the data currently stored in the database into the table
     */
    private void updateTable() {
    	ObservableList<User> users = DBUtils.getUsers();
    	
    	idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
    	fullNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
    	emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
    	usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
    	roleColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("role"));
    	passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
    	
    	table.setItems(users);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Integer[] options = {0, 1};
		roleField.getItems().addAll(options);
		updateTable();
	}

}
