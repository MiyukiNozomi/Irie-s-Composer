package com.miyuki.baddapple.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.views.View;

public class Tray extends JPanel {
	private static final long serialVersionUID = 699481845L;
	
	public Border selectedBorder;
	public List<TrayIcon> trayIcons;
	
	JPanel iconPanel;
	
	public Tray() {
		trayIcons = new ArrayList<TrayIcon>();
		selectedBorder = BorderFactory.createMatteBorder(0, 3, 0, 0, Theme.GetColor("tab-selected-border"));

		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(48, 0));
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.GetColor("tray-border")));
		
		setBackground(Theme.GetColor("tray-background"));
		iconPanel = new JPanel();
		iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
		iconPanel.setBackground(Theme.GetColor("tray-background"));
		
		add(iconPanel, BorderLayout.CENTER);
		
		JLabel badAppleButton = new JLabel("");
		badAppleButton.setMaximumSize(new Dimension(48,48));
		badAppleButton.setMinimumSize(new Dimension(48,48));
		badAppleButton.setPreferredSize(new Dimension(48,48));
		badAppleButton.setHorizontalAlignment(SwingConstants.CENTER);
		badAppleButton.setIcon(Resource.Resize(Resource.GetImageRecolored("internal://tray/whiteicon.png", Theme.GetColor("tray-foreground")), 32));
		add(badAppleButton, BorderLayout.SOUTH);
	}
	
	public TrayIcon AddTrayIcon(View targetView) {
		TrayIcon icn = new TrayIcon(targetView);
		trayIcons.add(icn);
		
		iconPanel.add(icn);
		return icn;
	}
	
	public class TrayIcon extends JLabel {
		private static final long serialVersionUID = 999941415L;
		
		public View targetView;
		public ImageIcon selectedIcon;
		public ImageIcon icon;
		
		private boolean isSelected;
		
		public TrayIcon(View target) {
			super("");
			this.targetView = target;
			
			this.selectedIcon = Resource.Resize(Resource.GetImageRecolored(this.targetView.trayIconPath, Theme.GetColor("tray-selected")), 32);
			this.icon =         Resource.Resize(Resource.GetImageRecolored(this.targetView.trayIconPath, Theme.GetColor("tray-foreground")), 32);
			
			setHorizontalAlignment(SwingConstants.CENTER);
			setMaximumSize(new Dimension(48,48));
			setMinimumSize(new Dimension(48,48));
			setPreferredSize(new Dimension(48,48));
			
			SetSelected(false);
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (isSelected)
						return;
					
					for (TrayIcon i : BadApple.Get.sideTray.trayIcons) {
						i.SetSelected(false);
					}
					
					Dimension leftSize = BadApple.Get.mainSplitPanel.getLeftComponent().getSize();
					
					targetView.setPreferredSize(leftSize);
					
					BadApple.Get.mainSplitPanel.setLeftComponent(targetView);
					
					SetSelected(true);
				}
			});
		}
		
		public void SetSelected(boolean selected) {
			isSelected = selected;
			if (selected) {
				setBorder(selectedBorder);
				setIcon(selectedIcon);
			} else {
				setIcon(icon);
				setBorder(null);
			}
		}
	}
}
