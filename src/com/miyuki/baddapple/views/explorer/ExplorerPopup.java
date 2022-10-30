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
import com.miyuki.baddapple.Debug;
import com.miyuki.baddapple.DiscordPresence;
import com.miyuki.baddapple.IconPack;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.editor.Editor;
import com.miyuki.baddapple.editor.viewers.ImageView;

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
					if (name == null)
						return;
					File newFolder = new File(target.getPath() + File.separator + name);
					if (!newFolder.mkdir()) {
						JOptionPane.showMessageDialog(BadApple.Get, Language.GetKey("new-folder-popup-fail"), name, JOptionPane.WARNING_MESSAGE);
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
					if (name == null)
						return;
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
						DiscordPresence.SetCurrentFile(f, "Editing");
						explorer.treeModel.reload(selectedNode);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(BadApple.Get, Language.GetKey("new-file-popup-fail"), name, JOptionPane.WARNING_MESSAGE);
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
					String ext = f.getName();
					ImageIcon icn = IconPack.current.fileIcon;
					if (ext.contains(".")) {
						ext = ext.substring(ext.indexOf(".") + 1);
						icn = IconPack.current.GetExtIcon(ext);
					}
					if (ext.matches("png|jpg|jpeg|bmp")) {
						ImageView view = new ImageView(f);
						BadApple.Get.tabPanel.tabbedPanel.addTab(f.getName(), icn, view);
						DiscordPresence.SetCurrentFile(f, "Viewing");
						return;
					}
					Editor editor = new Editor();
					BadApple.Get.tabPanel.tabbedPanel.addTab(f.getName(), icn, editor);
					editor.OpenFile(f);
					DiscordPresence.SetCurrentFile(f, "Editing");
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
						Debug.Info("Deleting file: " + f.getPath());
						f.delete();
					} else {
						explorer.RecursiveDelete(f);
					}
				}
			}
		});
		
		JMenuItem mntmRename = new JMenuItem(Language.GetKey("file-explorer-popup-rename"));
		mntmRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO implement me!
			}
		});
		add(mntmRename);
		
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
						JOptionPane.showMessageDialog(BadApple.Get, Language.GetKey("show-explorer-fail"), f.getName(), JOptionPane.WARNING_MESSAGE);
						System.err.println("unable to show in system explorer: ");
						errr.printStackTrace();
					}
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
	}
}
