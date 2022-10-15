package com.miyuki.baddapple.ui;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.miyuki.baddapple.Theme;

public class UIHelper {

	/**
	 * Creates a new JSplitPane configured with the specified
	 * orientation with BadApple's UI Style.
	 * @Parameters
	 * 	newOrientation JSplitPane.HORIZONTAL_SPLIT or
	 * 	JSplitPane.VERTICAL_SPLITThrows:IllegalArgumentException
	 * 
	 * if orientation is
	 * 		not one of HORIZONTAL_SPLIT or VERTICAL_SPLIT it throws an exception
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

}
