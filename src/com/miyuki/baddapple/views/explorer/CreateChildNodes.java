package com.miyuki.baddapple.views.explorer;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;

public class CreateChildNodes implements Runnable {

	private DefaultMutableTreeNode root;

	private File fileRoot;

	public CreateChildNodes(File fileRoot, DefaultMutableTreeNode root) {
		this.fileRoot = fileRoot;
		this.root = root;
	}

	public void run() {
		CreateChildren(fileRoot, root);
	}

	private void CreateChildren(File fileRoot, DefaultMutableTreeNode node) {
		File[] files = fileRoot.listFiles();
		if (files == null)
			return;
		
		Arrays.sort(files, new Comparator<File>() {
		    public int compare(File o1, File o2) {
		    	if(o1.isDirectory() && !o2.isDirectory()) {
		    		return -1;
		    	} else if(!o1.isDirectory() && o2.isDirectory()) {
		    		return 1;
		    	}
		    	
		    	return 0;
		    }
		});

		for (File file : files) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
					file);
			node.add(childNode);
			if (file.isDirectory()) {
				CreateChildren(file, childNode);
			}
		}
	}
}