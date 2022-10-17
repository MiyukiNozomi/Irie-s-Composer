package com.miyuki.baddapple.modules;

import java.awt.Image;
import java.util.HashMap;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.miyuki.baddapple.Resource;

public class ModuleResource {

	public HashMap<String, ImageIcon> LoadedIcons = new HashMap<String, ImageIcon>();
	
	public ModuleSign sign;
	
	public ModuleResource(ModuleSign sign) {
		this.sign = sign;
	}
	
	public ImageIcon GetImage(String name) {
		if (name.contains("internal://")) {
			return Resource.GetImage(name);
		}
		if (LoadedIcons.containsKey(name)) {
			return LoadedIcons.get(name);
		}
	    ZipFile zf;
		try {
			zf = new ZipFile(sign.source);
		
	    	Image img = ImageIO.read(zf.getInputStream(zf.getEntry(name)));
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
