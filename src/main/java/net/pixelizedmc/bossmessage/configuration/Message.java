package net.pixelizedmc.bossmessage.configuration;

import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs(value = "Message")
public class Message implements Cloneable {
	public String Message;
	public String Percent;
	public int Show;
	public int Interval;
	
	public Message(String msg, String percent, int show, int interval) {
		this.Interval = interval;
		this.Show = show;
		this.Percent = percent;
		this.Message = msg;
	}
	
	public Message clone() {
		try {
			return (Message) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public void setMessage(String msg) {
		this.Message = msg;
	}
	
	public void setPercent(String percent) {
		this.Percent = percent;
	}
}
