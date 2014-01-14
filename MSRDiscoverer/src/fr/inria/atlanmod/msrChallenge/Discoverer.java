package fr.inria.atlanmod.msrChallenge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Main class to launch the discovery process 
 * 
 * @author Javier Canovas (javier.canovas@inria.fr)
 *
 */
public class Discoverer {
	public void discover() throws SQLException {
		// Connecting to DB
		Connection conn = null;
		Properties connProps = new Properties();
		connProps.put("user", "root");
		connProps.put("password", "admin");
		String connString = "jdbc:mysql://localhost:3306/";
		conn = DriverManager.getConnection(connString, connProps);
		conn.setCatalog("msr14");
		
		// Query all the projects
		PreparedStatement projectsQuery = conn.prepareStatement("select * from projects");
		ResultSet project = projectsQuery.executeQuery();
		
		// For each project, we'll apply the metrics
		while(project.next()) {
			int projectId = project.getInt("id");
			
			ProjectDiscoverer projectDiscoverer = new ProjectDiscoverer(conn, projectId);
			projectDiscoverer.discover();
			
		}
	}
}
