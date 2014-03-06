package net.pixelizedmc.bossmessage;

import java.util.Random;

public class Utils {
	public static boolean isInteger(String s) {
    	try {
        	Integer.parseInt(s); 
    	} catch(NumberFormatException e) { 
    		return false; 
    	}
    	return true;
    }
	public static boolean isBoolean(String s) {
    	if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
    		return true;
    	} else {
        	return false;
    	}
    }
	
	public static int randInt(int min, int max)
    {
        return min + new Random().nextInt(Math.abs(max - min + 1));
    }
}
