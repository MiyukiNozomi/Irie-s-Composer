package com.miyuki.baddapple.ui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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

}
