import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jack Stockely
 * 
 * @version 1.0.1
 * 
 * @description The Occupation object of the Address Book project
 * 
 * @date 31 January 2019
 *
 */
public class Occupation {
	private int id;
	private String occupation;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * Overrides the built in toString command and replaces it with the current one which returns the id and occupation of a given occupation
	 */
	public String toString() {
		return "ID: " + this.getId() + " " + this.getOccupation();
	}

	/**
	 * Returns a list of all the occupations in the mySQL server
	 * @param conn The mySQL connection
	 * @return The list of all occupations on the mySQL server
	 */
	public static List<Occupation> getAll(Connection conn){
		try {
			List<Occupation> occupations = new ArrayList<Occupation>();
			PreparedStatement ps = conn.prepareStatement("SELECT id, occupation FROM occupation");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Occupation occupation = new Occupation();
				int col = 1;
				occupation.setId(rs.getInt(col++));
				occupation.setOccupation(rs.getString(col++));
				occupations.add(occupation);
			}
			return occupations;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a list of all occupation that have a user defined field type in common
	 * @param conn The mySQL connection
	 * @param fieldName The user defined field type
	 * @param field The field that the occupations have in common
	 * @return A list of similar occupations
	 */
	public static List<Occupation> getSimilar(Connection conn, String fieldName, String field){
		try {
			List<Occupation> occupations  = new ArrayList<Occupation>();
			PreparedStatement ps = conn.prepareStatement("SELECT id, occupation FROM occupation WHERE " + fieldName + " = ?");
			ps.setString(1, field);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Occupation occupation = new Occupation();
				int col = 1;
				occupation.setId(rs.getInt(col++));
				occupation.setOccupation(rs.getString(col++));
				occupations.add(occupation);
			}
			return occupations;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a single occupation
	 * @param conn The mySQL connection
	 * @param fieldName The user defined field
	 * @param field The given field
	 * @return The single occupation
	 */
	public static Occupation getBy(Connection conn, String fieldName, String field) {
		try {
			Occupation occupation = new Occupation();
			PreparedStatement ps = conn.prepareStatement("SELECT id, occupation FROM occupation WHERE " + fieldName + " = ?");
			ps.setString(1, field);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int col = 1;
				occupation.setId(rs.getInt(col++));
				occupation.setOccupation(rs.getString(col++));
			}
			return occupation;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Updates a user defined occupation on the mySQL server
	 * @param conn The mySQL connection
	 * @param id The id of the occupation the user wants to update
	 * @param occupationName The new occupation name
	 */
	public static void update(Connection conn, int id, String occupationName) {
		Occupation occupation = Occupation.getBy(conn, "id", Integer.toString(id));
		if(occupationName.equals("")) {
			occupationName=occupation.getOccupation();
		}
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE occupation SET occupation=? WHERE id = ?");
			ps.setString(1, occupationName);
			ps.setInt(2, id);
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts a new occupation onto the mySQL server
	 * @param conn The mySQL connection
	 * @param occupation The new occupation to be added
	 */
	public static void insert(Connection conn, String occupation) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO occupation (occupation) values (?)");
			ps.setString(1, occupation);
			ps.execute();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes an occupation from the mySQL server
	 * @param conn The mySQL connection
	 * @param id The id of the occupation the user wants to remove
	 */
	public static void remove(Connection conn, int id) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM occupation WHERE id = ? ");
			ps.setInt(1, id);
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}