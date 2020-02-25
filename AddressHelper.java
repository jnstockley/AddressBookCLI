package addressBook;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

/**
 * This part of the program helps connect the user to the MySQL database and address table by formating data and allowing user input
 * @author jnstockley
 * @version 2.00
 *
 */

@SuppressWarnings("deprecation")
public class AddressHelper {

	/**
	 * Prints out all the addresses from the MySQL database to the console!
	 * @param conn The MySQL connection
	 */
	public static void getAllAddresses(Connection conn){
		try {
			List<Address> addresses = Address.getAddress(conn); //Gets a list of all the address in the database
			for(Address address: addresses) { //Prints out the list of addresses to the console!
				System.out.println(address);
			}
		}catch(Exception e) {
			System.out.println("Error getting all address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "getAllAddresses()");
		}
	}

	/**
	 * Gets similar addresses from the database based on the field and data from the user
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void getSimilarAddresses(Connection conn, BufferedReader reader){
		try {
			List<String> fields = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("DESCRIBE address"); 
			ResultSet rs = ps.executeQuery(); //Gets all the columns from the address table
			while(rs.next()) { //Adds all the address fields to the fields list
				fields.add(WordUtils.capitalize(rs.getString(1))); //Adds fields to fields list
			}
			int fieldId = 1;
			for(String field: fields) { //Prints out all the fields with an id that the user can use to select which field to use
				System.out.println(fieldId + ": " + Helper.split(field));
				fieldId++;
			}
			System.out.println();
			String data;
			System.out.print("Please select a field: ");
			String selectedField = fields.get(Integer.parseInt(reader.readLine())-1); //Gets the string version of the field the user selected
			System.out.println();
			if(selectedField.equals("Date")) { //Gets correct formatting if field is date
				data = Helper.dateFinder(reader);
			}else if(selectedField.equals("Time")) { //Gets correct formatting if field is time
				data = Helper.timeFinder(reader);
			}else {
				System.out.print("Please enter data for " + Helper.split(selectedField).toLowerCase()+ ": ");
				data = reader.readLine();
			}
			System.out.println();
			List<Address> addresses = Address.getAddress(conn, selectedField.toLowerCase(), data);
			if(addresses.isEmpty()) { //Checks if the list is empty
				System.out.println("No matching addresses in the database!");
				Helper.log("No matching addresses in the database!", "AddressHelper.java", "getSimilarAddresses()");
			}else { //Prints out all the addresses to the console!
				for(Address address: addresses) {
					System.out.println(address);
				}
			}
		}catch(Exception e) {
			System.out.println("Error getting similar addresses. Please check the log!");
			Helper.log(e, "AddressHelper.java", "getSimilarAddresses()");
		}
	}

	/**
	 * Prints out a singular address based on a user based ID
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void getSingularAddress(Connection conn, BufferedReader reader) {
		try {
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM address");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ids.add(Integer.valueOf(rs.getInt(1))); //Adds all the id of the addresses in the database to the list of id's
			}
			System.out.println("Please select an ID: ");
			String stringId = "";
			for(Integer id: ids) {
				stringId+= id + ", ";
			}
			System.out.println(stringId.substring(0, stringId.length()-2)); //Prints a list of all the int's with commas in between them
			System.out.print("ID: ");
			int selectedId = Integer.parseInt(reader.readLine());
			System.out.println();
			if(ids.contains(selectedId)) { //Checks if the user entered id is in the list of id's from the database
				Address address = Address.getAddress(conn, selectedId);
				System.out.println(address); //Gets the selected address and prints out the address
			}else { //Prints out error message telling the user they entered an invalid id
				System.out.println("The ID the user entered is not valid!");
				Helper.log("The ID the user entered is not valid!", "AddressHelper.java", "getSingularAddress()");
			}
		}catch(Exception e) {
			System.out.println("Error getting singular address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "getSingularAddress()");
		}
	}

	/**
	 * Updates a user selected address from the database
	 * @param conn  The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void updateSingularAddress(Connection conn, BufferedReader reader) {
		try {
			List<String> fields = new ArrayList<>();
			List<String> data = new ArrayList<>();
			PreparedStatement ps1 = conn.prepareStatement("DESCRIBE address");
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) { //Adds all the fields except date, time, and id to the fields list
				if(!rs1.getString(1).equals("date") && !rs1.getString(1).equals("time") && !rs1.getString(1).equals("id")) {
					fields.add(WordUtils.capitalize(rs1.getString(1)));
				}
			}
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM address");
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) { //Adds all the id's of the addresses from the database to the id's list
				ids.add(rs2.getInt(1));
			}
			getAllAddresses(conn);
			System.out.print("Enter the ID of the address you want to update: ");
			int id = Integer.parseInt(reader.readLine()); 
			if(ids.contains(id)) { //Checks if the user entered id is a valid ID
				System.out.println("To keep exisiting data leave the field blank!");
				for(String field: fields) { //Loops through the fields and allows the user to update the data in a given field or leave the data the same
					String temp = "";
					System.out.print("Enter new data for " + Helper.split(field).toLowerCase() + ": ");
					temp = reader.readLine();
					data.add(WordUtils.capitalize(temp)); //Uses WordUtils to make the word capitalized every new word and adds the word to the list
				}
				System.out.println();
				System.out.println(Address.updateAddress(conn, id, Integer.parseInt(data.get(0)), data.get(1), data.get(2), data.get(3), data.get(4)));
			}else {
				System.out.println("The ID the user entered is not valid!");
				Helper.log("The ID the user entered in not valid!", "AddressHelper.java", "updateSingularAddress()");
			}
		}catch (Exception e) {
			System.out.println("Error updating address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "updateSingularAddress()");
		}
	}

	/**
	 * Uses updateSingularAddress() to update multiple addresses at once from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void updateMultipleAddresses(Connection conn, BufferedReader reader) {
		try {
			int maxNumAddresses = Address.getAddress(conn).size();
			System.out.print("How many addresses do you want to update (must be less then " + maxNumAddresses + "): ");
			int numAddresses = Integer.parseInt(reader.readLine());
			if(numAddresses>maxNumAddresses) { // Checks that the number of addresses the user wants to update is not more then the number of addresses in the database
				System.out.println("Can't update " + numAddresses + " only " + maxNumAddresses + " addresses are in the database!");
				Helper.log("Can't update " + numAddresses + " only " + maxNumAddresses + " addresses are in the database!", "AddressHelper.java", "updateMultipleAddresses()");
			}else if(!(numAddresses>=0)) { //Checks that the number of addresses the user wants to update is greater then 0
				System.out.println("Can't update " + numAddresses + " addresses!");
				Helper.log("Can't update " + numAddresses + " addresses!", "AddressHelper.java", "updateMultipleAddresses()");
			}else {
				System.out.println();
				for(int i=0; i<numAddresses; i++) { //Loops through updateSingularAddress() for the amount of addresses the user wants to update!
					int temp = i;
					System.out.println("Updating address #" + (temp+=1) + " of " + numAddresses); //Tells the user which number address they are updating and how many total they are updating
					System.out.println();
					updateSingularAddress(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error updating address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "updateMultipleAddresses()");
		}
	}

	/**
	 * Create a new address with user entered data into the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void insertSingularAddress(Connection conn, BufferedReader reader) {
		try {
			String temp = "";
			List<String> fields = new ArrayList<>();
			List<String> data = new ArrayList<>();
			PreparedStatement ps1 = conn.prepareStatement("DESCRIBE address");
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) { //Adds all the fields except date, time, and id to the fields list
				if(!rs1.getString(1).equals("date") && !rs1.getString(1).equals("time") && !rs1.getString(1).equals("id")) { 
					fields.add(WordUtils.capitalize(rs1.getString(1)));
				}
			}
			for(String field: fields) {
				System.out.print("Enter data for " + Helper.split(field).toLowerCase() + ": ");
				temp = reader.readLine();
				if(temp.equals("")) { //Makes sure the field isn't empty creating an error when adding new address
					System.out.println(Helper.split(field) + " can't be empty!");
					Helper.log(Helper.split(field) + " can't be empty!", "AddressHelper.java", "insertSingularAddress()");
				}else {
					data.add(WordUtils.capitalize(temp)); //Uses WordUtils to make the word capitalized every new word and adds the word to the list
				}
			}
			System.out.println();
			System.out.println(Address.insertAddress(conn, Integer.parseInt(data.get(0)), data.get(1), data.get(2), data.get(3), data.get(4)));
		}catch(Exception e) {
			System.out.println("Error creating new address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "newSingularAddress()");
		}
	}

	/**
	 * Uses insertSingularAddress() to insert multiple please at once into the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void insertMultipleAddresses(Connection conn, BufferedReader reader) {
		try {
			System.out.print("How many addresses do you want to create: ");
			int numAddresses = Integer.parseInt(reader.readLine());
			if(!(numAddresses>=0)) { //Checks that the number of addresses the user wants to create is greater then 0
				System.out.println("Can't create " + numAddresses + " addresses!");
				Helper.log("Can't create " + numAddresses + "addresses!", "AddressHelper.java", "insertMultipleAddresses()");
			}else {
				System.out.println();
				for(int i=0; i<numAddresses; i++) { //Loops though insertSingularAddress() for the amount of addresses the user wants to create
					int temp = i;
					System.out.println("Creating address #" + (temp+=1) + " of " + numAddresses); //Tells the user which number address they are creating
					System.out.println();
					insertSingularAddress(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error creating new address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "insertMultipleAddresses()");
		}
	}

	/**
	 * Asks the user to select an address and removes that address from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void removeSingularAddress(Connection conn, BufferedReader reader) {
		try {
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM address");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) { //Loops through all the id's in the address table and adds them to the id's list
				ids.add(rs.getInt(1));
			}
			getAllAddresses(conn);//Printing out all the address in the database
			System.out.print("Please select an address: ");
			int selectedId = Integer.parseInt(reader.readLine());
			if(ids.contains(selectedId)) { //Makes sure the selected id is a valid id
				boolean removed = Address.removeAddress(conn, "id", Integer.toString(selectedId));
				if(removed) { //Makes sure the address was removed from the database
					System.out.println();
					System.out.println("Address with the id " + selectedId + " was removed from the database!");
					System.out.println();
				}else {
					System.out.println("There was an error removing the address with " + selectedId + " from the database!");
					Helper.log("There was an error removing the address with " + selectedId + " from the database!", "AddressHelper.java", "removeSingularAddress()");
				}
			}else {
				System.out.println("The ID the user enter was not valid!");
				Helper.log("The ID the user enter was not valid!", "AddressHelper.java", "removeSingularAddress()");
			}
		}catch(Exception e) {
			System.out.println("Error removing address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "removeSingularAddress()");
		}
	}

	/**
	 * Uses removeSingularAddress() to remove multiple addresses at once from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void removeMultipleAddresses(Connection conn, BufferedReader reader) {
		try {
			int maxNumAddresses = Address.getAddress(conn).size();
			System.out.print("How many addresses do you want to update (must be less then " + maxNumAddresses + "): ");
			int numAddress = Integer.parseInt(reader.readLine());
			if(numAddress>maxNumAddresses) { //Checks that the number of addresses the user wants to remove is not more then the number of addresses in the database
				System.out.println("Can't remove " + numAddress + " only " + maxNumAddresses + " addresses are in the database!");
				Helper.log("Can't remove " + numAddress + " only " + maxNumAddresses + " addresses are in the database!", "AddressHelper.java", "removeMultipleAddresses()");
			}else if(!(numAddress>=0)) { //Checks that the number of addresses the user wants to remove is greater then 0
				System.out.println("Can't remove "+ numAddress + " addresses!");
				Helper.log("Can't remove "+ numAddress + " addresses!", "AddressHelper.java", "removeMultipleAddresses()");
			}else {
				System.out.println();
				for(int i=0; i<numAddress; i++) { //Loops through removeSingularAddress() for the amount of address the user wants to remove
					int temp = i;
					System.out.println("Removing address #" + (temp+=1) + " of " + numAddress);
					System.out.println();
					removeSingularAddress(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error removing address. Please check the log!");
			Helper.log(e, "AddressHelper.java", "removeMultipleAddresses()");
		}
	}
}
