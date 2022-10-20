package com.miyuki.baddapple.modules;

import java.awt.Font;
import java.awt.Image;
import java.util.HashMap;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.miyuki.baddapple.Resource;

public class ModuleResource {

	public HashMap<String, ImageIcon> LoadedIcons = new HashMap<String, ImageIcon>();
	
	public ModuleSign sign;
	
	private String loaderPath = "";
	
	public void SetCustomLoaderPath(String loaderPath) {
		this.loaderPath = loaderPath;
	}
	
	public ModuleResource(ModuleSign sign) {
		this.sign = sign;
	}
	
	public Font DeriveMainFont(int type, int size) {
		return Resource.DeriveMainFont(type, size);
	}
	
	public Font GetMainFont() {
		return Resource.mainFont;
	}
	
	public ImageIcon GetImage(String name) {
		if (name.contains("internal://")) {
			return Resource.GetImage(name);
		}
		if (LoadedIcons.containsKey(loaderPath +name)) {
			return LoadedIcons.get(loaderPath +name);
		}
	    ZipFile zf;
		try {
			zf = new ZipFile(sign.source);
		
	    	Image img = ImageIO.read(zf.getInputStream(zf.getEntry(loaderPath + name)));
	    	ImageIcon ic = new ImageIcon(img);
	    	
	    	LoadedIcons.put("name", ic);
			zf.close();
	    	return ic;
		} catch (Exception e) {
			System.err.println("Unable to open file.");
			e.printStackTrace();
		}
		return null;
	}
}
