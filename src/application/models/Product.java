package application.models;

/**
 * @author Remy Thompson
 *
 */
public class Product {
	private int id;
	private String title;
	private float price;
	private String category;
	private String description;
	private String image;
	private int quantity;
	
	/**
	 * @param id
	 * @param title
	 * @param price
	 * @param category
	 * @param description
	 * @param image
	 * @param quantity
	 */
	public Product(int id, String title, float price, String category, String description, String image, int quantity) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.category = category;
		this.description = description;
		this.image = image;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public float getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public int getQuantity() {
		return quantity;
	}
	
	
	
}
