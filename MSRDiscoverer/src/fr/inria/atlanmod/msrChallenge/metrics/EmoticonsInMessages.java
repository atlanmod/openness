package fr.inria.atlanmod.msrChallenge.metrics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmoticonsInMessages extends Metric {

	public EmoticonsInMessages(Connection conn, int projectId, String projectName) {
		super(conn, projectId, projectName);
		this.description = "Happiness emoticons in messages";
	}

	@Override
	public PreparedStatement getStatement() throws SQLException {
		String query = "select count(body) as result " +
				"	from (" +
				"		select c.id as id, cc.user_id as user, cc.body as body, cc.created_at as creation" +
				"		from commits c, commit_comments cc" +
				"		where cc.commit_id = c.id and c.project_id = 9215" +
				"		union" +
				"		select pr.id as id, prc.user_id as user, prc.body as body, prc.created_at as creation" +
				"		from pull_requests pr, pull_request_comments prc" +
				"		where prc.pull_request_id = pr.id and pr.base_repo_id = 9215) as messages" +
				"	where" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]])\\^\\^([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]])\\^\\_\\^([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]):\\)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]):\\-\\)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]):p([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]):\\-p([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]])lol([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]);\\)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]);\\-\\)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]):d)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]]):\\-d)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]])b\\-\\)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]])b\\)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]])=\\)([[:punct:]]|[[:space:]])' or" +
				"	lower(body) REGEXP '([[:punct:]]|[[:space:]])=d([[:punct:]]|[[:space:]])'";
		PreparedStatement stmt = conn.prepareStatement(query);
		return stmt;		
	}

}
