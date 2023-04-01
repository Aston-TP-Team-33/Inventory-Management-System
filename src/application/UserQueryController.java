package application;

import java.net.URL;
import java.util.ResourceBundle;

import application.models.Order;
import application.models.UserQuery;
import db.DBUtils;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserQueryController implements Initializable{

    @FXML
    private TableColumn<UserQuery, String> queryColumn;

    @FXML
    private TableColumn<UserQuery, String> fullNameColumn;

    @FXML
    private TableView<UserQuery> table;

    @FXML
    private TableColumn<UserQuery, Integer> idColumn;

    @FXML
    private TableColumn<UserQuery, String> emailColumn;
    
    @FXML
    private TextField searchField;
    
    /**
     * Loads the data currently stored in the database into the table
     */
    private void updateTable() {
    	ObservableList<UserQuery> queries = DBUtils.getQueries();
    	
    	idColumn.setCellValueFactory(new PropertyValueFactory<UserQuery, Integer>("id"));
    	fullNameColumn.setCellValueFactory(new PropertyValueFactory<UserQuery, String>("name"));
    	emailColumn.setCellValueFactory(new PropertyValueFactory<UserQuery, String>("email"));
    	queryColumn.setCellValueFactory(new PropertyValueFactory<UserQuery, String>("query"));
		
    	// Search for users
    	// Show all users by default 
    	FilteredList<UserQuery> filteredQueries = new FilteredList<UserQuery>(queries, query -> true);
    	
    	// Update table when text is entered
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredQueries.setPredicate(query -> {
				// If nothing is entered return true
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
 
				//  If name matches return true
				else if (query.getName().contains(newValue)){
					return true; 
				}
				// If email matches return true
				else if (query.getEmail().contains(newValue)) {
					return true;
				}
				// No match
				return false; 
			});
		});
    	
		SortedList<UserQuery> sortedQueries = new SortedList<>(filteredQueries);
		sortedQueries.comparatorProperty().bind(table.comparatorProperty());
		
		table.setItems(sortedQueries); 
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateTable();
	}
}
