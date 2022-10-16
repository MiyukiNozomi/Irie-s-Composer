package com.miyuki.baddapple;

import java.awt.Color;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Theme {
	
	public static Theme current;
	public static HashMap<String, Theme> loadedThemes;
	
	public HashMap<String, Color> colors;

	public Theme(String filename) {
		this(Resource.GetFile(filename), filename);
	}
	
	public Theme(String rawFile, String filename) {
		colors = new HashMap<String, Color>();
		JSONObject rawObject;
		try {
			rawObject = (JSONObject) JSONValue.parseWithException(rawFile);
		} catch(Exception e) {
			System.err.println("Failed to load theme file: " + filename);
			e.printStackTrace();
			return;
		}
		
		if (!rawObject.containsKey("general")) {
			throw new RuntimeErrorException(
					new Error("Malformated Theme File '" + filename + "' missing 'general' key."));
		}

		JSONObject generalColors = (JSONObject) rawObject.get("general");
		
		for (String k : RequiredGeneralColorKeys) {
			if (!generalColors.containsKey(k)) {
				throw new RuntimeErrorException(new Error("Error! Missing key: '" + k+ "' on theme file: " + filename));
			}
			
			String c = (String)generalColors.get(k);
			
			colors.put(k, Color.decode(c.startsWith("#") ? c : "#" + c));
		}
	}
	
	public static Color GetColor(String key) {
		if (Theme.current.colors.containsKey(key))
			return Theme.current.colors.get(key);
		else
			return Color.magenta;
	}
	
	public static void LoadThemes() {
		loadedThemes = new HashMap<String, Theme>();
		
		loadedThemes.put("default", new Theme("defaultTheme.json"));
		
		//TODO save user's current theme.
		current = loadedThemes.get("default");
	}
	
	public static final String[] RequiredGeneralColorKeys = {
		"main-foreground",
		"main-background",

		"panel-foreground",
		"panel-background",
		"panel-border",
		
		"menubar-background",
		"menubar-border",
		"menubar-foreground",
		
		"menubar-selected-background",
		"menubar-selected-foreground",
		
		"tray-icon-foreground",
		
		"view-title-background",
		"view-title-foreground",

		"tab-selected-background",
		"tab-selected-foreground",
		"tab-selected-border",
		"tab-border",
		"tab-foreground",
		"tab-background",
		"tab-close-color",

		"tray-foreground",
		"tray-background",
		"tray-selected",
		"tray-border",
		
		"editor-foreground",
		"editor-background",
		"editor-caret",
		
		"editor-current-line",
		
		"line-numbers-background",
		"line-numbers-foreground",
		"line-numbers-current",
		
		"scroller-background",
		"scroller-foreground",
	};
}