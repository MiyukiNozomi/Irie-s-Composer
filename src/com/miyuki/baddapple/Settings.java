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
	public String theme;
	public List<String> lastWorkspaces;
	
	public static File SettingsFile = new File("settings.json");
	
	public Settings() {
		lastWorkspaces = new ArrayList<String>();
		theme = "internal://defaultTheme.json";
		
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
			writer.write("\t\"last-workspaces\": [\n");
			
			for (int i = 0; i < lastWorkspaces.size(); i++) {
				// we only want to store up to 5 workspaces.
				if (i > 4)
					break;
				writer.write("\t\t\"" + lastWorkspaces.get(i) + "\"");
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
	
	public void AddWorkspace(String f) {
		lastWorkspaces.add(0, f);
	}
}
