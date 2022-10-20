package com.miyuki.baddapple;

import java.util.HashMap;

import com.miyuki.baddapple.editor.HighlightEngine;

public class Registry {

	public static HashMap<String, HighlightEngine> highlightingEngines = new HashMap<String, HighlightEngine>();
	
	public static void RegisterEngine(String ext, HighlightEngine engine) {
		if (highlightingEngines.containsKey(ext))
			throw new Error("Already Registered Highlighting Engine for ext " + ext + " already registered candidate is: "
					+ highlightingEngines.get(ext).getClass().getTypeName());
		highlightingEngines.put(ext,  engine);
		System.out.println("Engine for ext '" + ext+ "' registered.");
	}
	
	public static HighlightEngine GetEngineFor(String ext) {
		if (highlightingEngines.containsKey(ext))
			return highlightingEngines.get(ext);
		return null;
	}
}
