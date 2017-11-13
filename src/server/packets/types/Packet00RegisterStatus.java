package server.packets.types;

public class Packet00RegisterStatus extends Packet {

	private int status;

	public Packet00RegisterStatus(int status) {
		super(00);
		this.status = status;
	}

	public byte[] getData() {
		String[] data = new String[] {"" + status, };
		return packetData(data).getBytes();
	}
	
}
