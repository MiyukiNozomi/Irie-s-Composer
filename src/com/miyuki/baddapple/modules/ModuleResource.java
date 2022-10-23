package com.miyuki.baddapple.modules;

import java.awt.Font;
import java.awt.Image;
import java.util.HashMap;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.miyuki.baddapple.Resource;

/**
 * Internal Module Loader
 */
public class ModuleResource {

	public HashMap<String, ImageIcon> LoadedIcons = new HashMap<String, ImageIcon>();
	
	public ModuleSign sign;
	
	private String loaderPath = "";
	
	/**
	 * Changes the loader path for your module,
	 * use this if you're storing stuff
	 * inside another package, such as
	 * 
	 * assets/yourname/
	 */
	public void SetCustomLoaderPath(String loaderPath) {
		this.loaderPath = loaderPath;
	}
	
	public ModuleResource(ModuleSign sign) {
		this.sign = sign;
	}
	
	/**
	 * Wrapper around Resource.DeriveMainFont
	 */
	public Font DeriveMainFont(int type, int size) {
		return Resource.DeriveMainFont(type, size);
	}
	
	/**
	 * Returns the font used by the Editor.
	 */
	public Font GetMainFont() {
		return Resource.mainFont;
	}
	
	/**
	 * Loads an image.
	 * if the path starts with "internal://"
	 * it will use BadApple's internal asset package
	 * instead of the custom loader Path you've specified.
	 * 
	 * By the way, this function caches every icon you load.
	 */
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
	
	/**
	 * Deletes a cached image.
	 */
	public void DeleteCache(String name) {
		LoadedIcons.remove(name);
	}
}
