package net.pixelizedmc.bossmessage.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("ConfigurationMessage")
public class Message implements Cloneable, ConfigurationSerializable {
	private String Message;
	private String Percent;
	private int Show;
	private int Interval;
	
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
	
	public String getMessage() {
		return Message;
	}
	
	public void setMessage(String message) {
		Message = message;
	}
	
	public String getPercent() {
		return Percent;
	}
	
	public void setPercent(String percent) {
		Percent = percent;
	}
	
	public int getShow() {
		return Show;
	}
	
	public void setShow(int show) {
		Show = show;
	}
	
	public int getInterval() {
		return Interval;
	}
	
	public void setInterval(int interval) {
		Interval = interval;
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> output = new HashMap<String, Object>();
		output.put("Message", Message);
		output.put("Percent", Percent);
		output.put("Show", Show);
		output.put("Interval", Interval);
		return output;
	}
	
	public static Message deserialize(Map<String, Object> serial) {
		return new Message(serial.get("Message").toString(), serial.get("Percent").toString(), Integer.parseInt(serial.get("Show").toString()), Integer.parseInt(serial.get("Interval").toString()));
	}
}
