package server.packets.types;

public class Packet02Move extends Packet {

	private int y;

	public Packet02Move(int y) {
		super(02);
		this.y = y;
	}

	public byte[] getData() {
		String[] data = new String[] {"" + y, };
		return packetData(data).getBytes();
	}
	
}
