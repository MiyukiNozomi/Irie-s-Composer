package com.miyuki.baddapple.editor;

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
		Identifier, Symbol, EndOfFile
	}

	public class Token {
		String data;
		TokenType type;

		public Token(String data, TokenType type) {
			this.data = data;
			this.type = type;
		}
	}

	char current;
	int position;
	String rawData;

	public Lexer(String data) {
		this.rawData = data;
		this.position = 0;
		this.NextChar();
	}

	char NextChar() {
		char last = current;
		if (position >= rawData.length()) {
			current = '\0';
		} else {
			current = rawData.charAt(position++);
		}
		return last;
	}

	Token NextToken() {
		while (Character.isWhitespace(current) && current != '\0') {
			NextChar();
		}

		if (current == '\0')
			return new Token("", TokenType.EndOfFile);

		if (Character.isAlphabetic(current)) {
			String acc = "";
			while ((Character.isAlphabetic(current) || Character.isDigit(current) || current == '_')
					&& current != '\0') {
				acc += NextChar();
			}
			return new Token(acc, TokenType.Identifier);
		}

		// must be a symbol
		
		return new Token(current + "", TokenType.Symbol);
	}

}
