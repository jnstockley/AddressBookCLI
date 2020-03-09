package addressBook;

import java.sql.*;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This part of the program help with getting and saving connection to a CSV file and also helps with duplicate checking and password salting and hashing
 * @author jnstockley
 * @version 2.00
 *
 */

public class ConnectionHelper {

	/**
	 * Creates a salt when saving passwords
	 * @return A salt used for saving passwords
	 */
	private static String generateSalt() {
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] salt = new byte[16];
			sr.nextBytes(salt);
			return Base64.getEncoder().encodeToString(salt);
		} catch (Exception e) {
			Helper.log(e, "Helper.java", "generateSalt()");
			return null;
		} 
	}

	/**
	 * Takes a string and a salt and created an encrypted salted password
	 * @param password A non-encrypted string that will be used as a password
	 * @param salt A string that will be used as the salt
	 * @return
	 */
	private static String encryptPassword(String password, String salt) {
		try {
			password = String.valueOf(salt) + password;
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder passSb = new StringBuilder();
			byte b;
			int i;
			byte[] arrayOfByte1;
			for (i = (arrayOfByte1 = hashedPassword).length, b = 0; b < i; ) {
				byte b1 = arrayOfByte1[b];
				passSb.append(String.format("%02x", new Object[] { Byte.valueOf(b1) }));
				b++;
			} 
			return passSb.toString();
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "encryptPassword()");
			return null;
		} 
	}

	/**
	 * Checks that is a user entered server IP or domain is a valid IP or domain using regular expressions
	 * @param IP A string containing an IPV4 address or a domain name
	 * @return True if IP is a valid IPV4 address or domain name otherwise false
	 */
	private static boolean regularExpressionChecker(String IP) {
		String ipv4Pattern = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
		String domainNamePattern = "^(?!:\\/\\/)([a-zA-Z0-9-_]+\\.)*[a-zA-Z0-9][a-zA-Z0-9-_]+\\.[a-zA-Z]{2,11}?$";
		Pattern ipv4R = Pattern.compile(ipv4Pattern);
		Matcher ipv4M = ipv4R.matcher(IP);
		Pattern domainNameR = Pattern.compile(domainNamePattern);
		Matcher domainNameM = domainNameR.matcher(IP);
		if (ipv4M.matches())
			return true; 
		if (domainNameM.matches())
			return true; 
		return false;
	}

	/**
	 * Asks the user to input an IP address or domain name, database name, user name and password salts the passwords, checks the IP or domain name are valid and saves them to a CSV file in the working directory of the program
	 * @param reader How I retrieve all the data except the password
	 * @param passwordReader How i retrieve the password without showing the characters in the CLI
	 * @param fileName The file name of the connections CSV
	 * @return A list of server address, username, database name, user name, hashed password and regular password
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> saveConnection(BufferedReader reader, Console passwordReader, String fileName) {
		try {
			String newFileName = Helper.dirFixer(ConnectionHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()) + fileName;
			List<String> connection = new ArrayList<>();
			System.out.print("Please enter Server IP or Domain Name: ");
			String IP = reader.readLine();
			if (regularExpressionChecker(IP)) { //Checks if server address is in valid formatting
				String password, passConfirm;
				System.out.print("Please enter Database Name: ");
				String database = reader.readLine();
				System.out.print("Please enter User Name: ");
				String userName = reader.readLine();
				try { //Tries to use no show password in CLI
					char[] passwordArray = passwordReader.readPassword("Please enter password: ", new Object[0]);
					char[] passConfirmArray = passwordReader.readPassword("Please confirm your password: ", new Object[0]);
					password = new String(passwordArray);
					passConfirm = new String(passConfirmArray);
				} catch (Exception e) {
					System.out.print("Please enter password: ");
					password = reader.readLine();
					System.out.print("Please confirm your password: ");
					passConfirm = reader.readLine();
				}
				File file = new File(fileName);
				if(file.exists() && (password.equals(passConfirm) && !duplicateConnection(fileName, IP, database, userName))) { //Checks if the saved connections file exists
					System.out.println("Password are the same!");
					List<List> connections = getAllConnections(fileName);
					connection.add(IP);
					connection.add(database);
					connection.add(userName);
					String salt = generateSalt();
					String hashedPassword = encryptPassword(password, salt);
					connection.add(hashedPassword);
					connection.add(salt);
					connection.add(password);
					connections.add(connection);
					StringBuilder csv = new StringBuilder();
					for(List<String> oldConnection: connections) { //Builds the list of data to return 
						csv.append(oldConnection.get(0));
						csv.append(',');
						csv.append(oldConnection.get(1));
						csv.append(',');
						csv.append(oldConnection.get(2));
						csv.append(',');
						csv.append(oldConnection.get(3));
						csv.append(',');
						csv.append(oldConnection.get(4));
						csv.append("\r\n");
					}
					BufferedWriter csvFile = new BufferedWriter(new FileWriter(newFileName));
					csvFile.append(csv.toString());
					csvFile.close();
					return connection;
				}else if (password.equals(passConfirm)){
					System.out.println("Passowrds are the same!");
					connection.add(IP);
					connection.add(database);
					connection.add(userName);
					String salt = generateSalt();
					String hashedPassword = encryptPassword(password, salt);
					connection.add(hashedPassword);
					connection.add(password);
					StringBuilder csv = new StringBuilder(); //Builds the CSV file and writes it to disk
					csv.append(IP);
					csv.append(',');
					csv.append(database);
					csv.append(',');
					csv.append(userName);
					csv.append(',');
					csv.append(hashedPassword);
					csv.append(',');
					csv.append(salt);
					csv.append("\r\n");
					BufferedWriter csvFile = new BufferedWriter(new FileWriter(newFileName));
					csvFile.append(csv.toString());
					csvFile.close();
					return connection;
				}
				System.out.println("Error the passwords are not the same or the User already exists!");
				Helper.log("Error the passwords are not the same or the User already exists!", "ConnectionHelper.java", "saveConnection()");
				return null;
			} 
			System.out.println("IP/Domain Name is not valid!");
			Helper.log("IP/Domain Name is not valid!", "ConnectionHelper.java", "saveConnection()");
			return null;
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "saveConnection()");
			return null;
		} 
	}

	/**
	 * Checks if the connection a user entered is already in the connections file
	 * @param fileName The file where the saved connections are stored
	 * @param IP The server address
	 * @param database The database name
	 * @param userName The userName for the connection
	 * @return True if the connection is already in the file false otherwise
	 */
	@SuppressWarnings("resource")
	private static boolean duplicateConnection(String fileName, String IP, String database, String userName) {
		try {
			fileName = Helper.dirFixer(ConnectionHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()) + fileName;
			File file = new File(fileName);
			if (file.exists()) { //Checks if the file exists
				CSVReader csv = new CSVReader(new FileReader(fileName));
				String[] row = null;
				while ((row = csv.readNext()) != null) { //Loops through all the connections in the saved file
					if (row[0].equalsIgnoreCase(IP) && row[1].equalsIgnoreCase(database) && row[2].equalsIgnoreCase(userName)) //Checks if connection in the file is the same as the connection the user entered
						return true; 
				} 
			} 
			return false;
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "duplicateConnection()");
			return false;
		} 
	}

	/**
	 * Retrieves all the connection from the saved connections file and returns a list of all the connections
	 * @param fileName The save connection file name
	 * @return A list of lists with the data from the file with all the connections
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	private static List<List> getAllConnections(String fileName) {
		try {
			fileName = Helper.dirFixer(ConnectionHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()) + fileName;
			CSVReader csv = new CSVReader(new FileReader(fileName));
			String[] row = null;
			List<List> connections = new ArrayList<>();
			while ((row = csv.readNext()) != null) { //Loops through all the data in the file and adds it to the connections list
				List<String> connection = new ArrayList<>();
				connection.add(row[0]);
				connection.add(row[1]);
				connection.add(row[2]);
				connection.add(row[3]);
				connection.add(row[4]);
				connections.add(connection);
			} 
			return connections;
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "getAllConnections()");
			return null;
		} 
	}

	/**
	 * Prints out all the connection from the saved connection file and lets the user chose a connection and confirms the password
	 * @param reader How I am reading data from the console
	 * @param passwordReader How I am reading data for the password without showing the characters
	 * @param fileName The file where the saved connections are stored
	 * @param testingMode A boolean to override the password confirm part. Use only when bug testing program
	 * @return A list of strings that can be used to build an SQL connection string
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> retrieveConnection(BufferedReader reader, Console passwordReader, String fileName, boolean testingMode) {
		try {
			String password;
			List<List> connections = getAllConnections(fileName);
			int index = 1;
			for (List<String> list : connections) { //Prints out all the connections to the console
				System.out.println("Connection ID: " + index + " IP/Domain: " + (String)list.get(0) + " Database: " + (String)list.get(1) + " User: " + (String)list.get(2));
				index++;
			} 
			System.out.print("Please select a connection ID: ");
			int connectionId = Integer.parseInt(reader.readLine()) - 1;
			List<String> connection = connections.get(connectionId);
			try { //Tried to use the password reader reader to keep password hidden
				char[] passwordArray = passwordReader.readPassword("Please confirm password: ", new Object[0]);
				password = new String(passwordArray);
			} catch (Exception e) {
				System.out.print("Please confirm password: ");
				password = reader.readLine();
			} 
			if (confirmPassword(connection, password) || testingMode) {
				connection.add(password);
				return connection;
			} 
			return null;
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "retrieveConnection()");
			return null;
		} 
	}

	/**
	 * Checks if the password the user entered is the same as the password stored in the saved connections file
	 * @param connection The connection the user wants to connect to the server with
	 * @param password A non hashed password that the user entered
	 * @return
	 */
	private static boolean confirmPassword(List<String> connection, String password) {
		try {
			String hashedPassword = encryptPassword(password, connection.get(4));
			if (((String)connection.get(3)).equals(hashedPassword))
				return true; 
			return false;
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "confirmPassword()");
			return false;
		} 
	}

	/**
	 * Allows the user to remove a password from the saved connections file
	 * @param reader How I am reading data from the console
	 * @param passwordReader How I am reading the password to keep the characters hidden
	 * @param fileName The file name wehre the connections are saved
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void removeConnection(BufferedReader reader, Console passwordReader, String fileName) {
		try {
			String newFileName = Helper.dirFixer(ConnectionHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()) + fileName;
			List<String> removedConnection = retrieveConnection(reader, passwordReader, fileName, false);
			removedConnection.remove(removedConnection.size() - 1);
			List<List> connections = getAllConnections(fileName);
			connections.remove(connections.indexOf(removedConnection));
			BufferedWriter connectionFile = new BufferedWriter(new FileWriter(newFileName, false));
			for (List<String> connection : connections) { //Rebuilds the saved connection file without the connection the user removed
				StringBuilder csv = new StringBuilder();
				csv.append(connection.get(0));
				csv.append(',');
				csv.append(connection.get(1));
				csv.append(',');
				csv.append(connection.get(2));
				csv.append(',');
				csv.append(connection.get(3));
				csv.append(',');
				csv.append(connection.get(4));
				csv.append("\r\n");
				connectionFile.append(csv.toString());
			} 
			connectionFile.close();
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "removeConnection()");
		} 
	}

	/**
	 * Builds a SQL connection string from a saved connection
	 * @param savedConnection A list of strings from the save connection file
	 * @return A SQL connection string to connect to the MySQL database
	 */
	public static Connection connectionBuilder(List<String> savedConnection) {
		try {
			String connection = MessageFormat.format("jdbc:mysql://{0}/{1}?user={2}&password={3}&serverTimezone=UTC", new Object[] { savedConnection.get(0), savedConnection.get(1), savedConnection.get(2), savedConnection.get(savedConnection.size()-1) });
			return (Connection)DriverManager.getConnection(connection);
		} catch (Exception e) {
			Helper.log(e, "ConnectionManager.java", "connectionBuilder()");
			return null;
		} 
	}

	/**
	 * Allows the user to connect to the MySQL server without saving the connection to the save connection file
	 * @param reader How I am reading data from the console
	 * @param passwordReader How I am reading the password while keeping it hidden
	 * @return A list of strings that can be used to connect to the MySQL server
	 */
	@SuppressWarnings("unused")
	public static List<String> noSaveConnection(BufferedReader reader, Console passwordReader) {
		try{
			List<String> connection = new ArrayList<String>();
			System.out.print("Please enter Server IP or Domain Name: ");
			String IP = reader.readLine();
			if (regularExpressionChecker(IP)) {
				String password;
				String passConfirm;
				System.out.print("Please enter Database Name: ");
				String database = reader.readLine();
				System.out.print("Please enter User Name: ");
				String userName = reader.readLine();
				connection.add(IP);
				connection.add(database);
				connection.add(userName);
				try {
					char[] passwordArray = passwordReader.readPassword("Please enter password: ", new Object[0]);
					char[] passConfirmArray = passwordReader.readPassword("Please confirm your password: ", new Object[0]);
					password = new String(passwordArray);
					passConfirm = new String(passConfirmArray);
					connection.add(passConfirm);
				} catch (Exception e) {
					System.out.print("Please enter password: ");
					password = reader.readLine();
					System.out.print("Please confirm your password: ");
					passConfirm = reader.readLine();
					connection.add(passConfirm);
				}
			}
			return connection;
		}catch(Exception e) {
			Helper.log(e, "ConnectionManger.java", "noSaveConnection()");
			return null;
		}
	}
}
