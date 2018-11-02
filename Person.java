import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Person {
	private int id;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private int addressId;
	private int ocupationId;
	private String number;
	private String name;
	private String city;
	private String state;
	private String zip;
	private String occupation;

	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}
	
	/**
	 * 
	 * @param middleInitial
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAddressId() {
		return addressId;
	}
	
	/**
	 * 
	 * @param addressId
	 */
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getOcupationId() {
		return ocupationId;
	}
	
	/**
	 * 
	 * @param ocupationId
	 */
	public void setOcupationId(int ocupationId) {
		this.ocupationId = ocupationId;
	}
	
	public String getNumber(){
		return number;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getCity(){
		return city;
	}
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getState(){
		return state;
	}
	
	public void setState(String state){
		this.state = state;
	}
	
	public String getZip(){
		return zip;
	}
	
	public void setZip(String zip){
		this.zip = zip;
	}
	
	public String getOccupation(){
		return occupation;
	}
	
	public void setOccupation(String occupation){
		this.occupation = occupation;
	}

	/**
	 * Prints the id, first name, middle initial, and last name to the console
	 */
	public void print() {
		System.out.println("Id: " + this.getId() + " " + this.getFirstName() + " " + this.getMiddleInitial() + " " + this.getLastName() + " " + this.getNumber() + " " + this.getName() + " " + this.getCity() + ", " + this.getState() + " " + this.getZip() + " " + this.getOccupation());
	}
	
	/**
	 * Prints out all the people in the person table
	 * @param conn The mySQL connection
	 * @return A list of all the people in the person table
	 */
	public static List<Person> getAll(Connection conn){
		List<Person> people = new ArrayList<Person>();
		Encryption decrypt = new Encryption();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleInitial, person.lastName, address.number, address.name, address.city, address.state, address.zip, occupation.occupation from person inner join address on person.addressId=address.id inner join occupation on person.occupationId=occupation.id");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Person person = new Person();
				int col = 1;
				person.setId(rs.getInt(col++));
				person.setFirstName(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setMiddleInitial(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setLastName(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setNumber(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setName(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setCity(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setState(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setZip(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setOccupation(decrypt.decrypt(rs.getString(col++).getBytes()));
				people.add(person);
			}
			
			return people;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Prints out a singular person to the console
	 * @param conn The mySQL connection
	 * @param value the value of where the user is located in the table
	 * @param fieldName The field the user is looking up
	 * @return A singular person
	 */
	public static Person getBy(Connection conn, String value){
		Encryption decrypt = new Encryption();
		try {
			Person person = new Person();
			PreparedStatement ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleInitial, person.lastName, address.number, address.name, address.city, address.state, address.zip, occupation.occupation from person inner join address on person.addressId=address.id inner join occupation on person.occupationId=occupation.id where person.id=?;");
			ps.setInt(1, Integer.parseInt(value));
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				int col = 1;
				person.setId(rs.getInt(col++));
				person.setFirstName(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setMiddleInitial(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setLastName(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setNumber(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setName(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setCity(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setState(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setZip(decrypt.decrypt(rs.getString(col++).getBytes()));
				person.setOccupation(decrypt.decrypt(rs.getString(col++).getBytes()));
			}
			return person;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Uses the provided names and inserts them into the person table
	 * @param conn The mySQL connection
	 * @param firstName The first name of the person
	 * @param middleInitial The middle initial of the person
	 * @param lastName The last name of the person
	 */
	public static void insert(Connection conn, String firstName, String middleInitial, String lastName, int addressId, int occupationId){
		Encryption encrypt = new Encryption();
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO person (firstName, middleInitial, lastName, addressId, occupationId) values(?,?,?,?,?)");
			ps.setString(1, encrypt.encrypt(firstName));
			ps.setString(2, encrypt.encrypt(middleInitial));
			ps.setString(3, encrypt.encrypt(lastName));
			ps.setString(4, encrypt.encrypt(addressId+""));
			ps.setString(5, encrypt.encrypt(occupationId+""));
			ps.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Uses the provided id and names of the person to override the old persons name
	 * @param conn The mySQL connection
	 * @param id The id of which person to update
	 * @param firstName The new persons first name
	 * @param middleInitial The new persons middle initial
	 * @param lastName The new persons last name
	 */
	public static void update(Connection conn, int id, String firstName, String middleInitial, String lastName){
		Encryption encrypt = new Encryption();
		try{
			PreparedStatement ps = conn.prepareStatement("UPDATE person SET firstName=?,middleInitial=?,lastName=? WHERE id = ?");
			ps.setString(1, encrypt.encrypt(firstName));
			ps.setString(2, encrypt.encrypt(middleInitial));
			ps.setString(3, encrypt.encrypt(lastName));
			ps.setInt(4, id);
			ps.executeUpdate();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Uses the provided id and removes that person from the person table
	 * @param conn The mySQL connection
	 * @param id The id of which person to remove
	 */
	public static void remove(Connection conn, int id){
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM person WHERE id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}