import java.util.Scanner;
import java.io.*;

public class AmazonStore {
	//Store record of users and products
	private static ListADT<Product> products = new DLinkedList<Product>();
	private static ListADT<User> users = new DLinkedList<User>();
	private static User currentUser = null;//current user logged in

	//scanner for console input
	public static final Scanner stdin= new Scanner(System.in);

	//main method
	public static void main(String args[]) throws FileNotFoundException {

		//Populate the two lists using the input files: Products.txt User1.txt User2.txt ... UserN.txt
		if (args.length < 2) {
			System.out.println("Usage: java AmazonStore [PRODUCT_FILE] [USER1_FILE] [USER2_FILE] ...");
			System.exit(0);
		}

		//load store products
		try{
			loadProducts(args[0]);
		}catch (FileNotFoundException ex){
			System.out.println("Error: File not found.");	
		}

		//load users one file at a time
		for(int i=1; i<args.length; i++)
			loadUser(args[i]);

		//User Input for login
		boolean done = false;
		while (!done) 
		{
			System.out.print("Enter username : ");
			String username = stdin.nextLine();
			System.out.print("Enter password : ");
			String passwd = stdin.nextLine();

			if(login(username, passwd)!=null)
			{
				//generate random items in stock based on this user's wish list
				ListADT<Product> inStock=currentUser.generateStock();
				//show user menu
				userMenu(inStock);
			}
			else
				System.out.println("Incorrect username or password");

			System.out.println("Enter 'exit' to exit program or anything else to go back to login");
			if(stdin.nextLine().equals("exit"))
				done = true;
		}

	}

	/**
	 * Tries to login for the given credentials. Updates the currentUser if successful login
	 * @param username name of user
	 * @param passwd password of user
	 * @returns the currentUser 
	 */
	public static User login(String username, String passwd){
		boolean found = false;
		for(int i = 1; i <= users.size(); i++){
			if(users.get(i).checkLogin(username, passwd)){
				currentUser = users.get(i);
				found = true;
				break;
			}
		}
		if(!found)
			currentUser = null;
		return currentUser;
	}

	/**
	 * Reads the specified file to create and load products into the store.
	 * Every line in the file has the format: <NAME>#<CATEGORY>#<PRICE>#<RATING>
	 * Create new products based on the attributes specified in each line and insert them into the products list
	 * Order of products list should be the same as the products in the file
	 * For any problem in reading the file print: 'Error: Cannot access file'
	 * @param fileName name of the file to read
	 */
	public static void loadProducts(String fileName) throws FileNotFoundException{
		File tempFile = new File(fileName);
		if(!tempFile.exists()){
			System.out.println("Error: Cannot access file " + fileName);
			return;
		}
		Scanner fileSc = new Scanner(tempFile);
		if(!fileSc.hasNextLine()){
			System.out.println("Error: Empty file");
			fileSc.close();
			return;
		}
		while(fileSc.hasNextLine()){
			String[] line = fileSc.nextLine().split("#");
			if(line.length != 4){
				System.out.println("Error: invalid file format");
				fileSc.close();
				return;
			}
			Product prod = new Product(line[0], line[1], Integer.valueOf(line[2]), Float.valueOf(line[3]));
			products.add(prod);
		}
		fileSc.close();
	}

	/**
	 * Reads the specified file to create and load a user into the store.
	 * The first line in the file has the format:<NAME>#<PASSWORD>#<CREDIT>
	 * Every other line after that is a name of a product in the user's wishlist, format:<NAME>
	 * For any problem in reading the file print: 'Error: Cannot access file'
	 * @param fileName name of the file to read
	 */
	public static void loadUser(String fileName) throws FileNotFoundException{
		File tempFile = new File(fileName);
		if(!tempFile.exists()){
			System.out.println("Error: Cannot access file " + fileName);
			return;
		}
		Scanner fileSc = new Scanner(tempFile);
		if(!fileSc.hasNextLine()){
			System.out.println("Error: Empty file");
			fileSc.close();
			return;
		}
		String[] line = fileSc.nextLine().split("#");
		User user = new User(line[0], line[1], Integer.valueOf(line[2]));
		users.add(user);
		while(fileSc.hasNextLine()){
			String wish = fileSc.nextLine();
			String tempCat = null;
			int tempPrice = 0;
			float tempRat = 0;
			boolean found = false;
			for(int i = 1; i <= products.size(); i++){
				Product temp = products.get(i);
				if(temp.getName().contains(wish)){
					tempCat = temp.getCategory();
					tempPrice = temp.getPrice();
					tempRat = temp.getRating();
					found = true;
					break;
				}
			}
			if(!found){
				System.out.println("Error: wishlist item not found");
				fileSc.close();
				return;
			}
			Product prod = new Product(wish, tempCat, tempPrice, tempRat); //find this product
			user.addToWishList(prod);
		}
		fileSc.close();
	}

	/**
	 * See sample outputs
     * Prints the entire store inventory formatted by category
     * The input text file for products is already grouped by category, use the same order as given in the text file 
     * format:
     * <CATEGORY1>
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * ...
     * <NAME> [Price:$<PRICE> Rating:<RATING> stars]
     * Apple Macbook Air [Price:$854 Rating:4.5 stars]
     */
	public static void printByCategory(){
		Product temp;
		String category = null;
		for(int i = 1; i <= products.size(); i++){
			temp = products.get(i);
			if(!temp.getCategory().equals(category)){
				category = temp.getCategory();
				System.out.println();
				System.out.println(category + ":");
			}
			category = temp.getCategory();
			System.out.println(temp.toString());
		}
	}
	
	/**
	 * Interacts with the user by processing commands
	 * @param inStock list of products that are in stock
	 */
	public static void userMenu(ListADT<Product> inStock){
		boolean done = false;
		while (!done) 
		{
			System.out.print("Enter option : ");
			String input = stdin.nextLine();
			//only do something if the user enters at least one character
			if (input.length() > 0) 
			{
				String[] commands = input.split(":");//split on colon, because names have spaces in them
				if(commands[0].length()>1)
				{
					System.out.println("Invalid Command");
					continue;
				}
				switch(commands[0].charAt(0)){
				case 'v':
					switch(commands[1]){
					case "all":
						printByCategory();
						System.out.println();
						break;
					case "wishlist":
						PrintStream ps = new PrintStream(System.out);
						currentUser.printWishList(ps);
						break;
					case "instock":
						for(int i = 1; i <= inStock.size(); i++){
							Product temp = inStock.get(i);
							System.out.println(temp.toString());
						}						
						break;
					}
					break;

				case 's':
					for(int i = 1; i <= products.size(); i++){
						Product temp = products.get(i);
						if(temp.getName().toLowerCase().contains(commands[1].toLowerCase())){
							System.out.println(temp.toString());
						}
					}
					break;
					
				case 'a':
					boolean found = false;
					for(int i = 1; i <= products.size(); i++){
						Product temp = products.get(i);
						if(temp.getName().toLowerCase().contains(commands[1].toLowerCase())){
							currentUser.addToWishList(temp);
							System.out.println("Added to wishlist");
							found = true;
						}
					}
					if(!found)
						System.out.println("Product not found.");
					break;
					
				case 'r':
					Product temp = currentUser.removeFromWishList(commands[1]);
					if(temp == null)
						System.out.println("Product not found.");
					else
						System.out.println("Removed from wishlist");
					break;

				case 'b':
					for(int i = 1; i <= inStock.size(); i++){
						Product tempP = inStock.get(i);
						try{
							if(currentUser.buy(tempP.getName())){
								System.out.println("Bought " + tempP.getName());
							}
						}catch (InsufficientCreditException ex){
							System.out.println("Insufficient funds for "+ tempP.getName());
						}			
					}
					break;

				case 'c':
					System.out.println("$"+ currentUser.getCredit());
					break;

				case 'l':
					done = true;
					currentUser = null;
					System.out.println("Logged Out");
					break;

				default:  //a command with no argument
					System.out.println("Invalid Command");
					break;
				}
			}
		}
	}

}