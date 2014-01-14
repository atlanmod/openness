package fr.inria.atlanmod.msrChallenge.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AverageTimeBecomeCommitter extends Metric {
	
	public AverageTimeBecomeCommitter(Connection conn, int projectId, String projectName) {
		super(conn, projectId, projectName);
		this.description = "Average time (in days) to become a committer";
	}
	
	@Override
	public PreparedStatement getStatement() throws SQLException {
		String query = "select avg(days) as result " +
				"from (" +
				"	select avg(timestampdiff(DAY, cpp.issue_creation, cpp.commit_creation)) as days" +
				"	from (" +
				"		select i.reporter_id as user, min(i.created_at) as issue_creation, min(c.created_at) as commit_creation" +
				"		from issues i, commits c" +
				"		where i.reporter_id = c.committer_id and c.project_id = " + projectId + " and c.created_at > i.created_at" +
				"		group by i.reporter_id) as cpp, users u" +
				"	where u.id = cpp.user" +
				"	union" +
				"	(select avg(timestampdiff(DAY, cpp.issue_creation, cpp.commit_creation)) as days" +
				"	from (" +
				"		select i.assignee_id as user, min(i.created_at) as issue_creation, min(c.created_at) as commit_creation" +
				"		from issues i, commits c" +
				"		where i.assignee_id = c.committer_id and c.project_id = " + projectId + " and c.created_at > i.created_at" +
				"		group by i.assignee_id) as cpp, users u" +
				"	where u.id = cpp.user)) as a;";
		PreparedStatement stmt = conn.prepareStatement(query);
		return stmt;		
	}
}
