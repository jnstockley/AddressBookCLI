import java.sql.*;
import java.util.List;
import java.util.Scanner;

/**
 * @date April 17 2018
 * 
 * @Description An address book that retrieves data from a mySQL server and prints it out the server
 * 
 * @author Jack Stockley
 *
 * @version 0.5
 */

public class AddressBook_Main {

	public static String IP; //NULL Variables for the mySQL server IP
	public static final String user = "user"; //Username for the mySQL sever
	public static final String password = "pass"; //Password for the mySQL server TODO figure out how to hide password
	static Connection conn; //NULL connection to get connected to the mySQL server
	public static Scanner console = new Scanner(System.in); //Used to get numbers and strings from console

	/**
	 * Runs the program
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print("Internal or External IP (1 for Internal or 2 for external): ");
		int choice = console.nextInt();
		switch (choice){
		case 1:
			IP = "10.0.0.183:8080";
			break;
		case 2:
			IP = "jackstockley.ddns.net:8080";
			break;
		}
		//TRY CATCH method to ensure that the mySQL connection was successful
		try{
			conn = DriverManager.getConnection("jdbc:mysql://"+IP+"/mydb",user,password);
		}catch(SQLException e){
			System.out.println(e.getLocalizedMessage());
		}

		int request;
		while((request=welcome()) !=0){
			int action = action();
			switch (request){
			case 1:
				doAddresses(action);
				break;
			case 2:
				doPerson(action);
				break;
			case 3:
				doOccupation(action);
				break;
			}
		}

	}

	/**
	 * Prints a string to the console and asks the user to enter an option choice
	 * @return Returns a String prompting the user on what option choice to enter
	 */
	private static int welcome(){
		String prompt = "Welcome to the Address Book" + System.lineSeparator() + "What table do you want to access (Enter option number: ) 0 to Exit" + System.lineSeparator() +
				"1. Adddress" + System.lineSeparator() + "2. Person" + System.lineSeparator() + "3. Occupation";
		System.out.println(prompt);
		int response = console.nextInt();
		return response;
	}

	/**
	 * Asks the user if they want to insert, get, remove, or update data in the address table
	 * @return A string of options of what the user can do
	 */
	private static int action(){
		String prompt = "What action do you want to do (Enter option number: ) 0 to Exit" + System.lineSeparator() + "1. Get all data" + System.lineSeparator() + "2. Get one data" +
				System.lineSeparator() + "3. Insert data" + System.lineSeparator() + "4. Update data" + System.lineSeparator() + "5. Remove data";
		System.out.println(prompt);
		int response = console.nextInt();
		return response;
	}

	/**
	 * Figures out the action the user wants to do with the address table
	 * @param action The action the user wants to run (ex. get, update, insert etc.)
	 */
	private static void doAddresses(int action){
		switch(action){
		case 1:
			getAllAddresses(conn);
			break;
		case 2:
			System.out.print("Enter the id: ");
			int id = console.nextInt();
			getAddress(conn, id);
			break;
		case 3:
			addAddress(conn);
			break;
		case 4:
			updateAddress(conn);
			break;
		case 5:
			removeAddress(conn);
			break;
		}
	}
	
	/**
	 * Figures out what action the users wants to do with the person table
	 * @param action The action the user wants to run (ex. get, update, insert etc.)
	 */
	private static void doPerson(int action){
		switch(action){
		case 1:
			getAllPerson(conn);
			break;
		case 2:
			System.out.print("Enter the id: ");
			int id = console.nextInt();
			getPerson(conn, id);
			break;
		case 3:
			addPerson(conn);
			break;
		case 4:
			updatePerson(conn);
			break;
		case 5:
			removePerson(conn);
			break;
		}
	}
	
	/**
	 * Figures out what action the user want to do with the occupation table
	 * @param action The action the user want to run (ex. get, update, insert etc.)
	 */
	private static void doOccupation(int action){
		switch(action){
		case 1:
			getAllOccupation(conn);
			break;
		case 2:
			System.out.print("Enter the id: ");
			int id = console.nextInt();
			getOccupation(conn, id);
			break;
		case 3:
			addOccupation(conn);
			break;
		case 4:
			updateOccupation(conn);
			break;
		case 5:
			removeOccupation(conn);
			break;
		}
	}

	/**
	 * Prints out all of the addresses in the database to the console
	 * @param conn Passes the mySQL connection
	 */
	private static void getAllAddresses(Connection conn){
		List<Address> addresses = Address.getAll(conn);
		for (Address address: addresses) {
			address.print();
		}
	}

	/**
	 * Prints out a singular address by the id the user provides
	 * @param conn Passes the mySQL connection
	 * @param id the id of the address the user wants to be printed out
	 */
	private static void getAddress(Connection conn, int id){
		Address address = Address.getBy(conn, Integer.toString(id), "id");
		address.print();
	}

	/**
	 * Asks the user want for the street name number city state and zip code and passes it to the mySQL server
	 * @param conn Passes the mySQL connection
	 */
	private static void addAddress(Connection conn){
		System.out.print("Enter Street Number: ");
		String number = console.next();
		console.nextLine();
		System.out.print("Enter the street name: ");
		String name = console.nextLine();
		System.out.print("Enter the city: ");
		String city = console.next();
		System.out.print("Enter the state: ");
		String state = console.next();
		System.out.print("Enter the zip code: ");
		String zip = console.next();
		Address.insert(conn, number, name, city, state, zip);
	}

	/**
	 * Gets the new data for the mySQL data table and passes it on
	 * @param conn The mySQL connection
	 */
	private static void updateAddress(Connection conn){
		getAllAddresses(conn);
		System.out.print("Please enter the id of the address you want to update: ");
		int id = console.nextInt();
		System.out.print("Enter new house number: ");
		String number = console.next();
		console.nextLine();
		System.out.print("Enter new street name: ");
		String name = console.nextLine();
		System.out.print("Enter new city: ");
		String city = console.next();
		System.out.print("Enter new state: ");
		String state = console.next();
		System.out.print("Enter new zip code: ");
		String zip = console.next();
		Address.update(conn, id, number, name, city, state, zip);
	}

	/**
	 * Prints all the addresses and asks the user which address by id they want to remove
	 * @param conn The mySQL connection
	 */
	private static void removeAddress(Connection conn){
		getAllAddresses(conn);
		System.out.print("Please select the id of the address you want to remove: ");
		int id = console.nextInt();
		Address.remove(conn, id);
	}

	/**
	 * Prints out all of the people in the database to the console
	 * @param conn Passes the mySQL connection
	 */
	private static void getAllPerson(Connection conn){
		List<Person> people = Person.getAll(conn);
		for(Person person: people){
			person.print();
		}
	}
	
	/**
	 * Prints out a singular person by the id the user provides
	 * @param conn The mySQL connection
	 * @param id The id of the person the user wants
	 */
	private static void getPerson(Connection conn, int id){
		Person person = Person.getBy(conn, Integer.toString(id));
		person.print();
	}
	
	/**
	 * Get the first name, middle initial and last name and passes the data to the insert function in the person class
	 * @param conn The mySQL connection
	 */
	//TODO Allow user to create a new address and/or occupation
	private static void addPerson(Connection conn){
		System.out.print("Enter First Name: ");
		String firstName = console.next();
		System.out.print("Enter Middle Initial: ");
		String middleInitial = console.next();
		System.out.print("Enter Last Name: ");
		String lastName = console.next();
		getAllAddresses(conn);
		System.out.print("Enter the ID of the address " + firstName + ": ");
		int addressId = console.nextInt();
		/*if(addressId==0){
			addAddress(conn);
		}*/
		getAllOccupation(conn);
		System.out.print("Enter the ID of the occupation for " + firstName + ": ");
		int occupationId = console.nextInt();
		/*if(occupationId==0){
			//Occupation newOccupation = new Occupation();
			//occupationId= addOccupationGetId(conn);
		}*/
		Person.insert(conn, firstName, middleInitial, lastName, addressId, occupationId);
	}
	
	/**
	 * Gets the id of the person the user wants to update and gets the new names the person has and passes them to the update function in the person class
	 * @param conn They mySQL connection
	 */
	private static void updatePerson(Connection conn){
		getAllPerson(conn);
		System.out.print("Please enter the id of the person you want to update: ");
		int id = console.nextInt();
		System.out.print("Enter First Name: ");
		String firstName = console.next();
		console.nextLine();
		System.out.print("Enter Middle Initial: ");
		String middleInitial = console.nextLine();
		System.out.print("Enter Last Name: ");
		String lastName = console.next();
		Person.update(conn, id, firstName, middleInitial, lastName);
	}
	
	/**
	 * Get the id of the person the user wants to removes and passes it to the remove function in the person class
	 * @param conn The mySQL connection
	 */
	private static void removePerson(Connection conn){
		getAllPerson(conn);
		System.out.print("Please select the id of the peron you want to remove: ");
		int id = console.nextInt();
		Person.remove(conn, id);
	}

	/**
	 * Prints out all of the occupations in the database to the console
	 * @param conn Passes the mySQL connection
	 */
	private static void getAllOccupation(Connection conn){
		List<Occupation> occupations = Occupation.getAll(conn);
		for(Occupation occupation: occupations){
			occupation.print();
		}
	}
	
	/**
	 * Gets the id of the occupation the user wants and prints it to the console
	 * @param conn The mySQL connection
	 * @param id The id of the occupation to print out
	 */
	private static void getOccupation(Connection conn, int id){
		Occupation occupation = Occupation.getBy(conn, Integer.toString(id), "id");
		occupation.print();
	}
	
	/**
	 * Gets the name of the occupation and passes it to the insert function in the occupation class
	 * @param conn The mySQL connection
	 */
	private static void addOccupation(Connection conn){
		System.out.print("Enter Occupation Name: ");
		String occupationName = console.next();
		Occupation.insert(conn, occupationName);
	}
	
	/**
	 * Gets the id of the occupation the user wants to update and gets the new occupation name and passes it to the update function in the occupation class
	 * @param conn The mySQL connection
	 */
	private static void updateOccupation(Connection conn){
		getAllOccupation(conn);
		System.out.print("Please enter the id of the occupation you want to update: ");
		int id = console.nextInt();
		System.out.print("Enter Occupation Name: ");
		String occupationName = console.next();
		Occupation.update(conn, id, occupationName);
	}

	/**
	 * Gets the id of the occupation the user want to remove and passes it to the remove function in the occupation class
	 * @param conn The mySQL connection
	 */
	private static void removeOccupation(Connection conn){
		getAllOccupation(conn);
		System.out.print("Please select the id of the occupation you want to remove: ");
		int id = console.nextInt();
		Occupation.remove(conn, id);
	}
	
	/*private static int addOccupationGetId(Connection conn){
		System.out.print("Enter Occupation Name: ");
		String occupationName = console.next();
		Occupation.insert(conn, occupationName);
		return Occupation.getByName(conn, occupationName);
	}*/
}
