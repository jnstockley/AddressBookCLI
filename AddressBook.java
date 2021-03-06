package com.github.jnstockley.addressbookcli;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;
import org.apache.commons.lang3.text.WordUtils;

/**
 * This program allows a user to interact with an address book that is saved on a MySQL database over the Internet!
 * @author jnstockley
 * @version 3.1
 * 
 */

@SuppressWarnings("deprecation")
public class AddressBook {

	private static Updater updater = new Updater();
	private static ConnectionHelper connectionHelper = new ConnectionHelper();
	private static FrontendHelper frontendHelper = new FrontendHelper();
	private static AddressBook addressBook = new AddressBook();
	
	/**
	 * The main function of the program
	 * Allows the user to connect to the server, check for updates, and keeps running until the user wants to quit
	 * @param args Main arguments
	 */
	public static void main(String[] args) {
		try {
			double appVersion = 3.1;
			Connection conn = null;
			boolean running = true;
			if(updater.upToDate(appVersion)) { //Makes sure the program is on the latest version!
				while (running) {
					boolean testingMode = false;
					String fileName = "SavedConnections.csv";
					BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
					Console passwordReader = System.console();
					List<String> connection = null;
					System.out.println("Welcome to Address Book V" + appVersion + "!");
					int connectionType = addressBook.connectionHandler(reader);
					if (connectionType == 1) { //Save new connection without connecting
						connection = connectionHelper.saveConnection(reader, passwordReader, fileName);
						System.out.println("Connection saved!");
						addressBook.quit();
					}else if(connectionType == 2) { //Save a new connection and connect to server
						connection = connectionHelper.saveConnection(reader, passwordReader, fileName);
					}else if(connectionType == 3) { //Connect to server using a saved connection
						connection = connectionHelper.retrieveConnection(reader, passwordReader, fileName, testingMode);
					}else if(connectionType == 4) { //Connect to the server without saving a connection to the save connection file
						connection = connectionHelper.noSaveConnection(reader, passwordReader);
					}else if(connectionType == 5) { //Removes a connection from the save connection file
						connectionHelper.removeConnection(reader, passwordReader, fileName);
						addressBook.quit();
					}else {
						System.out.println("The connection type you entered in invalid!");
						frontendHelper.log("Invalid Connection type selected", "AddressBook.java", "main()");
					}
					if (testingMode) {
						conn = (Connection)DriverManager.getConnection("jdbc:mysql://10.0.0.191/addressBook?user=Jack&password=Dr1v3r0o&serverTimezone=UTC");
					} else {
						conn = connectionHelper.connectionBuilder(connection);
					} 
					while (running) { //Allows the user to select a table and method to interact with the database keeps running until the user quits by entering 0
						List<String> selection = addressBook.selector(reader, conn);
						addressBook.printTable(conn, selection.get(0), selection.get(1), reader);
						System.out.print("Do you want to quit the program? (Y/N): ");
						String quit = reader.readLine();
						if (quit.equalsIgnoreCase("y"))
							running = false; 
					} 
				}
				addressBook.quit(conn);
			}else {
				addressBook.quit();
			}
		} catch (Exception e) {
			System.out.println("Error in main function! Please check the log!");
			frontendHelper.log(e, "AddressBook.java", "main()");
		}
	}

	/**
	 * Helps manage which type of connection or action the user wants to make when starting the program
	 * @param reader How I am reading data from the console
	 * @return An integer corresponding to which action the user wants to make
	 */
	public int connectionHandler(BufferedReader reader) {
		try {
			System.out.println("Please select an option below!");
			System.out.println("1. Save a new conenction without connecting to server");
			System.out.println("2. Save a new connection with connecting to server");
			System.out.println("3. Connect to server using an existing connection");
			System.out.println("4. Connect to server without saving a connection");
			System.out.println("5. Remove a saved connection from local machine");
			return Integer.parseInt(reader.readLine());
		} catch (Exception e) {
			frontendHelper.log(e, "AddressBook.java", "connectionHandler()");
			return 0;
		} 
	}

	/**
	 * Allows the user to select which table and method they want to perform
	 * @param reader How I am reading data from the console
	 * @param conn The MySQL connection
	 * @return A list of strings with the table and method to perform
	 */
	public List<String> selector(BufferedReader reader, Connection conn) {
		String table = getTable(reader, conn);
		String method = getMethod(reader, table, conn);
		List<String> selection = new ArrayList<>();
		selection.add(table);
		selection.add(method);
		return selection;
	}

	/**
	 * Allows the user to select which table they want to interact with 
	 * @param reader How I am reading data from the console
	 * @param conn The MySQL connection
	 * @return A string corresponding to which table the user wants to interact with
	 */
	public String getTable(BufferedReader reader, Connection conn) {
		try {
			List<String> tables = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("SHOW TABLES");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) //Gets all the table names from the database
				tables.add(WordUtils.capitalize(rs.getString(1))); 
			System.out.println("Please select a table from below (Enter 0 to quit): ");
			int tableId = 1;
			for (String item : tables) { //Prints out all the tables
				System.out.println(String.valueOf(tableId) + ": " + item);
				tableId++;
			} 
			int selectedTable = Integer.parseInt(reader.readLine());
			if (selectedTable == 0)
				quit(conn); 
			return tables.get(selectedTable - 1);
		} catch (Exception e) {
			System.out.println("Error in selecting table. Please check the log!");
			frontendHelper.log(e, "AddressBook.java", "getTable(");
			return null;
		} 
	}

	/**
	 * Allows the user to select which method they want to interact with the selected table with
	 * @param reader How I am reading data from the console
	 * @param table The table the user wants to interact with
	 * @param conn THe MySQL connection
	 * @return A string corresponding to which method the user wants to interact with on the selected table
	 */
	public String getMethod(BufferedReader reader, String table, Connection conn) {
		try {
			List<String> methods = new ArrayList<>(Arrays.asList(new String[] { "Get All", "Get Singular", "Update Singular", "Update Multiple", "Insert Singular", "Insert Multiple", "Remove Singular", "Remove Multiple" }));
			System.out.println("Please select method for the " + table + " table (Enter 0 to change table): ");
			int methodId = 1;
			for (String item : methods) { //Prints all the methods
				System.out.println(String.valueOf(methodId) + ": " + item);
				methodId++;
			} 
			int method = Integer.parseInt(reader.readLine());
			while(method==0) { //Allows user to change table
				getTable(reader, conn);
				methodId = 1;
				for (String item : methods) {
					System.out.println(String.valueOf(methodId) + ": " + item);
					methodId++;
				} 
				method = Integer.parseInt(reader.readLine());
			}
			return methods.get(method-1);
		} catch (Exception e) {
			System.out.println("Error in selecting method. Please check the log!");
			frontendHelper.log(e, "AddressBook.java", "getMethod()");
			return null;
		} 
	}

	/**
	 * The main function of the program. Interacts with helper classes and performs the user selected action on the database
	 * @param conn The MySQL connection
	 * @param table The user selected table
	 * @param method The user selected method
	 * @param reader How I am reading data from the console
	 */
	public void printTable(Connection conn, String table, String method, BufferedReader reader) {
		AddressHelper addressHelper = new AddressHelper();
		OccupationHelper occupationHelper = new OccupationHelper();
		PersonHelper personHelper = new PersonHelper();
		if (method.equals("Get All")) {
			if (table.equals("Address")) {
				addressHelper.getAllAddresses(conn);
			} else if (table.equals("Occupation")) {
				occupationHelper.getAllOccupations(conn);
			} else if (table.equals("Person")) {
				personHelper.getAllPeople(conn);
			} 
		/*} else if (method.equals("Get By Field")) {
			if (table.equals("Address")) {
				addressHelper.getSimilarAddresses(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.getSimilarOccupations(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.getSimilarPeople(conn, reader);
			} */
		} else if (method.equals("Get Singular")) {
			if (table.equals("Address")) {
				addressHelper.getSingularAddress(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.getSingularOccupation(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.getSingularPerson(conn, reader);
			} 
		} else if (method.equals("Update Singular")) {
			if (table.equals("Address")) {
				addressHelper.updateSingularAddress(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.updateSingularOccupation(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.updateSingularPerson(conn, reader);
			} 
		} else if (method.equals("Update Multiple")) {
			if (table.equals("Address")) {
				addressHelper.updateMultipleAddresses(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.updateMultipleOccupations(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.updateMultiplePeople(conn, reader);
			} 
		} else if (method.equals("Insert Singular")) {
			if (table.equals("Address")) {
				addressHelper.insertSingularAddress(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.insertSingularOccupation(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.insertSingularPerson(conn, reader);
			} 
		} else if (method.equals("Insert Multiple")) {
			if (table.equals("Address")) {
				addressHelper.insertMultipleAddresses(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.insertMultipleOccuaptions(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.insertMultiplePeople(conn, reader);
			} 
		} else if (method.equals("Remove Singular")) {
			if (table.equals("Address")) {
				addressHelper.removeSingularAddress(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.removeSingularOccupation(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.removeSingularPerson(conn, reader);
			} 
		} else if (method.equals("Remove Multiple")) {
			if (table.equals("Address")) {
				addressHelper.removeMultipleAddresses(conn, reader);
			} else if (table.equals("Occupation")) {
				occupationHelper.removeMultipleOccupations(conn, reader);
			} else if (table.equals("Person")) {
				personHelper.removeMultiplePeople(conn, reader);
			} 
		} 
	}

	/**
	 * Quits the program and closes the SQL connection
	 * @param conn The MySQL connection
	 */
	public void quit(Connection conn) {
		try {
			conn.close();
			System.out.println();
			System.out.println("Thank you for using the Address Book application created by Jack Stockley");
			System.out.println("Find this project on github at: http://bit.ly/AddressBookJava");
			System.exit(0);
		} catch (Exception e) {
			frontendHelper.log(e, "AddressBook.java", "quit()");
		} 
	}

	/**
	 * Quits the program
	 */
	public void quit() {
		try {
			System.out.println();
			System.out.println("Thank you for using the Address Book application created by Jack Stockley");
			System.out.println("Find this project on github at: http://bit.ly/AddressBookJava");
			System.exit(0);
		} catch (Exception e) {
			frontendHelper.log(e, "AddressBook.java", "quit()");
		} 
	}
}