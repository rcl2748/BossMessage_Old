package net.PixelizedMC.BossMessage;

public class Utils {
	public static boolean isInteger(String s) {
    	try { 
        	Integer.parseInt(s); 
    	} catch(NumberFormatException e) { 
    		return false; 
    	}
    	return true;
    }
}
