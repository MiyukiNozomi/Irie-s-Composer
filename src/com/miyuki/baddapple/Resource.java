package com.miyuki.baddapple;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class Resource {
	
	public static Font mainFont = LoadFont("yaheiui.ttf");
	
	public static HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

	public static Font DeriveMainFont(int type, int size) {
		return mainFont.deriveFont(type, size);
	}
	
	public static Font LoadFont(String name) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, GetResourceAsStream("fonts/" + name));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String GetFile(String name) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(GetResourceAsStream(name)));
		StringBuilder builder = new StringBuilder();
		
		String ln;
		try {
			while ((ln = reader.readLine()) != null) {
				builder.append(ln);
			}
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return "";
		}
	}
	
	public static ImageIcon GetImage(String name) {
		if (icons.containsKey(name)) {
			return icons.get(name);
		} else {
			ImageIcon img = new ImageIcon(GetResource("icons/" + name));
			icons.put(name, img);
			return img;
		}
	}
	
	public static ImageIcon GetImageRecolored(String name, Color color) {
		String key = name + "#" + color.getRGB();
		if (icons.containsKey(key)) {
			return icons.get(key);
		} else {
			ImageIcon src;
			if (icons.containsKey(name)) {
				src = icons.get(name);
			} else {
				src = new ImageIcon(GetResource("icons/" + name));
				icons.put(name, src);
			}
			//Modify image so that we can have it with a diferent color.
			
			//TODO modify image's color
			
			return src;
		}
	}
	
	public static ImageIcon Resize(ImageIcon icon, int size) {
		return new ImageIcon(icon.getImage().getScaledInstance(size, size, 30));
	}
	
	public static InputStream GetResourceAsStream(String resource) {
		return Resource.class.getResourceAsStream("/assets/badapple/" + resource);
	}
	
	public static URL GetResource(String resource) {
		return Resource.class.getResource("/assets/badapple/" + resource);
	}
}
