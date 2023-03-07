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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

public class ProductController implements Initializable {

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<Product, Integer> inventoryIdColumn;

    @FXML
    private TextField descriptionField;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TextField categoryField;

    @FXML
    private Button btnDelete;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TextField priceField;

    @FXML
    private Button btnAdd;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, Float> priceColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;
    
    @FXML
    private TableColumn<Product, String> imageColumn;
    
    @FXML
    private TextField imageField;
    
    @FXML
    private TextField stockField;
    
    @FXML
    private TableColumn<Product, Integer> stockColumn;

    @FXML
    void add(ActionEvent event) {
    	String name = nameField.getText();
    	String category = categoryField.getText();
    	String description = descriptionField.getText();
    	String image = imageField.getText();
    	Float price = Float.parseFloat(priceField.getText());
    	Integer stock = Integer.parseInt(stockField.getText());
    	
    	if(name.equals("") || category.equals("") || description.equals("") || image.equals("") || price == null || stock == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setContentText("Please fill the form.");
			alert.show();
    		return;
    	}
    	
    	DBUtils.addProduct(name, price, category, description, image, stock);
    	
    	updateTable();
    	resetFields();
    }

    @FXML
    void update(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {
		Product product = table.getSelectionModel().getSelectedItem();

    	DBUtils.deleteProduct(product.getInventoryId());
    	
    	resetFields();
    	updateTable();
    }
    
    void updateTable() {
    	ObservableList<Product> products = DBUtils.getProducts();
    	
    	productIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productId"));
    	inventoryIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("inventoryId"));
    	categoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
    	imageColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("image"));
    	stockColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
    	
    	table.setItems(products);
    }
    
    private void resetFields() {
    	categoryField.setText("");
    	descriptionField.setText("");
    	nameField.setText("");
    	priceField.setText("");	
    	stockField.setText(null);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Set fields to only take numerical values
		priceField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
		stockField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		
		// Update text fields on click
		table.setOnMouseClicked((MouseEvent event) ->{
			Product product = table.getSelectionModel().getSelectedItem();
			if(product != null) {
				nameField.setText(product.getName());
				categoryField.setText(product.getCategory());
				descriptionField.setText(product.getDescription());
				priceField.setText(Float.toString(product.getPrice()));
				imageField.setText(product.getImage());
				stockField.setText(Integer.toString(product.getStock()));
			}
		});
		
		updateTable();
	}

}
