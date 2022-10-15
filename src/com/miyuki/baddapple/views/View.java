package com.miyuki.baddapple.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public abstract class View extends JPanel {
	private static final long serialVersionUID = 15622626923534L;
	
	public ImageIcon trayIcon;
	public String title;
	
	public JPanel content;
	
	public View(String title, ImageIcon trayIcon) {
		this.title = title;
		this.trayIcon = Resource.Resize(trayIcon, 32);
		
		this.content = new JPanel();
		this.content.setLayout(new BorderLayout());
		
		setLayout(new BorderLayout());
		add(content, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBackground(Theme.GetColor("view-title-background"));
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitleLabel = new JLabel(title);
		lblTitleLabel.setForeground(Theme.GetColor("view-title-foreground"));
		lblTitleLabel.setFont(Resource.DeriveMainFont(Font.PLAIN, 14));
		lblTitleLabel.setMinimumSize(new Dimension(0, 36));
		lblTitleLabel.setPreferredSize(new Dimension(0, 36));
		panel.add(lblTitleLabel);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);
	}
}
