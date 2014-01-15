package fr.inria.atlanmod.msrChallenge.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PullRequests extends Metric {

	public PullRequests(Connection conn, int projectId, String projectName) {
		super(conn, projectId, projectName);
		this.description = "Pull requests per month";
	}
	
	@Override
	public PreparedStatement getStatement() throws SQLException {
		String query = "select count(prh.pull_request_id) as pull_requests, date_format(prh.created_at, '%Y-%m') as period" +
				" from pull_request_history prh, pull_requests pr" +
				"where prh.pull_request_id = pr.pullreq_id and pr.base_repo_id = 9215" +
				"group by YEAR(prh.created_at), MONTH(prh.created_at);";
		PreparedStatement stmt = conn.prepareStatement(query);
		return stmt;		
	}
}
