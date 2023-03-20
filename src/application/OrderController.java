package application;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import db.DBUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.converter.IntegerStringConverter;

/**
 * @author Remy Thompson
 *
 */
public class OrderController implements Initializable{

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private Button btnDelete;

    @FXML
    private ChoiceBox<String> statusField;

    @FXML
    private TableColumn<Order, Integer> userIdColumn;

    @FXML
    private TableColumn<Order, Date> productIdColumn;

    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnSeeProduct;

    @FXML
    private TableView<Order> table;
    
    @FXML
    private TableColumn<Order, String> cityColumn;
    
    @FXML
    private TableColumn<Order, String> streetColumn;
    
    @FXML
    private TableColumn<Order, Integer> houseNumberColumn;
    
    @FXML
    private TableColumn<Order, String> postcodeColumn;
    
    @FXML
    private TextField cityField;
    
    @FXML
    private TextField houseNumberField;
    
    @FXML
    private TextField postcodeField;
    
    @FXML
    private TextField streetField;
    
    
    /**
     * Loads list of products for selected order
     * @param event
     */
    @FXML
    void seeUser(ActionEvent event) {
    	// Get selected order
    	Order order = table.getSelectionModel().getSelectedItem();
    	
    	// Display error if no order is selected 
    	if(order == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText("Please select an item from the table");
    		alert.show();
    		return;
    	}
    	
    	// Get user for selected order
    	ObservableList<User> users = DBUtils.seeUser(order.getUserId());    		
    	
    	// Load page for individual order
    	Button btn = (Button) event.getSource();
    	Scene scene = btn.getScene();
    	BorderPane bp = (BorderPane) scene.lookup("#bp");
    	
    	Parent root = null;
    	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("individualUser.fxml"));

    	try {
			root = loader.load();
		} catch (IOException e) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText(e.getMessage());
    		alert.show();
    		return;
		}
    	
    	bp.setCenter(root);
    	
    	// Fill the table
    	IndividualUserController iuc = loader.getController();
    	iuc.loadTable(users);
    	// Rename label to match order
    	iuc.setOrderName("User " + order.getUserId());
    }
    
    @FXML
    void seeProduct(ActionEvent event) {
    	// Get selected order
    	Order order = table.getSelectionModel().getSelectedItem();
    	
    	// Display error if no order is selected 
    	if(order == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText("Please select an item from the table");
    		alert.show();
    		return;
    	}
    	
    	// Get user for selected order
    	ObservableList<Product> products = DBUtils.seeProduct(order.getUserId());    		
    	
    	// Load page for individual order
    	Button btn = (Button) event.getSource();
    	Scene scene = btn.getScene();
    	BorderPane bp = (BorderPane) scene.lookup("#bp");
    	
    	Parent root = null;
    	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("individualProduct.fxml"));

    	try {
			root = loader.load();
		} catch (IOException e) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText(e.getMessage());
    		alert.show();
    		return;
		}
    	
    	bp.setCenter(root);
    	
    	// Fill the table
    	IndividualProductController ipc = loader.getController();
    	ipc.loadTable(products);
    	// Rename label to match order
    	ipc.setOrderName("Product " + order.getProductId());
    }

    @FXML
    void update(ActionEvent event) {
    	Order order = table.getSelectionModel().getSelectedItem();
    	
    	if(order == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText("Please select an item from the table");
    		alert.show();
    		return;
    	}
    	
    	String status = statusField.getValue();
    	String city = cityField.getText();
    	String postcode = postcodeField.getText();
    	String street = streetField.getText();
    	String houseNumberText = houseNumberField.getText();
    	Integer id = order.getOrderId();
    	
    	if(status.equals("") || city.equals("") || postcode.equals("") || street.equals("") || houseNumberText.equals("") || id == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setContentText("Please select an item from the table");
			alert.show();
    		return;
    	}
    	
    	Integer houseNumber = Integer.parseInt(houseNumberField.getText());
    	DBUtils.updateOrder(status, city, postcode, street, houseNumber, id);
    	
    	resetFields();
    	updateTable();
    }

    @FXML
    void delete(ActionEvent event) {
    	Order order = table.getSelectionModel().getSelectedItem();
    	
    	// If no user is selected display an error
    	if(order == null) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.setContentText("Please select an item from the table");
    		alert.show();
    		return;
    	}
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.setHeaderText("Are you sure?");
		alert.setContentText("Are you sure you would like to delete Order " + order.getOrderId() + " from the table?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			DBUtils.removeOrder(order.getOrderId());
		}
		
		resetFields();
		updateTable();
    }
    
    private void updateTable() {
    	ObservableList<Order> orders = DBUtils.getOrders();
  
    	userIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("userId"));
    	productIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Date>("productId"));
    	orderIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
    	statusColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderStatus"));
    	cityColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("city"));
    	postcodeColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("postcode"));
    	houseNumberColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("houseNumber"));
    	streetColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("street"));
    	table.setItems(orders);
    }
    
    private void resetFields() {
    	statusField.setValue(null);	
    	houseNumberField.setText("");
    	cityField.setText("");
    	postcodeField.setText("");
    	streetField.setText("");
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// House number can only be set to integer 
		houseNumberField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		
		// Setup role selection
		String[] options = {"Placed", "Dispatched", "Delivered", "Cancelled"};
		statusField.getItems().addAll(options);
		
		// Setup table
		// Update text fields on click
		table.setOnMouseClicked((MouseEvent event) ->{
			Order order = table.getSelectionModel().getSelectedItem();
			if(order != null) {
				statusField.getSelectionModel().select(order.getOrderStatus());
				cityField.setText(order.getCity());
				streetField.setText(order.getStreet());
				postcodeField.setText(order.getPostcode());
				houseNumberField.setText(Integer.toString(order.getHouseNumber()));
			}
		});
		
		updateTable();
	}

}

