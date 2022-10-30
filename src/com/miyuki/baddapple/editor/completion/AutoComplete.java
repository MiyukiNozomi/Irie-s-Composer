package com.miyuki.baddapple.editor.completion;

import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;

import com.miyuki.baddapple.Debug;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.editor.completion.CompletionSuggestion.CompletionType;

public class AutoComplete extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	private JTextPane textpane;
	private DefaultListModel<CompletionSuggestion> model = new DefaultListModel<CompletionSuggestion>();
	public JList<CompletionSuggestion> list = new JList<CompletionSuggestion>(model);
	private StringBuffer word = new StringBuffer();
	public List<CompletionSuggestion> words = new ArrayList<CompletionSuggestion>();
	public List<CompletionSuggestion> updatedWords = new ArrayList<CompletionSuggestion>();
	public CompletionSuggestion[] lastResult = { new CompletionSuggestion(CompletionType.Word, "","") };
	
	public boolean isShow = false;
	
	private Border elmBorder = BorderFactory.createEmptyBorder(2,3, 2,3);

	public AutoComplete(JTextPane textpane) {
		super();
		this.textpane = textpane;

		removeAll();
		setOpaque(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFocusable(false);
		list.setFont(Resource.editorFont);
		list.setBackground(Theme.GetColor("editor-background"));
		list.setForeground(Theme.GetColor("editor-foreground"));
		list.setSelectionBackground(Theme.GetColor("editor-selection"));
		list.setSelectionForeground(Theme.GetColor("editor-foreground"));
		list.setCellRenderer(new ListCellRenderer<CompletionSuggestion>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends CompletionSuggestion> list,
					CompletionSuggestion value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = new JLabel(value.title);
				label.setOpaque(true);
				label.setBorder(elmBorder);
				label.setFont(list.getFont());
				
				if (isSelected) {
					label.setBackground(Theme.GetColor("editor-selection"));
				} else {
					label.setBackground(Theme.GetColor("editor-background"));
				}
				label.setForeground(Theme.GetColor("editor-foreground"));
				
				switch(value.type) {
					case Word:
						label.setIcon(Resource.GetImage("internal://completion/word.png"));
						break;
					default:
						label.setIcon(Resource.GetImage("internal://completion/template.png"));
						break;
				}
				
				return label;
			}
		});
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					final int position = textpane.getCaretPosition();
					try {
						textpane.getDocument().insertString(position,
								GetSelectedString().substring(word.length()),
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
				// TODO turn this into a setting: Can Show Autocomplete on key type:
				
				// i've temporary removed this because it forces the Modules to reload their parsers
				// which is something that could be problematic on low end PCs.
				// so, until i find a better way to deal with this i'll just leave 
				// auto completion to be only fired if Control+Space is pressed.
				
				// TODO i should probably not hardcode keyStrokes and actually allow
				// custom key strokes.
				
				// TODO i should probably allow extensions to add their own Key Strokes.
				
				
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
									GetSelectedString()
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
				} else {
					word.delete(0, word.length());
					model.clear();
					setVisible(false);
					isShow = false;
				}/* else if (Character.isLetterOrDigit(e.getKeyChar())) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (word.length() == 0) {
								word.append(getWord(position - 1));
							}
							word.append(e.getKeyChar());
							if (word.length() >= 2) {
								model.clear();
								if (!UpdateWordList()) {
									setVisible(false);
									isShow = false;
									return;
								}
								isShow = true;
								ShowPanel();
							}
						}
					});
				*/
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

	private boolean UpdateWordList() {
		final CompletionSuggestion[] words = Search(word.toString());
		if (words.length < 1) {
			return false;
		}
		for (CompletionSuggestion str : words) {
			model.addElement(str);
		}
		return true;
	}

	public void ShowPanel() {
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
	// i don't need this method anymore, but i'll still leave it. check comment on line 109
	/*
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
	}*/

	public CompletionSuggestion[] Search(String text) {
		
		updatedWords.clear();
		
		List<CompletionSuggestion> result = new ArrayList<CompletionSuggestion>();
		Pattern p = Pattern.compile("([a-zA-Z])(.*([A-Z])[a-z]+)*");
		for (CompletionSuggestion str : words.toArray(new CompletionSuggestion[words.size()])) {
			Matcher m = p.matcher(str.content);
			if (m.find()
					&& text.matches("("
							+ m.group(1)
							+ ".*(?i)"
							+ m.group(3)
							+ "(?-i).*)|("
							+ str.content.substring(0,
									Math.min(text.length(), str.content.length()))
							+ ".*)")) {
				result.add(str);
			}
		}
		
		for (CompletionSuggestion a : updatedWords.toArray(new CompletionSuggestion[updatedWords.size()])) {
			Matcher m = p.matcher(a.content);
			if (m.find()
					&& text.matches("("
							+ m.group(1)
							+ ".*(?i)"
							+ m.group(3)
							+ "(?-i).*)|("
							+ a.content.substring(0,
									Math.min(text.length(), a.content.length()))
							+ ".*)")) {
				result.add(a);
			}
		}
		
		lastResult = result.toArray(new CompletionSuggestion[result.size()]);
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

	private String GetSelectedString() {
		if (list.getSelectedValue() != null) {
			return (String) list.getSelectedValue().content;
		}
		return "";
	}

	public void RequestShow() {
		word.delete(0, word.length());
		int position = textpane.getCaretPosition();
		
		if (textpane.getText().trim().length() == 0 || position == 0) {
			return;
		}
		
		String text = textpane.getText();
		
		String query = "";

		// modify it so that we'll start from the current character
		position = position - 1;
		
		// i'm not really sure how this happens,
		// but in some machines it seems that the caret position
		// goes outside of the text's length. which doesn't makes too much sense..
		if (position > text.length()) {
			Debug.Warn("what the hell! the caret position is outside text length!");
			Debug.Info("Text Length:" + text.length() + " Caret Position: " + position);
			return;
		}
		
		for (int i = position; i > 0; i--) {
			char c = text.charAt(i);
			
			if (Character.isWhitespace(c))
				break;
			
			// add it to start of string.
			query = c + query;
		}
		
		System.out.println("AutoCompletion Query: " + query);
		
		if (query == "")
			return;
		
		word.append(query);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (word.length() >= 2) {
					model.clear();
					if (!UpdateWordList()) {
						setVisible(false);
						isShow = false;
						return;
					}
					isShow = true;
					ShowPanel();
				}
			}
		});
	}
}