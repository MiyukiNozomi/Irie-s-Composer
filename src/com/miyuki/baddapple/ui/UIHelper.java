package com.miyuki.baddapple.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
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

		pane.getVerticalScrollBar().setUI(new UIScrollBar());
		pane.getHorizontalScrollBar().setUI(new UIScrollBar());
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
		UIManager.put("Label.font", menuFont);

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
	
		UIManager.put("OptionPane.background",Theme.GetColor("panel-background"));
		UIManager.put("OptionPane.foreground",Theme.GetColor("panel-foreground"));
		UIManager.put("OptionPane.messageForeground",Theme.GetColor("panel-foreground"));
		UIManager.put("OptionPane.warningDialog.titlePane.foreground",Theme.GetColor("panel-foreground"));
		UIManager.put("OptionPane.questionDialog.titlePane.foreground",Theme.GetColor("panel-foreground"));
		UIManager.put("OptionPane.errorDialog.titlePane.foreground",Theme.GetColor("panel-foreground"));
		UIManager.put("OptionPane.border",BorderFactory.createEmptyBorder(15, 15, 0, 0));
		UIManager.put("OptionPane.buttonAreaBorder",BorderFactory.createEmptyBorder());
		
		UIManager.put("OptionPane.errorIcon", Resource.GetImageRecolored("internal://msg/error.png", Theme.GetColor("error-icon")));
		UIManager.put("OptionPane.warningIcon", Resource.GetImageRecolored("internal://msg/error.png", Theme.GetColor("warn-icon")));
		UIManager.put("OptionPane.informationIcon", Resource.GetImageRecolored("internal://msg/info.png", Theme.GetColor("info-icon")));
		UIManager.put("OptionPane.questionIcon", Resource.GetImageRecolored("internal://msg/question.png", Theme.GetColor("info-icon")));
		
		UIManager.put("OptionPane.font", Resource.DeriveMainFont(Font.PLAIN, 14));
	
		UIManager.put("Button.focus",Theme.GetColor("panel-background"));
		UIManager.put("Button.font", Resource.DeriveMainFont(Font.PLAIN, 14));
		UIManager.put("Button.select",Theme.GetColor("panel-selection"));
		UIManager.put("Button.background",Theme.GetColor("panel-background"));
		UIManager.put("Button.foreground",Theme.GetColor("panel-foreground"));
		UIManager.put("Button.border",BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.GetColor("panel-border")),
				BorderFactory.createEmptyBorder(7,11,7,11)));
		
		UIManager.put("RootPane.fileChooserDialogBorder", BorderFactory.createLineBorder(Theme.GetColor("panel-border")));
		
		UIManager.put("Label.font", Resource.DeriveMainFont(Font.PLAIN, 14));
			
		
		UIManager.put("TextField.border", BorderFactory.createLineBorder(Theme.GetColor("panel-border")));
		UIManager.put("TextField.font", Resource.DeriveMainFont(Font.PLAIN, 14));
		UIManager.put("TextField.background", Theme.GetColor("panel-background"));
		UIManager.put("TextField.foreground", Theme.GetColor("panel-foreground"));
		UIManager.put("TextField.caretForeground", Theme.GetColor("panel-foreground"));
		UIManager.put("TextField.selectionBackground", Theme.GetColor("panel-selection"));
		UIManager.put("TextField.selectionForeground", Theme.GetColor("panel-foreground"));

		UIManager.put("ComboBox.selectionBackground", Theme.GetColor("panel-selection"));
		UIManager.put("ComboBox.selectionForeground", Theme.GetColor("panel-foreground"));
		UIManager.put("ComboBox.buttonBackground", Theme.GetColor("panel-background"));
		UIManager.put("ComboBox.background", Theme.GetColor("panel-background"));
		UIManager.put("ComboBox.foreground", Theme.GetColor("panel-foreground"));
		UIManager.put("ComboBox.font", Resource.DeriveMainFont(Font.PLAIN, 14));
		UIManager.put("ComboBoxUI", "com.miyuki.baddapple.ui.UIComboBox");
		
		UIManager.put("FileChooser.newFolderIcon", Resource.GetImageRecolored("internal://new-folder-16x16.png", Theme.GetColor("file-chooser-icons")));
		UIManager.put("FileChooser.detailsViewIcon", Resource.GetImageRecolored("internal://details-16x16.png", Theme.GetColor("file-chooser-icons")));
		
		UIManager.put("FileView.computerIcon", Resource.GetImageRecolored("internal://computer-16x16.png", Theme.GetColor("file-chooser-icons")));
		UIManager.put("FileView.directoryIcon", Resource.GetImageRecolored("internal://folder-16x16.png", Theme.GetColor("file-chooser-icons")));
		UIManager.put("FileView.fileIcon", Resource.GetImageRecolored("internal://file-16x16.png", Theme.GetColor("file-chooser-icons")));
		UIManager.put("FileView.floppyDriveIcon", Resource.GetImageRecolored("internal://floppy-16x16.png", Theme.GetColor("file-chooser-icons")));
		UIManager.put("FileView.hardDriveIcon", Resource.GetImageRecolored("internal://hdd-16x16.png", Theme.GetColor("file-chooser-icons")));

		UIManager.put("ScrollBar.thumb", Theme.GetColor("scroller-foreground"));
		UIManager.put("ScrollBar.thumbDarkShadow", Theme.GetColor("scroller-foreground"));
		UIManager.put("ScrollBar.thumbHighlight", Theme.GetColor("scroller-foreground"));
		UIManager.put("ScrollBar.thumbShadow", Theme.GetColor("scroller-foreground"));
		UIManager.put("ScrollBar.foreground", Theme.GetColor("scroller-foreground"));
		UIManager.put("ScrollBar.background", Theme.GetColor("scroller-background"));
		UIManager.put("ScrollBar.track", Theme.GetColor("scroller-background"));
		UIManager.put("ScrollBar.border", BorderFactory.createEmptyBorder());
		UIManager.put("ScrollBarUI", "com.miyuki.baddapple.ui.UIScrollBar");
	}
}
