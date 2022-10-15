package com.miyuki.baddapple;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import com.miyuki.baddapple.ui.ConsolePanel;
import com.miyuki.baddapple.ui.TabPanel;
import com.miyuki.baddapple.ui.Tray;
import com.miyuki.baddapple.ui.UIHelper;
import com.miyuki.baddapple.ui.UIMenuBar;
import com.miyuki.baddapple.views.FileExplorerView;

public class BadApple extends JFrame {
	private static final long serialVersionUID = 1345151351515L;
	
	JPanel contentPanel;
	
	Tray sideTray;
	TabPanel tabPanel;
	ConsolePanel consolePanel;
	FileExplorerView fileExplorerView;
	
	BadApple() {
		super("BadApple Studio");
		
		JMenuBar menuBar = new UIMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
	
		contentPanel = new JPanel();
		contentPanel.setBackground(Theme.GetColor("main-background"));
		contentPanel.setLayout(new BorderLayout());
		
		setContentPane(contentPanel);
		
		setSize(400, 400);
		
		setIconImage(Resource.GetImage("icon.png").getImage());
		
		JSplitPane splitPane = UIHelper.ManufactureSplit(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane editorCmdSplit = UIHelper.ManufactureSplit(JSplitPane.VERTICAL_SPLIT);
		
		tabPanel = new TabPanel();
		consolePanel = new ConsolePanel();
		
		editorCmdSplit.setLeftComponent(tabPanel);
		editorCmdSplit.setRightComponent(consolePanel);
			
		fileExplorerView = new FileExplorerView();

		splitPane.setLeftComponent(fileExplorerView);
		splitPane.setRightComponent(editorCmdSplit);
			
		contentPanel.add(splitPane, BorderLayout.CENTER);
		
		sideTray = new Tray();
		sideTray.AddTrayIcon(fileExplorerView.trayIcon, fileExplorerView);
		contentPanel.add(sideTray, BorderLayout.WEST);
		
		DiscordPresence.Init();
	}
	
	public static void main(String[] args) {
		Theme.LoadThemes();
		
		UIManager.put("MenuItem.selectionBackground", Theme.GetColor("menubar-selected-background"));
		UIManager.put("MenuItem.selectionForeground", Theme.GetColor("menubar-selected-foreground"));
		UIManager.put("MenuItem.background", Theme.GetColor("menubar-background"));
		UIManager.put("MenuItem.foreground", Theme.GetColor("menubar-foreground"));
		
		UIManager.put("Menu.selectionBackground", Theme.GetColor("menubar-selected-background"));
		UIManager.put("Menu.selectionForeground", Theme.GetColor("menubar-selected-foreground"));
		UIManager.put("Menu.background", Theme.GetColor("menubar-background"));
		UIManager.put("Menu.foreground", Theme.GetColor("menubar-foreground"));

		UIManager.put("Panel.background", Theme.GetColor("panel-background"));
		UIManager.put("Panel.foreground", Theme.GetColor("panel-foreground"));
		
		Font menuFont = Resource.DeriveMainFont(Font.PLAIN, 12);
		UIManager.put("Menu.font", menuFont);
		UIManager.put("MenuItem.font", menuFont);
		/*UIManager.put("PopupMenu.border",
				new LineBorder(Color.decode("#1e1e1e")));*/
		
		BadApple badApple = new BadApple();
		badApple.setSize(800,600);
		badApple.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		badApple.setLocationRelativeTo(null);
		badApple.setVisible(true);
	}
}
