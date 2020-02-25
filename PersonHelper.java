package addressBook;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

/**
 * This part of the program helps connect the user to the MySQL database and the person table by formatting data and allowing user input
 * @author jnstockley
 * @version 2.00
 *
 */

@SuppressWarnings("deprecation")
public class PersonHelper {

	/**
	 * Prints out all the people from the MySQL database to the console!
	 * @param conn The MySQL connection
	 */
	public static void getAllPeople(Connection conn){
		try {
			List<Person> people = Person.getPerson(conn); //Gets a list of all the people in the database
			for(Person person: people) { //Prints out the list of people to the console!
				System.out.println(person);
			}
		}catch(Exception e) {
			System.out.println("Error getting all people. Please check the log!");
			Helper.log(e, "PersonHelper.java", "getAllPeople()");
		}
	}

	/**
	 * Gets similar people from the database based on the field and data from the user
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void getSimilarPeople(Connection conn, BufferedReader reader){
		try {
			List<Person> people = new ArrayList<>();
			List<String> fields = new ArrayList<>();
			PreparedStatement ps1 = conn.prepareStatement("DESCRIBE person"); 
			PreparedStatement ps2 = conn.prepareStatement("DESCRIBE address");
			PreparedStatement ps3 = conn.prepareStatement("DESCRIBE occupation");
			ResultSet rs1 = ps1.executeQuery(); //Gets all the columns from the person table
			ResultSet rs2 = ps2.executeQuery(); //Gets all the columns from the address table
			ResultSet rs3 = ps3.executeQuery(); //Gets all the columns from the occupation table
			while(rs1.next()) { //Adds all the people fields to the fields list
				fields.add("Person -"+WordUtils.capitalize(rs1.getString(1))); //Adds fields to fields list
			}
			while(rs2.next()) { //Adds all the address fields to the fields list
				fields.add("Address -"+WordUtils.capitalize(rs2.getString(1))); //Adds fields to fields list
			}
			while(rs3.next()) { //Adds all the occupation fields to the fields list
				fields.add("Occupation -"+WordUtils.capitalize(rs3.getString(1))); //Adds fields to fields list
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
			if(selectedField.contains("Date")) { //Gets correct formatting if field is date
				data = Helper.dateFinder(reader);
			}else if(selectedField.contains("Time")) { //Gets correct formatting if field is time
				data = Helper.timeFinder(reader);
			}else {
				System.out.print("\nPlease enter data for " + Helper.split(selectedField).toLowerCase()+ ": ");
				data = reader.readLine();
				System.out.println();
			}
			if(selectedField.contains("Person")) { //Gets a list of similar people
				people = Person.getPerson(conn, selectedField.substring(selectedField.indexOf("- ")+2).toLowerCase(), data);
			}else if(selectedField.contains("Address")) { //Gets a list of similar people based on having a similar field from the address table
				List<Address> addresses = Address.getAddress(conn, selectedField.substring(selectedField.indexOf("- ")+2).toLowerCase(), data);
				List<String> addressIds = new ArrayList<>();
				for(Address address: addresses) { //Adds all the address id's to a list
					addressIds.add(Integer.toString(address.getId()));
				}
				for(String addressId: addressIds) { //Gets all the people that have matching address id's
					System.out.println(String.valueOf(addressId));
					List<Person> temp = Person.getPerson(conn, "addressid", addressId);
					for(Person person: temp) { // Adds the people with similar address fields to the people list
						people.add(person);
					}
				}
			}else if(selectedField.contains("Occupation")) { //Gets a list of similar people based on having a similar field from the occupation table
				List<Occupation> occupations = Occupation.getOccupation(conn, selectedField.substring(selectedField.indexOf("- ")+2).toLowerCase(), data);
				List<String> occupationIds = new ArrayList<>();
				for(Occupation occupation: occupations) { // Adds all the occupation id's to a list
					occupationIds.add(Integer.toString(occupation.getId()));
				}
				for(String occupationId: occupationIds) { //Gets all the people that have matching occupation id's
					List<Person> temp = Person.getPerson(conn, "occupationid", occupationId);
					for(Person person: temp) { //Adds the people with similar occupation fields to the people list
						people.add(person);
					}
				}
			}
			System.out.println();
			if(people.isEmpty()) { //Checks if the list is empty
				System.out.println("No matching people in the database!");
				Helper.log("No matching people in the database!", "PersonHelper.java", "getSimilarPeople()");
			}else { //Prints out all the people to the console!
				for(Person person: people) {
					System.out.println(person);
				}
			}
		}catch(Exception e) {
			System.out.println("Error getting similar people. Please check the log!");
			Helper.log(e, "PersonHelper.java", "getSimilarPeople()");
		}
	}

	/**
	 * Prints out a singular person based on a user based ID
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void getSingularPerson(Connection conn, BufferedReader reader) {
		try {
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM person");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ids.add(Integer.valueOf(rs.getInt(1))); //Adds all the id of the people in the database to the list of id's
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
				Person person = Person.getPerson(conn, selectedId);
				System.out.println(person); //Gets the selected person and prints out the person
			}else { //Prints out error message telling the user they entered an invalid id
				System.out.println("The ID the user entered is not valid!");
				Helper.log("The ID the user entered is not valid!", "PersonHelper.java", "getSingularPerson()");
			}
		}catch(Exception e) {
			System.out.println("Error getting singular person. Please check the log!");
			Helper.log(e, "PersonHelper.java", "getSingularPerson()");
		}
	}

	/**
	 * Updates a user selected person from the database and allows the user to create a new address or occupation 
	 * when updating addressId and occupationId and prints new person after updating
	 * @param conn  The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void updateSingularPerson(Connection conn, BufferedReader reader) {
		try {
			List<String> fields = new ArrayList<>();
			List<String> data = new ArrayList<>();
			int addressId = 0;
			int occupationId = 0;
			PreparedStatement ps1 = conn.prepareStatement("DESCRIBE person");
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) { //Adds all the fields except date, time, and person id to the fields list
				if(!rs1.getString(1).equals("date") && !rs1.getString(1).equals("time") && !rs1.getString(1).equals("id")) {
					fields.add(WordUtils.capitalize(rs1.getString(1)));
				}
			}
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps2 = conn.prepareStatement("SELECT id FROM person");
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) { //Adds all the id's of the people from the database to the id's list
				ids.add(rs2.getInt(1));
			}
			getAllPeople(conn);
			System.out.print("Enter the ID of the person you want to update: ");
			int id = Integer.parseInt(reader.readLine()); 
			if(ids.contains(id)) { //Checks if the user entered id is a valid ID
				System.out.println("To keep exisiting data leave the field blank!");
				for(String field: fields) { //Loops through the fields and allows the user to update the data in a given field or leave the data the same
					String temp = "";
					if(field.equals("AddressId")) { //Checks if the field is address id and allows user to create a new address or use an existing address
						System.out.print("Do you want to create a new address for this person (Y/N): ");
						String newAddress = reader.readLine();
						if(newAddress.equalsIgnoreCase("y")) { //Allows user to create a new address for selected person and updates id of person with new address
							AddressHelper.insertSingularAddress(conn, reader);
							addressId = Helper.mostRecent(conn, "address");
						}else if(newAddress.equalsIgnoreCase("n")) { //Allows user to select an address from the database and sets the id of the person for the selected address
							List<Address> addresses = Address.getAddress(conn);
							List<Integer> addressIds = new ArrayList<>();
							System.out.println();
							for(Address address: addresses) { //Prints out all the addresses in the database and adds all the id's to the addressIds list
								System.out.println(address);
								addressIds.add(address.getId());
							}
							System.out.print("Enter ID of the new address: ");
							String addressTemp = reader.readLine();
							if(addressTemp.equals("")) { //Checks if the address id is empty
								addressId = 0;
							}else if(addressIds.contains(Integer.parseInt(addressTemp))) { //Checks if the address id is a valid address id
								addressId = Integer.parseInt(addressTemp);
							}else {
								System.out.println("Invalid ID for Address ID!");
								Helper.log("Invalid ID for Address ID!", "PersonHelper.java", "updateSingularPerson()");
							}
						}else {
							System.out.println("Invalid response for creating a new address!");
							Helper.log("Invalid resposne for creating a new address!", "PersonHelper.java", "updateSingularPerson()");
						}
					}else if(field.equals("OccupationId")) { //Checks if the field is occupation id and allows user to create a new occupation or use an existing occupation
						System.out.print("Do you want to create a new occupation for this person (Y/N): ");
						String newOccupation = reader.readLine();
						if(newOccupation.equalsIgnoreCase("y")) { //Allows user to create a new occupation for selected person and updates id of person with new occupation
							OccupationHelper.insertSingularOccupation(conn, reader);
							occupationId = Helper.mostRecent(conn, "occupation");
						}else if(newOccupation.equalsIgnoreCase("n")) { //Allows user to select an occupation from the database and sets the id of person for the selected occupation
							List<Occupation> occupations = Occupation.getOccupation(conn);
							List<Integer> occupationIds = new ArrayList<Integer>();
							System.out.println();
							for (Occupation occupation : occupations) { //Prints out all the occupation ins the database and adds all the id's to the occupationIds list
								System.out.println(occupation);
								occupationIds.add(occupation.getId());
							}
							System.out.print("Enter ID of new occupation: ");
							String occupationTemp = reader.readLine();
							if(occupationTemp.equals("")) { //Checks if the occupation id is empty
								occupationId = 0;
							}else if(occupationIds.contains(Integer.parseInt(occupationTemp))){ //Checks if the occupation id is a valid occupation id
								occupationId = Integer.parseInt(occupationTemp);
							}else {
								System.out.println("Invalid ID for Occupation!");
								Helper.log("Invalid ID for Occupation!", "PersonHelper.java", "updateSingularPerson()");
							}
						}else {
							System.out.println("Invalid repsonse for creating a new occupation!");
							Helper.log("Invalid response for creating a new occupation", "PersonHelper.java", "updateSingularPerson()");
						}
					}else if(field.equals("HomePhone") || field.equals("MobilePhone") || field.equals("WorkPhone")) { //Checks if the field is a phone number field
						System.out.println();
						System.out.println("Please don't include the country code or dashes! Only 10 digit phone numbers are supported!");
						System.out.print("Enter new data for " + Helper.split(field).toLowerCase()+": ");
						temp = reader.readLine();
						if(temp.length()!=10 && !temp.equals("")) { //Checks to make sure phone number is a 10 digit number
							System.out.println("Phone number not supported!");
							Helper.log("Phone number not supported", "PersonHelper.java", "updateSingularperson()");
						}
					}else if(field.equals("Height")) { //Checks if the field is equal to height to make sure data is in correct units and formatting
						System.out.println();
						System.out.println("Please enter height in cm!");
						System.out.print("Enter new data for height: ");
						temp = reader.readLine();
						if(temp.equals("")) { //Checks if height is empty and sets to 0.0 for better compatibility when keeping original value
							temp = "0.0";
						}
					}else if(field.equals("Weight")) { //Checks if the field is equal to weight to make sure data is in correct units and formatting
						System.out.println();
						System.out.println("Please enter weight in lbs!");
						System.out.print("Enter new data for weight: ");
						temp = reader.readLine();
						if(temp.equals("")) { //Checks if weight is empty and sets to 0.0 for better compatibility when keeping original value
							temp = "0.0";
						}
					}else {
						System.out.print("Enter new data for " + Helper.split(field).toLowerCase() + ": ");
						temp = reader.readLine();
					}
					data.add(WordUtils.capitalize(temp)); //Uses WordUtils to make the word capitalized every new word and adds the word to the list
				}
				System.out.println();
				System.out.println(Person.updatePerson(conn, id, data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5), data.get(6), data.get(7), Double.parseDouble(data.get(8)), Double.parseDouble(data.get(9)), data.get(10), data.get(11), addressId, occupationId)); //Updates the person and prints out the updated person
			}else {
				System.out.println("The ID the user entered is not valid!");
				Helper.log("The ID the user entered in not valid!", "PersonHelper.java", "updateSingularPerson()");
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error updating person. Please check the log!");
			Helper.log(e, "PersonHelper.java", "updateSingularPerson()");
		}
	}

	/**
	 * Uses updateSingularPeople() to update multiple people at once from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void updateMultiplePeople(Connection conn, BufferedReader reader) {
		try {
			int maxNumPeople = Person.getPerson(conn).size();
			System.out.print("How many people do you want to update (must be less then " + maxNumPeople + "): ");
			int numPeople = Integer.parseInt(reader.readLine());
			if(numPeople>maxNumPeople) { // Checks that the number of people the user wants to update is not more then the number of people in the database
				System.out.println("Can't update " + numPeople + " only " + maxNumPeople + " people are in the database!");
				Helper.log("Can't update " + numPeople + " only " + maxNumPeople + " people are in the database!", "PersonHelper.java", "updateMultiplePeople()");
			}else if(!(numPeople>=0)) { //Checks that the number of people the user wants to update is greater then 0
				System.out.println("Can't update " + numPeople + " people!");
				Helper.log("Can't update " + numPeople + " people!", "PersonHelper.java", "updateMultiplePeople()");
			}else {
				System.out.println();
				for(int i=0; i<numPeople; i++) { //Loops through updateSingularPerson() for the amount of people the user wants to update!
					int temp = i;
					System.out.println("Updating person #" + (temp+=1) + " of " + numPeople); //Tells the user which number person they are updating and how many total they are updating
					System.out.println();
					updateSingularPerson(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error updating person. Please check the log!");
			Helper.log(e, "PersonHelper.java", "updateMultiplePeople()");
		}
	}

	/**
	 * Create a new person with user entered data into the database and allows the user to create a new address or occupation 
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void insertSingularPerson(Connection conn, BufferedReader reader) {
		try {
			String temp = "";
			int addressId = 0;
			int occupationId = 0;
			List<String> fields = new ArrayList<>();
			List<String> data = new ArrayList<>();
			PreparedStatement ps1 = conn.prepareStatement("DESCRIBE person");
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) { //Adds all the fields except date, time, and person id to the fields list
				if(!rs1.getString(1).equals("date") && !rs1.getString(1).equals("time") && !rs1.getString(1).equals("id")) { 
					fields.add(WordUtils.capitalize(rs1.getString(1)));
				}
			}
			for(String field: fields) {
				if(field.equals("AddressId")) { //Checks if the field is address id and allows user to create a new address or use an existing address
					System.out.print("Do you want to create a new address for this person (Y/N): ");
					String newAddress = reader.readLine();
					if(newAddress.equalsIgnoreCase("y")) { //Allows user to create a new address for selected person and updates id of person with new address
						AddressHelper.insertSingularAddress(conn, reader);
						addressId = Helper.mostRecent(conn, "address");
					}else if(newAddress.equalsIgnoreCase("n")) { //Allows user to select an address from the database and sets the id of the person for the selected address
						List<Address> addresses = Address.getAddress(conn);
						List<Integer> addressIds = new ArrayList<>();
						for(Address address: addresses) { //Prints out all the addresses in the database and adds all the id's to the addressIds list
							System.out.println(address);
							addressIds.add(address.getId());
							System.out.println();
						}
						System.out.print("Enter ID of the new address: ");
						String addressTemp = reader.readLine();
						if(addressTemp.equals("")) { //Checks if the address id is empty
							addressId = 0;
						}else if(addressIds.contains(Integer.parseInt(addressTemp))) { //Checks if the address id is a valid address id
							addressId = Integer.parseInt(addressTemp);
						}else {
							System.out.println("Invalid ID for Address ID!");
							Helper.log("Invalid ID for Address ID!", "PersonHelper.java", "insertSingularPerson()");
						}
					}else {
						System.out.println("Invalid response for creating a new address!");
						Helper.log("Invalid resposne for creating a new address!", "PersonHelper.java", "insertSingularPerson()");
					}
				}else if(field.equals("OccupationId")) { //Checks if the field is occupation id and allows user to create a new occupation or use an existing occupation
					System.out.print("Do you want to create a new occupation for this person (Y/N): ");
					String newOccupation = reader.readLine();
					if(newOccupation.equalsIgnoreCase("y")) { //Allows user to create a new occupation for selected person and updates id of person with new occupation
						OccupationHelper.insertSingularOccupation(conn, reader);
						occupationId = Helper.mostRecent(conn, "occupation");
					}else if(newOccupation.equalsIgnoreCase("n")) { //Allows user to select an occupation from the database and sets the id of person for the selected occupation
						List<Occupation> occupations = Occupation.getOccupation(conn);
						List<Integer> occupationIds = new ArrayList<Integer>();
						for (Occupation occupation : occupations) { //Prints out all the occupation ins the database and adds all the id's to the occupationIds list
							System.out.println(occupation);
							occupationIds.add(occupation.getId());
							System.out.println();
						}
						System.out.print("Enter ID of new occupation: ");
						String occupationTemp = reader.readLine();
						if(occupationTemp.equals("")) { //Checks if the occupation id is empty
							occupationId = 0;
						}else if(occupationIds.contains(Integer.parseInt(occupationTemp))){ //Checks if the occupation id is a valid occupation id
							occupationId = Integer.parseInt(occupationTemp);
						}else {
							System.out.println("Invalid ID for Occupation!");
							Helper.log("Invalid ID for Occupation!", "PersonHelper.java", "insertSingularPerson()");
						}
					}else {
						System.out.println("Invalid repsonse for creating a new occupation!");
						Helper.log("Invalid response for creating a new occupation", "PersonHelper.java", "insertSingularPerson()");
					}
				}else if(field.equals("HomePhone") || field.equals("MobilePhone") || field.equals("WorkPhone")) { //Checks if the field is a phone number field
					System.out.println();
					System.out.println("Please don't include the country code or dashes! Only 10 digit phone numbers are supported!");
					System.out.print("Enter new data for " + Helper.split(field).toLowerCase() + ": ");
					temp = reader.readLine();
					if(temp.length()!=10) { //Checks to make sure the phone number is a 10 digit number
						System.out.println("Phone number not supported!");
						Helper.log("Phone number not supported!", "PersonHelper.java", "insertSingularPerson()");
					}
				}else if(field.equals("Height")) { //Checks if the field is equal to height to make sure data is in correct units and formatting
					System.out.println();
					System.out.println("Please enter height in cm!");
					System.out.print("Enter data for height: ");
					temp = reader.readLine();
				}else if(field.equals("Weight")) { //Checks if the field is equal to weight to make sure data is in correct units and formatting
					System.out.println();
					System.out.println("Please enter weight in lbs!");
					System.out.print("Enter data for weight: ");
					temp = reader.readLine();
				}else {
					System.out.print("Enter data for " + Helper.split(field).toLowerCase() + ": ");
					temp = reader.readLine();
				}
				if(temp.equals("")) { //Makes sure the field isn't empty creating an error when adding new person
					System.out.println(Helper.split(field) + " can't be empty!");
					Helper.log(Helper.split(field) + " can't be empty!", "PersonHelper.java", "insertSingularPerson()");
				}else {
					data.add(WordUtils.capitalize(temp)); //Uses WordUtils to make the word capitalized every new word and adds the word to the list
				}
			}
			System.out.println();
			System.out.println(Person.insertPerson(conn, data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5), data.get(6), data.get(7), Double.parseDouble(data.get(8)), Double.parseDouble(data.get(9)), data.get(10), data.get(11), addressId, occupationId)); //Creates the new person and prints out the new person
		}catch(Exception e) {
			System.out.println("Error creating new person. Please check the log!");
			Helper.log(e, "PersonHelper.java", "newSingularPerson()");
		}
	}

	/**
	 * Uses insertSingularPerson() to insert multiple please at once into the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void insertMultiplePeople(Connection conn, BufferedReader reader) {
		try {
			System.out.print("How many people do you want to create: ");
			int numPeople = Integer.parseInt(reader.readLine());
			if(!(numPeople>=0)) { //Checks that the number of people the user wants to create is greater then 0
				System.out.println("Can't create " + numPeople + " people!");
				Helper.log("Can't create " + numPeople + "people!", "PersonHelper.java", "insertMultiplePeople()");
			}else {
				System.out.println();
				for(int i=0; i<numPeople; i++) { //Loops though insertSingularPerson() for the amount of people the user wants to create
					int temp = i; 
					System.out.println("Creating person #" + (temp+=1) + " of " + numPeople); //Tells the user which number person they are creating
					System.out.println();
					insertSingularPerson(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error creating new person. Please check the log!");
			Helper.log(e, "PersonHelper.java", "insertMultiplePeople()");
		}
	}

	/**
	 * Asks the user to select a person and removes that person from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void removeSingularPerson(Connection conn, BufferedReader reader) {
		try {
			List<Integer> ids = new ArrayList<>();
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM person");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) { //Loops through all the id's in the people table and adds them to the id's list
				ids.add(rs.getInt(1));
			}
			getAllPeople(conn);//Printing out all the people in the database
			System.out.print("Please select a person: ");
			int selectedId = Integer.parseInt(reader.readLine());
			if(ids.contains(selectedId)) { //Makes sure the selected id is a valid id
				boolean removed = Person.removePerson(conn, "id", Integer.toString(selectedId));
				if(removed) { //Makes sure the address was removed from the database
					System.out.println();
					System.out.println("Person with the id " + selectedId + " was removed from the database!");
					System.out.println();
				}else {
					System.out.println("There was an error removing the person with " + selectedId + " from the database!");
					Helper.log("There was an error removing the person with " + selectedId + " from the database!", "PersonHelper.java", "removeSingularPerson()");
				}
			}else {
				System.out.println("The ID the user enter was not valid!");
				Helper.log("The ID the user enter was not valid!", "PersonHelper.java", "removeSingularPerson()");
			}
		}catch(Exception e) {
			System.out.println("Error removing person. Please check the log!");
			Helper.log(e, "PersonHelper.java", "removeSingularPerson()");
		}
	}

	/**
	 * Uses removeSingularPerson() to remove multiple people at once from the database
	 * @param conn The MySQL connection
	 * @param reader How I am reading data from the console
	 */
	public static void removeMultiplePeople(Connection conn, BufferedReader reader) {
		try {
			int maxNumPeople = Person.getPerson(conn).size();
			System.out.print("How many people do you want to update (must be less then " + maxNumPeople + "): ");
			int numPeople = Integer.parseInt(reader.readLine());
			if(numPeople>maxNumPeople) { //Checks that the number of people the user wants to remove is not more then the number of people in the database
				System.out.println("Can't remove " + numPeople + " only " + maxNumPeople + " people are in the database!");
				Helper.log("Can't remove " + numPeople + " only " + maxNumPeople + " people are in the database!", "PersonHelper.java", "removeMultiplePeople()");
			}else if(!(numPeople>=0)) { //Checks that the number of people the user wants to remove is greater then 0
				System.out.println("Can't remove "+ numPeople + " people!");
				Helper.log("Can't remove "+ numPeople + " people!", "PersonHelper.java", "removeMultiplePeople()");
			}else {
				System.out.println();
				for(int i=0; i<numPeople; i++) { //Loops through removeSingularPerson() for the amount of people the user wants to remove
					int temp = i;
					System.out.println("Removing person #" + (temp+=1) + " of " + numPeople);
					System.out.println();
					removeSingularPerson(conn, reader);
				}
			}
		}catch(Exception e) {
			System.out.println("Error removing person. Please check the log!");
			Helper.log(e, "PersonHelper.java", "removeMultiplePeople()");
		}
	}
}
