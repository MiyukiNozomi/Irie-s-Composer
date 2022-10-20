package com.miyuki.baddapple.editor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;

import com.miyuki.baddapple.Theme;

public class RegexHighlightEngine extends HighlightEngine {
	private static final long serialVersionUID = 1L;

	public HighlightRegex[] regexes;

	public RegexHighlightEngine(HighlightRegex... regexes) {
		this.regexes = regexes;
	}

	public static class HighlightRegex {
		public Pattern regex;
		public String style;

		public HighlightRegex(String regex, String style) {
			this.regex = Pattern.compile(regex);
			this.style = style;
		}

		public HighlightRegex(String regex, int flags, String style) {
			this.regex = Pattern.compile(regex, flags);
			this.style = style;
		}
	}

	@Override
	public void UpdateHighlight(boolean textAdded, int ofset, String str, int removedLength) {
		this.ResetColoring();
		try {
			this.ExecuteRegexes(getText(0, getLength()));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void ExecuteRegexes(String text) {

		for (HighlightRegex regex : regexes) {
			Matcher matcher = regex.regex.matcher(text);

			while (matcher.find()) {
				setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(),
						Theme.GetEditorStyle(regex.style), true);
			}
		}
	}
}
