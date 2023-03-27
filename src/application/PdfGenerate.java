package application;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import db.DBUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author Shoaib Azad & Remy Thompson
 *
 */
public class PdfGenerate {
	
    @FXML
    private Button generateButton;
	
	/**
	 * Generates a report informing the admin on the current status of the stock & orders
	 * @param event
	 */
	@FXML
    void generateReport(ActionEvent event) {
        // Create a new PDF document
        Document document = new Document(PageSize.A4);
        Connection connection = null;
        try {
        	connection = DBUtils.getConnection();
            PdfWriter.getInstance(document, new FileOutputStream("./report.pdf"));
            document.open();

            // Add a title to the document
            Paragraph title = new Paragraph("Inventory Report");
            document.add(title);

            // Retrieve data from the orders table
            PreparedStatement ordersStmt = connection.prepareStatement("SELECT * FROM user_orders WHERE order_status='Placed'");
            ResultSet ordersResult = ordersStmt.executeQuery();

            // Create a table for the placed orders            
            PdfPTable placedOrdersTable = new PdfPTable(5);
            PdfPCell cell;
            cell = new PdfPCell(new Paragraph("Order ID"));
            placedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("City"));
            placedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("House Number"));
            placedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Street Name"));
            placedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Order Total"));
            placedOrdersTable.addCell(cell);

            while (ordersResult.next()) {
            	placedOrdersTable.addCell(ordersResult.getString("id"));
            	placedOrdersTable.addCell(ordersResult.getString("city"));
            	placedOrdersTable.addCell(ordersResult.getString("house_number"));
            	placedOrdersTable.addCell(ordersResult.getString("street"));
                placedOrdersTable.addCell("£" + ordersResult.getString("price"));
            }

            // Add the placed orders table to the document
            document.add(new Paragraph("The following items need to be dispatched:"));
            document.add(new Paragraph("\n"));
            document.add(placedOrdersTable);

            // Retrieve data from the orders table
            PreparedStatement dispatchedStmt = connection.prepareStatement("SELECT * FROM user_orders WHERE order_status='Dispatched'");
            ResultSet dispatchedResult = dispatchedStmt.executeQuery();

            // Create a table for the dispatched orders
            PdfPTable dispatchedOrdersTable = new PdfPTable(5);
            cell = new PdfPCell(new Paragraph("Order ID"));
            dispatchedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("City"));
            dispatchedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("House Number"));
            dispatchedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Street Name"));
            dispatchedOrdersTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Order Total"));
            dispatchedOrdersTable.addCell(cell);

            while (dispatchedResult.next()) {
                dispatchedOrdersTable.addCell(dispatchedResult.getString("id"));
                dispatchedOrdersTable.addCell(dispatchedResult.getString("city"));
                dispatchedOrdersTable.addCell(dispatchedResult.getString("house_number"));
                dispatchedOrdersTable.addCell(dispatchedResult.getString("street"));
                dispatchedOrdersTable.addCell("£" + dispatchedResult.getString("price"));
            }

            // Add the dispatched orders table to the document
            document.add(new Paragraph("The following orders need to be delivered:"));
            document.add(new Paragraph("\n"));
            document.add(dispatchedOrdersTable);

            // Retrieve data from the inventory table
            PreparedStatement inventoryStmt = connection.prepareStatement("SELECT * FROM products WHERE quantity <= 5");
            ResultSet inventoryResult = inventoryStmt.executeQuery();

            // Create a table for the low stock items
            PdfPTable lowStockTable = new PdfPTable(3);
            cell = new PdfPCell(new Paragraph("Item ID"));
            lowStockTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Item Name"));
            lowStockTable.addCell(cell);
            cell = new PdfPCell(new Paragraph("Stock Level"));
            lowStockTable.addCell(cell);

            while (inventoryResult.next()) {
            	lowStockTable.addCell(inventoryResult.getString("id"));
            	lowStockTable.addCell(inventoryResult.getString("title"));
                lowStockTable.addCell(inventoryResult.getString("quantity"));
            }

            // Add the low stock items table to the document
            document.add(new Paragraph("The following items are low on stock (<= 5 items):"));
            document.add(new Paragraph("\n"));
            document.add(lowStockTable);

            // Close the document and the database connection
            document.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
