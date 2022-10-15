package com.miyuki.baddapple.ui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.views.View;

public class Tray extends JPanel {
	private static final long serialVersionUID = 699481845L;
	
	public Border selectedBorder;
	
	public Tray() {
		selectedBorder = BorderFactory.createMatteBorder(0, 3, 0, 0, Theme.GetColor("tab-selected-border"));
		setMinimumSize(new Dimension(48, 0));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void AddTrayIcon(ImageIcon icon, View targetView) {
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBorder(selectedBorder);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setMaximumSize(new Dimension(48,48));
		lblNewLabel.setMinimumSize(new Dimension(48,48));
		lblNewLabel.setPreferredSize(new Dimension(48,48));
		lblNewLabel.setIcon(icon);
		
		//TODO add support for multiple views.
		
		add(lblNewLabel);
	}
}
