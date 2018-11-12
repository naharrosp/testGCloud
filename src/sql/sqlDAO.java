package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;


public class sqlDAO {
	
	public Connection con;
	
	private static sqlDAO dao;
	
	private sqlDAO() {
	}
	
	public static sqlDAO getDao() {
		
		if(dao==null) {
			dao=new sqlDAO();
		}
		
		return dao;
	}
	
	public static Connection createConnection ()  throws Exception
    {
		String databaseName= "psanchez";
		String instanceConnectionName= "comillas-221409:us-central1:mysql";
		String jdbcUrl = String.format(
				    "jdbc:mysql://google/%s?cloudSqlInstance=%s"
				        + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false",
				    databaseName,
				    instanceConnectionName);

	    //String url = "jdbc:mysql://google/psanchez?cloudSqlInstance=comillas-221409:us-central1:mysql&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=root&useSSL=false
	    System.out.println("connecting to: " + jdbcUrl);
	    
	    Connection connection = null;
	    try {
	        // Load the class that provides the new "jdbc:google:mysql://" prefix.
	        Class.forName("com.mysql.jdbc.Driver");
	        //Class.forName("com.google.cloud.sql.jdbc.Driver");
	      } catch (ClassNotFoundException e) {
	        throw new ServletException("Error loading Google JDBC Driver", e);
	     }
	    
	    try {
	    	connection = DriverManager.getConnection(jdbcUrl, "root", "root");
	    } catch (SQLException e) {
	      throw new ServletException("Unable to connect to Cloud SQL", e);
	    }
        
        return connection;
        
    }
	
	public ArrayList<String> getAllWords() throws Exception{
		
		if (con == null) 
        { 
			con = sqlDAO.createConnection();
        }
        System.out.println("Successfully created connection to database.");

        ArrayList <String> array = new ArrayList <String> ();
        // Perform some SQL queries over the connection.
        try
        {
            PreparedStatement preparedStatement = con.prepareStatement("select word from psanchez.list");
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {

				String word = rs.getString("word");
				array.add(word);

			}
        }
        catch (SQLException e)
        {
            throw new SQLException("Encountered an error when executing given sql statement.", e);
        }       
        
        System.out.println("Execution finished.");
        
        return array;
	}

}
