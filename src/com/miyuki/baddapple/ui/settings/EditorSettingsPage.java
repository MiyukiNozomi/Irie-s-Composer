package com.miyuki.baddapple.ui.settings;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.editor.Editor;

public class EditorSettingsPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField lblFieldFontSize;

	public EditorSettingsPage() {

		JLabel lblFontSize = new JLabel(Language.GetKey("settings-editor-fontsize"));

		lblFieldFontSize = new JTextField(BadApple.Get.settings.editorFontsize + "");
		lblFieldFontSize.setColumns(10);
		lblFieldFontSize.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					lblFieldFontSize.setEditable(true);
				} else {
					lblFieldFontSize.setEditable(false);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (lblFieldFontSize.isEditable()) {
					
					if (!lblFieldFontSize.getText().matches("[0-9]+[\\.]?[0-9]*")) {
						System.out.println("woops, inserted non-numeric value into fontSize Field");
						return;
					}
					int newSize = Integer.parseInt(lblFieldFontSize.getText());
					BadApple.Get.settings.editorFontsize = newSize;
					Resource.editorFont = Resource.editorFont.deriveFont(Font.PLAIN, newSize);
					
					try {
						for (int i = 0; i < BadApple.Get.tabPanel.tabbedPanel.getComponentCount(); i++) {
							Component c = BadApple.Get.tabPanel.tabbedPanel.getComponentAt(i);
							
							if (c instanceof Editor) {
								Editor ed = (Editor) c;
								ed.document.setFont(Resource.editorFont);
								ed.lineNumbers.setFont(Resource.editorFont);
								ed.autoComplete.list.setFont(Resource.editorFont);
								ed.revalidate();
							}
						}
					} catch(Exception err) {
						
					}
				}
			}
		});

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(
										groupLayout.createSequentialGroup().addContainerGap()
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
														.addComponent(lblFieldFontSize, GroupLayout.DEFAULT_SIZE, 430,
																Short.MAX_VALUE)
														.addComponent(lblFontSize))
												.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblFontSize)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblFieldFontSize,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(249, Short.MAX_VALUE)));
		setLayout(groupLayout);

	}
}
