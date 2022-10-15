package com.miyuki.baddapple.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;

import com.miyuki.baddapple.Theme;

public class UIMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	public Color background;

	public UIMenuBar() {
		setBorder(BorderFactory.createLineBorder(Theme.GetColor("menubar-border")));
	}
	
    @Override
    protected void paintComponent(Graphics g) {
    	if (background == null) {
    		background = Theme.GetColor("menubar-background");
    	}
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(background);
        g2.fillRect(0,0, getWidth(), getHeight());
    }
}