package sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

@SuppressWarnings("serial")
	// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
	@WebServlet(name = "CloudSQL",
	    description = "CloudSQL: Write timestamps of visitors to Cloud SQL",
	    urlPatterns = "/cloudsql")
	public class cloudSql extends HttpServlet {
	  Connection conn;

	  @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

	    final String createTableSql = "CREATE TABLE IF NOT EXISTS visits ( "
	        + "visit_id SERIAL NOT NULL, ts timestamp NOT NULL, "
	        + "PRIMARY KEY (visit_id) );";
	    final String createVisitSql = "INSERT INTO visits (ts) VALUES (?);";
	    final String selectSql = "SELECT ts FROM visits ORDER BY ts DESC "
	        + "LIMIT 10;";

	    String path = req.getRequestURI();
	    if (path.startsWith("/favicon.ico")) {
	      return; // ignore the request for favicon.ico
	    }

	    PrintWriter out = resp.getWriter();
	    resp.setContentType("text/plain");

	    try (PreparedStatement statementCreateVisit = conn.prepareStatement(createVisitSql)) {
	      conn.createStatement().executeUpdate(createTableSql);
	      statementCreateVisit.setTimestamp(1, new Timestamp(new Date().getTime()));
	      statementCreateVisit.executeUpdate();

	      try (ResultSet rs = conn.prepareStatement(selectSql).executeQuery()) {
	        out.print("Last 10 visits:\n");
	        while (rs.next()) {
	          String timeStamp = rs.getString("ts");
	          out.println("Visited at time: " + timeStamp);
	        }
	      }
	    } catch (SQLException e) {
	      throw new ServletException("SQL error", e);
	    }
	  }


		    
	  @Override
	  public void init() throws ServletException {
		  
		  
		String databaseName= "psanchez";
		String instanceConnectionName= "comillas-221409:us-central1:mysql";
		String jdbcUrl = String.format(
				    "jdbc:mysql://google/%s?cloudSqlInstance=%s"
				        + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false",
				    databaseName,
				    instanceConnectionName);

	    //String url = "jdbc:mysql://google/psanchez?cloudSqlInstance=comillas-221409:us-central1:mysql&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=root&useSSL=false
	    log("connecting to: " + jdbcUrl);
	    
	    try {
	        // Load the class that provides the new "jdbc:google:mysql://" prefix.
	        Class.forName("com.mysql.jdbc.Driver");
	        //Class.forName("com.google.cloud.sql.jdbc.Driver");
	      } catch (ClassNotFoundException e) {
	        throw new ServletException("Error loading Google JDBC Driver", e);
	     }
	    
	    try {
	      conn = DriverManager.getConnection(jdbcUrl, "root", "root");
	    } catch (SQLException e) {
	      throw new ServletException("Unable to connect to Cloud SQL", e);
	    }
	  }
	}
	

