package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Order;
import application.Product;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @author Remy Thompson
 *
 */
public class DBUtils {
	
	/**
	 * Changes the scene to the fxmlFile passed in. If there is no username passed in then it will redirect to the login page.
	 * @param event - action event that caused the scene change
	 * @param title - title of the new scene
	 * @param fxmlFile - name of the fxmlFile to change to
	 * @param username - username of the user currently signed in
	 */
	public static void changeScene(ActionEvent event, String title, String fxmlFile) {
		Parent root = null;
		
		
		try {
			FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
			root = loader.load();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle(title);
		stage.setScene(new Scene(root, 1000, 500));
		stage.setResizable(false);
		stage.show();
	}

	
	/**
	 * Checks if user exists, if they are an admin and logs them into the program.
	 * @param event - Event that caused the method to run
	 * @param username - username entered into the text field
	 * @param password - password entered into the password field
	 */
	public static void loginUser(ActionEvent event, String username, String password) {
		Connection connection = null;
		PreparedStatement checkLoginCredentials = null;
		ResultSet resultSet = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		try {
			// Connect to database
			//TODO connect to host database
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Query user
			checkLoginCredentials = connection.prepareStatement("SELECT password, role FROM user WHERE username = ?;");
			checkLoginCredentials.setString(1, username);
			
			resultSet = checkLoginCredentials.executeQuery();
			
			// If user does not exist then display an error
			if(!resultSet.isBeforeFirst()) {
				alert.setContentText("Incorrect login credentials please try again.");
				alert.show();
				return;
			}
			
			resultSet.next();
			String retrievedPassword = resultSet.getString("password");
			int retrievedRole = resultSet.getInt("role");
						
			// Check password
			if(!password.equals(retrievedPassword)) {
				alert.setContentText("Incorrect login credentials please try again.");
				alert.show();
				return;
			}
			
			// Check role
			if(retrievedRole != 1) {
				alert.setContentText("You are not an admin. Please contact an administrator to upgrade your role.");
				alert.show();
				return;
			}
			
			DBUtils.changeScene(event, "Test", "../application/SideNavigation.fxml");
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		} catch(Exception e) {
			e.printStackTrace();
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}
	
	/**
	 * Adds a new user to the database
	 * @param name - name entered into the text field
	 * @param email - email entered into the text field
	 * @param username - username entered into the email field
	 * @param password - password entered into the password field
	 * @param role - role selected 
	 */
	public static void addUser(String name, String email, String username, String password, int role) {
		Connection connection = null;
		PreparedStatement addUser = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Prepare statement to add user
			addUser = connection.prepareStatement("INSERT INTO `user` (`user_id`, `full_name`, `email`, `username`, `password`, `role`) VALUES (NULL, ?, ?, ?, ?, ?);");
			addUser.setString(1, name);
			addUser.setString(2, email);
			addUser.setString(3, username);
			addUser.setString(4, password);
			addUser.setString(5, Integer.toString(role));
			
			// Add user to database
			addUser.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}
		catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}
	
	/**
	 * Returns all the users currently in the database
	 * @return an ObervableList of users
	 */
	public static ObservableList<User> getUsers(){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		Statement getUsers = null;
			
		ObservableList<User> users = FXCollections.observableArrayList();
		
		try {
			// Connect to database
			//TODO connect to host database
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");	
			
			// Query users
			getUsers = connection.createStatement();
			ResultSet rs = getUsers.executeQuery("SELECT * FROM user");
			
			// Add users to list
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setFullName(rs.getString("full_name"));
				user.setEmail(rs.getString("email"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getInt("role"));
				users.add(user);
			}			
		} catch(SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		} catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}

		return users;
	}


	public static void removeUser(int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement deleteUser = null;
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Prepare statement to add user
			deleteUser = connection.prepareStatement("DELETE FROM user WHERE `user`.`user_id` = ?");
			deleteUser.setInt(1, id);
			
			// Delete user from
			deleteUser.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}


	public static void updateUser(Integer id, String fullName, String email, String username, String password, Integer role) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement updateUser = null;
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Prepare statement to add user
			updateUser = connection.prepareStatement("UPDATE `user` SET `full_name` = ?, `email` = ?, `username` = ?, `password` = ?, `role` = ? WHERE `user`.`user_id` = ?;");
			
			updateUser.setString(1, fullName);
			updateUser.setString(2, email);
			updateUser.setString(3, username);
			updateUser.setString(4, password);
			updateUser.setInt(5, role);
			updateUser.setInt(6, id);

			// Update user 
			updateUser.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}	
	}
	
	public static ObservableList<Order> getOrders() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		Statement getOrders = null;
			
		ObservableList<Order> orders = FXCollections.observableArrayList();
		
		try {
			// Connect to database
			//TODO connect to host database
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");	
			
			// Query users
			getOrders = connection.createStatement();
			ResultSet rs = getOrders.executeQuery("SELECT * FROM orders");
			
			// Add users to list
			while(rs.next()) {
				Order order = new Order();
				order.setOrder_date(rs.getDate("order_date"));
				order.setStatus(rs.getString("status"));
				order.setTotal(rs.getFloat("total"));
				order.setUser_id(rs.getInt("user_id"));
				order.setOrder_id(rs.getInt("order_id"));
				
				orders.add(order);
			}					
		} catch(SQLException e) {
			e.printStackTrace();
			alert.setContentText(e.getMessage());
			alert.show();
		} catch(Exception e) {
			e.printStackTrace();
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}

		return orders;
	}
	
	public static void removeOrder(int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement deleteOrder = null;
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Prepare statement to add user
			deleteOrder = connection.prepareStatement("DELETE FROM orders WHERE order_id = ?");
			deleteOrder.setInt(1, id);
			
			// Delete order from database
			deleteOrder.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}
	
	public static ObservableList<Product> seeProducts(int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		Connection connection = null;
		PreparedStatement getProducts = null;
		
		ObservableList<Product> products = FXCollections.observableArrayList();
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");		
			
			// Prepare statement to get products associated with current order
			getProducts = connection.prepareStatement("SELECT * FROM product INNER JOIN order_item INNER JOIN inventory WHERE product.product_id = order_item.product_id AND product.inventory_id = inventory.inventory_id AND order_item.order_id = ?;");
			getProducts.setInt(1, id);
			ResultSet rs = getProducts.executeQuery();
			
			while(rs.next()) {
				Product product = new Product(
						rs.getInt("product_id"),
						rs.getInt("inventory_id"),
						rs.getString("name"),
						rs.getFloat("price"),
						rs.getString("category"),
						rs.getString("description"),
						rs.getString("image"),
						rs.getInt("quantity")
				);
				
				products.add(product);
			}
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
		
		return products;
	}


	public static void updateOrder(String status, int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement updateOrder = null;
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Prepare statement to add order
			updateOrder = connection.prepareStatement("UPDATE `orders` SET `status` = ? WHERE order_id = ?;");
			
			updateOrder.setString(1, status);
			updateOrder.setInt(2, id);

			// Update order 
			updateOrder.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
		
	}
	
	/**
	 * Returns all the products currently in the database
	 * @return an ObervableList of products
	 */
	public static ObservableList<Product> getProducts(){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		Statement getProducts = null;
			
		ObservableList<Product> products = FXCollections.observableArrayList();
		
		try {
			// Connect to database
			//TODO connect to host database
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");	
			
			// Query products
			getProducts = connection.createStatement();
			ResultSet rs = getProducts.executeQuery("SELECT * FROM product INNER JOIN inventory WHERE product.inventory_id = inventory.inventory_id;");
			
			// Add products to list
			while(rs.next()) {
				Product product = new Product(
						rs.getInt("product_id"),
						rs.getInt("inventory_id"),
						rs.getString("name"),
						rs.getFloat("price"),
						rs.getString("category"),
						rs.getString("description"),
						rs.getString("image"),
						rs.getInt("quantity")
				);
				
				products.add(product);
			}
		} catch(SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		} catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}

		return products;
	}

	/**
	 * Adds a new product to the database
	 * @param name - name entered into the text field
	 * @param price - price enter into the price field 
	 * @param category - category entered into the category field
	 * @param description - description entered into the description field
	 * @param role - role selected 
	 */
	public static void addProduct(String name, Float price, String category, String description, String image, int stock) {
		Connection connection = null;
		PreparedStatement addProduct = null;
		PreparedStatement addInventory = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			// Insert into inventory
			addInventory = connection.prepareStatement("INSERT INTO `inventory` (`inventory_id`, `quantity`) VALUES (NULL, ?)", Statement.RETURN_GENERATED_KEYS);
			addInventory.setInt(1, stock);
			addInventory.executeUpdate();
			Integer inventoryId = null;
			
	        try (ResultSet generatedKeys = addInventory.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                inventoryId = generatedKeys.getInt(1);
	            }
	           }

			
			// Prepare statement to add user
			addProduct = connection.prepareStatement("INSERT INTO `product` (`product_id`, `inventory_id`, `name`, `price`, `category`, `description`, `image`) VALUES (NULL, ?, ?, ?, ?, ?, ?);");
			
			addProduct.setInt(1, inventoryId);
			addProduct.setString(2, name);
			addProduct.setFloat(3, price);
			addProduct.setString(4, category);
			addProduct.setString(5, description);
			addProduct.setString(6, image);
			
			// Add user to database
			addProduct.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}
		catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}
	
	public static void deleteProduct(int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement deleteProduct = null;
		
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Delete from inventory table - this will automatically delete it from the product table also
			deleteProduct = connection.prepareStatement("DELETE FROM inventory WHERE inventory_id = ?");
			deleteProduct.setInt(1, id);
						
			// Delete order from database
			deleteProduct.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
	}
	
	public static void updateProduct(String name, float price, String category, String description, String image, int stock, int inventoryId) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement updateProduct = null;
		PreparedStatement updateInventory = null; 
		
		// Connect to database
		//TODO connect to host database
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/team33", "root", "");
			
			// Prepare statement to update tables
			updateProduct = connection.prepareStatement("UPDATE `product` SET `name` = ?, `price` = ?, `category` = ?, `description` = ?, `image` = ? WHERE inventory_id = ?;");
			updateInventory = connection.prepareStatement("UPDATE `inventory` SET quantity = ? WHERE inventory_id = ?");
			
			updateProduct.setString(1, name);
			updateProduct.setFloat(2, price);
			updateProduct.setString(3, category);
			updateProduct.setString(4, description);
			updateProduct.setString(5, image);
			updateProduct.setInt(6, inventoryId);
			
			updateInventory.setInt(1, stock);
			updateInventory.setInt(2, inventoryId);
			
			updateProduct.executeUpdate();	
			updateInventory.executeUpdate();
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
		
	}
}
