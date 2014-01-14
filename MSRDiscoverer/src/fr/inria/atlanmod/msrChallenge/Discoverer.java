package fr.inria.atlanmod.msrChallenge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;

/**
 * Main class to launch the discovery process 
 * 
 * @author Javier Canovas (javier.canovas@inria.fr)
 *
 */
public class Discoverer {

	private static final int LIMIT = 1;

	private void discover() {
		// Connecting to DB
		Connection conn = null;
		Properties connProps = new Properties();
		connProps.put("user", "root");
		connProps.put("password", "admin");
		String connString = "jdbc:mysql://localhost:3306/";

		try {
			conn = DriverManager.getConnection(connString, connProps);
			conn.setCatalog("msr14");
		} catch(SQLException e) {
			System.err.println("Problem creating the connection");
			e.printStackTrace();
		}

		
		// OPENNESS
		System.out.println("Calculating Openness metrics...");
		long start = System.nanoTime();
		try {
			StringBuffer toWrite = new StringBuffer();
			
			PreparedStatement projectsQuery = conn.prepareStatement("select * from projects");
			ResultSet project = projectsQuery.executeQuery();
			int counter = 0;
			while(project.next()) {
				int projectId = project.getInt("id");
				String projectName = project.getString("name");

				ProjectDiscoverer projectDiscoverer = new ProjectDiscoverer(conn, projectId, projectName);
				String result = projectDiscoverer.discoverOpenness();
				toWrite.append(projectName + "," + result + "\n");
				if(counter++ > LIMIT) break;
			}

			FileWriter fw = new FileWriter(new File("resultsOpenness.csv"));
			fw.write(toWrite.toString());
			fw.close();
		} catch(SQLException e) {
			System.err.println("Problem traversing the projects");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Problem serializing the file");
			e.printStackTrace();
		}
		long end = System.nanoTime();
		System.out.println(String.valueOf((double) (((double) end - start) / 1000000000.0)) + " secs");

		// ACTIVITY
		System.out.println("Calculating Activity metrics...");
		start = System.nanoTime();
		try {
			StringBuffer toWrite = new StringBuffer();
			
			PreparedStatement projectsQuery = conn.prepareStatement("select * from projects");
			ResultSet project = projectsQuery.executeQuery();
			int counter = 0;
			while(project.next()) {
				int projectId = project.getInt("id");
				String projectName = project.getString("name");

				ProjectDiscoverer projectDiscoverer = new ProjectDiscoverer(conn, projectId, projectName);
				String result = projectDiscoverer.discoverActivity();
				toWrite.append(projectName + "," + result + "\n");
				if(counter++ > LIMIT) break;
			}

			FileWriter fw = new FileWriter(new File("resultsActivity.csv"));
			fw.write(toWrite.toString());
			fw.close();
			
		} catch(SQLException e) {
			System.err.println("Problem traversing the projects");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Problem serializing the file");
			e.printStackTrace();
		}
		end = System.nanoTime();
		System.out.println(String.valueOf((double) (((double) end - start) / 1000000000.0)) + " secs");

		// MOOD
		System.out.println("Calculating Mood metrics...");
		start = System.nanoTime();
		try {
			StringBuffer toWrite = new StringBuffer();
			
			PreparedStatement projectsQuery = conn.prepareStatement("select * from projects");
			ResultSet project = projectsQuery.executeQuery();
			int counter = 0;
			while(project.next()) {
				int projectId = project.getInt("id");
				String projectName = project.getString("name");

				ProjectDiscoverer projectDiscoverer = new ProjectDiscoverer(conn, projectId, projectName);
				String result = projectDiscoverer.discoverMood();
				toWrite.append(projectName + "," + result + "\n");
				if(counter++ > LIMIT) break;
			}

			FileWriter fw = new FileWriter(new File("resultsMood.csv"));
			fw.write(toWrite.toString());
			fw.close();
			
		} catch(SQLException e) {
			System.err.println("Problem traversing the projects");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Problem serializing the file");
			e.printStackTrace();
		}
		end = System.nanoTime();
		System.out.println(String.valueOf((double) (((double) end - start) / 1000000000.0)) + " secs");
	}

	public static void main(String[] args) {
		Discoverer discoverer = new Discoverer();
		discoverer.discover();
	}
}
