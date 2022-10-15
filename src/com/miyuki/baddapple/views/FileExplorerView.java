package com.miyuki.baddapple.views;

import java.awt.Dimension;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class FileExplorerView extends View {
	public static final long serialVersionUID = 434155115142L;
	
	public FileExplorerView() {
		super("File Explorer", Resource.GetImageRecolored("tray/explorer.png", Theme.GetColor("tray-icon-foreground")));
		setPreferredSize(new Dimension(256, 0));
	}
	
}
