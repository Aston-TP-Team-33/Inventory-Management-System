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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

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
    private TableColumn<Order, Date> orderDateColumn;

    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Order, Float> totalColumn;

    @FXML
    private Button btnSeeProducts;

    @FXML
    private TableView<Order> table;

    /**
     * Loads list of products for selected order
     * @param event
     */
    @FXML
    void seeProducts(ActionEvent event) {
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
    	
    	// Get products for selected order
    	ObservableList<Product> products = DBUtils.seeProducts(order.getOrder_id());    		
    	
    	// Load page for individual order
    	Button btn = (Button) event.getSource();
    	Scene scene = btn.getScene();
    	BorderPane bp = (BorderPane) scene.lookup("#bp");
    	
    	Parent root = null;
    	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("individualOrder.fxml"));

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
    	IndividualOrderController ioc = loader.getController();
    	ioc.loadTable(products);
    	// Rename label to match order
    	ioc.setOrderName("Order " + order.getOrder_id());
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
    	
    	DBUtils.updateOrder(status, order.getOrder_id());
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
		alert.setContentText("Are you sure you would like to delete Order " + order.getOrder_id() + " from the table?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			DBUtils.removeOrder(order.getOrder_id());
		}
		
		resetFields();
		updateTable();
    }
    
    private void updateTable() {
    	ObservableList<Order> orders = DBUtils.getOrders();
  
    	userIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("user_id"));
    	orderDateColumn.setCellValueFactory(new PropertyValueFactory<Order, Date>("order_date"));
    	orderIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("order_id"));
    	statusColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
    	totalColumn.setCellValueFactory(new PropertyValueFactory<Order, Float>("total"));
    	
    	table.setItems(orders);
    }
    
    private void resetFields() {
    	statusField.setValue(null);	
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Setup role selection
		String[] options = {"pending", "shipped", "completed", "cancelled"};
		statusField.getItems().addAll(options);
		
		// Setup table
		// Update text fields on click
		table.setOnMouseClicked((MouseEvent event) ->{
			Order order = table.getSelectionModel().getSelectedItem();
			if(order != null) {
				statusField.getSelectionModel().select(order.getStatus());
			}
		});
		
		updateTable();
	}

}

