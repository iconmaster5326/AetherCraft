package com.iconmaster.avlib;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iconmaster on 9/23/2014.
 */
public class AVLib {
	public static void testLib() {
		AVListGenerator gen = new AVListGenerator();
		HashMap<ItemData, List<RecipeData>> map = gen.getMap();
		System.out.println("[GEN TEST]");
		System.out.println(map);
		System.out.println("[/GEN TEST]");

		AVInterpreter aver = new TestInterpreter();
		System.out.println("[INTER TEST]");
		System.out.println(aver.interpret(map));
		System.out.println("[/INTER TEST]");
	}
}
