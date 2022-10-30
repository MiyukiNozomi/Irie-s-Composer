package com.miyuki.baddapple.editor.highlighting;

/**
 * a lexer for Non-RegexHighlightEngine based hightlighing engines why did i
 * name these classes with such big names? like, RegexHighlightEngine could
 * easily be just called RegexHighlight, anyway, its java so, no one cares about
 * the size of a class name, right?
 * 
 * anyway, this lexer is literally a port of a lexer i had written in D for a
 * GLSL uniform parser in a proprietary OpenGL project.
 * 
 */

public class Lexer {
	public enum TokenType {
		Identifier, Number, Symbol, EndOfFile
	}

	public class Token {
		public int position;
		public String data;
		public TokenType type;

		public Token(String data, TokenType type, int position) {
			this.data = data;
			this.type = type;
			this.position = position;
		}
	}

	public char current;
	public int position;
	public String rawData;

	public Lexer(String data) {
		this.rawData = data;
		this.position = 0;
		this.NextChar();
	}

	public char NextChar() {
		char last = current;
		if (position >= rawData.length()) {
			current = '\0';
		} else {
			current = rawData.charAt(position++);
		}
		return last;
	}

	public Token NextToken() {
		while (Character.isWhitespace(current) && current != '\0') {
			NextChar();
		}

		int p = this.position;
		if (current == '\0')
			return new Token("", TokenType.EndOfFile, p);

		if (Character.isAlphabetic(current)) {
			String acc = "";
			while ((Character.isAlphabetic(current) || Character.isDigit(current) || current == '_')
					&& current != '\0') {
				acc += NextChar();
			}
			return new Token(acc, TokenType.Identifier, p);
		}
		
		if (Character.isDigit(current)) {
			String acc = "";
			while (Character.isDigit(current) && current != '\0') {
				acc += NextChar();
			}
			return new Token(acc, TokenType.Number, p);
		}

		// must be a symbol
		return new Token(NextChar() + "", TokenType.Symbol, p);
	}
}
