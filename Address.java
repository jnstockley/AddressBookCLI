package addressBook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This part of the program interacts directly with the address table of the MySQL database and allows the user to perform full CRUD
 * @author jnstockley
 * @version 2.00
 *
 */

public class Address {
	private int id;

	private int number;

	private String street;

	private String city;

	private String state;

	private String zip;

	private String date;

	private String time;

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * Overrides the built in toString method and returns the address
	 */
	public String toString() {
		return "ID: " + this.getId() + " " + this.getNumber() + " " + this.getStreet() + "\n" + this.getCity() + ", " + this.getState() + " " + this.getZip() + "\nDate Created: " + this.getDate() + " Time Created: " + this.getTime() + "\n";
	}

	/**
	 * Overrides the built in equals methods and checks each of the fields to make sure they are the same ignoring case
	 * @param address The address that is being compared to
	 * @return True if the two addresses are equal otherwise false
	 */
	public boolean equals(Address address) {
		if (this.getNumber() == address.getNumber() && this.getStreet().equalsIgnoreCase(address.getStreet()) && this.getCity().equalsIgnoreCase(address.getCity()) && this.getState().equalsIgnoreCase(address.getState()) && this.getZip().equalsIgnoreCase(address.getZip())) {
			return true; 
		}else {
			return false;
		}
	}

	/**
	 * Creates a blank address object with no fields filled in
	 * Used when getting addresses from the database
	 */
	public Address() {}

	/**
	 * Creates a new address without an ID but has all the other required filed
	 * Used when inserting a new address 
	 * @param number The house number of the address
	 * @param street The street name of the address
	 * @param city The city the address is in
	 * @param state The state the address is in
	 * @param zip The zip code of the address
	 */
	public Address(int number, String street, String city, String state, String zip) {
		setNumber(number);
		setStreet(street);
		setCity(city);
		setState(state);
		setZip(zip);
	}

	/**
	 * Creates a new occupation with an ID and other required fields
	 * @param id The ID of the new address which resides on the MySQL server
	 * @param number The house number of the address
	 * @param street The street name of the address
	 * @param city The city the address is in
	 * @param state The state the address is in
	 * @param zip The zip code of the address
	 */
	public Address(int id, int number, String street, String city, String state, String zip) {
		setId(id);
		setNumber(number);
		setStreet(street);
		setCity(city);
		setState(state);
		setZip(zip);
	}

	/**
	 * Connects to the MySQL server and returns all the addresses in the database
	 * @param conn The MySQL connection
	 * @return A list of addresses that are stored on the MySQL database
	 */
	public static List<Address> getAddress(Connection conn) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				List<Address> addresses = new ArrayList<>(); //List where addresses from the database will be added to
				PreparedStatement ps = conn.prepareStatement("SELECT id, number, street, city, state, zip, date, time FROM address");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) { //Loops through all the people in the database and creates a new address and adds all required fields then adds the address to the address list
					int col = 1;
					Address address = new Address();
					address.setId(rs.getInt(col++));
					address.setNumber(rs.getInt(col++));
					address.setStreet(rs.getString(col++));
					address.setCity(rs.getString(col++));
					address.setState(rs.getString(col++));
					address.setZip(rs.getString(col++));
					address.setDate(rs.getString(col++));
					address.setTime(rs.getString(col++));
					addresses.add(address);
				} 
				return addresses;
			} 
			System.out.println("Connection not valid!");
			Helper.log("Connection is not valid!", "Address.java", "getAddress()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Address.java", "getAddress()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and returns all the addresses that have a similarity between them based on user input
	 * @param conn The MySQL connection
	 * @param field The field that will be compared with when finding similarities
	 * @param data The data that will need to be similar in order for the person to be considered similar
	 * @return A list of addresses that are considered similar based on user input
	 */
	public static List<Address> getAddress(Connection conn, String field, String data) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				List<Address> similarAddress = new ArrayList<>(); //List where addresses with similarities, based on a field and data provided by the user, are added
				PreparedStatement ps = null;
				if(field.equalsIgnoreCase("date") || field.equalsIgnoreCase("time")) { //Checks if field is time or date to allow user to search for people created within a certain year, month etc.
					ps = conn.prepareStatement("SELECT id, number, street, city, state, zip, date, time FROM address WHERE " + field + " LIKE CONCAT(" + data + ")");
				}else {
					ps = conn.prepareStatement("SELECT id, number, street, city, state, zip, date, time FROM address WHERE " + field + "=?");
					ps.setString(1, data);
				}
				ResultSet rs = ps.executeQuery();
				while (rs.next()) { //Loops through all the similar addresses in the database and creates a new address and adds all the required fields then adds the address to the similarAddresses list
					int col = 1;
					Address address = new Address();
					address.setId(rs.getInt(col++));
					address.setNumber(rs.getInt(col++));
					address.setStreet(rs.getString(col++));
					address.setCity(rs.getString(col++));
					address.setState(rs.getString(col++));
					address.setZip(rs.getString(col++));
					address.setDate(rs.getString(col++));
					address.setTime(rs.getString(col++));
					similarAddress.add(address);
				} 
				return similarAddress;
			} 
			System.out.println("Connection not valid!");
			Helper.log("Connection is not valid!", "Address.java", "getAddress()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Address.java", "getAddress()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and returns a singular address based on the id the user inputed
	 * @param conn The MySQL connection
	 * @param id The ID of the address that will be returned from the database
	 * @return A singular address based on the user entered ID
	 */
	public static Address getAddress(Connection conn, int id) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				Address address = new Address();
				PreparedStatement ps = conn.prepareStatement("SELECT id, number, street, city, state, zip, date, time FROM address WHERE id = ?");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) { //Checks if the MySQL prepared statement found any matching results and if so creates a new address and initializes that address and returns the address
					int col = 1;
					address.setId(rs.getInt(col++));
					address.setNumber(rs.getInt(col++));
					address.setStreet(rs.getString(col++));
					address.setCity(rs.getString(col++));
					address.setState(rs.getString(col++));
					address.setZip(rs.getString(col++));
					address.setDate(rs.getString(col++));
					address.setTime(rs.getString(col++));
				} 
				return address;
			} 
			System.out.println("Connection not valid!");
			Helper.log("Connection is not valid!", "Address.java", "getAddress()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Address.java", "getAddress()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and updates an address with a user inputed id and checks to make sure that the new address doesn't already exist in the database. If successful at updating the address the new address will be returned
	 * @param conn The MySQL connection
	 * @param id The ID of the address to update
	 * @param number The new number of the address to be updated blank if keeping the old number
	 * @param street The new street of the address to be updated blank if keeping the old street
	 * @param city The new city of the address to be updated blank if keeping the old city
	 * @param state The new stare of the address to be updated blank if keeping the old state
	 * @param zip The new zip of the address to be updated blank if keeping the old zip
	 * @return The updated address if successful if not null is returned
	 */
	public static Address updateAddress(Connection conn, int id, int number, String street, String city, String state, String zip) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				Address address = getAddress(conn, id); //Gets the current address that is being updated
				Address newAddress = new Address(id, number, street, city, state, zip); //Creates a new address that is overriding the old address
				Date date = Date.valueOf(LocalDate.now());
				Time time = Time.valueOf(LocalTime.now());
				//Sets the new date and time for the address
				newAddress.setDate(date.toString());
				newAddress.setTime(time.toString());
				//Checks if any fields are blank and changes them to the old addresses data
				if (number == 0) {
					newAddress.setNumber(address.getNumber());
				}
				if (street.equals("")) {
					newAddress.setStreet(address.getStreet());
				}
				if (city.equals("")) {
					newAddress.setCity(address.getCity());
				}
				if (state.equals("")) {
					newAddress.setState(address.getState());
				}
				if (zip.equals("")) {
					newAddress.setZip(address.getZip());
				}
				if (!Helper.exists(newAddress, conn)) { //Checks if the new address exists on the database and if not updates the address on the database
					PreparedStatement ps = conn.prepareStatement("UPDATE address SET number=?, street=?, city=?, state=?, zip=?, date=?, time=? WHERE id =?");
					ps.setInt(1, newAddress.getNumber());
					ps.setString(2, newAddress.getStreet());
					ps.setString(3, newAddress.getCity());
					ps.setString(4, newAddress.getState());
					ps.setString(5, newAddress.getZip());
					ps.setDate(6, date);
					ps.setTime(7, time);
					ps.setInt(8, id);
					ps.executeUpdate();
					if (getAddress(conn, id).equals(newAddress)) { //Checks that the address got updated
						System.out.println("Updated address at ID: " + id);
						return newAddress;
					}
				} 
				System.out.println("Address already exists!");
				return address;
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Address.java", "updateAddress()");
			return null;
		} catch (Exception e) {
			System.out.println("Error updating address. Please check the log!");
			Helper.log(e, "Address.java", "updateAddress()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and create a new address with a user inputed id and checks to make sure that the new address doesn't already exist in the database. If successful at creating the address the new address will be returned
	 * @param conn The MySQL connection
	 * @param number The new number of the address to be created 
	 * @param street The new street of the address to be created
	 * @param city The new city of the address to be created
	 * @param state The new stare of the address to be created
	 * @param zip The new zip of the address to be created
	 * @return The new address if successful if not null is returned
	 */
	public static Address insertAddress(Connection conn, int number, String street, String city, String state, String zip) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				Address address = new Address(number, street, city, state, zip); //Creates a new address that will be added to the database
				Date date = Date.valueOf(LocalDate.now());
				Time time = Time.valueOf(LocalTime.now());
				//Sets the date and time for the address
				address.setDate(date.toString());
				address.setTime(time.toString());
				if (!Helper.exists(address, conn)) { //Checks if the new address exists on the database and if not added the address to the database
					PreparedStatement ps = conn.prepareStatement("INSERT INTO address (number, street, city, state, zip, date, time) values (?,?,?,?,?,?,?)");
					ps.setInt(1, number);
					ps.setString(2, street);
					ps.setString(3, city);
					ps.setString(4, state);
					ps.setString(5, zip);
					ps.setDate(6, date);
					ps.setTime(7, time);
					ps.execute();
					address.setId(Helper.mostRecent(conn, "address"));
					if (getAddress(conn, address.getId()).equals(address)) { //Checks that the new person got added
						System.out.println("Added Address!");
						return address;
					} 
				}else {
					System.out.println("Address already exists!");
					return address;
				}
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Address.java", "insertAddress()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Address.java", "insertAddress()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and removes a selected address with user inputed fields and data
	 * @param conn The MySQL connection
	 * @param field The field that will be used when removing the address
	 * @param data The data that will need to be used when finding the address to remove
	 * @return True if the address was removed from the databases otherwise false
	 */
	public static boolean removeAddress(Connection conn, String field, String data) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				PreparedStatement ps = conn.prepareStatement("DELETE FROM address WHERE " + field + " = ?");
				ps.setString(1, data);
				ps.executeUpdate();
				if (getAddress(conn, field, data).isEmpty()) { //Checks to makes sure the person was removed
					return true; 
				}else {
					System.out.println("Error removing address.");
					Helper.log("Error removing address.", "Address.java", "removeAddress()");
					return false;
				}
			} 
			System.out.println("Connection is not valid");
			System.exit(1);
			return false;
		} catch (Exception e) {
			Helper.log(e, "Address.java", "removeAddress()");
			return false;
		} 
	}
}
