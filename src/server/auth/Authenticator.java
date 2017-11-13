package server.auth;

import server.Server;
import server.auth.bcrypt.BCrypt;
import server.sql.ResultList;
import server.sql.SQLManager;

public class Authenticator {
	
	private Server server;
	private SQLManager sql;
	
	public Authenticator(Server server) {
		this.server = server;
		this.sql = server.getSQLManager();
	}
	
	public boolean login(String username, String password) {
		if (!userExists(username))
			return false;
		
		ResultList results =
				sql.getQueryResult("SELECT * FROM users WHERE username = '"+username+"'");
		
		String hash = results.get(0).get("password").getString();
		
		if (BCrypt.checkpw(password, hash)) {
			return true;
		}
		
		return false;
	}
	
	public void register(String username, String password, String email) {
		if (userExists(username))
			return;
		
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		
		sql.insert("users", "0, '"+username+"', '"+hashed+"', '"+email+"'");
		server.log("Successfully registered "+username+".");
	}
	
	public boolean userExists(String username) {
		ResultList results =
				sql.getQueryResult("SELECT * FROM users WHERE username = '"+username+"'");
		
		return results.size() > 0;		
	}

}
