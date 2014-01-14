package fr.inria.atlanmod.msrChallenge.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Main class to represent metrics
 * 
 * @author Javier Canovas (javier.canovas@inria.fr)
 *
 */
public abstract class Metric {
	/**
	 * Short description of the metric
	 */
	String description;

	/**
	 * ID of the project to which the metric will be applied
	 */
	int projectId;

	/**
	 * Name of the project to which the metric will be applied 
	 */
	String projectName;

	/**
	 * SQL connection
	 */
	Connection conn;

	/**
	 * Result of the metric
	 */
	double result;

	public Metric(Connection conn, int projectId, String projectName) {
		this.conn = conn;
		this.projectId = projectId;
		this.projectName = projectName;
	}


	public double getResult() {
		return result;
	}


	public String getDescription() {
		return description;
	}

	public void calculate() {
		try {
			PreparedStatement stmt = getStatement();
			ResultSet stmtResult = stmt.executeQuery();
			if(stmtResult.next()) 
				result = stmtResult.getDouble("result");
			else 
				result = -1.0;
		} catch (SQLException e) {
			result = -1.0;
			e.printStackTrace();
		}
	}

	public abstract PreparedStatement getStatement() throws SQLException;
}
