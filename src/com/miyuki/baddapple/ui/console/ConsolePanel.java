package com.miyuki.baddapple.ui.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.ui.TabPanel;

public class ConsolePanel extends JPanel {
	private static final long serialVersionUID = 1394558923852L;
	
	public boolean minimized = true;
	public JLabel maximizeLabel;
	public TabPanel tabPanel;
	
	public ConsolePanel() {
		setPreferredSize(new Dimension(10, 200));
		tabPanel = new TabPanel();
		setLayout(new BorderLayout(0, 0));
		
		tabPanel.tabbedPanel.addTab("+",  new JLabel("You're not supposed to be able to open this tab."));
		tabPanel.tabbedPanel.addTab("Console." + tabPanel.tabbedPanel.getTabCount(), new TerminalPanel());
		tabPanel.tabbedPanel.setSelectedIndex(1);
		
		tabPanel.tabbedPanel.addChangeListener(new ChangeListener() {	
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = tabPanel.tabbedPanel.getSelectedIndex();
				if (tabPanel.tabbedPanel.getTitleAt(index).matches("\\+")) {
					AddConsoleTab();
				}
			}
		});
		
		tabPanel.tabbedPanel.setSelectedIndex(0);
		
		add(tabPanel);
	}
	
	public void AddConsoleTab() {
		tabPanel.tabbedPanel.addTab("Console." + tabPanel.tabbedPanel.getTabCount(), new TerminalPanel());
		tabPanel.tabbedPanel.setSelectedIndex(tabPanel.tabbedPanel.getTabCount() - 1);
	}
	
	public void ShowMinimized() {
		removeAll();
		maximizeLabel = new JLabel(Language.GetKey("console-view-minimized-title"));
		maximizeLabel.setFont(Resource.DeriveMainFont(Font.PLAIN, 13));
		maximizeLabel.setForeground(Theme.GetColor("panel-foreground"));
		
		add(maximizeLabel);
	}
}