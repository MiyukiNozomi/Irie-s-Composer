package com.miyuki.baddapple.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class UIScrollBar extends BasicScrollBarUI {
	
	public UIScrollBar() {
	}
	
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
}