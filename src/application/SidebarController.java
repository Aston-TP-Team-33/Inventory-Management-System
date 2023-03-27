package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * @author Remy Thompson
 *
 */
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
    void reportPage(MouseEvent  event) {
    	loadPage("report");
    }

    /**
     * Loads the replace the current page with the page the user clicked on
     * @param page - name of the page to load
     */
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
