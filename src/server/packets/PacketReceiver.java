package server.packets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import server.Server;

public class PacketReceiver extends Thread {
	
	private Server server;
	private DatagramSocket socket;

	public PacketReceiver(Server server) {
		this.server = server;
		this.socket = server.getServerSocket();
	}
	
	public void receive() {
       	byte[] data = new byte[1024];
    	DatagramPacket packet = new DatagramPacket(data, data.length);
    	try {
    		socket.receive(packet);
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	server.getPacketParser().parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
	}
	
	public void run() {
		while (true) {
			receive();
		}
	}

}