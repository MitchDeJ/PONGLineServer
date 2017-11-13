package server.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import server.Configuration;
import server.Server;

public class SQLManager {

	private Server server;

	private MysqlDataSource dataSource = new MysqlDataSource();
	private Connection connection;

	public SQLManager(Server server) {
		this.server = server;
		init();
	}

	public void init() {
		// getting the server config
		Configuration c = server.getConfig();

		/* configuring the data source */
		dataSource.setUser(c.dbUser);
		dataSource.setPassword(c.dbPass);
		dataSource.setServerName(c.dbHost);
		dataSource.setDatabaseName(c.dbName);
		
		/*Setting up the db connection*/
		 try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert(String table, String values) {
		try {
			Statement s = connection.createStatement();
			s.executeUpdate("INSERT INTO " + table + " VALUES (" + values + ")");
			s.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ResultList getQueryResult(String query) {
		try {
			Statement s = connection.createStatement();
			ResultSet r = s.executeQuery(query);
			r.beforeFirst();
			ResultSetMetaData rsmd = r.getMetaData();
			
			/*Adding a list entry for every ResultSet, containing a list of results of it's columns*/
			ResultList result = new ResultList();
			int i = 0;
			while(r.next()) {
				LinkedHashMap<String, QueryResult> newResult = new LinkedHashMap<String, QueryResult>();
				result.add(newResult);
				for (int c = 1; c <= rsmd.getColumnCount(); c++)
				result.get(i).put(rsmd.getColumnName(c), new QueryResult(r.getObject(c)));
				i++;
			}
			
			/*closing the sql related stuff*/
			r.close();
			s.close();
			
			return result;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
