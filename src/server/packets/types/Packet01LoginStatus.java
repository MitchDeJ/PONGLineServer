package server.packets.types;

public class Packet01LoginStatus extends Packet {

	private int status;

	public Packet01LoginStatus(int status) {
		super(01);
		this.status = status;
	}

	public byte[] getData() {
		String[] data = new String[] {"" + status, };
		return packetData(data).getBytes();
	}
	
}
