package com.miyuki.baddapple.views.explorer;

import java.io.File;

import javax.swing.JTree;

public class CreateChildNodes implements Runnable {

	private ExplorerTreeNode root;
	private JTree tree;

	private File fileRoot;

	public CreateChildNodes(JTree tree, File fileRoot, ExplorerTreeNode root) {
		this.fileRoot = fileRoot;
		this.root = root;
		this.tree = tree;
	}

	public void run() {
		CreateChildren(fileRoot, root);
	}

	private void CreateChildren(File fileRoot, ExplorerTreeNode node) {
		System.out.println("Creating Children for: " + fileRoot.getPath());
		File[] files = fileRoot.listFiles();
		if (files == null)
			return;

		for (File file : files) {
			ExplorerTreeNode childNode = new ExplorerTreeNode(
					file);
			node.add(childNode);
			if (file.isDirectory()) {
				CreateChildren(file, childNode);
			}
		}
		
		tree.revalidate();
	}
}