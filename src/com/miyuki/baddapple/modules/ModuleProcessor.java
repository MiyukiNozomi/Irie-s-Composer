package com.miyuki.baddapple.modules;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.miyuki.baddapple.Debug;

public class ModuleProcessor {
	private ModuleProcessor() {}

	private static String[] GetClasses(File f) throws FileNotFoundException,
			IOException {
		List<String> list = new ArrayList<String>();

		try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(
				new FileInputStream(f)), StandardCharsets.UTF_8)) {
			ZipEntry e;
			while ((e = zip.getNextEntry()) != null) {
				if (!e.isDirectory() && e.getName().endsWith(".class")) {
					String name = e.getName();
					String sub = name.replace("/", ".").substring(0,
							name.indexOf(".class"));
					Debug.Info("Listing class: " + sub);
					if (sub.startsWith("com.miyuki.baddapple") || sub.startsWith("assets.badapple")) {
						System.err.println("HMODULE: BadApple security deal broken; cannot load module:  " + f.getPath() + " since its using the same package name as BadApple.");
						break;
					}
					list.add(sub);
				}
			}
		}

		return list.toArray(new String[list.size()]);
	}
	
	public static ModuleSign GetModuleMainClass(File jar) throws Exception {
		String[] classes = GetClasses(jar);
		
		//if we kill the loader, The module will be unable to load more classes.
		@SuppressWarnings("resource")
		URLClassLoader loader = new URLClassLoader(new URL[]{jar.toURI().toURL()});
		
		Class<?> main_one = null;
		
		for(String str : classes){
			Class<?> clazz = loader.loadClass(str);
			
			if(clazz.getAnnotation(Character.class) != null){
				main_one = clazz;
				break;
			}
		}
		
		if(main_one == null)
			throw new IllegalClassFormatException("This is not a valid plugin: " + jar.getName() + "\n Missing main class.");
		return LoadModuleMainClass(main_one,jar);
	}
	
	private static ModuleSign LoadModuleMainClass(Class<?> target,File jar) throws Exception {
		Character sign = target.getAnnotation(Character.class);
		
		ModuleSign info = new ModuleSign();
		info.sign = sign;
		info.clazz = target;
		info.source = jar;
		info.id = target.getPackage().toString();
		
		info.module = (BadAppleModule) target.newInstance();
		
		info.module.Resource = new ModuleResource(info);
		
		return info;
	}
}
