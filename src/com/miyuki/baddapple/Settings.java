package com.miyuki.baddapple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Settings {

	public JSONObject rawSettings;
	public String language;
	public String theme;
	public int editorFontsize;
	public List<String> lastWorkspaces;
	
	public static File SettingsFile = new File(BadApple.ExecutionDir.getPath() + File.separator + "settings.json");
	
	public Settings() {
		lastWorkspaces = new ArrayList<String>();
		theme = "internal://tokyoNightDark.json";
		language = "english";
		editorFontsize = 14;
		
		if (!SettingsFile.exists() || !SettingsFile.isFile()) {
			SaveSettings();
			return;
		}

		try {
			rawSettings = (JSONObject) JSONValue.parseWithException(Resource.GetFile(SettingsFile.getPath()));
		} catch(Exception e) {
			System.err.println("Failed to load settings file: ");
			e.printStackTrace();
			return;
		}
		
		if (rawSettings.containsKey("theme")) {
			theme = (String)rawSettings.get("theme");
		}
		
		if (rawSettings.containsKey("editor-fontsize")) {
			editorFontsize = Integer.parseInt(rawSettings.get("editor-fontsize").toString());
			System.out.println(editorFontsize);
		}
		
		
		if (rawSettings.containsKey("language")) {
			language = (String)rawSettings.get("language");
		}
		
		if (rawSettings.containsKey("last-workspaces")) {
			JSONArray workspaces = (JSONArray) rawSettings.get("last-workspaces");
			
			for (int i = 0; i < workspaces.size(); i++) {
				lastWorkspaces.add((String) workspaces.get(i));
			}
		}
	}
	
	public void SaveSettings() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(SettingsFile));
			
			writer.write("{\n");
			writer.write("\t\"theme\":\"" + theme + "\",\n");
			writer.write("\t\"language\":\"" + language + "\",\n");
			writer.write("\t\"editor-fontsize\":\"" + editorFontsize + "\",\n");
			writer.write("\t\"last-workspaces\": [\n");
			
			for (int i = 0; i < lastWorkspaces.size(); i++) {
				// we only want to store up to 5 workspaces.
				if (i > 4)
					break;
				writer.write("\t\t\"" + CleanupPath(lastWorkspaces.get(i)) + "\"");
				if (i + 1 < lastWorkspaces.size()) {
					writer.write(",");
				}
				writer.write("\n");
			}
			
			writer.write("\t]\n");
			writer.write("}");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("Unable to save settings.");
			e.printStackTrace();
		}
	}
	
	String CleanupPath(String k) {
		String f = k;
		while (f.contains("\\"))
			f = f.replace("\\", "/");
		return f;
	}
	
	public void AddWorkspace(String f) {
		if (!lastWorkspaces.contains(f)) {
			lastWorkspaces.add(0, f);
		} else {
			lastWorkspaces.remove(f);
			lastWorkspaces.add(0, f);
		}
	}
}
