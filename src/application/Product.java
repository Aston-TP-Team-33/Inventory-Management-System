package application;

public class Product {
	private int productId;
	private int inventoryId;
	private String name;
	private int price;
	private String category;
	private String description;
	private String image;
	
	public Product(int productId, int inventoryId, String name, int price, String category, String description, String image) {
		this.productId = productId;
		this.inventoryId = inventoryId;
		this.name = name;
		this.price = price;
		this.category = category;
		this.description = description;
		this.image = image;
	}
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}	
}
