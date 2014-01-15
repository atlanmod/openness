package fr.inria.atlanmod.msrChallenge.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AverageTimeIssueNoMergedAndClosed extends Metric {

	public AverageTimeIssueNoMergedAndClosed(Connection conn, int projectId, String projectName) {
		super(conn, projectId, projectName);
		this.description = "Average time (in hours) an issue is closed but not merged";
	}
	
	@Override
	public PreparedStatement getStatement() throws SQLException {
		String query = "select avg(a.diff_hours) as result from (" +
				"	select opened.id_issue, timestampdiff(HOUR, opened.pull_request_creation, closed_and_not_merged.pull_request_creation) as diff_hours" +
				"	from" +
				"		(" +
				"		select *" +
				"		from (" +
				"			select t.project_id, t.project_name, t.id_issue, t.pull_request_id, t.pull_request_user, prh.action as pull_request_action, prh.created_at as pull_request_creation" +
				"			from (" +
				"				select p.id as project_id, p.name as project_name, ipr.id_issue, ipr.issue_creation as issue_creation, ipr.pull_request_id, pr.user_id as pull_request_user" +
				"				from" +
				"					(" +
				"					select distinct i.id as id_issue, i.created_at as issue_creation, prc.pull_request_id as pull_request_id" +
				"					from issues i, pull_request_commits prc" +
				"					where i.pull_request_id = prc.pull_request_id" +
				"					) as ipr," +
				"					pull_requests pr," +
				"					projects p" +
				"					where pr.pullreq_id = ipr.pull_request_id and pr.base_repo_id = p.id" +
				"				) as t," +
				"				pull_request_history prh" +
				"				where prh.pull_request_id = t.pull_request_id and t.project_id = '" + projectId + "' and prh.action = 'closed'" +
				"			) as closed" +
				"			where closed.id_issue not in (" +
				"				select distinct t.id_issue" +
				"				from" +
				"				(" +
				"					select p.id as project_id, p.name as project_name, ipr.id_issue, ipr.issue_creation as issue_creation, ipr.pull_request_id, pr.user_id as pull_request_user" +
				"					from" +
				"						(" +
				"						select distinct i.id as id_issue, i.created_at as issue_creation, prc.pull_request_id as pull_request_id" +
				"						from issues i, pull_request_commits prc" +
				"						where i.pull_request_id = prc.pull_request_id" +
				"						) as ipr," +
				"						pull_requests pr," +
				"						projects p" +
				"						where pr.pullreq_id = ipr.pull_request_id and pr.base_repo_id = p.id" +
				"					) as t," +
				"					pull_request_history prh" +
				"					where prh.pull_request_id = t.pull_request_id and t.project_id = '" + projectId + "' and prh.action = 'merged'" +
				"				)" +
				"		) as closed_and_not_merged," +
				"		(" +
				"			select t.project_id, t.project_name, t.id_issue, t.pull_request_id, t.pull_request_user, prh.action as pull_request_action, prh.created_at as pull_request_creation" +
				"			from" +
				"		(" +
				"				select p.id as project_id, p.name as project_name, ipr.id_issue, ipr.issue_creation as issue_creation, ipr.pull_request_id, pr.user_id as pull_request_user" +
				"				from" +
				"					(" +
				"				select distinct i.id as id_issue, i.created_at as issue_creation, prc.pull_request_id as pull_request_id" +
				"					from issues i, pull_request_commits prc" +
				"					where i.pull_request_id = prc.pull_request_id" +
				"					) as ipr," +
				"					pull_requests pr," +
				"					projects p" +
				"					where pr.pullreq_id = ipr.pull_request_id and pr.base_repo_id = p.id" +
				"				) as t," +
				"				pull_request_history prh" +
				"				where prh.pull_request_id = t.pull_request_id and t.project_id = '" + projectId + "' and prh.action = 'opened'" +
				"		) as opened" +
				"	where closed_and_not_merged.id_issue = opened.id_issue" +
				") as a;";
		PreparedStatement stmt = conn.prepareStatement(query);
		return stmt;		
	}
}
