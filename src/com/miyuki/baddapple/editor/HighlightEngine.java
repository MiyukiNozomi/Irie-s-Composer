package com.miyuki.baddapple.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import com.miyuki.baddapple.Theme;

public class HighlightEngine extends DefaultStyledDocument {
	private static final long serialVersionUID = -1915896951381L;

	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offset, str, a);
		UpdateHighlight(true, offset, str, str.length());
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		UpdateHighlight(false, offs, "", len);
	}

	public void SetColoring(int offset, int length, String style) {
		SetColoring(offset, length, style, true);
	}

	public void SetColoring(int offset, int length, String style, boolean replace) {
		setCharacterAttributes(offset, length, Theme.GetEditorStyle(style), replace);
	}

	public void ResetColoring() {
		setCharacterAttributes(0, this.getLength(), Theme.defaultStyle, true);
	}

	public void UpdateHighlight(boolean textAdded, int ofset, String str, int removedLength) {
	}

	/**
	 * This Function is innefective and doesn't properly
	 * tokenize the file.
	 * 
	 * use GetLexer() instead.
	 * */
	@Deprecated
	protected List<Token> GetTokens() {
		String content = "";
		try {
			content = getText(0, getLength()) + " ";
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		List<Token> words = new ArrayList<Token>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();

		for (int index = 0; index < data.length; index++) {
			char ch = data[index];
			if (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')) {
				lastWhitespacePosition = index;
				if (word.length() > 0) {

					words.add(new Token(word, (lastWhitespacePosition - word.length())));

					word = "";
				}
			} else {
				word += ch;
			}
		}
		return words;
	}
	
	/**
	 * Use com.miyuki.baddapple.editor.Lexer.Token instead
	 * this class was used by HighlightEngine#Token, which is a 
	 * Ineffective Lexer. 
	 */
	@Deprecated
	public static class Token {

		public int position;
		public String word;

		public Token(String word, int position) {
			this.position = position;
			this.word = word;
		}
	}
	
	public Lexer GetLexer() {
		return new Lexer(getText());
	}
	
	public String getText() {
		try {
			return getText(0, getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "";
		}
	}
}
