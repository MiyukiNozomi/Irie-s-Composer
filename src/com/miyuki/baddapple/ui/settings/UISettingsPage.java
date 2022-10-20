package com.miyuki.baddapple.ui.settings;

import java.awt.Component;
import java.awt.Font;
import java.util.Collection;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListCellRenderer;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.ui.UIHelper;

public class UISettingsPage extends JPanel {
	private static final long serialVersionUID = 167964921451L;
	
	public UISettingsPage() {
		
		ListCellRenderer<String> renderer = new ListCellRenderer<String>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends String> list,
					String value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel lbl = new JLabel(value);
				lbl.setOpaque(true);
				lbl.setBorder(SettingsPage.elmBorder);
				lbl.setFont(list.getFont());
				
				if (isSelected) {
					lbl.setBackground(Theme.GetColor("editor-selection"));
				} else {
					lbl.setBackground(Theme.GetColor("editor-background"));
				}
				lbl.setForeground(Theme.GetColor("editor-foreground"));
				
				return lbl;
			}
		};
		
		JLabel lblThemeTitle = new JLabel(Language.GetKey("settings-page-ui-theme"));
		lblThemeTitle.setFont(Resource.DeriveMainFont(Font.PLAIN, 16));
		
		JLabel lblLanguage = new JLabel(Language.GetKey("settings-page-ui-lang"));
		lblLanguage.setFont(lblThemeTitle.getFont());
		
		JLabel lblLower = new JLabel(Language.GetKey("settings-page-ui-lower"));
		lblLower.setFont(lblThemeTitle.getFont());

		String[] themes = Theme.loadedThemes.toArray(new String[Theme.loadedThemes.size()]);
		JList<String> themeList = new JList<String>(themes);
		themeList.addListSelectionListener(new ListSelectionListener() {	
			@Override
			public void valueChanged(ListSelectionEvent e) {
				BadApple.Get.settings.theme = themeList.getSelectedValue();
			}
		});
		themeList.setCellRenderer(renderer);
		themeList.setSelectedIndex(Theme.loadedThemes.indexOf(Theme.current.name));
		themeList.setFont(SettingsPage.elmFont);

		Collection<String> languages = Language.avaliableLanguagePacks.values();
		
		JList<String> langList = new JList<String>(languages.toArray(new String[languages.size()]));
		langList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String v = langList.getSelectedValue();
				String key = "english";
				
				for (String k : Language.avaliableLanguagePacks.keySet()) {
					if (v.matches(Language.avaliableLanguagePacks.get(k))) {
						key = k;
					}
				}
				
				BadApple.Get.settings.language = key;
			}
		});
		int selectedIndex = 0;
		int i = 0;
		for (String s : languages) {
			if (Language.current.name.matches(s)) {
				selectedIndex = i;
				break;
			}
			i++;
		}
		langList.setSelectedIndex(selectedIndex);
		langList.setFont(SettingsPage.elmFont);
		langList.setCellRenderer(renderer);
		
		JScrollPane themeListScroll = UIHelper.ManufactureScroll(themeList);
		JScrollPane langListScroll = UIHelper.ManufactureScroll(langList);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(themeListScroll, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(5)
							.addComponent(lblLanguage, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblThemeTitle, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLower)
						.addComponent(langListScroll, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblThemeTitle, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(themeListScroll, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(lblLanguage, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(langListScroll, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblLower)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}
}
