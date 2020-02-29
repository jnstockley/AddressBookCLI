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
import org.apache.commons.lang3.text.WordUtils;

/**
 * This part of the program interacts directly with the person table of the MySQL database and allows the user to perform full CRUD
 * @author jnstockley
 * @version 2.00
 *
 */

@SuppressWarnings("deprecation")
public class Person {
	private int id;

	private String firstName;

	private String middleName;

	private String lastName;

	private String homePhone;

	private String mobilePhone;

	private String workPhone;

	private String homeEmail;

	private String workEmail;

	private double height;

	private double weight;

	private String gender;

	private String race;

	private int addressId;

	private int number;

	private String street;

	private String city;

	private String state;

	private String zip;

	private int occupationId;

	private String companyName;

	private String jobTitle;

	private String employmentType;

	private String monthlySalary;

	private String industry;

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

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getWorkPhone() {
		return this.workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getHomeEmail() {
		return this.homeEmail;
	}

	public void setHomeEmail(String homeEmail) {
		this.homeEmail = homeEmail;
	}

	public String getWorkEmail() {
		return this.workEmail;
	}

	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}

	public double getHeight() {
		return this.height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRace() {
		return this.race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public int getAddressId() {
		return this.addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public int getOccupationId() {
		return this.occupationId;
	}

	public void setOccupationId(int occupationId) {
		this.occupationId = occupationId;
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

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getEmploymentType() {
		return this.employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getMonthlySalary() {
		return this.monthlySalary;
	}

	public void setMonthlySalary(String monthlySalary) {
		this.monthlySalary = monthlySalary;
	}

	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	//TODO Improve toString()
	/**
	 * Overrides the built in toString method and returns the person and the address and occupation that are tied to the person
	 * Also formats phone numbers and dollar amounts to make them readable to the user
	 */
	public String toString() {
		String firstNameF = WordUtils.capitalize(this.getFirstName());
		String middleNameF = WordUtils.capitalize(this.getMiddleName());
		String lastNameF = WordUtils.capitalize(this.getLastName());
		String homePhoneF = String.valueOf(this.getHomePhone().substring(0, 3)) + '-' + this.getHomePhone().substring(3, 6) + '-' + this.getHomePhone().substring(6);
		String mobilePhoneF = String.valueOf(this.getMobilePhone().substring(0, 3)) + '-' + this.getMobilePhone().substring(3, 6) + '-' + this.getMobilePhone().substring(6);
		String workPhoneF = String.valueOf(this.getWorkPhone().substring(0, 3)) + '-' + this.getWorkPhone().substring(3, 6) + '-' + this.getWorkPhone().substring(6);
		String genderF = WordUtils.capitalize(this.getGender());
		String raceF = WordUtils.capitalize(this.getRace());
		Address address = new Address(this.getAddressId(), this.getNumber(), this.getStreet(), this.getCity(), this.getState(), this.getZip());
		String addressS = address.toString().substring(0, address.toString().indexOf("Date Created"));
		Occupation occupation = new Occupation(this.getOccupationId(), this.getCompanyName(), this.getJobTitle(), this.getEmploymentType(), this.getMonthlySalary(), this.getIndustry());
		String occupationS = occupation.toString().substring(0, occupation.toString().indexOf("Date Created"));
		return "ID: " + this.getId() + " " + firstNameF + " " + middleNameF + " " + lastNameF + "\nHome Phone Number: " + homePhoneF + " Mobile Phone Number: " + mobilePhoneF + " Work Phone Number: " + workPhoneF + "\nHome Email: " + getHomeEmail() + " Work Email: " + getWorkEmail() + "\nHeight: " + getHeight() + " cm Weight: " + getWeight() + " lb Gender: " + genderF + " Race: " + raceF + "\nDate Created: " + getDate() + " Time Created: " + getTime() + "\n\n" + addressS + "\n"+ occupationS;
	}
	
	/**
	 * Override the built in equals methods and checks each of the fields to make sure they are the same ignoring case
	 * @param person The person that is being compared to 
	 * @return True if the two people are equal otherwise false
	 */
	public boolean equals(Person person) {
		if (this.getId() == person.getId() && this.getFirstName().equalsIgnoreCase(person.getFirstName()) && this.getMiddleName().equalsIgnoreCase(person.getMiddleName()) && this.getLastName().equalsIgnoreCase(person.getLastName()) && this.getHomePhone().equalsIgnoreCase(person.getHomePhone()) && this.getMobilePhone().equalsIgnoreCase(person.getMobilePhone()) && this.getWorkPhone().equalsIgnoreCase(person.getWorkPhone()) && this.getHomeEmail().equalsIgnoreCase(person.getHomeEmail()) && this.getWorkEmail().equalsIgnoreCase(person.getWorkEmail()) && this.getHeight() == person.getHeight() && this.getWeight() == person.getWeight() && this.getGender().equalsIgnoreCase(person.getGender()) && this.getRace().equalsIgnoreCase(person.getRace()) && this.getAddressId() == person.getAddressId() && this.getOccupationId() == person.getOccupationId())
			return true; 
		return false;
	}

	/**
	 * Creates a blank person object with no fields filled in
	 * Used when getting people from the database
	 */
	public Person() {}

	//TODO Possibility add conn in order to get data for address and occupation
	/**
	 * Creates a new person without an ID but has all the other required fields
	 * Used when inserting a new person
	 * @param firstName The first name of the person
	 * @param middleName The middle name of the person
	 * @param lastName The last name of the person
	 * @param homePhone The home phone of the person
	 * @param mobilePhone The mobile phone of the person
	 * @param workPhone The work phone of the person
	 * @param homeEmail The home or personal email of the person
	 * @param workEmail The work email of the person
	 * @param height The height in centimeters of the person
	 * @param weight The weigh in pounds of the person
	 * @param gender The gender of the person
	 * @param race The race of the person
	 * @param addressId The address ID of the person used when retrieving the address of the person
	 * @param occupationId The occupation ID of the person used when retrieving the occupation of the person
	 */
	public Person(String firstName, String middleName, String lastName, String homePhone, String mobilePhone, String workPhone, String homeEmail, String workEmail, double height, double weight, String gender, String race, int addressId, int occupationId) {
		setFirstName(firstName);
		setMiddleName(middleName);
		setLastName(lastName);
		setHomePhone(homePhone);
		setMobilePhone(mobilePhone);
		setWorkPhone(workPhone);
		setHomeEmail(homeEmail);
		setWorkEmail(workEmail);
		setHeight(height);
		setWeight(weight);
		setGender(gender);
		setRace(race);
		setAddressId(addressId);
		setOccupationId(occupationId);
	}

	//TODO Possibility add conn in order to get data for address and occupation
	/**
	 * Creates a new person with an ID but has all the other required fields
	 * @param id The ID of the new person which resides on the MySQL server
	 * @param firstName The first name of the person
	 * @param middleName The middle name of the person
	 * @param lastName The last name of the person
	 * @param homePhone The home phone of the person
	 * @param mobilePhone The mobile phone of the person
	 * @param workPhone The work phone of the person
	 * @param homeEmail The home or personal email of the person
	 * @param workEmail The work email of the person
	 * @param height The height in centimeters of the person
	 * @param weight The weigh in pounds of the person
	 * @param gender The gender of the person
	 * @param race The race of the person
	 * @param addressId The address ID of the person used when retrieving the address of the person
	 * @param occupationId The occupation ID of the person used when retrieving the occupation of the person
	 */
	public Person(int id, String firstName, String middleName, String lastName, String homePhone, String mobilePhone, String workPhone, String homeEmail, String workEmail, double height, double weight, String gender, String race, int addressId, int occupationId) {
		setId(id);
		setFirstName(firstName);
		setMiddleName(middleName);
		setLastName(lastName);
		setHomePhone(homePhone);
		setMobilePhone(mobilePhone);
		setWorkPhone(workPhone);
		setHomeEmail(homeEmail);
		setWorkEmail(workEmail);
		setHeight(height);
		setWeight(weight);
		setGender(gender);
		setRace(race);
		setAddressId(addressId);
		setOccupationId(occupationId);
	}

	/**
	 * Connects to the MySQL server and returns all the people in the database
	 * @param conn The MySQL connection
	 * @return A list of people that are stored on the MySQL database
	 */
	public static List<Person> getPerson(Connection conn) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				List<Person> people = new ArrayList<>(); //List where people from the database will be added to
				PreparedStatement ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleName, person.lastName, person.homePhone, person.mobilePhone, person.workPhone, person.homeEmail, person.workEmail, person.height, person.weight, person.gender, person.race, person.date, person.time, address.number, address.street, address.city, address.state, address.zip, occupation.companyName, occupation.jobTitle, occupation.employmentType, occupation.monthlySalary, occupation.industry, address.Id, occupation.Id FROM person inner join address on person.addressId=address.Id inner join occupation on person.occupationId=occupation.Id");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) { //Loops through all the people in the database and creates a new person and adds all the required fields then adds the person to the people list
					int col = 1;
					Person person = new Person();
					person.setId(rs.getInt(col++));
					person.setFirstName(rs.getString(col++));
					person.setMiddleName(rs.getString(col++));
					person.setLastName(rs.getString(col++));
					person.setHomePhone(rs.getString(col++));
					person.setMobilePhone(rs.getString(col++));
					person.setWorkPhone(rs.getString(col++));
					person.setHomeEmail(rs.getString(col++));
					person.setWorkEmail(rs.getString(col++));
					person.setHeight(rs.getDouble(col++));
					person.setWeight(rs.getDouble(col++));
					person.setGender(rs.getString(col++));
					person.setRace(rs.getString(col++));
					person.setDate(rs.getString(col++));
					person.setTime(rs.getString(col++));
					person.setNumber(rs.getInt(col++));
					person.setStreet(rs.getString(col++));
					person.setCity(rs.getString(col++));
					person.setState(rs.getString(col++));
					person.setZip(rs.getString(col++));
					person.setCompanyName(rs.getString(col++));
					person.setJobTitle(rs.getString(col++));
					person.setEmploymentType(rs.getString(col++));
					person.setMonthlySalary(rs.getString(col++));
					person.setIndustry(rs.getString(col++));
					person.setAddressId(rs.getInt(col++));
					person.setOccupationId(rs.getInt(col++));
					people.add(person);
				} 
				return people;
			}
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Person.java", "getPerson()");
			return null;

		} catch (Exception e) {
			System.out.println("Error getting all people. Please check the log!");
			Helper.log(e, "Person.java", "getPerson()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and returns all the people that have a similarity between them based on user input
	 * @param conn The MySQL connection
	 * @param field The field that will be compared with when finding similarities
	 * @param data The data that will need to be similar in order for the person to be considered similar
	 * @return A list of people that are considered similar based on user input
	 */
	public static List<Person> getPerson(Connection conn, String field, String data) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				List<Person> similarPeople = new ArrayList<>(); //List where people with similarities, based on a field and data provided by the user, are added
				PreparedStatement ps = null;
				if(field.equalsIgnoreCase("date") || field.equalsIgnoreCase("time")) { //Checks if field is time or date to allow user to search for people created within a certain year, month etc.
					ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleName, person.lastName, person.homePhone, person.mobilePhone, person.workPhone, person.homeEmail, person.workEmail, person.height, person.weight, person.gender, person.race, person.date, person.time, address.number, address.street, address.city, address.state, address.zip, occupation.companyName, occupation.jobTitle, occupation.employmentType, occupation.monthlySalary, occupation.industry, address.Id, occupation.Id FROM person inner join address on person.addressId=address.Id inner join occupation on person.occupationId=occupation.Id WHERE person." + field + "LIKE CONCAT(" + data + ")");
				}else {
					ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleName, person.lastName, person.homePhone, person.mobilePhone, person.workPhone, person.homeEmail, person.workEmail, person.height, person.weight, person.gender, person.race, person.date, person.time, address.number, address.street, address.city, address.state, address.zip, occupation.companyName, occupation.jobTitle, occupation.employmentType, occupation.monthlySalary, occupation.industry, address.Id, occupation.Id FROM person inner join address on person.addressId=address.Id inner join occupation on person.occupationId=occupation.Id WHERE person." + field + " = ?");
					ps.setString(1, data);
				}
				ResultSet rs = ps.executeQuery();
				while (rs.next()) { //Loops through all the similar people in the database and creates a new person and adds all the required fields then adds the person to the similarPeople list
					int col = 1;
					Person person = new Person();
					person.setId(rs.getInt(col++));
					person.setFirstName(rs.getString(col++));
					person.setMiddleName(rs.getString(col++));
					person.setLastName(rs.getString(col++));
					person.setHomePhone(rs.getString(col++));
					person.setMobilePhone(rs.getString(col++));
					person.setWorkPhone(rs.getString(col++));
					person.setHomeEmail(rs.getString(col++));
					person.setWorkEmail(rs.getString(col++));
					person.setHeight(rs.getDouble(col++));
					person.setWeight(rs.getDouble(col++));
					person.setGender(rs.getString(col++));
					person.setRace(rs.getString(col++));
					person.setDate(rs.getString(col++));
					person.setTime(rs.getString(col++));
					person.setNumber(rs.getInt(col++));
					person.setStreet(rs.getString(col++));
					person.setCity(rs.getString(col++));
					person.setState(rs.getString(col++));
					person.setZip(rs.getString(col++));
					person.setCompanyName(rs.getString(col++));
					person.setJobTitle(rs.getString(col++));
					person.setEmploymentType(rs.getString(col++));
					person.setMonthlySalary(rs.getString(col++));
					person.setIndustry(rs.getString(col++));
					person.setAddressId(rs.getInt(col++));
					person.setOccupationId(rs.getInt(col++));
					similarPeople.add(person);
				} 
				return similarPeople;
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Person.java", "getPerson()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Person.java", "getPerson()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and returns a singular person based on the id the user inputed
	 * @param conn The MySQL connection
	 * @param id The ID of the person that will be returned from the database
	 * @return A singular person based on the user entered ID
	 */
	public static Person getPerson(Connection conn, int id) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				PreparedStatement ps = conn.prepareStatement("SELECT person.id, person.firstName, person.middleName, person.lastName, person.homePhone, person.mobilePhone, person.workPhone, person.homeEmail, person.workEmail, person.height, person.weight, person.gender, person.race, person.date, person.time, address.number, address.street, address.city, address.state, address.zip, occupation.companyName, occupation.jobTitle, occupation.employmentType, occupation.monthlySalary, occupation.industry, address.Id, occupation.Id FROM person inner join address on person.addressId=address.Id inner join occupation on person.occupationId=occupation.Id WHERE person.id = ?");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) { //Checks if the MySQL prepared statement found any matching results and if so creates a new person and initializes that person and returns the person
					int col = 1;
					Person person = new Person();
					person.setId(rs.getInt(col++));
					person.setFirstName(rs.getString(col++));
					person.setMiddleName(rs.getString(col++));
					person.setLastName(rs.getString(col++));
					person.setHomePhone(rs.getString(col++));
					person.setMobilePhone(rs.getString(col++));
					person.setWorkPhone(rs.getString(col++));
					person.setHomeEmail(rs.getString(col++));
					person.setWorkEmail(rs.getString(col++));
					person.setHeight(rs.getDouble(col++));
					person.setWeight(rs.getDouble(col++));
					person.setGender(rs.getString(col++));
					person.setRace(rs.getString(col++));
					person.setDate(rs.getString(col++));
					person.setTime(rs.getString(col++));
					person.setNumber(rs.getInt(col++));
					person.setStreet(rs.getString(col++));
					person.setCity(rs.getString(col++));
					person.setState(rs.getString(col++));
					person.setZip(rs.getString(col++));
					person.setCompanyName(rs.getString(col++));
					person.setJobTitle(rs.getString(col++));
					person.setEmploymentType(rs.getString(col++));
					person.setMonthlySalary(rs.getString(col++));
					person.setIndustry(rs.getString(col++));
					person.setAddressId(rs.getInt(col++));
					person.setOccupationId(rs.getInt(col++));
					return person;
				} 
				System.out.println("Error getting person!");
				Helper.log("Error getting person!", "Person.java", "getPerson()");
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Person.java", "getPerson()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Person.java", "getPerson()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and updates a person with a user inputed id and checks to make sure that the new person doesn't already exist in the database. If successful at updating the person the new person will be returned
	 * @param conn The MySQL connection
	 * @param id The ID of the person to update
	 * @param firstName The new first name of the person to be updated blank if keeping the old first name
	 * @param middleName The new middle name of the person to be updated blank if keeping the old middle name
	 * @param lastName The new last name of the person to be updated blank if keeping the old last name
	 * @param homePhone The new home phone of the person to be updated blank if keeping the old home phone
	 * @param mobilePhone The new mobile phone name of the person to be updated blank if keeping the old mobile phone
	 * @param workPhone The new work phone of the person to be updated blank if keeping the old work phone
	 * @param homeEmail The new home email of the person to be updated blank if keeping the old home email
	 * @param workEmail The new work email of the person to be updated blank if keeping the old work email
	 * @param height The new height of the person to be updated blank if keeping the old height
	 * @param weight The new weight of the person to be updated blank if keeping the old weight
	 * @param gender The new gender of the person to be updated blank if keeping the old gender
	 * @param race The new race of the person to be updated blank if keeping the old race
	 * @param addressId The new address ID of the person to be updated blank if keeping the old address ID
	 * @param occupationId The new occupation ID of the person to be updated blank if keeping the old occupation ID
	 * @return The updated person if successful if not null is returned
	 */
	public static Person updatePerson(Connection conn, int id, String firstName, String middleName, String lastName, String homePhone, String mobilePhone, String workPhone, String homeEmail, String workEmail, double height, double weight, String gender, String race, int addressId, int occupationId) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				Person person = getPerson(conn, id); //Gets the current person that is being updated
				Person newPerson = new Person(firstName, middleName, lastName, homePhone, mobilePhone, workPhone, homeEmail, workEmail, height, weight, gender, race, addressId, occupationId); //Creates a new person that is overriding the old person
				Address address = Address.getAddress(conn, addressId); //Creates new address object with address ID
				Occupation occupation = Occupation.getOccupation(conn, occupationId); //Creates new occupation object with occupation ID
				Date date = Date.valueOf(LocalDate.now());
				Time time = Time.valueOf(LocalTime.now());
				//Sets ID, address fields and occupations fields to the new person
				newPerson.setId(person.getId());
				newPerson.setNumber(address.getNumber());
				newPerson.setStreet(address.getStreet());
				newPerson.setCity(address.getCity());
				newPerson.setState(address.getState());
				newPerson.setZip(address.getZip());
				newPerson.setCompanyName(occupation.getCompanyName());
				newPerson.setJobTitle(occupation.getJobTitle());
				newPerson.setEmploymentType(occupation.getEmploymentType());
				newPerson.setMonthlySalary(occupation.getMonthlySalary());
				newPerson.setIndustry(occupation.getIndustry());
				newPerson.setDate(date.toString());
				newPerson.setTime(time.toString());
				//Checks if any fields are blank and changes them to the old persons data
				if(firstName.equals("")) {
					newPerson.setFirstName(person.getFirstName());
				}
				if(middleName.equals("")) {
					newPerson.setMiddleName(person.getMiddleName());
				}
				if(lastName.equals("")) {
					newPerson.setLastName(person.getLastName());
				}
				if(homePhone.equals("")) {
					newPerson.setHomePhone(person.getHomePhone());
				}
				if(mobilePhone.equals("")) {
					newPerson.setMobilePhone(person.getMobilePhone());
				}
				if(workPhone.equals("")) {
					newPerson.setWorkPhone(person.getWorkPhone());
				}
				if(homeEmail.equals("")) {
					newPerson.setHomeEmail(person.getHomeEmail());
				}
				if(workEmail.equals("")) {
					newPerson.setWorkEmail(person.getWorkEmail());
				}
				if(height==0.0) {
					newPerson.setHeight(person.getHeight());
				}
				if(weight==0.0) {
					newPerson.setWeight(person.getWeight());
				}
				if(gender.equals("")) {
					newPerson.setGender(person.getGender());
				}
				if(race.equals("")) {
					newPerson.setRace(person.getRace());
				}
				if(addressId==0) {
					newPerson.setAddressId(person.getAddressId());
				}
				if(occupationId==0) {
					newPerson.setOccupationId(person.getOccupationId());
				}
				if (!Helper.exists(newPerson, conn)) { //Checks if the new person exists on the database and if not updates the person on the database
					PreparedStatement ps = conn.prepareStatement("UPDATE person SET firstName=?, middleName=?, lastName=?, homePhone=?, mobilePhone=?, workPhone=?, homeEmail=?, workEmail=?, height=?, weight=?, gender=?, race=?, addressId=?, occupationId=?, date=?, time=? WHERE id =?");
					ps.setString(1, newPerson.getFirstName());
					ps.setString(2, newPerson.getMiddleName());
					ps.setString(3, newPerson.getLastName());
					ps.setString(4, newPerson.getHomePhone());
					ps.setString(5, newPerson.getMobilePhone());
					ps.setString(6, newPerson.getWorkPhone());
					ps.setString(7, newPerson.getHomeEmail());
					ps.setString(8, newPerson.getWorkEmail());
					ps.setDouble(9, newPerson.getHeight());
					ps.setDouble(10, newPerson.getWeight());
					ps.setString(11, newPerson.getGender());
					ps.setString(12, newPerson.getRace());
					ps.setInt(13, newPerson.getAddressId());
					ps.setInt(14, newPerson.getOccupationId());
					ps.setDate(15, date);
					ps.setTime(16, time);
					ps.setInt(17, id);
					ps.executeUpdate();
					if(getPerson(conn, id).equals(newPerson)) { //Checks that the person got updated
						System.out.println("Updated person at ID: " + newPerson.getId());
						return newPerson;
					}
				} else {
					System.out.println("Person already Exists!");
					return person;
				} 
			}
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Person.java", "updatePerson()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Person.java", "updatePerson()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and creates a new person with a user inputed id and checks to make sure that the new person doesn't already exist in the database. If successful at creating the person the new person will be returned
	 * @param conn The MySQL connection
	 * @param firstName The new first name of the person to be created
	 * @param middleName The new middle name of the person to be created
	 * @param lastName The new last name of the person to be created
	 * @param homePhone The new home phone of the person to be created
	 * @param mobilePhone The new mobile phone name of the person to be created 
	 * @param workPhone The new work phone of the person to be created
	 * @param homeEmail The new home email of the person to be created
	 * @param workEmail The new work email of the person to be created
	 * @param height The new height of the person to be created
	 * @param weight The new weight of the person to be created
	 * @param gender The new gender of the person to be created
	 * @param race The new race of the person to be created
	 * @param addressId The new address ID of the person to be created
	 * @param occupationId The new occupation ID of the person to be  created
	 * @return The new person if successful if not null is returned
	 */
	public static Person insertPerson(Connection conn, String firstName, String middleName, String lastName, String homePhone, String mobilePhone, String workPhone, String homeEmail, String workEmail, double height, double weight, String gender, String race, int addressId, int occupationId) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				Person person = new Person(firstName, middleName, lastName, homePhone, mobilePhone, workPhone, homeEmail, workEmail, height, weight, gender, race, addressId, occupationId); //Creates a new person that will be added to the database
				Address address = Address.getAddress(conn, addressId); //Creates new address object with address ID
				Occupation occupation = Occupation.getOccupation(conn, occupationId); //Creates new occupation object with occupation ID
				Date date = Date.valueOf(LocalDate.now());
				Time time = Time.valueOf(LocalTime.now());
				//Sets ID, address fields and occupations fields to the new person
				person.setDate(date.toString());
				person.setTime(time.toString());
				person.setCompanyName(occupation.getCompanyName());
				person.setJobTitle(occupation.getJobTitle());
				person.setEmploymentType(occupation.getEmploymentType());
				person.setMonthlySalary(occupation.getMonthlySalary());
				person.setIndustry(occupation.getIndustry());
				person.setNumber(address.getNumber());
				person.setStreet(address.getStreet());
				person.setCity(address.getCity());
				person.setState(address.getState());
				person.setZip(address.getZip());
				if (!Helper.exists(person, conn)) { //Checks if the new person exists on the database and if not adds the person to the database
					PreparedStatement ps = conn.prepareStatement("INSERT INTO person (firstName, middleName, lastName, homePhone, mobilePhone, workPhone, homeEmail, workEmail, height, weight, gender, race, addressId, occupationId, date, time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setString(1, firstName);
					ps.setString(2, middleName);
					ps.setString(3, lastName);
					ps.setString(4, homePhone);
					ps.setString(5, mobilePhone);
					ps.setString(6, workPhone);
					ps.setString(7, homeEmail);
					ps.setString(8, workEmail);
					ps.setDouble(9, height);
					ps.setDouble(10, weight);
					ps.setString(11, gender);
					ps.setString(12, race);
					ps.setInt(13, addressId);
					ps.setInt(14, occupationId);
					ps.setDate(15, date);
					ps.setTime(16, time);
					ps.execute();
					person.setId(Helper.mostRecent(conn, "person"));
					if(getPerson(conn, person.getId()).equals(person)) { //Checks that the new person got added
						System.out.println("Added new person!");
						return person;
					}

				} else {
					System.out.println("Person already exists!");
					return person;
				} 
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Person.java", "insertPerson()");
			return null;
		} catch (Exception e) {
			System.out.println("Error adding person. Please check the log!");
			Helper.log(e, "Person.java", "insertPerson()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and removes a selected person with user inputed fields and data
	 * @param conn The MySQL connection
	 * @param field The field that will be used when removing the person
	 * @param data The data that will need to be used when finding the person to remove
	 * @return True if the person was removed from the databases otherwise false
	 */
	public static boolean removePerson(Connection conn, String field, String data) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				PreparedStatement ps = conn.prepareStatement("DELETE FROM person WHERE " + field + " = ?");
				ps.setString(1, data);
				ps.executeUpdate();
				if (getPerson(conn, field, data).isEmpty()) { //Checks to make sure the person was removed
					return true; 
				}else {
					System.out.println("Error removing person.");
					Helper.log("Error removing person.", "Person.java", "removePerson()");
					return false;
				}
			}
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Person.java", "removePerson()");
			return false;
		} catch (Exception e) {
			Helper.log(e, "Person.java", "removePerson()");
			return false;
		} 
	}
}
