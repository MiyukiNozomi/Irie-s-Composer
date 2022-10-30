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
import com.miyuki.baddapple.ui.TabPanel;
import com.miyuki.baddapple.ui.Tray;
import com.miyuki.baddapple.ui.UIHelper;
import com.miyuki.baddapple.ui.UIMenuBar;
import com.miyuki.baddapple.ui.console.ConsolePanel;
import com.miyuki.baddapple.views.ModulesView;
import com.miyuki.baddapple.views.explorer.FileExplorerView;

public class BadApple extends JFrame {
	private static final long serialVersionUID = 1345151351515L;
	
	public static File ExecutionDir;
	/**Actually this should be called "Debug-Build"*/
	public static boolean DevelopmentEnvironment;
	
	public static BadApplePlatform Platform;
	
	public static enum BadApplePlatform {
		Windows, Linux
	}
	
	static {
		try {
			//Grabs the execution directory
			ExecutionDir = new File(BadApple.class.getProtectionDomain().getCodeSource().getLocation()
					  .toURI());
			
			// If we're on the bin directory, it knows that its running within eclipse.
			if (ExecutionDir.getName().matches("bin")) {
				Debug.Info("Development environment detected!");
				DevelopmentEnvironment = true;
				ExecutionDir = ExecutionDir.getParentFile();
			}
			// otherwise, we're
			// running straight out from a jar file
			if (ExecutionDir.getName().endsWith(".jar")) {
				Debug.Info("Running out of a JAR file");
				ExecutionDir = ExecutionDir.getParentFile();
			}
			Debug.Info("Execution Dir is : " + ExecutionDir);
			if (System.getProperty("os.name").contains("Windows")) {
				Platform = BadApplePlatform.Windows;
			}
			Debug.Info("Running on " + Platform);
		} catch(Exception err) {
			// never happens
			err.printStackTrace();
		}
	}
	
	//TODO disallow assigns from whitin modules
	public static BadApple Get;
	
	public ModuleHandler handler;
	public Tray sideTray;
	public TabPanel tabPanel;
	public JSplitPane mainSplitPanel;
	public Settings settings;
	
	public JPanel contentPanel;
	
	public JMenuBar menuBar;
	public ConsolePanel consolePanel;
	public FileExplorerView fileExplorerView;
	
	BadApple(Settings settings) {
		super("BadApple Studio");
		this.settings = settings;
		
		//Sets the global handle
		BadApple.Get = this;
		// initializes the module handler,
		// already loads every module
		handler = new ModuleHandler();
		handler.Initialize();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// we want to save settings AND send a disable signal to every
				// module.
				settings.SaveSettings();
				handler.OnDisable();
			}
		});
		
		menuBar = new UIMenuBar();
		MakeMenus();
		setJMenuBar(menuBar);
		
		contentPanel = new JPanel();
		// honestly, i shouldn't really set this panel's background
		// since its not really visible
		contentPanel.setBackground(Theme.GetColor("main-background"));
		contentPanel.setLayout(new BorderLayout());
		
		setContentPane(contentPanel);
		setSize(400, 400);
		
		//Resource.GetImage() sometimes doesn't works with this function
		//i'm not sure why, so i'll just do it in the "legacy" way
		setIconImage(Toolkit.getDefaultToolkit().getImage(BadApple.class.getResource("/assets/badapple/icons/icon.png")));
		
		//I should probably replace the UIHelper manufacture functions with an actual LookAndFeel.
		// but i wasn't really able to do it without
		// losing the icons on the buttons of the scroll bars.
		// you can see the problem when you open a File Dialog.
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
				// honestly, i don't really like to use JFileChooser,
				// AWT's FileDialog seems much better, but it doesn't allows 
				// you to specifically load directories so. i'll have to use this one
				// instead.
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
		
		// fun fact: this option only exists to test
		// the welcome screen with the JVM's Hot Replacement
		// also known as editing the program while its running
		// that's why i love java, you can just say "screw it
		// i'm just going to change everything without rerunning the program"
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
	
	public static void main(String[] args) {
		Launcher launcher = null;
		try {
			Debug.CaptureSTD();
			// TODO i should probably block this function
			// from being used within modules
			System.setOut(new StandardOut(System.out));
			System.setErr(new ErrorSTD(System.err));
			
			// i'm already printing this out earlier
			// Debug.Info(ExecutionDir.getPath());
			
			// Initialize everything based off user settings
			Settings settings = new Settings();
			
			Resource.Initialize(settings);
			
			Language.LoadLanguagePack(settings.language);
			
			launcher = new Launcher();
			
			Theme.current = new Theme(settings.theme);
			// this looks cursed doesn't it?
			// we don't need to look for other themes if the one we're going
			// to use is already saved within the settings class.
			
			// TODO only search for themes when opening the settings page
			Theme.LoadThemes();
			
			// icon packs are a old functionality of this program
			// i never really finished them,
			// but i should go back to them
			IconPack.LoadIconPacks();
			
			UIHelper.InstallLAF();
	
			BadApple badApple = new BadApple(settings);
			
			// i am only setting up the actual window here
			// because of eclipse's WindowBuilder acting weird
			// when the thing you're editing is bigger than the viewport.
			badApple.setSize(800,600);
			badApple.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			badApple.setLocationRelativeTo(null);
			badApple.tabPanel.tabbedPanel.addTab(Language.GetKey("welcome-tab-title"), Resource.GetImageRecolored("internal://tray/whiteicon.png", Theme.GetColor("tab-close-color")), new WelcomePage());
				
			badApple.handler.OnEnable();
			badApple.sideTray.AddTrayIcon(new ModulesView());
			
			// yeah, we don't need to grab the last element of the lastWorkspaces array,
			// its because settings already """sorts it""" when an element is added
			if (settings.lastWorkspaces.size() > 0) {
				File f = new File(settings.lastWorkspaces.get(0));
				if (f.exists()) {
					badApple.fileExplorerView.OnFolderOpeningRequest(f);
				}
			}
			
			// hide the launcher, show main window
			badApple.setVisible(true);
			Debug.StopCapture();
			launcher.setVisible(false);
		} catch(Exception err) {
			Debug.HadErrors = true;
			err.printStackTrace();
			System.err.println("Bad Apple has Crashed!");
			launcher.Render();
		}
	}
}
