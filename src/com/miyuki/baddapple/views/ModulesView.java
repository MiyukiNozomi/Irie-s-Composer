package com.miyuki.baddapple.views;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.modules.ModuleSign;

public class ModulesView extends View {
	private static final long serialVersionUID = 1L;
	
	Font descFont = Resource.DeriveMainFont(Font.PLAIN, 12);
	Font titleFont = Resource.DeriveMainFont(Font.BOLD, 13);
	
	public ModulesView() {
		super(Language.GetKey("module-view-title"), "internal://tray/module.png");
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		for (ModuleSign sign : BadApple.Get.handler.LoadedModules) {
			MakePane(sign);
		}
		
		if (BadApple.Get.handler.LoadedModules.size() == 0) {
			JLabel lbl = new JLabel(Language.GetKey("module-view-zero-loaded"));
			lbl.setHorizontalAlignment(JLabel.CENTER);
			lbl.setFont(titleFont);
			content.add(lbl);
		}
	}
	
	public void MakePane(ModuleSign sign) {

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 64));
		panel.setMinimumSize(new Dimension(0, 64));
		panel.setMaximumSize(new Dimension(13451515, 64));
		content.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblIcon = new JLabel();
		lblIcon.setHorizontalAlignment(JLabel.CENTER);
		lblIcon.setIcon(Resource.Resize(sign.icon, 59));
		lblIcon.setPreferredSize(new Dimension(64, 64));
		panel.add(lblIcon, BorderLayout.WEST);
		
		JPanel subPanel = new JPanel();
		panel.add(subPanel, BorderLayout.CENTER);
		subPanel.setLayout(new BorderLayout(0, 0));
		JPanel panel_1 = new JPanel();
		subPanel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		Component verticalStrut = Box.createVerticalStrut(3);
		panel_1.add(verticalStrut);
		
		JLabel lblTitle = new JLabel("<html>" + sign.sign.name());
		lblTitle.setFont(titleFont);
		panel_1.add(lblTitle);
		
		Component verticalStrut_1 = Box.createVerticalStrut(3);
		panel_1.add(verticalStrut_1);
		
		JLabel lblDescription = new JLabel("<html>" + sign.sign.description());
		lblDescription.setFont(descFont);
		panel_1.add(lblDescription);
		
		JPanel panel_2 = new JPanel();
		subPanel.add(panel_2, BorderLayout.WEST);
	}
}
