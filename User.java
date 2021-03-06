import java.util.Random;
import java.io.PrintStream;

/**
 * The User class uses DLinkedList to build a price ordered list called 'wishlist' of products 
 * Products with higher Price fields should come earlier in the list.
 */
public class User {
	//Random number generator, used for generateStock. DO NOT CHANGE
	private static Random randGen = new Random(1234);
	
	private String username;
	private String passwd;
	private int credit;
	private ListADT<Product> wishList;
	
	/**
     * Constructs a User instance with a name, password, credit and an empty wishlist.      */
	public User(String username, String passwd, int credit){
		this.username = username;
		this.passwd = passwd;
		this.credit = credit;
		this.wishList = new DLinkedList<Product>();
	}
	
	/**
     * Checks if login for this user is correct
     * @param username the name to check
     * @param passwd the password to check
     * @return true if credentials correct, false otherwise
     */
	public boolean checkLogin(String username, String passwd){  //username!
		if(username.equals(this.username) && passwd.equals(this.passwd))
			return true;
		else
			return false;
	}
	
	/**
     * Adds a product to the user's wishlist. 
     * Maintain the order of the wishlist from highest priced to lowest priced products.
     * @param product the Product to add
     */
	public void addToWishList(Product product){
		if(wishList.isEmpty())
			this.wishList.add(1, product);
		else{
			int i = 1;
			while(i <= wishList.size() && wishList.get(i).getPrice() > product.getPrice()){
				i++;
			}
			this.wishList.add(i, product);
		}
	}
	
	/**
     * Removes a product from the user's wishlist. 
     * Do not charge the user for the price of this product
     * @param productName the name of the product to remove
     * @return the product on success, null if no such product found
     */
	public Product removeFromWishList(String productName){
		int size = wishList.size();
		for(int i = 1; i <= size; i++){
			Product temp = wishList.get(i);
			if(temp.getName().equals(productName)){
				wishList.remove(i);
				return temp;
			}	
		}
		return null;
	}
	
	/**
     * Print each product in the user's wishlist in its own line using the PrintStream object passed in the argument
	 * @param printStream The printstream object on which to print out the wishlist
     */
	public void printWishList(PrintStream printStream){
		for(int i = 1; i <= wishList.size(); i++){
			printStream.println(wishList.get(i).toString());
		}
	}
	
	/**
     * Buys the specified product in the user's wishlist.
     * Charge the user according to the price of the product by updating the credit
     * Remove the product from the wishlist as well
     * Throws an InsufficientCreditException if the price of the product is greater than the credit available.
     * 
     * @param productName name of the product
     * @return true if successfully bought, false if product not found 
     * @throws InsufficientCreditException if price > credit 
     */
	public boolean buy(String productName) throws InsufficientCreditException{
		
		Product prod = removeFromWishList(productName);
		if(prod == null)
			return false;
		if(prod.getPrice() > credit){
			addToWishList(prod);
			throw new InsufficientCreditException();
		}
		credit -= prod.getPrice(); //buy
		return true;
	}
	
	/** 
     * Returns the credit of the user
     * @return the credit
     */
	public int getCredit(){
		return credit;
	}
	
	/**
	 * This method is already implemented for you. Do not change.
	 * Declare the first N items in the currentUser's wishlist to be in stock
	 * N is generated randomly between 0 and size of the wishlist
	 * @returns list of products in stock 
	 */
	public ListADT<Product> generateStock() {
		ListADT<Product> inStock= new DLinkedList<Product>();

		int size=wishList.size();
		if(size==0)
			return inStock;

		int n=randGen.nextInt(size+1);//N items in stock where n>=0 and n<size

		//pick first n items from wishList
		for(int ndx=1; ndx<=n; ndx++)
			inStock.add(wishList.get(ndx));
		
		return inStock;
	}

}