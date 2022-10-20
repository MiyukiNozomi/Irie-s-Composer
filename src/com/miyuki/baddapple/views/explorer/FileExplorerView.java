package com.miyuki.baddapple.views.explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.DiscordPresence;
import com.miyuki.baddapple.IconPack;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.editor.Editor;
import com.miyuki.baddapple.ui.UIHelper;
import com.miyuki.baddapple.ui.UITree;
import com.miyuki.baddapple.views.View;

public class FileExplorerView extends View {
	public static final long serialVersionUID = 434155115142L;

	public JTree tree;
	public Thread thread;
	public File currentFile;
	public JScrollPane scrollPane;

	public DefaultTreeModel treeModel;
	public ExplorerTreeNode root;

	public Font childFont;

	public FileExplorerView() {
		super(Language.GetKey("file-explorer-title"), "internal://tray/explorer.png");
		setPreferredSize(new Dimension(256, 0));

		childFont = Resource.DeriveMainFont(Font.PLAIN, 13);

		tree = new JTree(new ExplorerTreeNode(Language.GetKey("file-explorer-none-opened")));
		tree.setUI(new UITree());
		tree.setBorder(BorderFactory.createEmptyBorder());
		tree.setBackground(Theme.GetColor("panel-background"));

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					new ExplorerPopup(FileExplorerView.this).show(tree, e.getX(), e.getY());
					;
				}
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					TreePath scl = tree.getSelectionPath();
					if (scl == null)
						return;

					ExplorerTreeNode node = (ExplorerTreeNode) scl.getLastPathComponent();
					Object obj = node.getUserObject();

					if (!(obj instanceof File)) {
						return;
					}
					File f = (File) obj;

					if (!f.exists() || f.isDirectory()) {
						return;
					}

					Editor editor = new Editor();
					Icon icn;
					String ext = f.getName();
					if (ext.contains(".")) {
						ext = ext.substring(ext.indexOf(".") + 1);
						icn = IconPack.current.GetExtIcon(ext);
					} else {
						icn = IconPack.current.fileIcon;
					}
					BadApple.Get.tabPanel.tabbedPanel.addTab(f.getName(), icn, editor);
					editor.OpenFile(f);
					DiscordPresence.SetCurrentFile(f);
				}
			}
		});

		tree.setCellRenderer(new TreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object raw, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				JLabel label = new JLabel();
				label.setOpaque(true);
				label.setFont(childFont);

				ExplorerTreeNode node = (ExplorerTreeNode) raw;
				Object value = node.getUserObject();

				if (value instanceof String) {
					label.setText(value.toString());
				} else if (value instanceof File) {
					File f = (File) value;

					if (f.isDirectory()) {
						label.setIcon(IconPack.current.folderIcon);
					} else {
						String ext = f.getName();
						if (ext.contains(".")) {
							ext = ext.substring(ext.indexOf(".") + 1);
							label.setIcon(IconPack.current.GetExtIcon(ext));
						} else {
							label.setIcon(IconPack.current.fileIcon);
						}
					}

					label.setText(f.getName());
				}

				if (selected) {
					label.setBackground(Theme.GetColor("explorer-selected-background"));
					label.setForeground(Theme.GetColor("explorer-selected-foreground"));
				} else {
					label.setBackground(Theme.GetColor("panel-background"));
					label.setForeground(Theme.GetColor("panel-foreground"));
				}

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
			} catch (Exception e) {
			}
		}

		root = new ExplorerTreeNode(f);
		treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
		CreateChildNodes ccn = new CreateChildNodes(f, root);
		thread = new Thread(ccn);
		thread.start();

		this.currentFile = f;
	}

	public void RecursiveDelete(File targetDirectory) {
		System.out.println("Deleting: " + targetDirectory.getPath());
        File[] data = targetDirectory.listFiles();

        for (File file : data) {
            if(file.isDirectory())
            	RecursiveDelete(file);

            else
                file.delete();
        }

        targetDirectory.delete();
    }
}
