package application;
import java.net.URL;
import java.util.ResourceBundle;

import application.models.Product;
import application.models.User;
import db.DBUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * @author Remy Thompson
 *
 */
public class UserController implements Initializable {

    @FXML
    private TextField fullNameField;

    @FXML
    private ChoiceBox<Integer> typeField;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<User, String> fullNameColumn;

    @FXML
    private TableColumn<User, Integer> typeColumn;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAdd;

    @FXML
    private TableView<User> table;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> emailColumn;
    
    @FXML
    private TableColumn<User, String> passwordColumn;
    
    @FXML
    private TextField searchField;
    
    /**
     * Takes the users input and adds it to the database
     * @param event
     */
    @FXML
    void add(ActionEvent event) {
    	String fullName = fullNameField.getText();
    	String password = passwordField.getText();
    	String email = emailField.getText();
    	Integer type = typeField.getValue();
    	
    	// Make sure form was filled
    	if(fullName.equals("") || password.equals("") || email.equals("") || type == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setContentText("Please fill the form.");
			alert.show();
    		return;
    	}
    	
    	DBUtils.addUser(fullName, email, password, type);

    	resetFields();
    	updateTable();
    }

	/**
	 * Updates a use stored in the database
	 * @param event
	 */
	@FXML
    void update(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

		User user = table.getSelectionModel().getSelectedItem();
		
		// Make sure user is selected
    	if(user == null) {
			alert.setContentText("Please select a user.");
			alert.show();
    		return;
    	}
		
		Integer id = user.getId();
    	String fullName = fullNameField.getText();
    	String password = passwordField.getText();
    	String email = emailField.getText();
    	Integer type = typeField.getValue();
    	
    	// Make sure form was filled
    	if(fullName.equals("") || email.equals("") || type == null) {
			alert.setContentText("Please provide a name, email & type.");
			alert.show();
    		return;
    	}
    	
    	DBUtils.updateUser(id, fullName, email, password, type);
    	

    	resetFields();
    	updateTable();
    }

    /**
     * Removes selected user from database and table
     * @param event
     */
    @FXML
    void delete(ActionEvent event) {
    	User user = table.getSelectionModel().getSelectedItem();
    	
    	// If no user is selected display an error
    	if(user == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText("Please select an item from the table");
    		alert.show();
    		return;
    	}
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.setHeaderText("Are you sure?");
		alert.setContentText("Are you sure you would like to delete " + user.getName() + " from the table?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			DBUtils.removeUser(user.getId());
		}
		
		resetFields();
		updateTable();
    }
    
    /**
     * Loads the data currently stored in the database into the table
     */
    private void updateTable() {
    	ObservableList<User> users = DBUtils.getUsers();
    	
    	idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
    	fullNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
    	emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
    	typeColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("type"));
    	passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));

    	// Search for users
    	// Show all users by default 
    	FilteredList<User> filteredUsers = new FilteredList<User>(users, user -> true);
    	
    	// Update table when text is entered
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredUsers.setPredicate(user -> {
				// If nothing is entered return true
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// If name matches return true
				if (user.getName().toLowerCase().contains(newValue.toLowerCase())) {
					return true;  
				} 
				// Else if ID matches return true
				else if (Integer.toString(user.getId()).contains(newValue)) {
					return true; 
				}
				// Else if email matches return true 
				else if(user.getEmail().contains(newValue)) {
					return true;
				}
				// No match
				return false; 
			});
		});
    	
		SortedList<User> sortedUsers = new SortedList<>(filteredUsers);
		sortedUsers.comparatorProperty().bind(table.comparatorProperty());
		
		table.setItems(sortedUsers);    	
    }
    
    /**
     * Resets input fields to empty
     */
    private void resetFields() {
    	fullNameField.setText("");
    	passwordField.setText("");
    	emailField.setText("");
    	typeField.setValue(null);	
    	searchField.setText("");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Setup role selection
		Integer[] options = {1, 2};
		typeField.getItems().addAll(options);
		
		// Setup table
		// Update text fields on click
		table.setOnMouseClicked((MouseEvent event) ->{
			User user = table.getSelectionModel().getSelectedItem();
			if(user != null) {
				fullNameField.setText(user.getName());
				emailField.setText(user.getEmail());
				typeField.setValue(user.getType());
				passwordField.setText("");	
			}
		});
		
		updateTable();
	}

}
