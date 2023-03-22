package application;

import application.models.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IndividualUserController {

    @FXML
    private TableView<User> table1;

    @FXML
    private TableColumn<User, String> fullNameColumn;

    @FXML
    private TableColumn<User, Integer> typeColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private Label userName;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> emailColumn;
    
    public void setOrderName(String name) {
    	userName.setText(name);
    }
    
    void loadTable(ObservableList<User> users) {
    	  
    	fullNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
    	typeColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("type"));
    	passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
    	idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
    	emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
    	
    	table1.setItems(users);
    }
}


