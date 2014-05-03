package com.iconmaster.aec.util;

import java.text.DecimalFormat;

/**
 * A collection of utilities for dealing with number display and formatting.
 * @author iconmaster
 * @author Graknol
 *
 */
public class NumberUtils {
	/**
	 * Returns if a string is an integer value or not.
	 * @param s
	 * @return
	 */
	public static boolean isInteger(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch(NumberFormatException e) {
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Returns the GUI-friendly display value of an AV.
	 * @param i
	 * @return
	 */
	public static String display(float i) {
		//return String.format("%.2f",i);
		return new DecimalFormat("###,###.##").format(i);
	}
}
