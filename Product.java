/**
 * Stores the name, category, price and rating of a product
 */
public class Product {
	
	private String name;
	private String category;
	private int price;
	private float rating;
	
	/**
     * Constructs a Product with a name, category, price and rating. 
     * 
     * @param name name of product
     * @param category category of product
     * @param price price of product in $ 
     * @param rating rating of product out of 5
     */
	public Product(String name, String category, int price, float rating){
		this.name = name;
		this.category = category;
		this.price = price;
		this.rating = rating;
	}
	
	/** 
     * Returns the name of the product
     */
	public String getName(){
		return name;
	}
	
	/** 
     * Returns the category of the product*/
	public String getCategory(){
		return category;
	}
	
	/** 
     * Returns the price of the product
     */
	public int getPrice(){
		return price;
	}
	
	/** 
     * Returns the rating of the product */
	public float getRating(){
		return rating;
	}
	
	/** 
     * Returns the Product's information in the following format: <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     */
	public String toString(){
		return this.getName() + " [Price:$"+this.getPrice() + " Rating:" + this.getRating() + " stars]";
	}

}