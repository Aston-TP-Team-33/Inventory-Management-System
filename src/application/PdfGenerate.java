package ReportGen;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerate {
	
    public static void main(String[] args) {
        // Connect to the database
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("cs2410-web01pvm.aston.ac.uk", "u-210151525", "LtRKNSRcD+kQQ+j+");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Create a new PDF document
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
            document.open();
            
            // Add a title to the document
            Paragraph title = new Paragraph("Inventory Report");
            document.add(title);
            
            // Retrieve data from the orders table
            PreparedStatement ordersStmt = conn.prepareStatement("SELECT * FROM user_orders WHERE order_status='placed'");
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

            // Close the document and the database connection
            document.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

