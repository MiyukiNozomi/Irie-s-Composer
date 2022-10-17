package com.miyuki.baddapple;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.miyuki.baddapple.ui.Pixels;

public class Resource {
	
	public static Font mainFont = LoadFont("yaheiui.ttf");
	
	//TODO add support for custom fonts
	public static Font editorFont = LoadFont("consolas.ttf").deriveFont(Font.PLAIN, 16);
	
	public static HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

	public static Font DeriveMainFont(int type, int size) {
		return mainFont.deriveFont(type, size);
	}
	
	public static Font LoadFont(String name) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, GetResourceAsStream("internal://fonts/" + name));
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
				builder.append(ln+"\n");
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
			BufferedImage src;
			
			try {
				src = ImageIO.read(GetResource("icons/" + name));
			} catch(Exception e) {
				System.err.println("Error! Failed to load icon: icons/" + name);
				e.printStackTrace();
				return null;
			}
			
			//Modify image so that we can have it with a diferent color.
			
			for (int j = 0; j < src.getHeight();j++) {
				for (int i = 0; i < src.getWidth(); i++) {
					byte originalAlpha = ((byte)(src.getRGB(i, j) >>> 24));
					src.setRGB(i, j, Pixels.encodeInt(color.getRed(), color.getGreen(), color.getBlue(), originalAlpha & 0xFF));
				}	
			}
			
			ImageIcon finalIcon = new ImageIcon(src);
			icons.put(key, finalIcon);
			return finalIcon;
		}
	}
	
	public static ImageIcon Resize(ImageIcon icon, int size) {
		return new ImageIcon(icon.getImage().getScaledInstance(size, size, 30));
	}
	
	public static InputStream GetResourceAsStream(String resource) {
		if (resource.contains("internal://")) {
			System.out.println("Opening Internal File: " + resource);
			String target = resource.replace("internal://", "");
			return Resource.class.getResourceAsStream("/assets/badapple/" + target);
		} else {
			try {
				System.out.println("Opening External File: " + resource);
				return new FileInputStream(new File(resource));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static URL GetResource(String resource) {
		if (resource.contains("internal://")) {
			String target = resource.replace("internal://", "");
			return Resource.class.getResource("/assets/badapple/" + target);
		} else {
			try {
				return new URL("file://" + resource);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
