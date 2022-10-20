package com.miyuki.baddapple.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class UIHelper {

	/**
	 * Creates a new JSplitPane configured with the specified orientation with
	 * BadApple's UI Style.
	 * 
	 * @Parameters newOrientation JSplitPane.HORIZONTAL_SPLIT or
	 *             JSplitPane.VERTICAL_SPLITThrows:IllegalArgumentException
	 * 
	 *             if orientation is not one of HORIZONTAL_SPLIT or VERTICAL_SPLIT
	 *             it throws an exception
	 */
	public static JSplitPane ManufactureSplit(int newOrientation) {
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(newOrientation);
		splitPane.setBackground(Theme.GetColor("panel-border"));

		splitPane.setUI(new BasicSplitPaneUI());
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		BasicSplitPaneDivider divider = ((BasicSplitPaneDivider) splitPane.getComponent(2));
		divider.setBorder(BorderFactory.createEmptyBorder());
		divider.setDividerSize(2);
		return splitPane;
	}

	public static JScrollPane ManufactureScroll(Component c) {
		JScrollPane pane = new JScrollPane(c);
		pane.setBorder(BorderFactory.createEmptyBorder());
		pane.setBackground(Theme.GetColor("scroller-background"));

		pane.getVerticalScrollBar().setBackground(Theme.GetColor("scroller-background"));
		pane.getVerticalScrollBar().setForeground(Theme.GetColor("scroller-foreground"));

		pane.getHorizontalScrollBar().setBackground(Theme.GetColor("scroller-background"));
		pane.getHorizontalScrollBar().setForeground(Theme.GetColor("scroller-foreground"));

		pane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
		pane.getHorizontalScrollBar().setBorder(BorderFactory.createEmptyBorder());

		pane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = Theme.GetColor("scroller-foreground");
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				JButton e = super.createIncreaseButton(orientation);

				e.setBackground(Theme.GetColor("scroller-background"));

				e.setBorder(BorderFactory.createEmptyBorder());
				e.setIcon(Resource.GetImage("up_arrow.png"));

				return e;
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				JButton e = super.createDecreaseButton(orientation);

				e.setBackground(Theme.GetColor("scroller-background"));

				e.setBorder(BorderFactory.createEmptyBorder());
				e.setIcon(Resource.GetImage("down_arrow.png"));
				return e;
			}
		});

		pane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = Theme.GetColor("scroller-foreground");
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				JButton e = super.createIncreaseButton(orientation);

				e.setBackground(Theme.GetColor("scroller-background"));

				e.setBorder(BorderFactory.createEmptyBorder());
				e.setIcon(Resource.GetImage("up_arrow.png"));

				return e;
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				JButton e = super.createDecreaseButton(orientation);

				e.setBackground(Theme.GetColor("scroller-background"));

				e.setBorder(BorderFactory.createEmptyBorder());
				e.setIcon(Resource.GetImage("down_arrow.png"));
				return e;
			}
		});
		return pane;
	}

	public static void InstallLAF() {
		Font menuFont = Resource.DeriveMainFont(Font.PLAIN, 14);
		
		UIManager.put("MenuItem.selectionBackground", Theme.GetColor("menubar-selected-background"));
		UIManager.put("MenuItem.selectionForeground", Theme.GetColor("menubar-selected-foreground"));
		UIManager.put("MenuItem.background", Theme.GetColor("menubar-background"));
		UIManager.put("MenuItem.foreground", Theme.GetColor("menubar-foreground"));
		UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder(2,3, 2,3));
		
		UIManager.put("Menu.selectionBackground", Theme.GetColor("menubar-selected-background"));
		UIManager.put("Menu.selectionForeground", Theme.GetColor("menubar-selected-foreground"));
		UIManager.put("Menu.background", Theme.GetColor("menubar-background"));
		UIManager.put("Menu.foreground", Theme.GetColor("menubar-foreground"));
		UIManager.put("Menu.border", BorderFactory.createEmptyBorder(2,3, 2,3));
		
		UIManager.put("List.selectionBackground", Theme.GetColor("editor-selection"));
		UIManager.put("List.selectionForeground", Theme.GetColor("editor-foreground"));
		UIManager.put("List.background", Theme.GetColor("editor-background"));
		UIManager.put("List.foreground", Theme.GetColor("editor-foreground"));
		UIManager.put("List.noFocusBorder", BorderFactory.createEmptyBorder());
		
		UIManager.put("List.font", menuFont);

		UIManager.put("Panel.background", Theme.GetColor("panel-background"));
		UIManager.put("Panel.foreground", Theme.GetColor("panel-foreground"));
		UIManager.put("Label.foreground", Theme.GetColor("panel-foreground"));
		
		UIManager.put("Menu.font", menuFont);
		UIManager.put("MenuItem.font", menuFont);
		UIManager.put("PopupMenu.font", menuFont);
		UIManager.put("PopupMenu.background", Theme.GetColor("panel-background"));
		UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Theme.GetColor("panel-border")));
		
		UIManager.put("Tree.paintLines", false);
		 
		UIManager.put("Tree.dropLineColor", new ColorUIResource(Theme.GetColor("panel-background")));
		UIManager.put("Tree.expandedIcon",  Resource.GetImageRecolored("internal://extended.png", Theme.GetColor("explorer-colapse-extend-button")));
		UIManager.put("Tree.collapsedIcon", Resource.GetImageRecolored("internal://colapsed.png", Theme.GetColor("explorer-colapse-extend-button")));
	}

}
