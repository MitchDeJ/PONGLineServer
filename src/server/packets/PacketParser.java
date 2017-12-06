package server.packets;

import java.net.InetAddress;
import java.util.ArrayList;

import server.Server;
import server.model.Player;
import server.packets.types.Packet;
import server.packets.types.Packet.PacketTypes;
import server.packets.types.Packet00RegisterStatus;
import server.packets.types.Packet01LoginStatus;
import server.packets.types.Packet02Move;

public class PacketParser {

	private Server server;

	public PacketParser(Server server) {
		this.server = server;
	}

	public void parsePacket(byte[] data, InetAddress address, int port) {

		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet;
		Player player = server.getPlayerByConnection(address, port);;
		int status;
		//server.log(message);

		switch (type) {
		
		case REGISTER:
			status = server.getAuth().register(
					parseIndex(message, 0), //email
					parseIndex(message, 1), //username
					parseIndex(message, 2), //password
					parseIndex(message, 3) //confirm password
					);
		
			if (status == 1) {
			//accepted, registered
			} else {
			//declined, there was an error
			}
			
			//tell the user about his register status
			packet = new Packet00RegisterStatus(status);
			packet.sendData(server, address, port);
			break;
			
		case LOGIN:
			status = server.getAuth().login(parseIndex(message, 0), parseIndex(message, 1));
			
			if (status == 1) {
			//accepted, entered correct details
				//adding the player to the player list
				server.getPlayers().add(new Player(
						parseIndex(message, 0), //player name
						address, //player ip address
						port //player port
						));
			} else {
			//declined, entered wrong password
			}
			
			//tell the user about his login status
			packet = new Packet01LoginStatus(status);
			packet.sendData(server, address, port);
			break;
			
		case QUIT:
			if (player == null)
				return;
			server.getPlayers().remove(player);
			break;
			
		case MOVE:
			if (player == null)
				return;
			player.setY(Integer.parseInt(parseIndex(message, 0)));
			packet = new Packet02Move(player.getY());
			packet.sendData(server, address, port);
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
