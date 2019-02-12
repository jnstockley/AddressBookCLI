import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jack Stockely
 * 
 * @version 1.0.2
 * 
 * @description The Address object of the Address Book project
 * 
 * @date 11 February 2019
 *
 */
public class Address {
	private int id;
	private String number;
	private String name;
	private String city;
	private String state;
	private String zip;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * Overrides the built in toString command and replaces with the current one which returns the id, the house number, the street name, 
	 * the city, the state, and the zip code
	 */
	public String toString() {
		return "ID: " + this.getId() + " " +this.getNumber() + " " + this.getName() + " " + this.getCity() + ", " + this.getState() + " " + this.getZip();
	}

	/**
	 * Returns a list of all the addresses in the mySQL server
	 * @param conn The mySQL connection
	 * @return The list of all the address on the mySQL server
	 */
	public static List<Address> getAll(Connection conn){
		try {
			List<Address> addresses = new ArrayList<Address>();
			PreparedStatement ps = conn.prepareStatement("SELECT id, number, name, city, state, zip FROM address");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Address address = new Address();
				int col = 1;
				address.setId(rs.getInt(col++));
				address.setNumber(rs.getString(col++));
				address.setName(rs.getString(col++));
				address.setCity(rs.getString(col++));
				address.setState(rs.getString(col++));
				address.setZip(rs.getString(col++));
				addresses.add(address);
			}
			return addresses;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a list of all addresses that have a user defined field type in common
	 * @param conn The mySQL connection
	 * @param fieldName The user defined field type
	 * @param field The field that the addresses have in common
	 * @return A list of similar addresses
	 */
	public static List<Address> getSimilar(Connection conn, String fieldName, String field){
		try {
			List<Address> addresses  = new ArrayList<Address>();
			PreparedStatement ps = conn.prepareStatement("SELECT id, number, name, city, state, zip FROM address WHERE " + fieldName + " = ?");
			ps.setString(1, field);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Address address = new Address();
				int col = 1;
				address.setId(rs.getInt(col++));
				address.setNumber(rs.getString(col++));
				address.setName(rs.getString(col++));
				address.setCity(rs.getString(col++));
				address.setState(rs.getString(col++));
				address.setZip(rs.getString(col++));
				addresses.add(address);
			}
			return addresses;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a single address
	 * @param conn The mySQL connection
	 * @param fieldName The user defined field
	 * @param field The given field
	 * @return The single person
	 */
	public static Address getBy(Connection conn, String fieldName, String field) {
		try {
			Address address = new Address();
			PreparedStatement ps = conn.prepareStatement("SELECT id, number, name, city, state, zip FROM address WHERE " + fieldName + " = ?");
			//ps.setString(1, fieldName);
			ps.setString(1, field); 
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int col = 1;
				address.setId(rs.getInt(col++));
				address.setNumber(rs.getString(col++));
				address.setName(rs.getString(col++));
				address.setCity(rs.getString(col++));
				address.setState(rs.getString(col++));
				address.setZip(rs.getString(col++));
			}
			return address;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Updates a user defined address on the mySQL server
	 * @param conn The mySQL connection
	 * @param id The id of the address
	 * @param number The new house number 
	 * @param name The new street name
	 * @param city The new city
	 * @param state The new state
	 * @param zip The new zip code
	 */
	public static void update(Connection conn, int id, String number, String name, String city, String state, String zip) {
		Address address = Address.getBy(conn, "id", Integer.toString(id));
		if(number.equals("")) {
			number=address.getNumber();
		}
		if(name.equals("")) {
			name=address.getName();
		}
		if(city.equals("")) {
			city=address.getCity();
		}
		if(state.equals("")) {
			state=address.getState();
		}
		if(zip.equals("")) {
			zip=address.getZip();
		}
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE address SET number=?, name=?, city=?, state=?, zip=? WHERE id =?");
			ps.setString(1, number);
			ps.setString(2, name);
			ps.setString(3, city);
			ps.setString(4, state);
			ps.setString(5,zip);
			ps.setInt(6, id);
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts a new address onto the mySQL server
	 * @param conn The mySQL connection
	 * @param number The house number
	 * @param name The street name
	 * @param city The city
	 * @param state The state
	 * @param zip The zip code
	 */
	public static void insert(Connection conn, String number, String name, String city, String state, String zip) {
		try{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO address (number, name, city, state, zip) values (?,?,?,?,?)");
			ps.setString(1, number);
			ps.setString(2, name);
			ps.setString(3, city);
			ps.setString(4, state);
			ps.setString(5, zip);
			ps.execute();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes an address from the mySQL server
	 * @param conn The mySQL connection
	 * @param id The id of the address the user wants to remove
	 */
	public static void remove(Connection conn, int id) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM address WHERE id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}