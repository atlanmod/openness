package fr.inria.atlanmod.msrChallenge;

import java.sql.Connection;
import java.util.HashMap;

/**
 * This class performs the calculations to get the values for the different
 * metrics we have defined.
 * 
 * @author Javier Canovas (javier.canovas@inria.fr)
 *
 */
public class ProjectDiscoverer {
	private Connection conn;
	private int projectId;
	private HashMap<String, Double> result = new HashMap<>();

	public ProjectDiscoverer(Connection conn, int projectId) {
		this.conn = conn; 
		this.projectId = projectId;
	}

	public void discover() {
		discoverOpenness();
		discoverActivity();
		discoverMood();
	}

	private void discoverOpenness() {

	}

	private void discoverActivity() {

	}
	
	private void discoverMood() {

	}
}
