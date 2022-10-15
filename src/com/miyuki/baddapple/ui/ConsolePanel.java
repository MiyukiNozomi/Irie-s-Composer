package com.miyuki.baddapple.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class ConsolePanel extends JPanel {
	private static final long serialVersionUID = 1394558923852L;
	
	public boolean minimized = true;
	public JLabel maximizeLabel;
	
	public ConsolePanel() {
		ShowMinimized();
	}
	
	public void ShowMinimized() {
		removeAll();
		maximizeLabel = new JLabel("Pull the handle upwards to reveal the built-in terminal.");
		maximizeLabel.setFont(Resource.DeriveMainFont(Font.PLAIN, 13));
		maximizeLabel.setForeground(Theme.GetColor("panel-foreground"));
		
		add(maximizeLabel);
		

		setPreferredSize(new Dimension(32,32));
	}
}
