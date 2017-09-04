
import java.sql.*;
public class testDB{
	//$name1 = "pettea";
	//$pPW = "pRi27J2A";

	public testDB()  throws SQLException{
		connectToAndQueryDatabase("pettea", "pRi27J2A");
	}

	public static void main(String[] args)  throws SQLException{
		new testDB();
	}

	public void connectToAndQueryDatabase(String username, String password) throws SQLException{

	    Connection con = DriverManager.getConnection(
	                         "jdbc:myDriver:mysql-vt2016.csc.kth.se",
	                         username,
	                         password);

	    Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM Table1");

	    while (rs.next()) {
	        int x = rs.getInt("a");
	        String s = rs.getString("b");
	        float f = rs.getFloat("c");
	    }
	}
}