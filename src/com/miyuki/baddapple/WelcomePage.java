package com.miyuki.baddapple;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class WelcomePage extends JPanel {
	private static final long serialVersionUID = 4592627579185313L;
	
	public WelcomePage() {
		Font size13 = Resource.DeriveMainFont(Font.PLAIN, 13);
		
		JLabel title = new JLabel("Welcome to BadApple Studio");
		title.setFont(Resource.DeriveMainFont(Font.PLAIN, 22));
		
		JLabel lblNewLabel = new JLabel("check out https://miyukinozomi.github.io/wiki/projects.html");
		lblNewLabel.setFont(size13);
		
		JLabel lblNewLabel_1 = new JLabel("Previous Projects");
		lblNewLabel_1.setFont(size13);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
						.addComponent(title)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(title)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addContainerGap())
		);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		Font p = Resource.DeriveMainFont(Font.PLAIN, 12);
		
		JLabel projLabel = new JLabel("<HTML><U>projLabel</HTML></U>");
		projLabel.setFont(p);
		projLabel.setForeground(Theme.GetColor("panel-selection"));
		panel.add(projLabel);
		setLayout(groupLayout);
		
	}
}
