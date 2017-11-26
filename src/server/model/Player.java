package server.model;

import java.net.InetAddress;

public class Player {
	
	private String name;
	private InetAddress ip;
	private int port;
	
	public Player(String name, InetAddress ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}

	public InetAddress getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

}
