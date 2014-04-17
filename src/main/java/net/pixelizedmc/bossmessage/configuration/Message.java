package net.pixelizedmc.bossmessage.configuration;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Message")
public class Message implements Cloneable, ConfigurationSerializable {
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
