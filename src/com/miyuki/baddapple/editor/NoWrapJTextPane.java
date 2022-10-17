package com.miyuki.baddapple.editor;

import java.awt.Dimension;

import javax.swing.JTextPane;

// from https://stackoverflow.com/questions/7156038/how-to-turn-off-jtextpane-line-wrapping
public class NoWrapJTextPane extends JTextPane {
	private static final long serialVersionUID = 2890950824365125276L;

	@Override
    public boolean getScrollableTracksViewportWidth() {
        // Only track viewport width when the viewport is wider than the preferred width
        return getUI().getPreferredSize(this).width 
            <= getParent().getSize().width;
    };

    @Override
    public Dimension getPreferredSize() {
        // Avoid substituting the minimum width for the preferred width when the viewport is too narrow
        return getUI().getPreferredSize(this);
    };
}