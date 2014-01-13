import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Test {
	public static void main(String[] args) throws SQLException, FileNotFoundException {
		Connection conn = null;

		Properties connProps = new Properties();
		connProps.put("user", "root");
		connProps.put("password", "admin");

		String connString = "jdbc:mysql://localhost:3306/";

		conn = DriverManager.getConnection(connString, connProps);
		conn.setCatalog("msr14");

		PreparedStatement issuesQuery = conn.prepareStatement("select * from issues where repo_id = ?");
		PreparedStatement issueCommentsQuery = conn.prepareStatement("select * from issue_comments where issue_id = ?");
		PreparedStatement userQuery = conn.prepareStatement("select * from users where id = ?");

		issuesQuery.setInt(1, 9215);
		ResultSet issuesSet = issuesQuery.executeQuery();

		HashMap<String, List<String>> users = new HashMap<>();
		HashMap<String, Integer> weights = new HashMap<>();
		while(issuesSet.next()) {
			int issueId = issuesSet.getInt("id");
			issueCommentsQuery.setInt(1, issueId);
			ResultSet issueCommentsSet = issueCommentsQuery.executeQuery();
			List<String> issueUsers = new ArrayList<>();
			while(issueCommentsSet.next()) {
				int userId = issueCommentsSet.getInt("user_id");
				issueUsers.add(String.valueOf(userId));
			}

			for(String user : issueUsers) {
				List<String> linkedUsers = users.get(user);
				if(linkedUsers == null) users.put(user, new ArrayList<String>());

				Integer weight = weights.get(user);
				if(weight == null) weights.put(user, new Integer(1));
				else weights.put(user, new Integer(weight.intValue() + 1));
			}


			for(String user : issueUsers) {
				List<String> linkedUsers = users.get(user);

				for(String linkedUser : issueUsers) {
					if(!linkedUser.equals(user)) 
						linkedUsers.add(linkedUser);
				}
				users.put(user, linkedUsers);
			}
		}

		StringBuffer result = new StringBuffer();
		Timestamp timeStamp = new Timestamp(Calendar.getInstance().getTimeInMillis());

		result.append("<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\" xmlns:viz=\"http://www.gexf.net/1.2draft/viz\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\">\n" +
				"\t<meta lastmodifieddate=\""+ timeStamp.toString() + "\">\n" +
				"\t\t<creator>Atlanmod</creator>\n" +
				"\t\t<description>Users in a project</description>\n" +
				"\t</meta>\n");

		result.append("\t<graph mode=\"static\" defaultedgetype=\"directed\">\n");

		result.append("\t\t<nodes>\n");

		List<String> generatedEdges = new ArrayList<>();

		StringBuffer edges = new StringBuffer();
		Iterator<String> userIds = users.keySet().iterator();
		int defaultSize = 10;
		while(userIds.hasNext()) {
			String userId = userIds.next();
			int weight = weights.get(userId).intValue();
			int size = defaultSize + weight;

			userQuery.setInt(1, Integer.valueOf(userId).intValue());
			ResultSet userName = userQuery.executeQuery();

			String userLogin = userId;
			if(userName.next()) {
				userLogin = userName.getString("login");
			}			

			result.append("\t\t\t<node id =\"" + "n" + userId + "\" label=\"" + userLogin + "\">\n");
			result.append("\t\t\t\t<viz:size value=\"" + String.valueOf(size) + "\"></viz:size>\n");

			result.append("\t\t\t</node>\n");

			List<String> linkedUsers = users.get(userId);
			for(String linkedUser : linkedUsers) {
				if(!generatedEdges.contains(userId + "-" + linkedUser)) {
					edges.append("\t\t\t<edge id =\"" + "e" + userId + "-" + linkedUser + "\" source=\"" + "n" + userId + "\" target=\"" + "n" + linkedUser + "\">\n");
					edges.append("\t\t\t</edge>\n");
					generatedEdges.add(userId + "-" + linkedUser);
				}
			}

		}

		result.append("\t\t</nodes>\n");
		result.append("\t\t<edges>\n");
		result.append(edges.toString());
		result.append("\t\t</edges>\n");

		result.append("\t</graph>\n");
		result.append("\t</gexf>\n");	

		PrintWriter pw = new PrintWriter(new File("./result.gexf"));
		pw.print(result);
		pw.close();

	}
}
