package com.iconmaster.aec.util;

import java.text.DecimalFormat;

public class NumberUtils {
	public static boolean isInteger(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch(NumberFormatException e) {
	        return false;
	    }
	    return true;
	}
	
	public static String display(float i) {
		//return String.format("%.2f",i);
		return new DecimalFormat("###,###.##").format(i);
	}
}
