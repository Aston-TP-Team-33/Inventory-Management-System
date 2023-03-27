package application;

import java.net.URL;
import java.util.ResourceBundle;

import application.models.Product;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class ProductController implements Initializable {

    @FXML
    private TableColumn<Product, String> imageColumn;

    @FXML
    private TextField imageField;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField titleField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TextField categoryField;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private Button btnDelete;

    @FXML
    private TableColumn<Product, String> titleColumn;

    @FXML
    private TextField priceField;

    @FXML
    private Button btnAdd;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, Float> priceColumn;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    void add(ActionEvent event) {
    	String title = titleField.getText();
    	String category = categoryField.getText();
    	String description = descriptionField.getText();
    	String image = imageField.getText();
    	String priceText = priceField.getText();
    	String quantityText = quantityField.getText();
    	
    	if(title.equals("") || category.equals("") || description.equals("") || image.equals("") || priceText.equals("") || quantityText.equals("")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setContentText("Please fill the form.");
			alert.show();
    		return;
    	}
    	
    	Float price = Float.parseFloat(priceText);
    	int quantity = Integer.parseInt(quantityText);
    	
    	DBUtils.addProduct(title, price, category, description, image, quantity);
    	
    	updateTable();
    	resetFields();
    }

    @FXML
    void update(ActionEvent event) {
    	Product product = table.getSelectionModel().getSelectedItem();

    	
    	if(product == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setContentText("Please select an item from the table");
			alert.show();
    		return;
    	}
    	
    	int id = product.getId();
    	String title = titleField.getText();
    	String category = categoryField.getText();
    	String description = descriptionField.getText();
    	String image = imageField.getText();
    	String priceText = priceField.getText();
    	String quantityText = quantityField.getText();
    	
    	if(title.equals("") || category.equals("") || description.equals("") || image.equals("") ||  priceText.equals("") || quantityText.equals("")) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setContentText("Please select an item from the table");
			alert.show();
    		return;
    	}
    	
    	Float price = Float.parseFloat(priceText);
    	int quantity = Integer.parseInt(quantityText);
    	
    	DBUtils.updateProduct(title, price, category, description, image, quantity, id);
    	
    	updateTable();
    	resetFields();
    }

    @FXML
    void delete(ActionEvent event) {
		Product product = table.getSelectionModel().getSelectedItem();
		
    	if(product == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText("Please select an item from the table");
    		alert.show();
    		return;
    	}
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.setHeaderText("Are you sure?");
		alert.setContentText("Are you sure you would like to delete " + product.getTitle() + " from the table?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			DBUtils.deleteProduct(product.getId());
		}
    	
    	resetFields();
    	updateTable();
    }
    
    void updateTable() {
    	ObservableList<Product> products = DBUtils.getProducts();
    	
    	categoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
    	idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
    	imageColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("image"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
    	quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
    	titleColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("title"));
    	
    	// Search for products
    	// Show all products by default 
    	FilteredList<Product> filteredProducts = new FilteredList<Product>(products, product -> true);
    	
    	// Update table when text is entered
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredProducts.setPredicate(product -> {
				// If nothing is entered return true
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// If title matches return true
				if (product.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
					return true;  
				} 
				// Else if ID matches return true
				else if (Integer.toString(product.getId()).contains(newValue)) {
					return true; 
				}
				
				// No match
				return false; 
			});
		});
    	
		SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
		sortedProducts.comparatorProperty().bind(table.comparatorProperty());
		
		table.setItems(sortedProducts);
    }
    
    private void resetFields() {
    	categoryField.setText("");
    	descriptionField.setText("");
    	titleField.setText("");
    	priceField.setText("");	
    	quantityField.setText(null);
    	imageField.setText("");
    	searchField.setText("");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Set fields to only take numerical values
		priceField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
		quantityField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		
		// Update text fields on click
		table.setOnMouseClicked((MouseEvent event) ->{
			Product product = table.getSelectionModel().getSelectedItem();
			if(product != null) {
				titleField.setText(product.getTitle());
				categoryField.setText(product.getCategory());
				descriptionField.setText(product.getDescription());
				priceField.setText(Float.toString(product.getPrice()));
				imageField.setText(product.getImage());
				quantityField.setText(Integer.toString(product.getQuantity()));
			}
		});
		
		updateTable();
	}

}
