package com.iconmaster.aec.util;

public class ModHelpers {
	public static Class getTERecipeObject(String type) {
		try {
			Class manc = Class.forName("thermalexpansion.util.crafting."+type+"Manager");
			Class[] decs = manc.getDeclaredClasses();
			Class man = null;
			for (Class c : decs) {
				//System.out.println("[AEC TE] "+c.getName());
				if (c.getName().contains("Recipe"+type)) {
					man = c;
				}
			}
			return man;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
