package fr.inria.atlanmod.msrChallenge.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AverageTimeIssueMergedAndClosed extends Metric {

	public AverageTimeIssueMergedAndClosed(Connection conn, int projectId, String projectName) {
		super(conn, projectId, projectName);
		this.description = "Average time (in hours) an issue is merged and closed";
	}
	
	@Override
	public PreparedStatement getStatement() throws SQLException {
		String query = "select avg(t.diff_hours) as result " +
				"from " +
				"	(" +
				"	select opened.id_issue, timestampdiff(HOUR, opened.pull_request_creation, merged.pull_request_creation) as diff_hours" +
				"	from" +
				"	(" +
				"	select t.project_id, t.project_name, t.id_issue, t.pull_request_id, t.pull_request_user, prh.action as pull_request_action, prh.created_at as pull_request_creation" +
				"	from" +
				"	(" +
				"		select p.id as project_id, p.name as project_name, ipr.id_issue, ipr.issue_creation as issue_creation, ipr.pull_request_id, pr.user_id as pull_request_user" +
				"		from" +
				"			(" +
				"			select distinct i.id as id_issue, i.created_at as issue_creation, prc.pull_request_id as pull_request_id" +
				"			from issues i, pull_request_commits prc" +
				"			where i.pull_request_id = prc.pull_request_id" +
				"			) as ipr," +
				"			pull_requests pr," +
				"			projects p" +
				"			where pr.pullreq_id = ipr.pull_request_id and pr.base_repo_id = p.id" +
				"		) as t," +
				"		pull_request_history prh" +
				"		where prh.pull_request_id = t.pull_request_id and t.project_id = '" + projectId + "' and prh.action = 'opened'" +
				"	) as opened," +
				"	(" +
				"	select t.project_id, t.project_name, t.id_issue, t.pull_request_id, t.pull_request_user, prh.action as pull_request_action, prh.created_at as pull_request_creation" +
				"	from (" +
				"		select p.id as project_id, p.name as project_name, ipr.id_issue, ipr.issue_creation as issue_creation, ipr.pull_request_id, pr.user_id as pull_request_user" +
				"		from" +
				"			(" +
				"			select distinct i.id as id_issue, i.created_at as issue_creation, prc.pull_request_id as pull_request_id" +
				"			from issues i, pull_request_commits prc" +
				"			where i.pull_request_id = prc.pull_request_id" +
				"			) as ipr," +
				"			pull_requests pr," +
				"			projects p" +
				"			where pr.pullreq_id = ipr.pull_request_id and pr.base_repo_id = p.id" +
				"		) as t," +
				"		pull_request_history prh" +
				"		where prh.pull_request_id = t.pull_request_id and t.project_id = '" + projectId + "' and prh.action = 'merged'" +
				"	) as merged" +
				"	where opened.id_issue = merged.id_issue" +
				"	) as t;";
		PreparedStatement stmt = conn.prepareStatement(query);
		return stmt;		
	}

}
