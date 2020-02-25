package addressBook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This part of the program interacts directly with the occupation table of the MySQL database and allows the user to perform full CRUD
 * @author jnstockley
 * @version 2.00
 *
 */

public class Occupation {
	private int id;

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

	public void setMonthlySalary(String monthySalary) {
		this.monthlySalary = monthySalary;
	}

	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	/**
	 * Overrides the built in toString method and returns the occupation
	 * Also formats dollar amount to make them readable to the user
	 */
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		return "ID: " + this.getId() + " Company Name: " + this.getCompanyName() + " Job Title: " + this.getJobTitle() + "\nEmployment Type: " + this.getEmploymentType() + " Monthly Salary: $" + formatter.format(Double.parseDouble(this.getMonthlySalary())) + "\nIndustry: " + this.getIndustry() + "\nDate Created: " + this.getDate() + " Time Created: " + this.getTime() + "\n";
	}

	/**
	 * Overrides the built in equals methods and checks each of the fields to make sure they are the same ignoring case
	 * @param occupation The occupation that is being compared to
	 * @return True if the two occupations are equal otherwise false
	 */
	public boolean equals(Occupation occupation) {
		if (this.getCompanyName().equalsIgnoreCase(occupation.getCompanyName()) && this.getEmploymentType().equalsIgnoreCase(occupation.getEmploymentType()) && this.getIndustry().equalsIgnoreCase(occupation.getIndustry()) && this.getJobTitle().equalsIgnoreCase(occupation.getJobTitle()) && this.getMonthlySalary().equalsIgnoreCase(occupation.getMonthlySalary())) {
			return true; 
		}else {
			return false;
		}
	}

	/**
	 * Create a blank occupation object with no fields filled in.
	 * Used when getting occupations from the database
	 */
	public Occupation() {}

	/**
	 * Creates a new occupation without an ID but has all the other required fields
	 * Used when inserting a new occupation
	 * @param companyName The company name of the occupation
	 * @param jobTitle The job title of the occupation
	 * @param employmentType The employment type of the occupations
	 * @param monthlySalary The monthly salary of the occupation
	 * @param industry The industry of the occupation
	 */
	public Occupation(String companyName, String jobTitle, String employmentType, String monthlySalary, String industry) {
		setCompanyName(companyName);
		setJobTitle(jobTitle);
		setEmploymentType(employmentType);
		setMonthlySalary(monthlySalary);
		setIndustry(industry);
	}

	/**
	 * Creates a new occupation with an ID and other required fields
	 * @param id The ID of the new occupation which resides on the MySQL server
	 * @param companyName The company name of the occupation
	 * @param jobTitle The job title of the occupation
	 * @param employmentType The employment type of the occupation
	 * @param monthlySalary The monthly salary of the occupation
	 * @param industry The industry of the occupation
	 */
	public Occupation(int id,String companyName, String jobTitle, String employmentType, String monthlySalary, String industry) {
		setId(id);
		setCompanyName(companyName);
		setJobTitle(jobTitle);
		setEmploymentType(employmentType);
		setMonthlySalary(monthlySalary);
		setIndustry(industry);
	}

	/**
	 * Connects to the MySQL server and returns all the occupations in the database
	 * @param conn The MySQL connection
	 * @return A list of occupations that are stored on the MySQL database
	 */
	public static List<Occupation> getOccupation(Connection conn) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				List<Occupation> occupations = new ArrayList<>(); //List where occupations from the database will be added to
				PreparedStatement ps = conn.prepareStatement("SELECT id, companyName, jobTitle, employmentType, monthlySalary, industry, date, time FROM occupation");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) { //Loops through all the occupations in the database and creates a new occupation and adds all required fields then adds the occupation to the occupation list
					int col = 1;
					Occupation occupation = new Occupation();
					occupation.setId(rs.getInt(col++));
					occupation.setCompanyName(rs.getString(col++));
					occupation.setJobTitle(rs.getString(col++));
					occupation.setEmploymentType(rs.getString(col++));
					occupation.setMonthlySalary(rs.getString(col++));
					occupation.setIndustry(rs.getString(col++));
					occupation.setDate(rs.getString(col++));
					occupation.setTime(rs.getString(col++));
					occupations.add(occupation);
				} 
				return occupations;
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Occupation.java", "getOccupation()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Occupation.java", "getOccupation()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and returns all the occupations that have a similarity between them based on user input
	 * @param conn The MySQL connection
	 * @param field The field that will be compared with when finding similarities
	 * @param data The data that will need to be similar in order for the person to be considered similar
	 * @return A list of occupations that are considered similar based on user input
	 */
	public static List<Occupation> getOccupation(Connection conn, String field, String data) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				List<Occupation> similarOccupations = new ArrayList<>(); //List where occupations with similarities, based on a field and data provided by the user, are added
				PreparedStatement ps = null;
				if(field.equalsIgnoreCase("date") || field.equalsIgnoreCase("time")) { //Checks if field is time or date to allow user to search for occupations created within a certain year, month etc.
					ps = conn.prepareStatement("SELECT id, companyName, jobTitle, employmentType, monthlySalary, industry, date, time FROM occupation WHERE " + field + " LIKE CONCAT(" + data + ")");
				}else {
					ps = conn.prepareStatement("SELECT id, companyName, jobTitle, employmentType, monthlySalary, industry, date, time FROM occupation WHERE " + field + "=?");
					ps.setString(1, data);
				}
				ResultSet rs = ps.executeQuery();
				while (rs.next()) { //Loops through all the similar occupations in the database and creates a new occupation and adds all the required fields then adds the occupation to the similarOccupations list
					int col = 1;
					Occupation occupation = new Occupation();
					occupation.setId(rs.getInt(col++));
					occupation.setCompanyName(rs.getString(col++));
					occupation.setJobTitle(rs.getString(col++));
					occupation.setEmploymentType(rs.getString(col++));
					occupation.setMonthlySalary(rs.getString(col++));
					occupation.setIndustry(rs.getString(col++));
					occupation.setDate(rs.getString(col++));
					occupation.setTime(rs.getString(col++));
					similarOccupations.add(occupation);
				} 
				return similarOccupations;
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Occupation.java", "getOccupation()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Occupation.java", "getOccupation()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and returns a singular address based on the id the user inputed
	 * @param conn The MySQL connection
	 * @param id The ID of the address that will be returned from the database
	 * @return A singular occupation based on the user entered ID
	 */
	public static Occupation getOccupation(Connection conn, int id) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				Occupation occupation = new Occupation();
				PreparedStatement ps = conn.prepareStatement("SELECT id, companyName, jobTitle, employmentType, monthlySalary, industry, date, time FROM occupation WHERE id=?");
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) { //Checks if the MySQL prepared statement found any matching results and if so creates a new occupation and initializes that occupation and returns the occupation
					int col = 1;
					occupation.setId(rs.getInt(col++));
					occupation.setCompanyName(rs.getString(col++));
					occupation.setJobTitle(rs.getString(col++));
					occupation.setEmploymentType(rs.getString(col++));
					occupation.setMonthlySalary(rs.getString(col++));
					occupation.setIndustry(rs.getString(col++));
					occupation.setDate(rs.getString(col++));
					occupation.setTime(rs.getString(col++));
				} 
				return occupation;
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Occupation.java", "getOccupation()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Occupation.java", "getOccupation()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and updates an occupation with a user inputed id and checks to make sure that the new occupation doesn't already exist in the database. If successful at updating the occupation the new occupation will be returned
	 * @param conn The MySQL connection
	 * @param id The ID of the occupation to update
	 * @param companyName The new company name of the address to be updated blank if keeping the old company name
	 * @param jobTitle The new job title of the address to be updated blank if keeping the old job title
	 * @param employmentType The new employment type of the address to be updated blank if keeping the old employment type
	 * @param monthlySalary The new monthly salary of the address to be updated blank if keeping the old monthly salary
	 * @param industry The new industry of the address to be updated blank if keeping the old industry
	 * @return The updated occupation if successful if not null is returned
	 */
	public static Occupation updateOccupation(Connection conn, int id, String companyName, String jobTitle, String employmentType, String monthlySalary, String industry) {
		try {
			if (conn.isValid(0)) {//Checks if the connection is valid
				Occupation occupation = getOccupation(conn, id); //Gets the current occupation that is being updated
				Occupation newOccupation = new Occupation(id, companyName, jobTitle, employmentType, monthlySalary, industry);//Creates a new occupation that is overriding the old occupation
				Date date = Date.valueOf(LocalDate.now());
				Time time = Time.valueOf(LocalTime.now());
				//Sets the new date and time for the occupation
				newOccupation.setDate(date.toString());
				newOccupation.setTime(time.toString());
				//Checks if any of the fields are blank and changes them to the old occupations data
				if (companyName.equals("")) {
					newOccupation.setCompanyName(occupation.getCompanyName()); 
				}
				if (jobTitle.equals("")) {
					newOccupation.setJobTitle(occupation.getJobTitle()); 
				}
				if (employmentType.equals("")) {
					newOccupation.setEmploymentType(occupation.getEmploymentType());
				}
				if (monthlySalary.equals("")) {
					newOccupation.setMonthlySalary(occupation.getMonthlySalary());
				}
				if (industry.equals("")) {
					newOccupation.setIndustry(occupation.getIndustry());
				}
				if (!Helper.exists(newOccupation, conn)) { //Checks if the new occupation exists on the database and if not updates the occupation on the database
					PreparedStatement ps = conn.prepareStatement("UPDATE occupation SET companyName = ?, jobTitle = ?, employmentType = ?, monthlySalary = ?, industry = ?, date = ?, time = ? WHERE id = ?");
					ps.setString(1, newOccupation.getCompanyName());
					ps.setString(2, newOccupation.getJobTitle());
					ps.setString(3, newOccupation.getEmploymentType());
					ps.setString(4, newOccupation.getMonthlySalary());
					ps.setString(5, newOccupation.getIndustry());
					ps.setDate(6, date);
					ps.setTime(7, time);
					ps.setInt(8, id);
					ps.executeUpdate();
					if(getOccupation(conn, id).equals(newOccupation)) { //Checks that the occupation got updated
						System.out.println("Updated occupation at ID: " + id);
						return newOccupation;
					}
				} else {
					System.out.println("Occupation already exists!");
					return occupation;
				}
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Occupation.java", "updateOccupation()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Occupation.java", "updateOccupation()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and create a new occupation with a user inputed id and checks to make sure that the new occupation doesn't already exist in the database. If successful at creating the occupation the new occupation will be returned
	 * @param conn The MySQL connection
	 * @param companyName The new company name of the address to be created
	 * @param jobTitle The new job title of the address to be created
	 * @param employmentType The new employment type of the address to be created
	 * @param monthlySalary The new monthly salary of the address to be created
	 * @param industry The new industry of the address to be created
	 * @return The new occupation if successful if not null is returned
	 */
	public static Occupation insertOccupation(Connection conn, String companyName, String jobTitle, String employmentType, String monthlySalary, String industry) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				Occupation occupation = new Occupation(companyName, jobTitle, employmentType, monthlySalary, industry); //Creates a new occupation that will be added to the database
				Date date = Date.valueOf(LocalDate.now());
				Time time = Time.valueOf(LocalTime.now());
				//Sets the data and time for the occupation
				occupation.setDate(date.toString());
				occupation.setTime(time.toString());
				if (!Helper.exists(occupation, conn)) {
					PreparedStatement ps = conn.prepareStatement("INSERT INTO occupation (companyName, jobTitle, employmentType, monthlySalary, industry, date, time) values(?,?,?,?,?,?,?)");
					ps.setString(1, companyName);
					ps.setString(2, jobTitle);
					ps.setString(3, employmentType);
					ps.setString(4, monthlySalary);
					ps.setString(5, industry);
					ps.setDate(6, date);
					ps.setTime(7, time);
					ps.execute();
					occupation.setId(Helper.mostRecent(conn, "occupation"));
					if (getOccupation(conn, occupation.getId()).equals(occupation)) {
						System.out.println("Added new occupation!");
						return occupation;
					} 
				} else {
					System.out.println("Occupation already exists!");
					return occupation;
				}
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid!", "Occupation.java", "insertOccupation()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "Occupation.java", "insertOccupation()");
			return null;
		} 
	}

	/**
	 * Connects to the MySQL server and removes a selected occupation with user inputed fields and data
	 * @param conn The MySQL connection
	 * @param field The field that will be used when removing the occupation
	 * @param data The data that will need to be used when finding the address to remove
	 * @return True if the occupation was removed from the databases otherwise false
	 */
	public static boolean removeOccupation(Connection conn, String field, String data) {
		try {
			if (conn.isValid(0)) { //Checks if the connection is valid
				PreparedStatement ps = conn.prepareStatement("DELETE FROM occupation WHERE " + field + " = ?");
				ps.setString(1, data);
				ps.executeUpdate();
				if (getOccupation(conn, field, data).isEmpty()) { //Checks to make sure the person was removed
					return true; 
				}else {
					System.out.println("Error removing occupaiton.");
					Helper.log("Error removing occupation.", "Occupation.java", "removeOccupation()");
					return false;
				}
			} 
			System.out.println("Connection is not valid!");
			Helper.log("Connection is not valid", "Occupation.java", "removeOccupation()");
			return false;
		} catch (Exception e) {
			Helper.log(e, "Occupation.java", "removeOccupation()");
			return false;
		} 
	}
}
