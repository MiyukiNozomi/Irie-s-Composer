package com.miyuki.baddapple.ui.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Resource;

public class AddConsoleView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public JLabel titleLbl;
	public JLabel closeButton;
	public boolean selected = false;
	
	public boolean closeable = true;

	public AddConsoleView() {
		setOpaque(false);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(29, 29));
		setMaximumSize(new Dimension(29, 29));
		setPreferredSize(new Dimension(29, 29));
		titleLbl = new JLabel("+");
		titleLbl.setHorizontalAlignment(JLabel.CENTER);
		titleLbl.setMinimumSize(new Dimension(29, 29));
		titleLbl.setPreferredSize(new Dimension(29, 29));
		titleLbl.setFont(Resource.DeriveMainFont(Font.BOLD, 14)); 
		titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 5));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					BadApple.Get.consolePanel.AddConsoleTab();
			}
		});
		add(titleLbl, BorderLayout.WEST);
	}
}