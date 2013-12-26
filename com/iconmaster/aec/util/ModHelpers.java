package com.iconmaster.aec.util;

public class ModHelpers {
	public static Class getRecipePulverizer() {
		try {
			Class manc = Class.forName("thermalexpansion.util.crafting.PulverizerManager");
			Class[] decs = manc.getDeclaredClasses();
			Class man = null;
			for (Class c : decs) {
				//System.out.println("[AEC TE] "+c.getName());
				if (c.getName().contains("RecipePulverizer")) {
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
