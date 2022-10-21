package com.miyuki.baddapple.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class ConsolePanel extends JPanel {
	private static final long serialVersionUID = 1394558923852L;
	
	public static JTextPane textPane = new JTextPane();
	
	public boolean minimized = true;
	public JLabel maximizeLabel;
	public TabPanel tabPanel;
	
	public ConsolePanel() {
		setPreferredSize(new Dimension(10, 200));
		tabPanel = new TabPanel();
		setLayout(new BorderLayout(0, 0));

		textPane.setFont(Resource.DeriveMainFont(Font.PLAIN, 14));
		textPane.setForeground(Theme.GetColor("editor-foreground"));
		textPane.setBackground(Theme.GetColor("editor-background"));
		textPane.setEditable(false);
		
		textPane.setText("<html>");
		
		tabPanel.tabbedPanel.addTab(Language.GetKey("console-view-debug-title"), Resource.GetImageRecolored("internal://console.png", Theme.GetColor("tab-close-color")), UIHelper.ManufactureScroll(textPane));
		
		add(tabPanel);
	}
	
	public void ShowMinimized() {
		removeAll();
		maximizeLabel = new JLabel(Language.GetKey("console-view-minimized-title"));
		maximizeLabel.setFont(Resource.DeriveMainFont(Font.PLAIN, 13));
		maximizeLabel.setForeground(Theme.GetColor("panel-foreground"));
		
		add(maximizeLabel);
	}
}
