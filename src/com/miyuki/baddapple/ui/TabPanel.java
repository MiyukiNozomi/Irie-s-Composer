package com.miyuki.baddapple.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.miyuki.baddapple.DiscordPresence;
import com.miyuki.baddapple.IconPack;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.editor.Editor;
import com.miyuki.baddapple.editor.viewers.ImageView;

public class TabPanel extends JPanel {
	private static final long serialVersionUID = 688529252359L;
	
	public JTabbedPane tabbedPanel;
	
	public TabPanel() {
		setLayout(new BorderLayout());
		setBackground(Theme.GetColor("main-background"));
		
		tabbedPanel = new UITabbedPane() {
			private static final long serialVersionUID = 9458268346906L;
			@Override
			public void addTab(String title, Component component) {
				this.addTab(title, IconPack.current.fileIcon, component);
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
				TabCompView view = new TabCompView(this, title, (ImageIcon) icon, component);

				if (index == super.getSelectedIndex())
					view.onShown();
				else
					view.onHide();

				super.setTabComponentAt(index, view);
			}
		};
		tabbedPanel.setBackground(getBackground());
		tabbedPanel.setBorder(BorderFactory.createEmptyBorder());
		
		tabbedPanel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int saveI = tabbedPanel.getSelectedIndex();

				if (saveI == -1) {
					return;
				}

				for (int i = 0; i < tabbedPanel.getTabCount(); i++) {
					if (tabbedPanel.getTabComponentAt(i) == null)
						return;
					((TabCompView) tabbedPanel.getTabComponentAt(i)).onHide();
				}

				((TabCompView) tabbedPanel.getTabComponentAt(saveI)).onShown();
				
				Component c = tabbedPanel.getComponentAt(saveI);
				if (c instanceof Editor) {
					DiscordPresence.SetCurrentFile(((Editor) c).targetFile,"Editing");
				} else if (c instanceof ImageView) {
					DiscordPresence.SetCurrentFile(((Editor) c).targetFile,"Viewing");
				} else {
					DiscordPresence.Reset();
				}
			}
		});
		
		add(tabbedPanel, BorderLayout.CENTER);
	}
	
	public void SetTitleAt(Component c, String t) {
		SetTitleAt(tabbedPanel.indexOfComponent(c), t);
	}
	
	public void SetTitleAt(int i, String title) {
		((TabCompView) tabbedPanel.getTabComponentAt(i)).titleLbl.setText(title);
		tabbedPanel.setTitleAt(i, title);
	}
}
