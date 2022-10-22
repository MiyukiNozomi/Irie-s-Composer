package com.miyuki.baddapple;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.miyuki.baddapple.modules.ModuleHandler;
import com.miyuki.baddapple.ui.ConsolePanel;
import com.miyuki.baddapple.ui.TabPanel;
import com.miyuki.baddapple.ui.Tray;
import com.miyuki.baddapple.ui.UIHelper;
import com.miyuki.baddapple.ui.UIMenuBar;
import com.miyuki.baddapple.views.ModulesView;
import com.miyuki.baddapple.views.explorer.FileExplorerView;

public class BadApple extends JFrame {
	private static final long serialVersionUID = 1345151351515L;
	
	public static File ExecutionDir;
	
	static {
		try {
			ExecutionDir = new File(BadApple.class.getProtectionDomain().getCodeSource().getLocation()
					  .toURI());
			
			if (ExecutionDir.getName().matches("bin")) {
				System.out.println("Development environment detected!");
				ExecutionDir = ExecutionDir.getParentFile();
			}
			// running straight out from a jar file
			if (ExecutionDir.getName().endsWith(".jar")) {
				System.out.println("Running out of a JAR file");
				ExecutionDir = ExecutionDir.getParentFile();
			}
		} catch(Exception err) {
			// never happens
			err.printStackTrace();
		}
	}
	
	public static BadApple Get;
	
	public ModuleHandler handler;
	public Tray sideTray;
	public TabPanel tabPanel;
	public JSplitPane mainSplitPanel;
	public Settings settings;
	
	JPanel contentPanel;
	
	JMenuBar menuBar;
	ConsolePanel consolePanel;
	FileExplorerView fileExplorerView;
	
	BadApple(Settings settings) {
		super("BadApple Studio");
		this.settings = settings;
		BadApple.Get = this;
		
		handler = new ModuleHandler();
		handler.Initialize();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				settings.SaveSettings();
				handler.OnDisable();
			}
		});
		
		menuBar = new UIMenuBar();
		MakeMenus();
		setJMenuBar(menuBar);
		
		contentPanel = new JPanel();
		contentPanel.setBackground(Theme.GetColor("main-background"));
		contentPanel.setLayout(new BorderLayout());
		
		setContentPane(contentPanel);
		setSize(400, 400);
		
		//Resource.GetImage() sometimes doesn't works with this function
		//i'm not sure why, so i'll just do it in the "legacy" way
		setIconImage(Toolkit.getDefaultToolkit().getImage(BadApple.class.getResource("/assets/badapple/icons/icon.png")));
		
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
		contentPanel.add(sideTray, BorderLayout.WEST);
		
		DiscordPresence.Init();
	}
	
	public void MakeMenus() { 
		JMenu mnFile = new JMenu(Language.GetKey("menu-file"));
		
		JMenuItem mnOpenFolder = new JMenuItem(Language.GetKey("menu-file-open"));
		mnOpenFolder.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if (chooser.showOpenDialog(BadApple.this) == JFileChooser.APPROVE_OPTION) {
					settings.AddWorkspace(chooser.getSelectedFile().getPath());
					fileExplorerView.OnFolderOpeningRequest(chooser.getSelectedFile());
				}
			}
		});
		mnFile.add(mnOpenFolder);
		menuBar.add(mnFile);
		
		JMenu mnEdit = new JMenu(Language.GetKey("menu-edit"));
		JMenuItem mnShowWelcome = new JMenuItem(Language.GetKey("menu-edit-show-welcome"));
		mnShowWelcome.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				tabPanel.tabbedPanel.addTab(Language.GetKey("welcome-tab-title"), Resource.GetImageRecolored("internal://tray/whiteicon.png", Theme.GetColor("tab-close-color")), new WelcomePage());
			}
		});
		mnEdit.add(mnShowWelcome);
		menuBar.add(mnEdit);	
	}
	
	public static void main(String[] args) throws Exception {
		
		StandardOut.CaptureSTD();
		System.setOut(new StandardOut(System.out,"INFO"));
		System.setErr(new StandardOut(System.err,"ERROR"));
		System.out.println(ExecutionDir.getPath());
		
		Settings settings = new Settings();
		Language.LoadLanguagePack(settings.language);

		Launcher launcher = new Launcher();
		
		Theme.current = new Theme(settings.theme);
		
		Theme.LoadThemes();
		IconPack.LoadIconPacks();
		UIHelper.InstallLAF();

		BadApple badApple = new BadApple(settings);
		
		badApple.setSize(800,600);
		badApple.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		badApple.setLocationRelativeTo(null);
		badApple.tabPanel.tabbedPanel.addTab(Language.GetKey("welcome-tab-title"), Resource.GetImageRecolored("internal://tray/whiteicon.png", Theme.GetColor("tab-close-color")), new WelcomePage());
			
		badApple.handler.OnEnable();
		badApple.sideTray.AddTrayIcon(new ModulesView());
		
		if (settings.lastWorkspaces.size() > 0) {
			File f = new File(settings.lastWorkspaces.get(0));
			if (f.exists()) {
				badApple.fileExplorerView.OnFolderOpeningRequest(f);
			}
		}
		
		badApple.setVisible(true);
		launcher.setVisible(false);
	}
}
