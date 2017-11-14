package server.packets;

import java.net.InetAddress;
import java.util.ArrayList;


import server.Server;
import server.packets.types.Packet;
import server.packets.types.Packet.PacketTypes;

public class PacketParser {

	private Server server;

	public PacketParser(Server server) {
		this.server = server;
	}

	public void parsePacket(byte[] data, InetAddress address, int port) {

		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet;
		server.log(message);

		switch (type) {
		
		case REGISTER:
			server.getAuth().register(
					parseIndex(message, 0), 
					parseIndex(message, 1), 
					parseIndex(message, 2) 
					);
			break;
			
		case LOGIN:
			if (server.getAuth().login(
					parseIndex(message, 0), 
					parseIndex(message, 1)
					)) {
				//accepted, entered correct details
			} else {
				//declined, entered wrong password
			}
			break;
		
		case INVALID:
			server.log("Received an invalid packet!");
			break;
			
		default:
			server.log("Received unhandled packet!");
			break;

		}

	}

	private String parseIndex(String message, int index) {

		ArrayList<Integer> list = new ArrayList<Integer>();
		char divider = ';';
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == divider) {
				list.add(i);
			}
		}
		int[] div = new int[list.size()];

		for (int i = 0; i < div.length; i++) {
			div[i] = list.get(i).intValue();
		}

		return message.substring(div[index] + 1, div[index + 1]);
	}
}
