package com.miyuki.baddapple.editor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import com.miyuki.baddapple.Theme;

public class AutoComplete extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	private JTextPane textpane;
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(model);
	private StringBuffer word = new StringBuffer();
	public List<String> words = new ArrayList<String>();
	public List<String> updatedWords = new ArrayList<String>();
	public String[] lastResult = { "" };
	
	private boolean isShow = false;

	public AutoComplete(JTextPane textpane) {
		super();
		this.textpane = textpane;

		removeAll();
		setMinimumSize(new Dimension(168, 168));
		setOpaque(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFocusable(false);
		list.setBackground(Theme.GetColor("editor-background"));
		list.setForeground(Theme.GetColor("editor-foreground"));
		list.setSelectionBackground(Theme.GetColor("editor-selection"));
		list.setSelectionForeground(Theme.GetColor("editor-foreground"));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					final int position = textpane.getCaretPosition();
					try {
						textpane.getDocument().insertString(position,
								getSelectedString().substring(word.length()),
								null);
					} catch (BadLocationException e2) {
						e2.printStackTrace();
					}
					word.delete(0, word.length());
					model.clear();
					setVisible(false);
					isShow = false;
				}
			}
		});
		textpane.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				final int position = textpane.getCaretPosition();
				if (e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_DOWN) {
					return;
				}
				if (Character.isWhitespace(e.getKeyChar())) {
					if (e.getKeyChar() == '\n' && isShow) {
						try {
							textpane.getDocument().insertString(
									position,
									getSelectedString()
											.substring(word.length()), null);
						} catch (Exception e2) {
							//e2.printStackTrace();
						}
						e.consume();
					}
					word.delete(0, word.length());
					model.clear();
					setVisible(false);
					isShow = false;
					return;
				} else if (Character.isLetterOrDigit(e.getKeyChar())) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (word.length() == 0) {
								word.append(getWord(position - 1));
							}
							word.append(e.getKeyChar());
							if (word.length() >= 2) {
								model.clear();
								if (!updateWordList()) {
									setVisible(false);
									isShow = false;
									return;
								}
								isShow = true;
								showPanel();
							}
						}
					});
				} else {
					word.delete(0, word.length());
					model.clear();
					setVisible(false);
					isShow = false;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN && isShow) {
					list.setSelectedIndex(Math.min(
							list.getModel().getSize() - 1,
							list.getSelectedIndex() + 1));
					e.consume();
				} else if (e.getKeyCode() == KeyEvent.VK_UP && isShow) {
					list.setSelectedIndex(Math.max(list.getSelectedIndex() - 1,
							0));
					e.consume();
				}
			}
		});
		
		
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.GetColor("panel-border"),1),
				BorderFactory.createLineBorder(Theme.GetColor("editor-background"), 2)));
	}

	private boolean updateWordList() {
		final String[] words = search(word.toString());
		if (words.length < 1) {
			return false;
		}
		for (String str : words) {
			model.addElement(str);
		}
		return true;
	}

	public void showPanel() {
		final int position = textpane.getCaretPosition();
		try {
			Point location = textpane.modelToView(position).getLocation();
			setVisible(true);
			setOpaque(true);
			add(list);
			show(textpane, location.x, textpane.getBaseline(0, 0) + location.y
					+ 15);
			list.setSelectedIndex(0);
			textpane.setCaretPosition(position);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textpane.requestFocusInWindow();
			}
		});
	}

	private String getWord(int index) {
		if (index < 0) {
			return "";
		}
		String text = textpane.getText();
		char word;
		StringBuffer br = new StringBuffer();

		if (index > text.length())
			return "";

		while (!Character.isWhitespace(word = text.charAt(index))) {
			index--;
			br.append(word);
			if (index < 0) {
				br.reverse();
				return br.toString();
			}
		}
		br.reverse();
		return br.toString();
	}

	public String[] search(String text) {
		
		updatedWords.clear();
		
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("([a-zA-Z])(.*([A-Z])[a-z]+)*");
		for (String str : words.toArray(new String[words.size()])) {
			Matcher m = p.matcher(str);
			if (m.find()
					&& text.matches("("
							+ m.group(1)
							+ ".*(?i)"
							+ m.group(3)
							+ "(?-i).*)|("
							+ str.substring(0,
									Math.min(text.length(), str.length()))
							+ ".*)")) {
				result.add(str);
			}
		}
		
		for (String a : updatedWords.toArray(new String[updatedWords.size()])) {
			Matcher m = p.matcher(a);
			if (m.find()
					&& text.matches("("
							+ m.group(1)
							+ ".*(?i)"
							+ m.group(3)
							+ "(?-i).*)|("
							+ a.substring(0,
									Math.min(text.length(), a.length()))
							+ ".*)")) {
				result.add(a);
			}
		}
		
		lastResult = result.toArray(new String[result.size()]);
		return lastResult;
	}

	public boolean hasValue(String text) {
		Pattern p = Pattern.compile("([a-zA-Z])(.*([A-Z])[a-z]+)*");
		for (String str : words.toArray(new String[words.size()])) {
			Matcher m = p.matcher(str);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	private String getSelectedString() {
		if (list.getSelectedValue() != null) {
			return (String) list.getSelectedValue();
		}
		return "";
	}
}