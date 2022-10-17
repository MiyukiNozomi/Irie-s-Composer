package com.miyuki.baddapple;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import com.miyuki.baddapple.ui.ConsolePanel;
import com.miyuki.baddapple.ui.TabPanel;
import com.miyuki.baddapple.ui.Tray;
import com.miyuki.baddapple.ui.UIHelper;
import com.miyuki.baddapple.ui.UIMenuBar;
import com.miyuki.baddapple.views.ModulesView;
import com.miyuki.baddapple.views.explorer.FileExplorerView;

public class BadApple extends JFrame {
	private static final long serialVersionUID = 1345151351515L;
	
	public static BadApple Get;

	public Tray sideTray;
	public TabPanel tabPanel;
	public JSplitPane mainSplitPanel;
	
	JPanel contentPanel;
	
	JMenuBar menuBar;
	ConsolePanel consolePanel;
	FileExplorerView fileExplorerView;
	
	BadApple() {
		super("BadApple Studio");
		
		BadApple.Get = this;
		
		menuBar = new UIMenuBar();
		MakeMenus();
		setJMenuBar(menuBar);
		
		contentPanel = new JPanel();
		contentPanel.setBackground(Theme.GetColor("main-background"));
		contentPanel.setLayout(new BorderLayout());
		
		setContentPane(contentPanel);
		
		setSize(400, 400);
		
		setIconImage(Resource.GetImage("icon.png").getImage());
		
		mainSplitPanel = UIHelper.ManufactureSplit(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane editorCmdSplit = UIHelper.ManufactureSplit(JSplitPane.VERTICAL_SPLIT);
		
		tabPanel = new TabPanel();
		consolePanel = new ConsolePanel();
		
		editorCmdSplit.setLeftComponent(tabPanel);
		editorCmdSplit.setRightComponent(consolePanel);
			
		fileExplorerView = new FileExplorerView();

		mainSplitPanel.setLeftComponent(fileExplorerView);
		mainSplitPanel.setRightComponent(editorCmdSplit);
			
		contentPanel.add(mainSplitPanel, BorderLayout.CENTER);
		
		sideTray = new Tray();
		sideTray.AddTrayIcon(fileExplorerView).SetSelected(true);
		sideTray.AddTrayIcon(new ModulesView());
		contentPanel.add(sideTray, BorderLayout.WEST);
		
		DiscordPresence.Init();
	}
	
	public void MakeMenus() { 
		JMenu mnFile = new JMenu("File");
		
		JMenuItem mnOpenFolder = new JMenuItem("Open Folder");
		mnOpenFolder.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if (chooser.showOpenDialog(BadApple.this) == JFileChooser.APPROVE_OPTION) {
					fileExplorerView.OnFolderOpeningRequest(chooser.getSelectedFile());
				}
			}
		});
		mnFile.add(mnOpenFolder);
		menuBar.add(mnFile);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);	
	}
	
	public static void main(String[] args) {
		Theme.LoadThemes();
		IconPack.LoadIconPacks();
		
		UIManager.put("MenuItem.selectionBackground", Theme.GetColor("menubar-selected-background"));
		UIManager.put("MenuItem.selectionForeground", Theme.GetColor("menubar-selected-foreground"));
		UIManager.put("MenuItem.background", Theme.GetColor("menubar-background"));
		UIManager.put("MenuItem.foreground", Theme.GetColor("menubar-foreground"));
		UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder());
		
		UIManager.put("Menu.selectionBackground", Theme.GetColor("menubar-selected-background"));
		UIManager.put("Menu.selectionForeground", Theme.GetColor("menubar-selected-foreground"));
		UIManager.put("Menu.background", Theme.GetColor("menubar-background"));
		UIManager.put("Menu.foreground", Theme.GetColor("menubar-foreground"));
		UIManager.put("Menu.border", BorderFactory.createEmptyBorder());

		UIManager.put("Panel.background", Theme.GetColor("panel-background"));
		UIManager.put("Panel.foreground", Theme.GetColor("panel-foreground"));
		
		Font menuFont = Resource.DeriveMainFont(Font.PLAIN, 12);
		UIManager.put("Menu.font", menuFont);
		UIManager.put("MenuItem.font", menuFont);
		UIManager.put("PopupMenu.border", BorderFactory.createEmptyBorder());
		
		UIManager.put("Tree.paintLines", false);
		 
		UIManager.put("Tree.dropLineColor", new ColorUIResource(Theme.GetColor("panel-background")));
		UIManager.put("Tree.expandedIcon",  Resource.GetImageRecolored("internal://extended.png", Theme.GetColor("explorer-colapse-extend-button")));
		UIManager.put("Tree.collapsedIcon", Resource.GetImageRecolored("internal://colapsed.png", Theme.GetColor("explorer-colapse-extend-button")));
		
		BadApple badApple = new BadApple();
		
		badApple.setSize(800,600);
		badApple.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		badApple.setLocationRelativeTo(null);
		badApple.setVisible(true);
	}
}
