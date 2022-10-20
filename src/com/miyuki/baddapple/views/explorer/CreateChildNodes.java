package com.miyuki.baddapple.views.explorer;

import java.io.File;

public class CreateChildNodes implements Runnable {

	private ExplorerTreeNode root;

	private File fileRoot;

	public CreateChildNodes(File fileRoot, ExplorerTreeNode root) {
		this.fileRoot = fileRoot;
		this.root = root;
	}

	public void run() {
		CreateChildren(fileRoot, root);
	}

	private void CreateChildren(File fileRoot, ExplorerTreeNode node) {
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
	}
}