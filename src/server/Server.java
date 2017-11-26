package server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import server.auth.Authenticator;
import server.model.Player;
import server.packets.PacketParser;
import server.packets.PacketReceiver;
import server.sql.SQLManager;

public class Server implements Runnable {
	
	public boolean running = false;
	
	private Configuration config;
	private SQLManager sqlManager;
	private Authenticator auth;
	private PlayerList players;
	
	private PacketReceiver packetReceiver;
	private PacketParser packetParser;
	
	private Thread t;

	private DatagramSocket serverSocket;
		
	public static void main(String[] args) {
		new Server().start();
	}
		
	public void init() {
		/*Loading a default config*/
		config = new Configuration();
		
		/*setting up handlers*/
		sqlManager = new SQLManager(this);
		auth = new Authenticator(this);
		
		/*init player list*/
		players = new PlayerList(this);
		
		/*attempt to open a DatagramSocket*/
		try {
			serverSocket = new DatagramSocket(config.serverPort);
		} catch (SocketException e) {
			log("Could not open the DatagramSocket (maybe there is an instance of the server still running?)");
			System.exit(0);
		}
		
		/*packet handling*/
		packetReceiver = new PacketReceiver(this);
		packetReceiver.start();
		packetParser = new PacketParser(this);
	}
	
	public synchronized void start() {
		if (running)
			return;
		
		running = true;
		t = new Thread(this);
		t.start();
	}

	public void run() {
		init();
		log("Started server. Listening for packets on port "+config.serverPort+".");
					
		/*server loop*/
		long lastTime = System.nanoTime();
		double amountOfTicks = 64.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}
			
		}
	}

	private void tick() {
		//
	}
	
	public void log(String message) {
		System.out.println("["+getConfig().serverPrefix+"] > "+message);
	}
	
	public void sendData(byte[] data, InetAddress ip, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			serverSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DatagramSocket getServerSocket() {
		return serverSocket;
	}
	
	public Configuration getConfig() {
		return config;
	}
	
	public SQLManager getSQLManager() {
		return sqlManager;
	}
	
	public Authenticator getAuth() {
		return auth;
	}

	public PacketParser getPacketParser() {
		return packetParser;
	}

	public PlayerList getPlayers() {
		return players;
	}

	public Player getPlayerByConnection(InetAddress ip, int port) {
		for (int i = 0; i < players.count(); i++) {
			if (players.get(i).getIP().toString().equalsIgnoreCase(ip.toString()) && players.get(i).getPort() == port) {
				return players.get(i);
			}
		}
		return null;
	}

}
