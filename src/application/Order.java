package application;

public class Order {
	private int orderId;
	private int userId;
	private int houseNumber;
	private String street;
	private String postcode; 
	private String city; 
	private int productId;
	private String orderStatus;
	private int quantity;
	
	public Order(int orderId, int userId, int houseNumber, String street, String postcode, String city,
			int productId, float price, String orderStatus, int quantity) {
		this.orderId = orderId;
		this.userId = userId;
		this.houseNumber = houseNumber;
		this.street = street;
		this.postcode = postcode;
		this.city = city;
		this.productId = productId;
		this.orderStatus = orderStatus;
		this.quantity = quantity;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public int getHouseNumber() {
		return houseNumber;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getPostcode() {
		return postcode;
	}
	
	public String getCity() {
		return city;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}
	
	public int getQuantity() {
		return quantity;
	}
}
