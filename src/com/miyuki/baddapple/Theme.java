package com.miyuki.baddapple;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Theme {

	public static Theme current = new Theme();
	public static List<String> loadedThemes = new ArrayList<String>();
	public static File themeFolder = new File("themes");

	public HashMap<String, Color> colors;
	public HashMap<String, HashMap<String, Style>> editorColors;
	public String name;

	public static Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

	public static final StyleContext context = StyleContext.getDefaultStyleContext();

	public Theme() {
		colors = new HashMap<String, Color>();
	}

	public Theme(String filename) {
		this(Resource.GetFile(filename.startsWith("internal://") ? filename : "themes/" + filename), filename);
	}

	public Theme(String rawFile, String filename) {
		colors = new HashMap<String, Color>();
		editorColors = new HashMap<>();
		this.name = filename;
		JSONObject rawObject;
		try {
			rawObject = (JSONObject) JSONValue.parseWithException(rawFile);
		} catch (Exception e) {
			System.err.println("Failed to load theme file: " + name);
			e.printStackTrace();
			return;
		}

		if (!rawObject.containsKey("general")) {
			throw new RuntimeErrorException(
					new Error("Malformated Theme File '" + filename + "' missing 'general' key."));
		}
		if (!rawObject.containsKey("editor")) {
			throw new RuntimeErrorException(
					new Error("Malformated Theme File '" + filename + "' missing 'editor' key."));
		}

		JSONObject generalColors = (JSONObject) rawObject.get("general");

		for (String k : RequiredGeneralColorKeys) {
			if (!generalColors.containsKey(k)) {
				throw new RuntimeErrorException(
						new Error("Error! Missing key: '" + k + "' on theme file: " + filename));
			}

			String c = (String) generalColors.get(k);

			colors.put(k, Color.decode(c.startsWith("#") ? c : "#" + c));
		}

		JSONObject arr = (JSONObject) rawObject.get("editor");

		if (!arr.containsKey("default")) {
			throw new RuntimeErrorException(
					new Error("Malformated Theme File '" + filename + "' missing 'editor.default' key."));
		}

		for (Object ark : arr.keySet()) {
			JSONObject tg = (JSONObject) arr.get(ark);
			editorColors.put((String) ark, new HashMap<>());
			for (String k : RequiredDefaultEditorKeys) {
				if (!tg.containsKey(k)) {
					throw new RuntimeErrorException(
							new Error("Error! Missing key: '" + ark + "." + k + "' on theme file: " + filename));
				}

				String c = (String) tg.get(k);
				Color color = Color.decode(c.startsWith("#") ? c : "#" + c);

				Style style = context.addStyle(k, null);
				StyleConstants.setForeground(style, color);
				editorColors.get(ark.toString()).put(k, style);
			}
		}
	}

	public static AttributeSet GetEditorStyle(String style) {
		String key = style.substring(0, style.indexOf("."));
		String color = style.substring(style.indexOf(".") + 1);

		if (!current.editorColors.containsKey(key)) {
			key = "default";
		}

		HashMap<String, Style> colors = current.editorColors.get(key);

		if (colors.containsKey(color))
			return colors.get(color);
		return defaultStyle;
	}

	public static Color GetColor(String key) {
		if (Theme.current.colors.containsKey(key))
			return Theme.current.colors.get(key);
		else
			return Color.magenta;
	}

	public static void LoadThemes() {
		if (!themeFolder.exists() || !themeFolder.isDirectory()) {
			themeFolder.mkdir();
		}

		File[] files = themeFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".json");
			}
		});

		loadedThemes.add("internal://tokyoNightDark.json");
		for (File f : files) {
			loadedThemes.add(f.getName());
		}

		System.out.println("Loaded " + loadedThemes.size() + " Themes.");
	}

	public static final String[] RequiredDefaultEditorKeys = { "keyword", "access-modifier", "datatype", "symbols",
			"block-symbols", "import-path", "numbers", "quotes", "comments", "function" };

	public static final String[] RequiredGeneralColorKeys = { "main-foreground", "main-background",

			"panel-foreground", "panel-background", "panel-border", "panel-selection",

			"menubar-background", "menubar-border", "menubar-foreground",

			"menubar-selected-background", "menubar-selected-foreground",

			"tray-icon-foreground",

			"view-title-background", "view-title-foreground",

			"tab-selected-background", "tab-selected-foreground", "tab-selected-border", "tab-border", "tab-foreground",
			"tab-background", "tab-close-color",

			"tray-foreground", "tray-background", "tray-selected", "tray-border",

			"editor-foreground", "editor-background", "editor-caret", "editor-selection",

			"editor-current-line",

			"line-numbers-background", "line-numbers-foreground", "line-numbers-current",

			"scroller-background", "scroller-foreground",

			"explorer-selected-foreground", "explorer-selected-background", "explorer-colapse-extend-button",
			"error-icon", "warn-icon", "info-icon","file-chooser-icons"};
}