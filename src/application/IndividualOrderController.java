package application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IndividualOrderController {
    @FXML
    private Button btnDelete;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> inventoryIdColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;
    
    @FXML
    private Label orderName;

    @FXML
    void delete(ActionEvent event) {
    	//TODO deleting a product should also update the total cost of the order
    }
    
    public void setOrderName(String name) {
    	orderName.setText(name);
    }
    
    void loadTable(ObservableList<Product> products) {
    	  
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
    	inventoryIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("inventoryId"));
    	categoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
    	productIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productId"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
    	
    	table.setItems(products);
    }
}


