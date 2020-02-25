package addressBook;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

/**
 * This part of the program helps connect the user to the MySQL database and occupation table by formatting data and allowing user input
 * @author jnstockley
 * @version 2.00
 *
 */

@SuppressWarnings("deprecation")
public class OccupationHelper {

	/**
	 * Prints out all the occupations from the MySQL database to the console!
	 * @param conn The MySQL connection
	 */
	public static void getAllOccupations(Connection conn){
		try {
			List<Occupation> occupations = Occupation.getOccupation(conn); //Gets a list of all the occupations in the database
			for(Occupation occupation: occupations) { //Prints out the list of occupations to the console!
				System.out.println(occupation);
			}
		}catch(Exception e) {
			System.out.println("Error getting all occupations. Please check the log!");
			Helper.log(e, "OccupationHelper.java", "getAllOccupations()");
		}
	}

	/**
	 * Gets similar occupations from the database based on the field and data from the user
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void getSimilarOccupations(Connection conn, BufferedReader reader){
		try {
			List<String> fields = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("DESCRIBE occupation"); 
			ResultSet rs = ps.executeQuery(); //Gets all the columns from the occupation table
			while(rs.next()) { //Adds all the occupation fields to the fields list
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
			}else if(selectedField.equals("MonthlySalary")){ //Ensures user know what formatting is required when entering monthly salary
				System.out.print("Please enter as an integer without $, comma or cents ex: 4200 is $4,200.00");
				data = reader.readLine();
			}else {
				System.out.print("\nPlease enter data for " + Helper.split(selectedField).toLowerCase()+ ": ");
				data = reader.readLine();
			}
			System.out.println();
			List<Occupation> occupations = Occupation.getOccupation(conn, selectedField.toLowerCase(), data);
			if(occupations.isEmpty()) { //Checks if the list is empty
				System.out.println("No matching occupations in the database!");
				Helper.log("No matching occupations in the database!", "OccupationHelper.java", "getSimilarOccupations()");
			}else { //Prints out all the occupations to the console!
				for(Occupation occupation: occupations) {
					System.out.println(occupation);
				}
			}
		}catch(Exception e) {
			System.out.println("Error getting similar occupation. Please check the log!");
			Helper.log(e, "OccuaptionHelper.java", "getSimilarOccupations()");
		}
	}

	/**
	 * Prints out a singular occupation based on a user based ID
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void getSingularOccupation(Connection conn, BufferedReader reader) {
		try {
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM occupation");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ids.add(Integer.valueOf(rs.getInt(1))); //Adds all the id of the occupations in the database to the list of id's
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
				Occupation occupation = Occupation.getOccupation(conn, selectedId);
				System.out.println(occupation); //Gets the selected occupation and prints out the occupation
			}else { //Prints out error message telling the user they entered an invalid id
				System.out.println("The ID the user entered is not valid!");
				Helper.log("The ID the user entered is not valid!", "OccupationHelper.java", "getSingularOccupation()");
			}
		}catch(Exception e) {
			System.out.println("Error getting singular occupation. Please check the log!");
			Helper.log(e, "OccupationHelper.java", "getSingularOccupation()");
		}
	}

	/**
	 * Updates a user selected occupation from the database
	 * @param conn  The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void updateSingularOccupation(Connection conn, BufferedReader reader) {
		try {
			List<String> fields = new ArrayList<>();
			List<String> data = new ArrayList<>();
			PreparedStatement ps1 = conn.prepareStatement("DESCRIBE occupation");
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) { //Adds all the fields except date, time, and id to the fields list
				if(!rs1.getString(1).equals("date") && !rs1.getString(1).equals("time") && !rs1.getString(1).equals("id")) {
					fields.add(WordUtils.capitalize(rs1.getString(1)));
				}
			}
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM occupation");
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) { //Adds all the id's of the occupations from the database to the id's list
				ids.add(rs2.getInt(1));
			}
			getAllOccupations(conn);
			System.out.print("Enter the ID of the occupation you want to update: ");
			int id = Integer.parseInt(reader.readLine()); 
			if(ids.contains(id)) { //Checks if the user entered id is a valid ID
				System.out.println("To keep exisiting data leave the field blank!");
				for(String field: fields) { //Loops through the fields and allows the user to update the data in a given field or leave the data the same
					String temp = "";
					if(field.equals("MonthlySalary")) {
						System.out.println("Please enter as an integer without $, comma or cents ex: 4200 is $4,200.00");
						System.out.print("Enter new data for monthly salary: ");
						temp = reader.readLine();
					}else {
						System.out.print("Enter new data for " + Helper.split(field).toLowerCase() + ": ");
						temp = reader.readLine();
					}
					data.add(WordUtils.capitalize(temp)); //Uses WordUtils to make the word capitalized every new word and adds the word to the list
				}
				System.out.println();
				System.out.println(Occupation.updateOccupation(conn, id, data.get(0), data.get(1), data.get(2), data.get(3), data.get(4)));
			}else {
				System.out.println("The ID the user entered is not valid!");
				Helper.log("The ID the user entered in not valid!", "OccupationHelper.java", "updateSingularOccupation()");
			}
		}catch (Exception e) {
			System.out.println("Error updating occupation. Please check the log!");
			Helper.log(e, "OccupationHelper.java", "updateSingularOccupation()");
		}
	}

	/**
	 * Uses updateSingularOccupation() to update multiple occupations at once from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void updateMultipleOccupations(Connection conn, BufferedReader reader) {
		try {
			int maxNumOccupations = Occupation.getOccupation(conn).size();
			System.out.print("How many occupations do you want to update (must be less then " + maxNumOccupations + "): ");
			int numOccupations = Integer.parseInt(reader.readLine());
			if(numOccupations>maxNumOccupations) { // Checks that the number of occupations the user wants to update is not more then the number of occupations in the database
				System.out.println("Can't update " + numOccupations + " only " + maxNumOccupations + " occupations are in the database!");
				Helper.log("Can't update " + numOccupations + " only " + maxNumOccupations + " occupations are in the database!", "OccupationHelper.java", "updateMultipleOccupations()");
			}else if(!(numOccupations>=0)) { //Checks that the number of occupations the user wants to update is greater then 0
				System.out.println("Can't update " + numOccupations + " occupations!");
				Helper.log("Can't update " + numOccupations + " occupations!", "OccupationHelper.java", "updateMultipleOccupations()");
			}else {
				System.out.println();
				for(int i=0; i<numOccupations; i++) { //Loops through updateSingularOccupation() for the amount of occupations the user wants to update!
					int temp = i;
					System.out.println("Updating occupation #" + (temp+=1) + " of " + numOccupations); //Tells the user which number occupation they are updating and how many total they are updating
					System.out.println();
					updateSingularOccupation(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error updating occupation. Please check the log!");
			Helper.log(e, "OccupationHelper.java", "updateMultipleOccupations()");
		}
	}

	/**
	 * Create a new occupation with user entered data into the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void insertSingularOccupation(Connection conn, BufferedReader reader) {
		try {
			String temp = "";
			List<String> fields = new ArrayList<>();
			List<String> data = new ArrayList<>();
			PreparedStatement ps1 = conn.prepareStatement("DESCRIBE occupation");
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) { //Adds all the fields except date, time, and id to the fields list
				if(!rs1.getString(1).equals("date") && !rs1.getString(1).equals("time") && !rs1.getString(1).equals("id")) { 
					fields.add(WordUtils.capitalize(rs1.getString(1)));
				}
			}
			for(String field: fields) {
				if(field.equals("MonthlySalary")) {
					System.out.println("Please enter as an integer without $, comma or cents ex: 4200 is $4,200.00");
					System.out.print("Enter new data for monthly salary: ");
					temp = reader.readLine();
				}else {
					System.out.print("Enter data for " + Helper.split(field).toLowerCase() + ": ");
					temp = reader.readLine();
				}
				if(temp.equals("")) { //Makes sure the field isn't empty creating an error when adding new occupation
					System.out.println(Helper.split(field) + " can't be empty!");
					Helper.log(Helper.split(field) + " can't be empty!", "OccupationHelper.java", "insertSingularOccupation()");
				}else {
					data.add(WordUtils.capitalize(temp)); //Uses WordUtils to make the word capitalized every new word and adds the word to the list
				}
			}
			System.out.println();
			System.out.println(Occupation.insertOccupation(conn, data.get(0), data.get(1), data.get(2), data.get(3), data.get(4)));
		}catch(Exception e) {
			System.out.println("Error creating new occupation. Please check the log!");
			Helper.log(e, "OccuaptionHelper.java", "newSingularOccupation()");
		}
	}

	/**
	 * Uses insertSingularOccupation() to insert multiple please at once into the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void insertMultipleOccuaptions(Connection conn, BufferedReader reader) {
		try {
			System.out.print("How many occupations do you want to create: ");
			int numOccupations = Integer.parseInt(reader.readLine());
			if(!(numOccupations>=0)) { //Checks that the number of occupations the user wants to create is greater then 0
				System.out.println("Can't create " + numOccupations + " occupations!");
				Helper.log("Can't create " + numOccupations + "occupations!", "OccupationHelper.java", "insertMultipleOccupations()");
			}else {
				System.out.println();
				for(int i=0; i<numOccupations; i++) { //Loops though insertSingularOccupation() for the amount of occupations the user wants to create
					int temp = 1;
					System.out.println("Creating occupation #" + (temp+=1) + " of " + numOccupations); //Tells the user which number occupation they are creating
					System.out.println();
					insertSingularOccupation(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error creating new occupation. Please check the log!");
			Helper.log(e, "OccupationHelper.java", "insertMultipleOccupations()");
		}
	}

	/**
	 * Asks the user to select an occupation and removes that occupation from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void removeSingularOccupation(Connection conn, BufferedReader reader) {
		try {
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM occupation");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) { //Loops through all the id's in the occupation table and adds them to the id's list
				ids.add(rs.getInt(1));
			}
			getAllOccupations(conn);//Printing out all the occupations in the database
			System.out.print("Please select an occupation: ");
			int selectedId = Integer.parseInt(reader.readLine());
			if(ids.contains(selectedId)) { //Makes sure the selected id is a valid id
				boolean removed = Occupation.removeOccupation(conn, "id", Integer.toString(selectedId));
				if(removed) { //Makes sure the address was removed from the database
					System.out.println();
					System.out.println("Occupation with the id " + selectedId + " was removed from the database!");
					System.out.println();
				}else {
					System.out.println("There was an error removing the occupation with " + selectedId + " from the database!");
					Helper.log("There was an error removing the occupation with " + selectedId + " from the database!", "OccupationHelper.java", "removeSingularOccupation()");
				}
			}else {
				System.out.println("The ID the user enter was not valid!");
				Helper.log("The ID the user enter was not valid!", "OccupationHelper.java", "removeSingularOccupation()");
			}
		}catch(Exception e) {
			System.out.println("Error removing occupation. Please check the log!");
			Helper.log(e, "OccupationHelper.java", "removeSingularOccupation()");
		}
	}

	/**
	 * Uses removeSingularOccupation() to remove multiple occupations at once from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void removeMultipleOccupations(Connection conn, BufferedReader reader) {
		try {
			int maxNumOccupations = Occupation.getOccupation(conn).size();
			System.out.print("How many occupations do you want to update (must be less then " + maxNumOccupations + "): ");
			int numOccupations = Integer.parseInt(reader.readLine());
			if(numOccupations>maxNumOccupations) { //Checks that the number of occupations the user wants to remove is not more then the number of occupations in the database
				System.out.println("Can't remove " + numOccupations + " only " + maxNumOccupations + " occupations are in the database!");
				Helper.log("Can't remove " + numOccupations + " only " + maxNumOccupations + " occupations are in the database!", "OccupationHelper.java", "removeMultipleOccupations()");
			}else if(!(numOccupations>=0)) { //Checks that the number of occupations the user wants to remove is greater then 0
				System.out.println("Can't remove "+ numOccupations + " occupations!");
				Helper.log("Can't remove "+ numOccupations + " occupations!", "OccupationHelper.java", "removeMultipleOccupations()");
			}else {
				System.out.println();
				for(int i=0; i<numOccupations; i++) { //Loops through removeSingularOccupation() for the amount of occupations the user wants to remove
					int temp = i;
					System.out.println("Removing occupation #" + (temp+=1) + " of " + numOccupations);
					System.out.println();
					removeSingularOccupation(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error removing occupation. Please check the log!");
			Helper.log(e, "OccupationHelper.java", "removeMultipleOccupation()");
		}
	}
}
