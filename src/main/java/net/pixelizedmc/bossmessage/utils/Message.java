package net.pixelizedmc.bossmessage.utils;

public class Message implements Cloneable {
	public String msg;
	public String percent;
	public int show;
	public int interval;
	public boolean calcpct;
	
	public Message(String msg, String percent, int show, int interval, boolean calcpct) {
		this.calcpct = calcpct;
		this.interval = interval;
		this.show = show;
		this.percent = percent;
		this.msg = msg;
	}
	
	public Message clone() {
		try {
			return (Message) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public void setMessage(String msg) {
		this.msg = msg;
	}
	
	public void setPercent(String percent) {
		this.percent = percent;
	}
}
