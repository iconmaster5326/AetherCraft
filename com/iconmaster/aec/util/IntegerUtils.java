package com.iconmaster.aec.util;

public class IntegerUtils {
	public static boolean isInteger(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch(NumberFormatException e) {
	        return false;
	    }
	    return true;
	}
}
