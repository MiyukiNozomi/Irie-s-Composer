package com.miyuki.baddapple.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.miyuki.baddapple.Theme;

public class TabPanel extends JPanel {
	private static final long serialVersionUID = 688529252359L;
	
	private JTabbedPane tabbedPanel;
	
	public TabPanel() {
		setLayout(new BorderLayout());
		setBackground(Theme.GetColor("main-background"));
		
		tabbedPanel = new UITabbedPane();
		tabbedPanel.setBackground(getBackground());
		tabbedPanel.setBorder(BorderFactory.createEmptyBorder());
		tabbedPanel.add("Test", new JPanel());
		add(tabbedPanel, BorderLayout.CENTER);
	}
}
