package application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IndividualProductController {

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, String> imageColumn;

    @FXML
    private TableColumn<Product, String> titleColumn;

    @FXML
    private Label productName;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, Float> priceColumn;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;
    
    public void setOrderName(String product) {
    	productName.setText(product);
    }
    
    void loadTable(ObservableList<Product> products) {
    	  
    	quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
    	imageColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("image"));
    	titleColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("title"));
    	categoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
    	idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));

    	table.setItems(products);
    }
}
