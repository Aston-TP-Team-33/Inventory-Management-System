package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class SidebarController {

    @FXML
    private BorderPane bp;

    @FXML
    private AnchorPane ap;

    @FXML
    void homePage(MouseEvent  event) {
    	bp.setCenter(ap);
    }

    @FXML
    void usersPage(MouseEvent  event) {
    	loadPage("users");
    }

    @FXML
    void ordersPage(MouseEvent  event) {
    	loadPage("orders");
    }

    @FXML
    void productsPage(MouseEvent  event) {
    	loadPage("products");
    }

    @FXML
    void inventoryPage(MouseEvent  event) {
    	loadPage("inventory");
    }
    
    //TODO use DBUtils?
    private void loadPage(String page) {
    	Parent root = null;
    	
    	try {
			root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	bp.setCenter(root);
    }

}
