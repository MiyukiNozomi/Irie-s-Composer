package com.miyuki.baddapple.editor.viewers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class ImageView extends JPanel {
	private static final long serialVersionUID = 46066161161511L;
	
	public ImageView(File imageFile) {
		setLayout(new BorderLayout(0, 0));
		
		ImageIcon icon = Resource.GetImage(imageFile.getPath());
		
		JLabel lblImageView = new JLabel("");	
		lblImageView.setIcon(icon);
		Resource.DeleteCache(imageFile.getPath());
		
		add(lblImageView);
		
		JLabel lblNewLabel = new JLabel(icon.getIconWidth() + "x" + icon.getIconHeight());
		lblNewLabel.setFont(Resource.DeriveMainFont(Font.PLAIN, 14));
		lblNewLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.GetColor("panel-background")));
		add(lblNewLabel, BorderLayout.SOUTH);
	}
}
