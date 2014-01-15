package fr.inria.atlanmod.msrChallenge;

import java.sql.Connection;

import fr.inria.atlanmod.msrChallenge.metrics.ActiveUsers;
import fr.inria.atlanmod.msrChallenge.metrics.AverageTimeBecomeCommitter;
import fr.inria.atlanmod.msrChallenge.metrics.AverageTimeIssueMergedAndClosed;
import fr.inria.atlanmod.msrChallenge.metrics.AverageTimeIssueNoMergedAndClosed;
import fr.inria.atlanmod.msrChallenge.metrics.EmoticonsInMessages;
import fr.inria.atlanmod.msrChallenge.metrics.Metric;

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
	private String projectName;

	public ProjectDiscoverer(Connection conn, int projectId, String projectName) {
		this.conn = conn; 
		this.projectId = projectId;
		this.projectName = projectName;
	}

	public String discoverAll() {
		String resultOpenness = discoverOpenness();
		String resultActivity = discoverActivity();
		String resultMood = discoverMood();
		return resultOpenness + "," + resultActivity + "," + resultMood;
	}

	public String discoverOpenness() {
		Metric m1 = new AverageTimeBecomeCommitter(conn, projectId, projectName);
		m1.calculate();
		
		return String.valueOf(m1.getResult());
	}

	public String discoverActivity() {
		Metric m1 = new AverageTimeIssueMergedAndClosed(conn, projectId, projectName);
		m1.calculate();
		
		Metric m2 = new AverageTimeIssueNoMergedAndClosed(conn, projectId, projectName);
		m2.calculate();
		
		Metric m3 = new ActiveUsers(conn, projectId, projectName);
		m3.calculate();
		
		return String.valueOf(m1.getResult()) + "," + String.valueOf(m2.getResult()) + "," + String.valueOf(m3.getResult());
	}
	
	public String discoverMood() {
		Metric m1 = new EmoticonsInMessages(conn, projectId, projectName);
		m1.calculate();
		return String.valueOf(m1.getResult());
	}
}
