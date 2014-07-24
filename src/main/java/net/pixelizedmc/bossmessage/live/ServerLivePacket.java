package net.pixelizedmc.bossmessage.live;

import java.io.Serializable;

public class ServerLivePacket implements Serializable{
	
    private static final long serialVersionUID = 5914037568318221387L;
	private ServerLivePacketType dataType;
	private Object data;
	
	public ServerLivePacket(ServerLivePacketType dataType, Object data) {
		this.dataType = dataType;
		this.data = data;
	}
	
	public ServerLivePacketType getDataType() {
		return dataType;
	}
	
	public void setDataType(ServerLivePacketType dataType) {
		this.dataType = dataType;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
}
