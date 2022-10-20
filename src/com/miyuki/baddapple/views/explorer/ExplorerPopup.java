package com.miyuki.baddapple.views.explorer;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.DiscordPresence;
import com.miyuki.baddapple.IconPack;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.editor.Editor;

/**
 * Honestly, i'm not proud of this class at all.
 */
public class ExplorerPopup extends JPopupMenu {
	private static final long serialVersionUID = 1414811848L;

	public ExplorerPopup(FileExplorerView explorer) {

		JMenuItem mntmNewFolder = new JMenuItem(Language.GetKey("file-explorer-popup-newfolder"));
		mntmNewFolder.setIcon(IconPack.current.folderIcon);

		mntmNewFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File target = explorer.currentFile;
				ExplorerTreeNode selectedNode = explorer.root;

				if (explorer.tree.getSelectionPath() != null) {
					ExplorerTreeNode node = (ExplorerTreeNode) explorer.tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File f = (File) node.getUserObject();

						if (f.exists() && f.isDirectory()) {
							target = f;
							selectedNode = node;
						}
					}
				}

				if (target != null) {
					String name = JOptionPane.showInputDialog(Language.GetKey("new-folder-popup-message"));
					File newFolder = new File(target.getPath() + File.separator + name);
					if (!newFolder.mkdir()) {
						JOptionPane.showMessageDialog(BadApple.Get, Language.GetKey("new-folder-popup-fail"));
						return;
					}
					selectedNode.add(new ExplorerTreeNode(newFolder));
					explorer.treeModel.reload(selectedNode);
				}
			}
		});

		add(mntmNewFolder);

		JMenuItem menuNewFile = new JMenuItem(Language.GetKey("file-explorer-popup-newfile"));
		menuNewFile.setIcon(IconPack.current.fileIcon);
		menuNewFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File target = explorer.currentFile;
				ExplorerTreeNode selectedNode = explorer.root;

				if (explorer.tree.getSelectionPath() != null) {
					ExplorerTreeNode node = (ExplorerTreeNode) explorer.tree.getSelectionPath()
							.getLastPathComponent();

					if (node.getUserObject() instanceof File) {
						File f = (File) node.getUserObject();

						if (f.exists() && f.isDirectory()) {
							target = f;
							selectedNode = node;
						}
					}
				}

				if (target != null) {
					String name = JOptionPane.showInputDialog(Language.GetKey("new-file-popup-message"));
					try {
						Files.write(Paths.get(target.getPath() + File.separator + name), "".getBytes());
						selectedNode.add(new ExplorerTreeNode(new File(target.getPath() + File.separator + name)));
						
						File f = new File(target.getPath() + File.separator + name);
						Editor editor = new Editor();
						String ext = f.getName();
						ImageIcon icn = IconPack.current.fileIcon;
						if (ext.contains(".")) {
							ext = ext.substring(ext.indexOf(".") + 1);
							icn = IconPack.current.GetExtIcon(ext);
						}
						BadApple.Get.tabPanel.tabbedPanel.addTab(f.getName(), icn, editor);
						editor.OpenFile(f);
						DiscordPresence.SetCurrentFile(f);
						explorer.treeModel.reload(selectedNode);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(BadApple.Get, Language.GetKey("new-file-popup-fail"));
						e1.printStackTrace();
					}
				}
			}
		});
		add(menuNewFile);

		JMenuItem mntmOpen = new JMenuItem(Language.GetKey("file-explorer-popup-open"));
		mntmOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (explorer.tree.getSelectionPath() == null)
					return;
				
				ExplorerTreeNode node = (ExplorerTreeNode)explorer.tree.getSelectionPath().getLastPathComponent();
				
				if (node.getUserObject() instanceof File) {
					File f = (File) node.getUserObject();
					Editor editor = new Editor();
					String ext = f.getName();
					ImageIcon icn = IconPack.current.fileIcon;
					if (ext.contains(".")) {
						ext = ext.substring(ext.indexOf(".") + 1);
						icn = IconPack.current.GetExtIcon(ext);
					}
					BadApple.Get.tabPanel.tabbedPanel.addTab(f.getName(), icn, editor);
					editor.OpenFile(f);
					DiscordPresence.SetCurrentFile(f);
				}
			}
		});
		add(mntmOpen);


		JMenuItem mntmDelete = new JMenuItem(Language.GetKey("file-explorer-popup-delete"));
		mntmDelete.setIcon(Resource.GetImage("internal://delete.png"));
		mntmDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (explorer.tree.getSelectionPath() == null)
					return;
				
				ExplorerTreeNode node = (ExplorerTreeNode)explorer.tree.getSelectionPath().getLastPathComponent();
				
				if (node.getUserObject() instanceof File) {
					File f = (File) node.getUserObject();
				
					if (f.getPath() == explorer.currentFile.getPath())
						return;
					
					TreeNode parent = node.getParent();
					node.removeFromParent();
					explorer.treeModel.reload(parent);
					
					if (f.isFile()) {
						System.out.println("Deleting file: " + f.getPath());
						f.delete();
					} else {
						explorer.RecursiveDelete(f);
					}
				}
			}
		});
		add(mntmDelete);
		
		JMenu mnNewMenu = new JMenu(Language.GetKey("file-explorer-popup-showin"));
		add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem(Language.GetKey("file-explorer-popup-sysexplorer"));
		mntmNewMenuItem.setIcon(IconPack.current.folderIcon);
		mntmNewMenuItem.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (explorer.tree.getSelectionPath() == null)
					return;
				
				ExplorerTreeNode node = (ExplorerTreeNode)explorer.tree.getSelectionPath().getLastPathComponent();
				
				if (node.getUserObject() instanceof File) {
					File f = (File) node.getUserObject();

					try {
					if (f.isDirectory())
						Desktop.getDesktop().open(f);
					else if (f.getParentFile() != null)
						Desktop.getDesktop().open(f.getParentFile());
					} catch(Exception errr) {
						System.err.println("unable to show in system explorer: ");
						errr.printStackTrace();
					}
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
	}
}
