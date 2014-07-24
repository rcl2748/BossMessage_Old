package net.pixelizedmc.bossmessage.live;

import java.io.Serializable;

public class ClientLivePacket implements Serializable {
	
    private static final long serialVersionUID = -1945447003518503969L;
	private ClientLivePacketType dataType;
	private Object data;
	
	public ClientLivePacket(ClientLivePacketType dataType, Object data) {
		this.dataType = dataType;
		this.data = data;
	}
	
	public ClientLivePacketType getDataType() {
		return dataType;
	}
	
	public void setDataType(ClientLivePacketType dataType) {
		this.dataType = dataType;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
}
