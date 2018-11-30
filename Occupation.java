import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
	 * Prints out the occupation name to the console
	 */
	public void print() {
		System.out.println("ID: " + this.getId() + " " + this.getOccupation());
	}

	/**
	 * Adds the occupation to a list and returns it
	 * @param conn The mySQL connection
	 * @return The list of occupations
	 */
	public static List<Occupation> getAll(Connection conn){
		List<Occupation> occupations = new ArrayList<Occupation>();
		try {
			//Encryption decrypt = new Encryption();
			PreparedStatement ps = conn.prepareStatement("SELECT id, occupation FROM occupation");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Occupation occupation = new Occupation();
				int col = 1;
				occupation.setId(rs.getInt(col++));
				occupation.setOccupation(rs.getString(col++));
				//occupation.setOccupation(decrypt.decryptText(rs.getString(col++),decrypt.getPrivate("KeyPair/privateKey")));
				occupations.add(occupation);
			}
			return occupations;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Returns a singular occupation passed on the filed and value the user provided
	 * @param conn The mySQL connection
	 * @param value The value the filed type need to equal
	 * @param fieldName The filed type of how the user is looking up
	 * @return
	 */
	public static Occupation getBy(Connection conn, String value, String fieldName){
		try {
			//Encryption decrypt = new Encryption();
			Occupation occupation = new Occupation();
			PreparedStatement ps = conn.prepareStatement("SELECT id, occupation FROM occupation WHERE " + fieldName + "  = ?");
			ps.setInt(1, Integer.parseInt(value));
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				int col = 1;
				occupation.setId((rs.getInt(col++)));
				occupation.setOccupation(rs.getString(col++));
				//occupation.setOccupation(decrypt.decryptText(rs.getString(col++),decrypt.getPrivate("KeyPair/privateKey")));
			}
			return occupation;
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/*public static int getByName(Connection conn, String name){
		Occupation occupation = new Occupation();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("Select occupation FROM occupation WHERE occupation = ?");
			ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			int col = 1;
			occupation.setId((rs.getInt(col++)));
			occupation.setOccupation(rs.getString(col++));
		}return occupation.getId();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
		
	}*/

	/**
	 * Uses the provided occupation and adds it to the occupation table
	 * @param conn The mySQL connection
	 * @param occupation The name of the occupation the user is adding
	 */
	public static void insert(Connection conn, String occupation){
		//String occupationEncrypt = Encryption.encryptOccupation(occupation);
		try {
			//Encryption encrypt = new Encryption();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO occupation (occupation) values(?)");
			ps.setString(1, occupation);
			//ps.setString(1, encrypt.encryptText(occupation,encrypt.getPublic("KeyPair/publicKey")));
			ps.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Uses the provided id to update the occupation name
	 * @param conn The mySQL connection
	 * @param id The id of the occupation to update
	 * @param occupation The new occupation name
	 */
	public static void update(Connection conn, int id, String occupation){
		try{
			//Encryption encrypt = new Encryption();
			PreparedStatement ps = conn.prepareStatement("UPDATE occupation SET occupation=? WHERE id = ?");
			ps.setString(1, occupation);
			//ps.setString(1, encrypt.encryptText(occupation,encrypt.getPublic("KeyPair/publicKey")));
			ps.setInt(2, id);
			ps.executeUpdate();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Uses the provided id to remove the occupation from the occupation table
	 * @param conn The mySQL connection
	 * @param id The id of the occupation to remove
	 */
	public static void remove(Connection conn, int id){
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM occupation WHERE id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
