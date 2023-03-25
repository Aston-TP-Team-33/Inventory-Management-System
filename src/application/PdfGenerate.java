package application;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class PdfGenerate {

        // Connect to the database
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
		    
		} catch (Exception e) {
		    // handle any errors that occur
			e.printStackTrace();
		}
		
	    return connection;
	}

    public static void generateReport() {
        // Create a new PDF document
        Document document = new Document(PageSize.A4);
	Connection connection = null;
        try {
	    connection = getConnection();
            PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
            document.open();

            // Add a title to the document
            Paragraph title = new Paragraph("Inventory Report");
            document.add(title);

            // Retrieve data from the orders table
            PreparedStatement ordersStmt = connection.prepareStatement("SELECT * FROM user_orders WHERE order_status='Placed'");
            ResultSet ordersResult = ordersStmt.executeQuery();

            // Create a table for the placed orders
            PdfPTable placedOrdersTable = new PdfPTable(3);
            PdfPCell cell;
            cell = new PdfPCell(new Paragraph("User ID"));
            placedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Customer Name"));
            placedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Order Total"));
            placedOrdersTable.addCell(cell);

            while (ordersResult.next()) {
            	placedOrdersTable.addCell(Integer.toString(ordersResult.getInt("user_id")));
            	placedOrdersTable.addCell(ordersResult.getString("name"));
                placedOrdersTable.addCell(Double.toString(ordersResult.getDouble("price")));
            }

            // Add the placed orders table to the document
            document.add(new Paragraph("The following items need to be dispatched:"));
            document.add(placedOrdersTable);

            // Retrieve data from the orders table
            PreparedStatement dispatchedStmt = connection.prepareStatement("SELECT * FROM user_orders WHERE order_status='Dispatched'");
            ResultSet dispatchedResult = dispatchedStmt.executeQuery();

            // Create a table for the dispatched orders
            PdfPTable dispatchedOrdersTable = new PdfPTable(3);
            cell = new PdfPCell(new Paragraph("User ID"));
            dispatchedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Customer Name"));
            dispatchedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Order Total"));
            dispatchedOrdersTable.addCell(cell);

            while (dispatchedResult.next()) {
                dispatchedOrdersTable.addCell
                (Integer.toString(dispatchedResult.getInt("user_id")));
                dispatchedOrdersTable.addCell(dispatchedResult.getString("name"));
                dispatchedOrdersTable.addCell(Double.toString(dispatchedResult.getDouble("price")));
                }

            // Add the dispatched orders table to the document
            document.add(new Paragraph("The following orders need to be delivered:"));
            document.add(dispatchedOrdersTable);

            // Retrieve data from the inventorcy table
            PreparedStatement inventoryStmt = connection.prepareStatement("SELECT * FROM products WHERE quantity < 5");
            ResultSet inventoryResult = inventoryStmt.executeQuery();

            // Create a table for the low stock items
            PdfPTable lowStockTable = new PdfPTable(2);
            cell = new PdfPCell(new Paragraph("Item Name"));
            lowStockTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Stock Level"));
            lowStockTable.addCell(cell);

            while (inventoryResult.next()) {
                lowStockTable.addCell(inventoryResult.getString("title"));
                lowStockTable.addCell(Integer.toString(inventoryResult.getInt("quantity")));
            }

            // Add the low stock items table to the document
            document.add(new Paragraph("The following items are low on stock (> 5 items):"));
            document.add(lowStockTable);

            // Close the document and the database connection
            document.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
