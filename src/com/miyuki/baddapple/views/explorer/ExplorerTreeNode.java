package com.miyuki.baddapple.views.explorer;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class ExplorerTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 6963463900835273711L;

	public ExplorerTreeNode(Object userObject) {
		super(userObject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(MutableTreeNode newChild) {
		super.add(newChild);
		Collections.sort(this.children, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				ExplorerTreeNode e1 = (ExplorerTreeNode) o1;
				ExplorerTreeNode e2 = (ExplorerTreeNode) o2;
				
				Object usr1 = e1.getUserObject();
				Object usr2 = e2.getUserObject();
				
				if (usr1 instanceof File && usr2 instanceof File) {
					File file1 = (File) usr1;
					File file2 = (File) usr2;
					return file1.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
				} else 
					return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
			}
		});

		Collections.sort(this.children, new Comparator<Object>() {
			public int compare(Object of1, Object of2) {
				ExplorerTreeNode e1 = (ExplorerTreeNode) of1;
				ExplorerTreeNode e2 = (ExplorerTreeNode) of2;
				
				Object usr1 = e1.getUserObject();
				Object usr2 = e2.getUserObject();
				
				if (usr1 instanceof File && usr2 instanceof File) {
					File o1 = (File) usr1;
					File o2 = (File) usr2;
			    	if(o1.isDirectory() && !o2.isDirectory()) {
			    		return -1;
			    	} else if(!o1.isDirectory() && o2.isDirectory()) {
			    		return 1;
			    	}
				}
		    	return 0;
		    }
		});
	}
}