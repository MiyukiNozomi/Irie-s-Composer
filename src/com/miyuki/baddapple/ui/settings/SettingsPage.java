package com.miyuki.baddapple.ui.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class SettingsPage extends JPanel {
	private static final long serialVersionUID = 345613467142135191L;

	static Border elmBorder = BorderFactory.createEmptyBorder(2, 15, 2,3);
	static Font elmFont = Resource.DeriveMainFont(Font.PLAIN, 15);
	
	public SettingsPage() {
		setLayout(new BorderLayout(0, 0));
		
		JList<SettingsMenu> list = new JList<SettingsMenu>(new SettingsMenu[] {
				 new SettingsMenu("settings-page-ui", new UISettingsPage()),
				 new SettingsMenu("settings-page-general", new JLabel("panel1"))
		});
		list.setBackground(Theme.GetColor("editor-background"));
		list.setPreferredSize(new Dimension(168, 0));
		list.setCellRenderer(new ListCellRenderer<SettingsMenu>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends SettingsMenu> list,
					SettingsMenu value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = new JLabel(value.title);
				label.setOpaque(true);
				label.setBorder(elmBorder);
				label.setFont(elmFont);
				
				if (isSelected) {
					label.setBackground(Theme.GetColor("editor-selection"));
				} else {
					label.setBackground(Theme.GetColor("editor-background"));
				}
				label.setForeground(Theme.GetColor("editor-foreground"));
				
				return label;
			}
		});
		
		add(list, BorderLayout.WEST);
		
		JPanel menuContainer = new JPanel();
		menuContainer.setLayout(new BorderLayout());
		
		JLabel logo = new JLabel();
		logo.setHorizontalAlignment(JLabel.CENTER);
		
		logo.setIcon(Resource.Resize(Resource.GetImageRecolored("internal://tray/whiteicon.png", Theme.GetColor("panel-border")), 128));
		menuContainer.add(logo, BorderLayout.CENTER);
		
		JLabel bottom = new JLabel(Language.GetKey("settings-page-select"));
		bottom.setFont(elmFont);
		bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 32));
		bottom.setHorizontalAlignment(JLabel.CENTER);
		menuContainer.add(bottom , BorderLayout.SOUTH);
		
		add(menuContainer, BorderLayout.CENTER);
	

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("Set panel to " + list.getSelectedValue().title);
				menuContainer.removeAll();
				JComponent content = list.getSelectedValue().content;
				
				menuContainer.add(content, BorderLayout.CENTER);
				menuContainer.revalidate();
			}
		});
	}
	
	public class SettingsMenu {
		
		public String title;
		public JComponent content;
		
		public SettingsMenu(String title, JComponent content) {
			this.content = content;
			this.title = Language.GetKey(title);
		}
	}
}
