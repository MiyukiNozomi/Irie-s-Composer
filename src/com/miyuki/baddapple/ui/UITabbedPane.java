package com.miyuki.baddapple.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.miyuki.baddapple.Theme;

public class UITabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 4596826963253L;
	
	public UITabbedPane() {
		setUI(new TabbedPaneUI());
	}
	
	private static class TabbedPaneUI extends BasicTabbedPaneUI {

		public Color borderColor;
		public Color background;

		static {
			Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
			insets.bottom = -1;
			insets.left = -1;
			insets.right = -1;
			UIManager.put("TabbedPane.contentBorderInsets", insets);
		}

		// no tab border
		@Override
		protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
				boolean isSelected) {
			if (borderColor == null)
				borderColor = Theme.GetColor("panel-border");
			g.setColor(borderColor);
			g.fillRect(x - 1, y, w + 1, h);
		}

		@Override
		protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
				int h) {
			if (borderColor == null)
				borderColor = Theme.GetColor("panel-border");
			g.setColor(borderColor);
			g.fillRect(x - 1, y, w + 1, h);
		}

		protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
				boolean isSelected) {
			if (borderColor == null)
				borderColor = Theme.GetColor("panel-border");
			if (background == null)
				background = Theme.GetColor("panel-background");
			
			if (isSelected) {

				Graphics2D g2d = (Graphics2D) g;

				g2d.setBackground(background);
				g2d.fillRect(x, y, w, h);
			}
		}
	}
}
