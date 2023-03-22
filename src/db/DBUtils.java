package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import application.models.Order;
import application.models.Product;
import application.models.User;
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
	
	public static Connection getConnection(){
		// SSH Info
		String sshHost = "cs2410-web01pvm.aston.ac.uk"; 
		String sshUsername = "u-210151525"; 
		String sshPassword = "LtRKNSRcD+kQQ+j+";
		int sshPort = 22; 
		
		// DB Info
		String databaseHost = "localhost";
		int databasePort = 3306;
		String databaseUsername = "u-210151525"; 
		String databasePassword = "eIbZxnt2MeJkJiT";
		String databaseName = "u_210151525_laravel";
		
		Connection connection = null;
		
		try {
			// Create SSH connection
		    JSch jsch = new JSch();
		    Session session = jsch.getSession(sshUsername, sshHost, sshPort);
		    session.setPassword(sshPassword);
		    session.setConfig("StrictHostKeyChecking", "no");
		    session.connect();

		    int tunnelLocalPort = 0; // replace with a local port number that is not already in use
		    int tunnelRemotePort = databasePort;
		    String tunnelRemoteHost = databaseHost;
		    int assignedPort = session.setPortForwardingL(tunnelLocalPort, tunnelRemoteHost, tunnelRemotePort);

		    String url = "jdbc:mysql://localhost:" + assignedPort + "/" + databaseName;
		    connection = DriverManager.getConnection(url, databaseUsername, databasePassword);
		    
//		    connection.close();
//		    session.disconnect();
		} catch (Exception e) {
		    // handle any errors that occur
			e.printStackTrace();
		}
		
	    return connection;
	}
	
	/**
	 * Checks if user exists, if they are an admin and logs them into the program.
	 * @param event - Event that caused the method to run
	 * @param name - username entered into the text field
	 * @param password - password entered into the password field
	 */
	public static void loginUser(ActionEvent event, String email, String password) {
		Connection connection = null;
		PreparedStatement checkLoginCredentials = null;
		ResultSet resultSet = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		try {
			// Connect to database
			//TODO connect to host database
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Query user
			checkLoginCredentials = connection.prepareStatement("SELECT password, type FROM users WHERE email = ?;");
			checkLoginCredentials.setString(1, email);
			
			resultSet = checkLoginCredentials.executeQuery();
			
			
			// If user does not exist then display an error
			if(!resultSet.isBeforeFirst()) {
				alert.setContentText("Incorrect login credentials please try again.");
				alert.show();
				return;
			}
			
			resultSet.next();
			String retrievedPassword = resultSet.getString("password");
			int retrievedType = resultSet.getInt("type");
									
			Verifyer v = BCrypt.verifyer();
			
			// Check password by comparing the entered password to the password from the database 
			if(!v.verify(password.toCharArray(), retrievedPassword.toCharArray()).verified) {
				alert.setContentText("Incorrect login credentials please try again.");
				alert.show();
				return;
			}
			
			// Check role
			if(retrievedType != 1) {
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
	public static void addUser(String name, String email, String password, int type) {
		Connection connection = null;
		PreparedStatement addUser = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Hash users password 
			String hashedPassword = BCrypt.withDefaults().hashToString(10, password.toCharArray());
			
			// Prepare statement to add user
			addUser = connection.prepareStatement("INSERT INTO `users` (`name`, `email`, `password`, `type`) VALUES (?, ?, ?, ?);");
			addUser.setString(1, name);
			addUser.setString(2, email);
			addUser.setString(3, hashedPassword);
			addUser.setString(4, Integer.toString(type));
			
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
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");	
			connection = getConnection();
			// Query users
			getUsers = connection.createStatement();
			ResultSet rs = getUsers.executeQuery("SELECT * FROM users");
			
			// Add users to list
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				int type = rs.getInt("type");
				
				User user = new User(name, email, password, type, id);
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


	/**
	 * Removes a user from the database based on their id 
	 * @param id - id of the user to be deleted 
	 */
	public static void removeUser(int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement deleteUser = null;
		// Connect to database
		//TODO connect to host database
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Prepare statement to add user
			deleteUser = connection.prepareStatement("DELETE FROM users WHERE id = ?");
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


	/**
	 * Update the user stored on the database based on their id (new password)
	 * @param id - id of the user to be update
	 * @param name - new name of the user
	 * @param email - new email of the user
	 * @param password - new password of the user
	 * @param type - new type of the user
	 */
	public static void updateUser(Integer id, String name, String email, String password, Integer type) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement updateUser = null;
		// Connect to database
		//TODO connect to host database
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Prepare statement to add user
			updateUser = connection.prepareStatement("UPDATE users SET `name` = ?, `email` = ?, `password` = ?, `type` = ? WHERE id = ?;");
			
			// Hash Password 
			String hashedPassword = BCrypt.withDefaults().hashToString(10, password.toCharArray());
			
			updateUser.setString(1, name);
			updateUser.setString(2, email);
			updateUser.setString(3, hashedPassword);
			updateUser.setInt(4, type);
			updateUser.setInt(5, id);

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
	

	/**
	 * Update the user stored on the database based on their id (no new password)
	 * @param id - id of the user to be update
	 * @param name - new name of the user
	 * @param email - new email of the user
	 * @param password - new password of the user
	 * @param type - new type of the user
	 */
	public static void updateUser(Integer id, String name, String email, Integer type) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement updateUser = null;
		// Connect to database
		//TODO connect to host database
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Prepare statement to add user
			updateUser = connection.prepareStatement("UPDATE users SET `name` = ?, `email` = ?, `type` = ? WHERE id = ?;");
			
			updateUser.setString(1, name);
			updateUser.setString(2, email);
			updateUser.setInt(3, type);
			updateUser.setInt(4, id);

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
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");	
			connection = getConnection();
			
			// Query users
			getOrders = connection.createStatement();
			ResultSet rs = getOrders.executeQuery("SELECT * FROM user_orders");
			
			// Add users to list
			while(rs.next()) {
				int orderId = rs.getInt("id");
				int userId = rs.getInt("user_id");
				int houseNumber = rs.getInt("house_number");
				String street = rs.getString("street");
				String postcode = rs.getString("postcode");
				String city = rs.getString("city");
				int productId = rs.getInt("product_id");
				String orderStatus = rs.getString("order_status");
				float price = rs.getFloat("price");
				int quantity = rs.getInt("quantity");
				
				Order order = new Order(orderId, userId, houseNumber, street, postcode, city, productId, price, orderStatus, quantity);
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
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Prepare statement to add user
			deleteOrder = connection.prepareStatement("DELETE FROM user_orders WHERE id = ?");
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
	
	public static ObservableList<User> seeUser(int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		Connection connection = null;
		PreparedStatement seeUser = null;
		
		ObservableList<User> users = FXCollections.observableArrayList();
		
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");		
			connection = getConnection();
			
			// Prepare statement to get products associated with current order
			seeUser = connection.prepareStatement("SELECT * FROM users WHERE id = ?;");
			seeUser.setInt(1, id);
			ResultSet rs = seeUser.executeQuery();
			
			while(rs.next()) {
				User user = new User(
						rs.getString("name"),
						rs.getString("email"),
						rs.getString("password"),
						rs.getInt("type"),
						rs.getInt("id")
				);
				
				users.add(user);
			}
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
		
		return users;
	}
	
	public static ObservableList<Product> seeProduct(int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		Connection connection = null;
		PreparedStatement seeProduct = null;
		
		ObservableList<Product> products = FXCollections.observableArrayList();
		
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");		
			connection = getConnection();
			
			// Prepare statement to get products associated with current order
			seeProduct = connection.prepareStatement("SELECT * FROM products WHERE id = ?;");
			seeProduct.setInt(1, id);
			ResultSet rs = seeProduct.executeQuery();
			
			while(rs.next()) {
				Product product = new Product(
						rs.getInt("id"),
						rs.getString("title"),
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

	public static void updateOrder(String status, String city, String postcode, String street, int houseNumber, int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement updateOrder = null;
		// Connect to database
		//TODO connect to host database
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Prepare statement to add order
			updateOrder = connection.prepareStatement("UPDATE `user_orders` SET `order_status` = ?, `house_number` = ?, `street` = ?, `postcode` = ?, `city` = ? WHERE id = ?;");

			updateOrder.setString(1, status);
			updateOrder.setInt(2, houseNumber);
			updateOrder.setString(3, street);
			updateOrder.setString(4, postcode);
			updateOrder.setString(5, city);
			updateOrder.setInt(6, id);

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
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");	
			connection = getConnection();
			
			// Query products
			getProducts = connection.createStatement();
			ResultSet rs = getProducts.executeQuery("SELECT * FROM products;");
			
			// Add products to list
			while(rs.next()) {
				Product product = new Product(
						rs.getInt("id"),
						rs.getString("title"),
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
	public static void addProduct(String title, Float price, String category, String description, String image, int quantity) {
		Connection connection = null;
		PreparedStatement addProduct = null;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		// Connect to database
		//TODO connect to host database
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Prepare statement to add product
			addProduct = connection.prepareStatement("INSERT INTO `products` (`id`, `title`, `price`, `category`, `description`, `image`, `quantity` ) VALUES (NULL, ?, ?, ?, ?, ?, ?);");
			
			addProduct.setString(1, title);
			addProduct.setFloat(2, price);
			addProduct.setString(3, category);
			addProduct.setString(4, description);
			addProduct.setString(5, image);
			addProduct.setInt(6, quantity);
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
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Delete from inventory table - this will automatically delete it from the product table also
			deleteProduct = connection.prepareStatement("DELETE FROM products WHERE id = ?");
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
	
	public static void updateProduct(String title, Float price, String category, String description, String image, int quantity, int id) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		Connection connection = null;
		PreparedStatement updateProduct = null;
		
		// Connect to database
		//TODO connect to host database
		try {
			//connection = DriverManager.getConnection("jdbc:mysql://localhost/group33", "root", "");
			connection = getConnection();
			
			// Prepare statement to update tables
			updateProduct = connection.prepareStatement("UPDATE `products` SET `title` = ?, `price` = ?, `category` = ?, `description` = ?, `image` = ?, `quantity` = ? WHERE id = ?;");
			
			updateProduct.setString(1, title);
			updateProduct.setFloat(2, price);
			updateProduct.setString(3, category);
			updateProduct.setString(4, description);
			updateProduct.setString(5, image);
			updateProduct.setInt(6, quantity);
			updateProduct.setInt(7, id);
						
			updateProduct.executeUpdate();	
		} catch (SQLException e) {
			alert.setContentText(e.getMessage());
			alert.show();
		}catch(Exception e) {
			alert.setContentText("An unexpected error has occured.");
			alert.show();
		}
		
	}
}
