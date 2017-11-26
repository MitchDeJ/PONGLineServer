package server;

import java.util.ArrayList;
import java.util.List;

import server.model.Player;

public class PlayerList {

	private Server server;
	private List<Player> players = new ArrayList<Player>();
	
	public PlayerList(Server server) {
		this.server = server;
	}
	
	public void add(Player p) {
		players.add(p);
		server.log("Added "+p.getName()+" to the player list. ("+players.size()+" online)");
	}
	
	public void remove(Player p) {
		players.remove(p);
		server.log("Removed "+p.getName()+" from the player list. ("+players.size()+" online)");
	}
	
	public void clear(Player p) {
		players.clear();
	}

	public Player get(int index) {
		return players.get(index);
	}

	public int count() {
		return players.size();
	}
	
	public boolean isOnline(String name) {
		for(int i = 0; i < count(); i++) {
			if (get(i).getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
}
