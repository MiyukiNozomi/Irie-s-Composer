package com.miyuki.baddapple.editor;

public class CompletionSuggestion {

	public String content;
	public String title;
	public CompletionType type;
	
	public CompletionSuggestion(CompletionType type, String title, String content) {
		this.type = type;
		this.title = title;
		this.content = content;
	} 
	
	public static enum CompletionType {
		Word,
		Template
	}
	
}
