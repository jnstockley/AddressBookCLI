import java.io.Console;
import java.sql.DriverManager;
import java.util.List;
import java.util.Scanner;
import com.mysql.jdbc.Connection;

/**
 * 
 * @author Jack Stockley
 * 
 * @version 1.0.1
 * 
 * @description The main class of the Address Book which uses CRUD to modify data on a mySQL server
 * 
 * @date 31 January 2019
 *
 */

public class addressBookMain {
	static String IP ;//User defined IP
	static String user ; //User defined user name for mySQL server
	static String pass ; //User defined password for mySQL server
	static Connection conn; //The mySQL connection
	static Scanner console = new Scanner(System.in);
	static  Console password = System.console();

	public static void main(String[] args) {
		System.out.print("Welcome to the Address Book Program! Please type the IP address of the mySQL server you wish to connect to: ");
		IP = console.nextLine();
		System.out.println("Please enter the username then password for the mySQL at: " + IP + " address");
		System.out.print("Username: ");
		user = console.nextLine();
		//Try Catch to mask the password in a console but allow password entry in IDE
		try {
			char passwordArray[] = password.readPassword("Password: ");
			pass = new String(passwordArray);
		}catch (Exception e){
			System.out.print("Password: ");
			pass = console.nextLine();
		}

		//Try Catch method to ensure the connection was successful
		try {
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://"+IP+":3306/mydb",user,pass);
		}catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		int table;
		while((table = getTableRequest()) != 0) {
			redirecter(table,getMethodRequest());
		}
	}

	/**
	 * Asks the user which table they would like to access
	 * @return The number of the table they want to access
	 */
	private static int getTableRequest() {
		int request = 0;
		System.out.println("Which table would you like to access: " + System.lineSeparator() + "1. Address Table" + System.lineSeparator() + "2. Person Table" + System.lineSeparator() + "3. Occupation Table");
		request = console.nextInt();
		return request;
	}

	/**
	 * Asks the user for the method they would like to access
	 * @return The number of the method they want to access
	 */
	private static int getMethodRequest() {
		int request = 0;
		System.out.println("Please select an action to preform on the table: " + System.lineSeparator() + "1. Get all the Data" + System.lineSeparator() + "2. Get a Single Entry" + System.lineSeparator() + "3. Get Similiar Addresses" + System.lineSeparator() + "4. Update Data" + System.lineSeparator() + "5. Insert Data" + System.lineSeparator() + "6. Delete Data");
		request = console.nextInt();
		return request;
	}

	//TODO Make better less else if statements
	/**
	 * Takes in the table and method number and redirects program to the correct method
	 * @param table The number of the table the user wants to use
	 * @param method The number of the method the user wants to use
	 */
	private static void redirecter(int table, int method) {
		if(table == 1) {
			if(method == 1) {
				getAllAddresses(conn);
			}else if(method == 2) {
				getSingleAddress(conn);
			}else if(method == 3) {
				getSimilarAddresses(conn);
			}else if(method == 4) {
				updateAddress(conn);
			}else if(method == 5) {
				insertAddress(conn);
			}else if(method == 6) {
				removeAddress(conn);
			}else {
				System.out.println("Invalid method number: " + method);
			}
		}else if(table == 2) {
			if(method == 1) {
				getAllPeople(conn);
			}else if(method == 2) {
				getSinglePerson(conn);
			}else if(method == 3) {
				getSimilarPeople(conn);
			}else if(method == 4) {
				updatePerson(conn);
			}else if(method == 5) {
				insertPerson(conn);
			}else if(method == 6) {
				removePerson(conn);
			}else {
				System.out.println("Invalid method number: " + method);
			}
		}else if(table == 3) {
			if(method == 1) {
				getAllOccupations(conn);
			}else if(method == 2) {
				getSingleOccupation(conn);
			}else if(method == 3) {
				getSimilarOccupations(conn);
			}else if(method == 4) {
				updateOccupation(conn);
			}else if(method == 5) {
				insertOccupation(conn);
			}else if(method == 6) {
				removeOccupation(conn);
			}else {
				System.out.println("Invalid method number: " + method);
			}
		}else {
			System.out.println("Invlaid table number: " + table);
		}
	}

	/**
	 * Asks the user for the ID of the occupation the user wants to remove and then removes it from the MySQL database
	 * @param conn The MySQL connection
	 */
	private static void removeOccupation(Connection conn) {
		getAllOccupations(conn);
		System.out.print("Please enter the ID of the occupation you want to remove: ");
		int id = console.nextInt();
		Occupation.remove(conn, id);
	}

	/**
	 * Asks the user for the occupation name and then adds it to the MySQL database
	 * @param conn The MySQL connection
	 */
	private static void insertOccupation(Connection conn) {
		console.nextLine();
		System.out.print("Enter Occupation Name: ");
		String occupationName = console.nextLine();
		Occupation.insert(conn, occupationName);		
	}

	/**
	 * Asks the user for the ID of the occupation they want to update and then asks them for the new occupation name
	 * Then updates the occupation
	 * @param conn The MySQL connection
	 */
	private static void updateOccupation(Connection conn) {
		getAllOccupations(conn);
		console.nextLine();
		System.out.print("Please enter the id of the occupation you want to update: ");
		int id = console.nextInt();
		System.out.print("Enter new Occupation Name: ");
		console.nextLine();
		String occupationName = console.nextLine();
		Occupation.update(conn, id, occupationName);		
	}

	/**
	 * Gets all occupations with a similar, user defined, field and then prints them out to the console
	 * @param conn The MySQL connection
	 */
	private static void getSimilarOccupations(Connection conn) {
		String fieldName = getFieldName();
		System.out.print("Enter the " + fieldName + " of the similar occupations you want to retreive: ");
		String field = console.nextLine();
		List<Occupation> occupations = Occupation.getSimilar(conn, fieldName, field);
		for(Occupation occupation: occupations) {
			System.out.println(occupation);
		}
	}

	/**
	 * Gets a single occupation from a user defined field and then prints it out to the console
	 * @param conn The MySQL connection
	 */
	private static void getSingleOccupation(Connection conn) {
		String fieldName = getFieldName();
		System.out.print("Enter the " + fieldName + " of the occupation you want to retrieve: ");
		String field = console.nextLine();
		Occupation occupation = Occupation.getBy(conn, fieldName, field);
		System.out.println(occupation);
	}

	/**
	 * Gets all the occupations and then prints them out to the console
	 * @param conn The MySQL connection
	 */
	private static void getAllOccupations(Connection conn) {
		List <Occupation> occupations = Occupation.getAll(conn);
		for(Occupation occupation: occupations) {
			System.out.println(occupation);
		}
	}

	/**
	 * Removes a single person from the user defined ID
	 * @param conn The MySQL connection
	 */
	private static void removePerson(Connection conn) {
		getAllPeople(conn);
		System.out.print("Please enter the ID of the person you want to remove: ");
		int id = console.nextInt();
		Person.remove(conn, id);
	}

	/**
	 * Inserts a person using the data the user provided from the console
	 * @param conn The MySQL connection
	 */
	private static void insertPerson(Connection conn) {
		console.nextLine();
		System.out.print("Enter First Name: ");
		String firstName = console.nextLine();
		System.out.print("Enter Middle Initial: ");
		String middleInitial = console.nextLine();
		System.out.print("Enter Last Name: ");
		String lastName = console.nextLine();
		getAllAddresses(conn);
		System.out.print("Enter the ID of the address " + firstName + ": ");
		int addressId = console.nextInt();
		getAllOccupations(conn);
		System.out.print("Enter the ID of the occupation for " + firstName + ": ");
		int occupationId = console.nextInt();
		Person.insert(conn, firstName, middleInitial, lastName, addressId, occupationId);		
	}

	/**
	 * Updates a person based on the user defined field and updates the person with the data the user entered from the console
	 * @param conn The MySQL connection
	 */
	private static void updatePerson(Connection conn) {
		getAllPeople(conn);
		System.out.print("Please enter the id of the person you want to update: ");
		int id = console.nextInt();
		System.out.print("Enter new First Name: ");
		console.nextLine();
		String firstName = console.nextLine();
		System.out.print("Enter new Middle Initial: ");
		String middleInitial = console.nextLine();
		System.out.print("Enter new Last Name: ");
		String lastName = console.nextLine();
		getAllAddresses(conn);
		System.out.print("Enter new Address ID: (Type 0 to keep exisiting) ");
		int addressId = console.nextInt();
		console.nextLine();
		getAllOccupations(conn);
		System.out.print("Enter new Occupation ID: (Type 0 to keep exisiting) ");
		int occupationId = console.nextInt();
		Person.update(conn, id, firstName, middleInitial, lastName, addressId, occupationId);		
	}

	/**
	 * Gets all people with a similar, user defined, field and then prints them out to the console
	 * @param conn
	 */
	private static void getSimilarPeople(Connection conn) {
		String fieldName = getFieldName();
		System.out.print("Enter the " + fieldName + " of the similar people you want to retreive: ");
		String field = console.nextLine();
		List<Person> people = Person.getSimilar(conn, fieldName, field);
		for(Person person: people) {
			System.out.println(person);
		}
	}

	/**
	 * Gets a singles person from the user defined field and prints it out
	 * @param conn The MySQL connection
	 */
	private static void getSinglePerson(Connection conn) {
		String fieldName = getFieldName();
		System.out.print("Enter the " + fieldName + " of the person you want to retrieve: ");
		String field = console.nextLine();
		Person person = Person.getBy(conn, fieldName, field);
		System.out.println(person);
	}

	/**
	 * Gets all people and prints them out to the console
	 * @param conn The MySQL connection
	 */
	private static void getAllPeople(Connection conn) {
		List <Person> people = Person.getAll(conn);
		for(Person person: people) {
			System.out.println(person);
		}
	}

	/**
	 * Removes a single address from the user defined ID number
	 * @param conn The MySQL connection
	 */
	private static void removeAddress(Connection conn) {
		getAllAddresses(conn);
		System.out.print("Please enter the ID of the address you want to remove: ");
		int id = console.nextInt();
		Address.remove(conn, id);
	}

	/**
	 * Inserts a new address using the user defined data
	 * @param conn The MySQL connection
	 */
	private static void insertAddress(Connection conn) {
		console.nextLine();
		System.out.print("Enter Street Number: ");
		String number = console.nextLine();
		System.out.print("Enter the street name: ");
		String name = console.nextLine();
		System.out.print("Enter the city: ");
		String city = console.nextLine();
		System.out.print("Enter the state: ");
		String state = console.nextLine();
		System.out.print("Enter the zip code: ");
		String zip = console.nextLine();
		Address.insert(conn, number, name, city, state, zip);		
	}

	/**
	 * Updates an address from the user defined ID and uses the user defined data to update it
	 * @param conn The MySQL connection
	 */
	private static void updateAddress(Connection conn) {
		getAllAddresses(conn);
		System.out.print("Please enter the id of the address you want to update: ");
		console.nextLine();
		int id = console.nextInt();
		System.out.print("Enter new house number: ");
		console.nextLine();
		String number = console.nextLine();
		System.out.print("Enter new street name: ");
		String name = console.nextLine();
		System.out.print("Enter new city: ");
		String city = console.nextLine();
		System.out.print("Enter new state: ");
		String state = console.nextLine();
		System.out.print("Enter new zip code: ");
		String zip = console.nextLine();
		Address.update(conn, id, number, name, city, state, zip);		
	}

	/**
	 * Gets all the addresses with a similar user defined field and prints them out to the console
	 * @param conn The MySQL connection
	 */
	private static void getSimilarAddresses(Connection conn) {
		String fieldName = getFieldName();
		System.out.print("Enter the " + fieldName + " of the similar addresses you want to retreive: ");
		String field = console.nextLine();
		List<Address> addresses = Address.getSimilar(conn, fieldName, field);
		for(Address address: addresses) {
			System.out.println(address);
		}
	}

	/**
	 * Gets a single address with a user defined field and prints it out
	 * @param conn The MySQL connection
	 */
	private static void getSingleAddress(Connection conn) {
		String fieldName = getFieldName();
		System.out.print("Enter the " + fieldName + " of the address you want to retrieve: ");
		String field = console.nextLine();
		Address address = Address.getBy(conn, fieldName, field);
		System.out.println(address);
	}

	/**
	 * Gets all the address and prints them out
	 * @param conn The MySQL connection
	 */
	private static void getAllAddresses(Connection conn) {
		List <Address> addresses = Address.getAll(conn);
		for(Address address: addresses) {
			System.out.println(address);
		}
	}

	/**
	 * Asks the user what field they want to use
	 * @return The field type
	 */
	private static String getFieldName() {
		System.out.print("Please enter a field name for the data you want to retrieve: ");
		console.nextLine();
		return console.nextLine();
	}
}