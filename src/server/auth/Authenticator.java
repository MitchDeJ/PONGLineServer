package server.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.Server;
import server.auth.bcrypt.BCrypt;
import server.sql.ResultList;
import server.sql.SQLManager;

public class Authenticator {
	
	private Server server;
	private SQLManager sql;
	
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	public Authenticator(Server server) {
		this.server = server;
		this.sql = server.getSQLManager();
	}
	
	public int login(String username, String password) {
		if (!userExists(username))
			return -1;
		
		if (server.getPlayers().isOnline(username)) {
			server.log(username+" is already logged in.");
			return 2;
		}
		
		ResultList results =
				sql.getQueryResult("SELECT * FROM users WHERE username = '"+username+"'");
		
		String hash = results.get(0).get("password").getString();
		
		if (BCrypt.checkpw(password, hash)) {
			return 1;
		}
		
		return 0;
	}
	
	public int register(String email, String username, String password, String confirm) {		
		if (userExists(username)) {
			server.log("Username already used.");
			return 0;
		}
		
		if (emailUsed(email)) {
			server.log("Email already used.");
			return 2;
		}
		
		if (!validateEmail(email)) {
			server.log("Invalid email adress.");
			return 3;
		}
		
		String[] vars = new String[] 
				{
					username,
					password,
					confirm
				};
		
		if (!validateString(vars)) {
			server.log("Invalid chars.");
			return 5;
		}
		
		if (!password.equals(confirm)) {
			server.log(password);
			server.log(confirm);
			server.log("Passwords dont match");
			return 4;
		}
		
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		
		sql.insert("users", "0, '"+username+"', '"+hashed+"', '"+email+"'");
		server.log("Successfully registered "+username+".");
		return 1;
	}
	
	public boolean userExists(String username) {
				try {
					ResultList results =
				sql.getQueryResult("SELECT * FROM users WHERE username = '"+username+"'");
					return results.size() > 0;	
				} catch(NullPointerException e) {
					return false;
				}
	}
	
	public boolean emailUsed(String email) {
		ResultList results =
				sql.getQueryResult("SELECT * FROM users WHERE email = '"+email+"'");
		
		return results.size() > 0;		
	}
	
	public boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();	
	}
	
	public boolean validateString(String[] args) {
		for(int i = 0; i < args.length; i++) {
			if (!args[i].matches("[a-zA-Z0-9]*")) {
				return false;
			}
		}
		return true;
	}

}
