package com.miyuki.baddapple;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class Language {

	public static HashMap<String, String> avaliableLanguagePacks = new HashMap<String, String>();
	
	static {
		AddLanguagePack("english");
		AddLanguagePack("nihongo");
	}
	
	public static Language current;
	
	private static void AddLanguagePack(String langPackName) {
		JSONObject rawObject;
		try {
			rawObject = (JSONObject) JSONValue.parseWithException(Resource.GetFile("internal://lang/" + langPackName + ".json"));
			avaliableLanguagePacks.put(langPackName, (String)rawObject.get("lang-name"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void LoadLanguagePack(String languagePackName) {
		try {
			JSONObject rawObject = (JSONObject) JSONValue.parseWithException(Resource.GetFile("internal://lang/" + languagePackName + ".json"));
			@SuppressWarnings("unchecked")
			Set<Object> keys = rawObject.keySet();
			
			Language l = new Language((String)rawObject.get("lang-name"));
			
			for (Object p : keys) {
				if (p != "lang-name")
					l.text.put(p.toString(), rawObject.get(p).toString());
			}
			
			current = l;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	private Language(String name) { this.name = name;}
	
	public final String name;
	private Map<String, String> text = new HashMap<String, String>();
	
	public static String GetKey(String k) {
		if (current.text.containsKey(k))
			return current.text.get(k);
		return "<missing in langfile>";
	}
}