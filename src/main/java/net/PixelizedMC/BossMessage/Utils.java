package net.PixelizedMC.BossMessage;

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
	
	public static int randInt(int min, int max)
    {
        return min + new Random().nextInt(Math.abs(max - min + 1));
    }
}
