package com.miyuki.baddapple.editor.highlighting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.editor.Editor;
import com.miyuki.baddapple.editor.completion.CompletionSuggestion;
import com.miyuki.baddapple.editor.completion.CompletionSuggestion.CompletionType;

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
	protected List<LegacyToken> GetTokens() {
		String content = "";
		try {
			content = getText(0, getLength()) + " ";
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		List<LegacyToken> words = new ArrayList<LegacyToken>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();

		for (int index = 0; index < data.length; index++) {
			char ch = data[index];
			if (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')) {
				lastWhitespacePosition = index;
				if (word.length() > 0) {

					words.add(new LegacyToken(word, (lastWhitespacePosition - word.length())));

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
	public static class LegacyToken {

		public int position;
		public String word;

		public LegacyToken(String word, int position) {
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

	public void RequestCompletion(Editor e) {
		// regex to only grab words
		String regex = "([^a-zA-Z']+)'*\\1*";
		// split that thing by the regex
		String[] split = getText().split(regex);
		
		// turn it into a list, remove duplicates
		List<String> words = Arrays.asList(split);
		List<String> withoutDuplicates = words.stream().distinct().collect(Collectors.toList());
		
		// now turn it into Completion Suggestions
		List<CompletionSuggestion> suggestions = new ArrayList<>();

		for (String s : withoutDuplicates) {
			suggestions.add(new CompletionSuggestion(CompletionType.Word, s, s));
		}

		// send it to the auto complete window.
		e.autoComplete.words = suggestions;
	}
}
