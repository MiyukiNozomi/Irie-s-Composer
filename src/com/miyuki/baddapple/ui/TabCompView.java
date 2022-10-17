package com.miyuki.baddapple.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;

public class TabCompView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public JLabel titleLbl;
	public JLabel closeButton;
	public boolean selected = false;

	public TabCompView(final JTabbedPane tabbedPane, String title, ImageIcon icon, final Component arg2) {
		setOpaque(false);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(123, 29));
		setPreferredSize(new Dimension(123, 29));
		setMaximumSize(new Dimension(123, 29));
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		titleLbl = new JLabel(title);
		titleLbl.setMinimumSize(new Dimension(123, 29));
		titleLbl.setPreferredSize(new Dimension(123, 29));
		titleLbl.setIcon(Resource.Resize(icon, 16));
		titleLbl.setFont(Resource.DeriveMainFont(Font.PLAIN, 12)); 
		titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					//new TabPopup(tabbedPane).show(titleLbl, e.getX(), e.getY());
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(arg2));
					selected = !selected;
				}
			}
		});
		add(titleLbl, BorderLayout.WEST);
		closeButton = new JLabel(Resource.GetImage("internal://delete.png"));
		closeButton.setHorizontalAlignment(JLabel.CENTER);
		closeButton.setVerticalAlignment(JLabel.CENTER);
		closeButton.setFont(new Font("Dialog",Font.BOLD,14));
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				tabbedPane.remove(arg2);
			}
		});
		add(closeButton, BorderLayout.EAST);
	}
	
	public void onShown() {
		selected = true;
		titleLbl.setForeground(Theme.GetColor("tab-selected-foreground"));
	}
	
	public void onHide() {
		selected = false; 
		titleLbl.setForeground(Theme.GetColor("tab-foreground"));
	}
}