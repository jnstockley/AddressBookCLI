package addressBook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This part of the program helps with random tasks to make the program function
 * Part of the functions are logging and allowing for any data or time searching on the database
 * @author jnstockley
 * @version 2.00
 *
 */

public class Helper {

	/**
	 * Takes an exception from a try catch method and print the error message to a log file in the current directory of the program
	 * @param e The error message from a try catch
	 * @param object The object or class the error message came from
	 * @param method The method or function the error message came from
	 */
	public static void log(Exception e, String object, String method) {
		try {
			String fileName = dirFixer(Helper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()) + "AddressBook.log"; //Sets up the log file in the current directory of the program
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); //Sets up date and time of the error
			File log = new File(fileName);
			FileWriter fr = new FileWriter(log, true);
			fr.write(String.valueOf(object) + ": " + method + " - " + formatter.format(date) + " " + e + "\n"); //Writes the error message to the log file
			fr.close();
			System.exit(1);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(1);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} 
	}

	/**
	 * Take an exception as a string and prints the error message to a log file in the current directory of the program
	 * @param e A hard coded error message in the program
	 * @param object The object or class the error message came from
	 * @param method The method or function the error message came from
	 */
	public static void log(String e, String object, String method) {
		try {
			String fileName = dirFixer(Helper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()) + "AddressBook.log"; //Sets up the log file in the current directory of the program
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); //Sets up date and time of the error
			File log = new File(fileName);
			FileWriter fr = new FileWriter(log, true);
			fr.write(String.valueOf(object) + ": " + method + " - " + formatter.format(date) + " " + e + "\n"); //Writes the error message to the log file
			fr.close();
			System.exit(1);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(1);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} 
	}

	/**
	 * Returns the most recent address, occupation, or person based on ID in the database
	 * @param conn The MySQL connection
	 * @param table The table that the prepared statement will get the highest ID from
	 * @return An integer of he id of the most recent item in a given table
	 */
	public static int mostRecent(Connection conn, String table) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT MAX(ID) from " + table); //A statement that will find the newest item in a table based on ID
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int col = 1;
				return rs.getInt(col++);
			} 
			return -1;
		} catch (Exception e) {
			log(e, "Helper.java", "mostRecent()");
			return -1;
		} 
	}

	/**
	 * Checks if a newly created address was added to the database
	 * @param newAddress The new address that was inserted
	 * @param conn The MySQL connection
	 * @return True if the address was added to the database otherwise false
	 */
	public static boolean exists(Address newAddress, Connection conn) {
		List<Address> addresses = Address.getAddress(conn); //Gets all the addresses in the database
		for (Address address : addresses) { //Loops through all addresses
			if (address.equals(newAddress)) //Checks if an address on the database equals the newly created address
				return true; 
		} 
		return false;
	}

	/**
	 * Checks if a newly created occupation was added to the database
	 * @param newOccupation The new occupation that was inserted
	 * @param conn The MySL connection
	 * @return True if the occupation was added otherwise false
	 */
	public static boolean exists(Occupation newOccupation, Connection conn) {
		List<Occupation> occupations = Occupation.getOccupation(conn); //Gets all the occupations in the database
		for (Occupation occupation : occupations) { //Loops through all occupations
			if (occupation.equals(newOccupation)) //Checks if an occupation on the database equals the newly created occupation
				return true; 
		} 
		return false;
	}

	/**
	 * Checks if a newly created person was added to the database
	 * @param newPerson The new person that was inserted
	 * @param conn The MySQL connection
	 * @return True if the person was added otherwise false
	 */
	public static boolean exists(Person newPerson, Connection conn) {
		List<Person> people = Person.getPerson(conn); //Gets all the people in the database
		for (Person person : people) { //Loops through all people
			if (person.equals(newPerson)) //Checks if a person on the database equals the newly created person
				return true; 
		} 
		return false;
	}

	/**
	 * Takes in a string with no spaces and adds a space whenever the word has a capital letter
	 * @param word String with capital letter and no spaces
	 * @return String with capital letter and spaces
	 */
	public static String split(String word) {
		String splitWord = word.substring(0, 1);
		for (int i = 1; i < word.length(); i++) { //Loops through the whole word
			Character ch = Character.valueOf(word.charAt(i));
			if (Character.isUpperCase(ch.charValue())) { //Checks if a given character of a word if capitalized or not
				splitWord = String.valueOf(splitWord) + " " + ch;
			} else {
				splitWord = String.valueOf(splitWord) + ch;
			} 
		} 
		return splitWord;
	}

	/**
	 * Makes sure the directory when saving files is valid and doesn't include the bin folder or AddressBook.jar
	 * @param dir The current directory of the jar file
	 * @return The fixed directory not including AddressBook.jar or the bin folder
	 */
	public static String dirFixer(String dir) {
		if(dir.indexOf("AddressBook.jar") != -1) {
			return dir.substring(0, dir.length()-19);
		}else if(dir.indexOf("bin") != -1) {
			return dir.substring(0, dir.length()-4);
		}else {
			return dir;
		}
	}

	/**
	 * Uses regular expression to allow a user to find address, occupations, and people based on if they were created within a certain year, month, or day
	 * @param reader How I am reading data from the console
	 * @return A string that can be read by MySQL to find data from a certain year, month, or day
	 */
	public static String dateFinder(BufferedReader reader) {
		try{
			String optionalDate = "";
			System.out.println("You can use a start (*) to search for all with that year, month, or day");
			System.out.print("Enter four digit year yyyy: ");
			String year = reader.readLine();
			System.out.print("Enter two digit month mm: ");
			String month = reader.readLine();
			System.out.print("Enter two digit day dd: ");
			String day = reader.readLine();
			//Creates the regular expressions and patterns for year, month, and day
			String optionalYearPattern = "\\*{1,4}";
			Pattern optionalYearR = Pattern.compile(optionalYearPattern);
			Matcher optionalYearM = optionalYearR.matcher(year);
			String optionalMonthPattern = "\\*{1,2}";
			Pattern optionalMonthR = Pattern.compile(optionalMonthPattern);
			Matcher optionalMonthM = optionalMonthR.matcher(month);
			String optionalDayPattern = "\\*{1,2}";
			Pattern optionalDayR = Pattern.compile(optionalDayPattern);
			Matcher optionalDayM = optionalDayR.matcher(day);
			//Check if year, month, or day are empty and replaces it with sql code to find any date
			if(optionalYearM.matches()) {
				optionalDate+="'%-',";
			}else {
				optionalDate+="'"+year+"-',";
			}
			if(optionalMonthM.matches()) {
				optionalDate+="'%-',";
			}else {
				optionalDate+="'"+month+"-',";
			}if(optionalDayM.matches()) {
				optionalDate+="'%'";
			}else {
				optionalDate+="'"+day+"'";
			}
			return optionalDate;
		}catch(Exception e) {
			log(e, "Helper.java", "dateFinder()");
			return null;
		}
	}

	/**
	 * Uses regular expression to allow a user to find address, occupations, and people based on if they were created within a certain hour, minute, or second
	 * @param reader How I am reading data from the console
	 * @return A string that can be read by MySQL to find data from a certain hour, minute, or second
	 */
	public static String timeFinder(BufferedReader reader) {
		try {
			String optionalTime = "";
			System.out.println("You can use a start (*) to search for all with the hour, minute, or second");
			System.out.print("Enter two digit hour (24hr) hh: ");
			String hour = reader.readLine();
			System.out.print("Enter two digit minute mm: ");
			String minute = reader.readLine();
			System.out.print("Enter two digit second ss: ");
			String second = reader.readLine();
			//Creates the regular expressions and patterns for hour, minute, and second
			String pattern = "\\*{1,2}";
			Pattern patternR = Pattern.compile(pattern);
			Matcher optionalHour = patternR.matcher(hour);
			Matcher optionalMinute = patternR.matcher(minute);
			Matcher optionalSecond = patternR.matcher(second);
			//Check if hour, minute, or second are empty and replaces it with sql code to find any time
			if(optionalHour.matches()) {
				optionalTime+="'%:',";
			}else {
				optionalTime+="'"+hour+":',";
			}
			if(optionalMinute.matches()) {
				optionalTime+="'%:',";
			}else {
				optionalTime+="'"+minute+":',";
			}
			if(optionalSecond.matches()) {
				optionalTime+="'%'";
			}else {
				optionalTime+="'"+second+"'";
			}
			return optionalTime;
		}catch(Exception e) {
			log(e, "Helper.java", "timeFinder()");
			return null;
		}
	}
}
