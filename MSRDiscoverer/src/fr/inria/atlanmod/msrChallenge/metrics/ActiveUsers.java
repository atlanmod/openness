package fr.inria.atlanmod.msrChallenge.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActiveUsers extends Metric {

	public ActiveUsers(Connection conn, int projectId, String projectName) {
		super(conn, projectId, projectName);
		this.description = "Active users per month";
	}
	
	@Override
	public PreparedStatement getStatement() throws SQLException {
		String query = "select count(distinct result.actors) as result, result.period" +
				" from (" +
				"	select ic.user_id as actors, date_format(ic.created_at, '%Y-%m') as period" +
				"	from issue_comments ic, issues i" +
				"	where i.issue_id = ic.issue_id and i.repo_id = " + projectId + " "+
				"	union" +
				"	select cc.user_id as actors, date_format(cc.created_at, '%Y-%m') as period" +
				"	from commit_comments cc, commits c" +
				"	where cc.commit_id = c.id and c.project_id = " + projectId + " "+
				"	union" +
				"	select prc.user_id as actors, date_format(prc.created_at, '%Y-%m') as period" +
				"	from pull_requests pr, pull_request_comments prc" +
				"	where pr.pullreq_id = prc.pull_request_id and pr.base_repo_id = " + projectId + " "+
				"	union" +
				"	select c.author_id as actors, date_format(c.created_at, '%Y-%m') as period" +
				"	from commits c" +
				"	where c.project_id = " + projectId + " "+
				"	union" +
				"	select c.committer_id as actors, date_format(c.created_at, '%Y-%m') as period" +
				"	from commits c" +
				"	where c.project_id = " + projectId + " "+
				"	union" +
				"	select pr.user_id as actors, date_format(prh.created_at, '%Y-%m') as period" +
				"	from pull_requests pr, pull_request_history prh" +
				"	where pr.pullreq_id = prh.pull_request_id and pr.base_repo_id = " + projectId + " "+
				"	union" +
				"	select i.reporter_id as actors, date_format(i.created_at, '%Y-%m') as period" +
				"	from issues i" +
				"	where i.repo_id = " + projectId + " "+
				"	union" +
				"	select i.assignee_id as actors, date_format(i.created_at, '%Y-%m') as period" +
				"	from issues i" +
				"	where i.repo_id = " + projectId + " "+
				"	) as result" +
				"group by result.period;";
		PreparedStatement stmt = conn.prepareStatement(query);
		return stmt;		
	}
	
	/* (non-Javadoc)
	 * @see fr.inria.atlanmod.msrChallenge.metrics.Metric#calculate()
	 * We need to particularize the behaviour for this
	 * 
	 */
	@Override
	public void calculate() {
		try {
			PreparedStatement stmt = getStatement();
			ResultSet stmtResult = stmt.executeQuery();
			if(stmtResult.next()) {
				// Takes the result and returns it if there is no next element 
				// (for the case of projects younger than 1 month)
				result = stmtResult.getDouble("result");
				if(stmtResult.next())
					// Returns the second element
					// (for the case of project older than 1 month)
					result = stmtResult.getDouble("result");
			} else 
				result = -1.0;
		} catch (SQLException e) {
			result = -1.0;
			e.printStackTrace();
		}
	}
	
	
}
