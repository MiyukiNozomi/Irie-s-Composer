package com.miyuki.baddapple.modules;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.miyuki.baddapple.BadApple;

public class ModuleHandler {
	
	public static final File ModuleFolder = new File(BadApple.ExecutionDir.getPath() + File.separator + "mods");
	public List<ModuleSign> LoadedModules = new ArrayList<ModuleSign>();
	
	public void Initialize() {
		try {
			if (!ModuleFolder.exists() || !ModuleFolder.isDirectory()) {
				ModuleFolder.mkdir();
				System.out.println("HMODULE: No plugins to load.");
				return;
			}
			
			File[] jars = ModuleFolder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().toLowerCase().endsWith(".jar");
				}
			});
			
			if (jars == null || jars.length == 0) {
				System.out.println("HMODULE: No jars to load");
				return;
			}
			
			for (File f : jars) {
				ModuleSign info = ModuleProcessor.GetModuleMainClass(f);
				
				boolean isDuplicate = false;
				//Checking for duplicates
				for (ModuleSign il : LoadedModules) {
					if (il.clazz.equals(info.clazz)) {
						System.err.println("Found a duplicated module at paths:\n\t" + f.getPath() + "\n\t" + il.source.getPath());
						isDuplicate = true;
						break;
					}
				}
				
				if (!isDuplicate) {
					LoadedModules.add(info);
					System.out.println("Loaded Module: " + info.sign.name());
				}
			}
		} catch(Exception ee) {
			System.err.println("HMODULE: Unable to Initialize ModuleHandler!");
			ee.printStackTrace();
		}
	}
	
	public void OnEnable() {
		for(ModuleSign ps : LoadedModules) {
			ps.icon = ps.module.Resource.GetImage(ps.sign.iconPath());
			ps.module.onAwake();
		}
	}
	
	public void OnDisable() {
		for(ModuleSign ps :LoadedModules) {
			ps.module.onQuit();
		}
	}
}
