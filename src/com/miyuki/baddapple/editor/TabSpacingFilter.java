package com.miyuki.baddapple.editor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;

public class TabSpacingFilter extends DocumentFilter {

	public void insertString(FilterBypass fb, int offs, String str,
			AttributeSet a) throws BadLocationException {
		if ("\n".equals(str))
			str = addWhiteSpace(fb.getDocument(), offs);

		super.insertString(fb, offs, str, a);
	}

	public void replace(FilterBypass fb, int offs, int length, String str,
			AttributeSet a) throws BadLocationException {
		if ("\n".equals(str))
			str = addWhiteSpace(fb.getDocument(), offs);

		super.replace(fb, offs, length, str, a);
	}

	private String addWhiteSpace(Document doc, int offset)
			throws BadLocationException {
		StringBuilder whiteSpace = new StringBuilder("\n");
		Element root = doc.getDefaultRootElement();
		int line = root.getElementIndex(offset);
		int length = doc.getLength();

		for (int i = root.getElement(line).getStartOffset(); i < length; i++) {
			String temp = doc.getText(i, 1);

			if (temp.equals(" ") || temp.equals("\t")) {
				whiteSpace.append(temp);
			} else
				break;
		}

		return whiteSpace.toString();
	}
}