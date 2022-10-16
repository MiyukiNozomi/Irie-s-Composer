package com.miyuki.baddapple.views.explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.ui.UIHelper;
import com.miyuki.baddapple.ui.UITree;
import com.miyuki.baddapple.views.View;

public class FileExplorerView extends View {
	public static final long serialVersionUID = 434155115142L;
	
	public JTree tree;
	public Thread thread;
	public File currentFile;
	public JScrollPane scrollPane;
	
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;
	
	public Font childFont;
	
	public FileExplorerView() {
		super("File Explorer", "tray/explorer.png");
		setPreferredSize(new Dimension(256, 0));
		 
		childFont = Resource.DeriveMainFont(Font.PLAIN, 12);
		
		tree = new JTree(new DefaultMutableTreeNode("Open a Folder :("));
		tree.setUI(new UITree());
		tree.setBorder(BorderFactory.createEmptyBorder());
		tree.setBackground(Theme.GetColor("panel-background"));
		
		tree.setCellRenderer(new TreeCellRenderer() {		
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object raw, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				JLabel label = new JLabel();
				label.setFont(childFont);
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) raw;
				Object value = node.getUserObject();
				
				if (value instanceof String) {
					label.setText(value.toString());
				} else if (value instanceof File) {
					File f = (File) value;
					
					if (f.isDirectory()) {
						label.setIcon(Resource.GetImage("folder-16x16.png"));
					} else {
						label.setIcon(Resource.GetImage("file-16x16.png"));
					}
					
					label.setText(f.getName());
				}
				
				label.setBackground(Theme.GetColor("panel-background"));
				label.setForeground(Theme.GetColor("panel-foreground"));
				
				return label;
			}
		});
		
		scrollPane = UIHelper.ManufactureScroll(tree);
		content.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void OnFolderOpeningRequest(File f) {
		if (thread != null) {
			try {
				thread.join();
			} catch(Exception e) {}
		}
		
		root = new DefaultMutableTreeNode(f);
		treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
		CreateChildNodes ccn = new CreateChildNodes(f, root);
		thread = new Thread(ccn);
		thread.start();
		
		this.currentFile = f;
	}
}
