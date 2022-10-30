package com.miyuki.baddapple.ui.console;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class ConsoleTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 4596826963253L;

	public ConsoleTabbedPane() {
		setUI(new TabbedPaneUI());
		setFocusable(false);
		
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int saveI = getSelectedIndex();

				if (saveI == -1) {
					return;
				}

				for (int i = 0; i < getTabCount(); i++) {
					Component t = getTabComponentAt(i);
					if (t == null || !(t instanceof ConsoleTabView))
						return;
					
					((ConsoleTabView) getTabComponentAt(i)).onHide();
				}

				Component tabComponent = getTabComponentAt(saveI);
				
				if (tabComponent instanceof ConsoleTabView)
					((ConsoleTabView) getTabComponentAt(saveI)).onShown();
			}
		});
	}

	@Override
	public void addTab(String title, Component component) {
		this.addTab(title, Resource.GetImageRecolored("internal://console.png", Theme.GetColor("explorer-icons")), component);
	}

	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		this.addTab(title, icon, component);
	}

	@Override
	public void addTab(String title, Icon icon, Component component) {
		super.addTab(title, icon, component);

		int index = super.indexOfComponent(component);
		setSelectedIndex(index);
		ConsoleTabView view = new ConsoleTabView(this, title, (ImageIcon) icon, component);

		if (index == super.getSelectedIndex())
			view.onShown();
		else
			view.onHide();

		super.setTabComponentAt(index, view);
	}
	
	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		
		setSelectedIndex(index);
		ConsoleTabView view = new ConsoleTabView(this, title, (ImageIcon) icon, component);

		if (index == super.getSelectedIndex())
			view.onShown();
		else
			view.onHide();

		super.setTabComponentAt(index, view);
	}

	private static class TabbedPaneUI extends BasicTabbedPaneUI {

		public Color background;
		public Color selectedBackground;
		public Color border;
		public Color selectedBorder;

		public ImageIcon logo;

		static {
			Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
			insets.bottom = -1;
			insets.left = -1;
			insets.right = -1;
			UIManager.put("TabbedPane.contentBorderInsets", insets);
		}

		public TabbedPaneUI() {
			border = Theme.GetColor("tab-border");
			background = Theme.GetColor("tab-background");

			selectedBackground = Theme.GetColor("tab-selected-background");
			selectedBorder = Theme.GetColor("tab-selected-border");

			logo = Resource.Resize(Resource.GetImageRecolored("internal://tray/whiteicon.png", background), 128);
		}

		protected void installDefaults() {
			super.installDefaults();
			highlight = Theme.GetColor("panel-background");
			lightHighlight = Theme.GetColor("panel-background");
			shadow = Theme.GetColor("panel-background");
			darkShadow = Theme.GetColor("panel-background");
			focus = Theme.GetColor("panel-background");
		}

		// no tab border
		@Override
		protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
				boolean isSelected) {
		}
		
		@Override
		protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
				int h) {
			g.setColor(border);
			g.fillRect(x - 1, y, w + 1, h);

			if (selectedIndex == -1) {
				int lx = w / 2 - logo.getIconWidth() / 2;
				int ly = h / 2 - logo.getIconHeight() / 2;

				g.drawImage(logo.getImage(), lx, ly, null);
			}
		}

		protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
				boolean isSelected) {
			Graphics2D g2d = (Graphics2D) g;

			if (super.tabPane.getTitleAt(tabIndex) == "+") {
				g2d.setColor(border);
				g2d.fillRect(x + 8, y + 4, w - 8, h - 8);
				return;
			}

			// drawing its background
			g2d.setColor(isSelected ? selectedBackground : background);
			g2d.fillRect(x, y, w, h);

			// Drawing its border
			g2d.setColor(border);

			if (isSelected) {
				g2d.drawRect(x + 1, y + 1, w, h - 1);
				g2d.drawRect(x + 2, y + 2, w, h - 2);

				g2d.setColor(selectedBorder);
				g2d.fillRect(x + 2, (y + h) - 8, w + 1, 4);
			} else {
				g2d.drawRect(x, y, w, h);
				g2d.drawRect(x + 1, y + 1, w - 1, h - 1);
			}
		}
	}
}
