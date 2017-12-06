package server.model;

import java.net.InetAddress;

public class Player {
	
	private String name;
	private InetAddress ip;
	private int port;
	private int y = 0;
	
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

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

}
