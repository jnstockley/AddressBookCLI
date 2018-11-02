import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Address {
	private int id;
	private String number;
	private String name;
	private String city;
	private String state;
	private String zip;

	public Address(){
	}

	/**
	 * Returns the id of the object
	 * @return The id given to the object
	 */
	public int getId(){
		return id;
	}

	/**
	 * Sets the id of the object
	 * @param id The id from the mySQL database
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the house number of the object
	 * @return The given house number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Sets the house number of the object
	 * @param number The house number from the mySQL database
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * Returns the street name of the object
	 * @return The given street name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the street name of the object
	 * @param name The street name from the mySQL database
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the city of the object
	 * @return The given city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city of the object
	 * @param city The city from the mySQL database
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Returns the state of the object
	 * @return The given state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state of the object
	 * @param state The state from the mySQL database
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Returns the zip code of the object
	 * @return The given zip code
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * Sets the zip code of the object
	 * @param zip The zip code from the mySQL database
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * Prints out the id, the house number, the street name, the city, the state, and the zip code of the address called
	 */
	public void print() {
		System.out.println("Id: " + this.getId() + " " + this.getNumber() + " " + this.getName() + " " + this.getCity() + ", " + this.getState() + " " + this.getZip());
	}
	
	/**
	 * Returns id, the house number, the street name, the city, the state, and the zip code of the address called
	 */
	public String display() {
		return "Id: " + this.getId() + " " + this.getNumber() + " " + this.getName() + " " + this.getCity() + ", " + this.getState() + " " + this.getZip();
	}

	/**
	 * Returns all the addresses in the table
	 * @param conn The mySQL connection
	 * @return The List of all the addresses in the address table
	 */
	public static List<Address> getAll(Connection conn){
		List<Address> addresses = new ArrayList<Address>();
		Encryption decrypt = new Encryption();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT id, number, name, city, state, zip FROM address");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Address address = new Address();
				int col = 1;
				address.setId(rs.getInt(col++));
				address.setNumber(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setName(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setCity(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setState(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setZip(decrypt.decrypt(rs.getString(col++).getBytes()));
				addresses.add(address);
			}
			return addresses;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Returns a singular address by the passed in field Name
	 * @param conn The mySQL connection
	 * @param value The value the fieldName needs to equal for the address to be returned
	 * @param fieldName The field the user is looking up
	 * @return The singular address the user wants
	 */
	public static Address getBy(Connection conn, String value, String fieldName){
		Encryption decrypt = new Encryption();
		try {
			Address address = new Address();
			PreparedStatement ps = conn.prepareStatement("SELECT id, number, name, city, state, zip FROM address WHERE " + fieldName + "  = ?");
			//TODO Bug fixed?
			if(fieldName.equals("id") || fieldName.equals("zip")){
				ps.setInt(1,  Integer.parseInt(value));
			}else{
				ps.setString(1, value);
			}
			ps.setInt(1, Integer.parseInt(value));
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				int col = 1;
				address.setId(rs.getInt(col++));
				address.setNumber(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setName(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setCity(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setState(decrypt.decrypt(rs.getString(col++).getBytes()));
				address.setZip(decrypt.decrypt(rs.getString(col++).getBytes()));
			}
			return address;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Inserts a new row into the table
	 * @param conn The mySQL connection
	 * @param number The house number the user entered
	 * @param name The street name the user entered
	 * @param city The city the user entered
	 * @param state The state the user entered
	 * @param zip The zip code the user entered
	 */
	public static void insert(Connection conn, String number, String name, String city, String state, String zip){
		Encryption encrypt = new Encryption();
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO address (number, name, city, state, zip) values(?,?,?,?,?)");
			ps.setString(1, encrypt.encrypt(number));
			ps.setString(2, encrypt.encrypt(name));
			ps.setString(3, encrypt.encrypt(city));
			ps.setString(4, encrypt.encrypt(state));
			ps.setString(5, encrypt.encrypt(zip));
			ps.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	//TODO How can I leave some null but not update them aka(update some but not all)
	/**
	 * Updates all the fields of a given data table in the address table
	 * @param conn The mySQL connection
	 * @param id The id of the data the user wants to update
	 * @param number The new house number
	 * @param name The new street name
	 * @param city The new city name
	 * @param state The new state
	 * @param zip The new zip code
	 */
	public static void update(Connection conn, int id, String number, String name, String city, String state, String zip){
		Encryption encrypt = new Encryption();
		try{
			PreparedStatement ps = conn.prepareStatement("UPDATE address SET number=?,name=?,city=?,state=?,zip=? WHERE id = ?");
			ps.setString(1, encrypt.encrypt(number));
			ps.setString(2, encrypt.encrypt(name));
			ps.setString(3, encrypt.encrypt(city));
			ps.setString(4, encrypt.encrypt(state));
			ps.setString(5, encrypt.encrypt(zip));
			ps.executeUpdate();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Removes data from the mySQL table where the id's equal each other
	 * @param conn The mySQL connection
	 * @param id The id of the data in the table the user wants to remove
	 */
	public static void remove(Connection conn, int id){
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM address WHERE id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}	
}