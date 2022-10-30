package com.miyuki.baddapple.editor.completion;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

public class AutoClose implements KeyListener {

	private JTextPane editArea;
	
	public AutoClose(JTextPane edit) {
		this.editArea = edit;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent e) {
		autoClose1();
		autoClose2();
		autoClose3();
	}
	
	@SuppressWarnings("serial")
	private void autoClose1(){

		Action action1 = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editArea = (JTextPane) e.getSource();
				try {
					int position2 = editArea.getCaretPosition();
					editArea.getDocument().remove(position2, 1);
					editArea.getDocument().insertString(position2, ")", null);

				} catch (Exception e1) {
				}
			}

		};
		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int position = editArea.getCaretPosition();
				editArea.replaceSelection("()");
				editArea.setCaretPosition(position + 1);
			}

		};

		String key = "typed (";
		String key1 = "typed )";
		editArea.getInputMap().put(KeyStroke.getKeyStroke(key), key);
		editArea.getActionMap().put(key, action);
		editArea.getActionMap().put(key1, action1);
	}
	
	@SuppressWarnings("serial")
	private void autoClose2(){

		Action action1 = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editArea = (JTextPane) e.getSource();
				try {
					int position2 = editArea.getCaretPosition();
					editArea.getDocument().remove(position2, 1);
					editArea.getDocument().insertString(position2, "}", null);

				} catch (Exception e1) {
				}
			}

		};
		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int position = editArea.getCaretPosition();
				editArea.replaceSelection("{}");
				editArea.setCaretPosition(position + 1);
			}

		};

		String key = "typed {";
		String key1 = "typed }";
		editArea.getInputMap().put(KeyStroke.getKeyStroke(key), key);
		editArea.getActionMap().put(key, action);
		editArea.getActionMap().put(key1, action1);
	}
	
	@SuppressWarnings("serial")
	private void autoClose3(){

		Action action1 = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editArea = (JTextPane) e.getSource();
				try {
					int position2 = editArea.getCaretPosition();
					editArea.getDocument().remove(position2, 1);
					editArea.getDocument().insertString(position2, "]", null);

				} catch (Exception e1) {
				}
			}

		};
		Action action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int position = editArea.getCaretPosition();
				editArea.replaceSelection("[]");
				editArea.setCaretPosition(position + 1);
			}

		};

		String key = "typed [";
		String key1 = "typed ]";
		editArea.getInputMap().put(KeyStroke.getKeyStroke(key), key);
		editArea.getActionMap().put(key, action);
		editArea.getActionMap().put(key1, action1);
	}
}