package com.miyuki.baddapple.ui.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class ConsolePanel extends JPanel {
	private static final long serialVersionUID = 1394558923852L;
	
	public boolean minimized = true;
	public JLabel maximizeLabel;
	public ConsoleTabbedPane tabPanel;
	
	public ConsolePanel() {
		setPreferredSize(new Dimension(10, 200));
		tabPanel = new ConsoleTabbedPane();
		setLayout(new BorderLayout(0, 0));

		JLabel errorLabel = new JLabel("You're not supposed to be able to open this tab.");
		errorLabel.setHorizontalAlignment(JLabel.CENTER);
		tabPanel.addTab("+",  errorLabel);
		tabPanel.setTabComponentAt(0, new AddConsoleView());

		AddConsoleTab();
		
		/* how to make a memory leak in java:
		 * I had to comment out this because for some reason
		 * this generates a memory leak when closing a tab from the ConsoleTabView class.
		 * 
		tabPanel.addChangeListener(new ChangeListener() {	
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = tabPanel.getSelectedIndex();
				System.out.println("New Tab");
				if (tabPanel.getTitleAt(index).matches("\\+")) {
					AddConsoleTab();
				}
			}
		});*/
		
		tabPanel.setSelectedIndex(0);
		
		add(tabPanel);
	}
	
	public void AddConsoleTab() {
		int tabCount = tabPanel.getTabCount();
		tabPanel.insertTab("Console." + tabPanel.getTabCount(), Resource.GetImageRecolored("internal://console.png", Theme.GetColor("explorer-icons")), new TerminalPanel(), "Console", tabCount - 1);
	}
	
	public void ShowMinimized() {
		removeAll();
		maximizeLabel = new JLabel(Language.GetKey("console-view-minimized-title"));
		maximizeLabel.setFont(Resource.DeriveMainFont(Font.PLAIN, 13));
		maximizeLabel.setForeground(Theme.GetColor("panel-foreground"));
		
		add(maximizeLabel);
	}
}