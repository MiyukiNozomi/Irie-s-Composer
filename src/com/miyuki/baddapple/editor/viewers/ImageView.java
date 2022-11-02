package com.miyuki.baddapple.editor.viewers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Debug;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.ui.UIHelper;

public class ImageView extends JPanel {
	private static final long serialVersionUID = 46066161161511L;
	public File targetFile;

	public ImageView(File imageFile) {
		this.targetFile = imageFile;
		Debug.Info("Opening Image: " + imageFile.getPath());
		setBackground(Theme.GetColor("editor-background"));
		setLayout(new BorderLayout(0, 0));

		ImageIcon icon = null;
		try {
			icon = new ImageIcon(imageFile.toURI().toURL());
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(BadApple.Get,
					Language.GetKey("error-opening").replace("%F%", imageFile.getName()), Language.GetKey("error-title"),
					JOptionPane.ERROR_MESSAGE, null);
			e.printStackTrace();
		}
		
		JLabel lblImageView = new JLabel("");
		lblImageView.setBackground(Theme.GetColor("editor-background"));
		lblImageView.setIcon(icon);
		lblImageView.setHorizontalAlignment(JLabel.CENTER);
		JPanel c = new JPanel();
		c.setBackground(Theme.GetColor("editor-background"));
		c.setLayout(new BorderLayout());
		c.add(lblImageView, BorderLayout.CENTER);
		
		add(UIHelper.ManufactureScroll(c));

		JLabel lblNewLabel = new JLabel(icon.getIconWidth() + "x" + icon.getIconHeight() + " " + (icon.getIconWidth() * icon.getIconHeight()) + " pixels.");
		lblNewLabel.setFont(Resource.DeriveMainFont(Font.PLAIN, 14));
		lblNewLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.GetColor("panel-background")));
		add(lblNewLabel, BorderLayout.SOUTH);
	}
}
