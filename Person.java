import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jack Stockely
 * 
 * @version 1.0.3
 * 
 * @description The Person object of the Address Book project
 * 
 * @date 11 February 2019
 *
 */
public class Person {
	private int id;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private int addressId;
	private int occupationId;
	private String number;
	private String name;
	private String city;
	private String state;
	private String zip;
	private String occupation;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public int getOccupationId() {
		return occupationId;
	}
	public void setOccupationId(int occupationId) {
		this.occupationId = occupationId;
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
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * Overrides the built in toString command and replaces it with the current one which return the id, first name, middle initial, 
	 * last name, house number, the street name, the city, the state, the zip code, and the occupation of the given person
	 */
	public String toString() {
		return "ID: " + this.getId() + " " + this.getFirstName() + " " + this.getMiddleInitial() + ". " + this.getLastName() + " " + this.getNumber() + " " + this.getName() + " " + this.getCity() + ", " + this.getState() + " " + this.getZip() + " " + this.getOccupation();
	}

	/**
	 * Returns a list of all the people in the mySQL server
	 * @param conn The mySQL connection
	 * @return The list of all the people on the mySQL server
	 */
	public static List<Person> getAll(Connection conn){
		try {
			List<Person> people = new ArrayList<Person>();
			PreparedStatement ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleInitial, person.lastName, address.number, address.name, address.city, address.state, address.zip, occupation.occupation, address.Id, occupation.Id from person inner join address on person.addressId=address.id inner join occupation on person.occupationId=occupation.id");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Person person = new Person();
				int col = 1;
				person.setId(rs.getInt(col++));
				person.setFirstName(rs.getString(col++));
				person.setMiddleInitial(rs.getString(col++));
				person.setLastName(rs.getString(col++));
				person.setNumber(rs.getString(col++));
				person.setName(rs.getString(col++));
				person.setCity(rs.getString(col++));
				person.setState(rs.getString(col++));
				person.setZip(rs.getString(col++));
				person.setOccupation(rs.getString(col++));
				person.setAddressId(rs.getInt(col++));
				person.setOccupationId(rs.getInt(col++));
				people.add(person);
			}
			return people;
		}catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
	}

	/**
	 * Returns a list of all people that have a user defined field type in common
	 * @param conn The mySQL connection
	 * @param fieldName The user defined filed
	 * @param field The filed that the people have in common
	 * @return The list of similar people
	 */
	public static List<Person> getSimilar(Connection conn, String fieldName, String field){
		try {
			List<Person> people  = new ArrayList<Person>();
			PreparedStatement ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleInitial, person.lastName, address.number, address.name, address.city, address.state, address.zip, occupation.occupation, address.Id, occupation.Id from person inner join address on person.addressId=address.id inner join occupation on person.occupationId=occupation.id WHERE person." + fieldName + " =?;");
			ps.setString(1, field);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Person person = new Person();
				int col = 1;
				person.setId(rs.getInt(col++));
				person.setFirstName(rs.getString(col++));
				person.setMiddleInitial(rs.getString(col++));
				person.setLastName(rs.getString(col++));
				person.setNumber(rs.getString(col++));
				person.setName(rs.getString(col++));
				person.setCity(rs.getString(col++));
				person.setState(rs.getString(col++));
				person.setZip(rs.getString(col++));
				person.setOccupation(rs.getString(col++));
				person.setAddressId(rs.getInt(col++));
				person.setOccupationId(rs.getInt(col++));
				people.add(person);
			}
			return people;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a single person
	 * @param conn The mySQL connection
	 * @param fieldName The user defined field
	 * @param field The given field
	 * @return The single person
	 */
	public static Person getBy(Connection conn, String fieldName, String field) {
		try {
			Person person = new Person();
			PreparedStatement ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleInitial, person.lastName, address.number, address.name, address.city, address.state, address.zip, occupation.occupation, address.Id, occupation.Id from person inner join address on person.addressId=address.id inner join occupation on person.occupationId=occupation.id WHERE person." + fieldName + " =?;");
			ps.setString(1, field);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int col = 1;
				person.setId(rs.getInt(col++));
				person.setFirstName(rs.getString(col++));
				person.setMiddleInitial(rs.getString(col++));
				person.setLastName(rs.getString(col++));
				person.setNumber(rs.getString(col++));
				person.setName(rs.getString(col++));
				person.setCity(rs.getString(col++));
				person.setState(rs.getString(col++));
				person.setZip(rs.getString(col++));
				person.setOccupation(rs.getString(col++));
				person.setAddressId(rs.getInt(col++));
				person.setOccupationId(rs.getInt(col++));
			}
			return person;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Updates a user defined person on the mySQL server
	 * @param conn The mySQL connection
	 * @param id The id of the person the user wants to update
	 * @param firstName The new first name of the person
	 * @param middleInitial The new middle initial of the person
	 * @param lastName The new last name of the person
	 * @param addressId The new address ID of the person
	 * @param occupationId The new occupation ID of the person
	 */
	public static void update(Connection conn, int id, String firstName, String middleInitial, String lastName, int addressId, int occupationId) {
		Person person = Person.getBy(conn, "id", Integer.toString(id));
		if(firstName.equals("")) {
			firstName=person.getFirstName();
		}
		if(middleInitial.equals("")) {
			middleInitial=person.getMiddleInitial();
		}
		if(lastName.equals("")) {
			lastName=person.getLastName();
		}
		if(addressId==0) {
			addressId=person.getAddressId();
		}
		if(occupationId==0) {
			occupationId=person.getOccupationId();
		}
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE person SET firstName=?, middleInitial=?, lastName=?, addressId=?, occupationId=? WHERE id = ?");
			ps.setString(1, firstName);
			ps.setString(2, middleInitial);
			ps.setString(3, lastName);
			ps.setInt(4, addressId);
			ps.setInt(5, occupationId);
			ps.setInt(6, id);
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts a new person onto the mySQL server
	 * @param conn The mySQL connection
	 * @param firstName The first name of the person
	 * @param middleInitial The middle initial of the person
	 * @param lastName The last name of the person
	 * @param addressId The address ID of the person
	 * @param occupationId The occupation ID of the person
	 */
	public static void insert(Connection conn, String firstName, String middleInitial, String lastName, int addressId, int occupationId) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO person (firstName, middleInitial, lastName, addressId, occupationId) values (?,?,?,?,?)");
			ps.setString(1, firstName);
			ps.setString(2, middleInitial);
			ps.setString(3, lastName);
			ps.setInt(4, addressId);
			ps.setInt(5, occupationId);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes a person from the mySQL server
	 * @param conn The mySQL connection
	 * @param id The id of the person the user wants to remove
	 */
	public static void remove(Connection conn, int id) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM person WHERE id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}