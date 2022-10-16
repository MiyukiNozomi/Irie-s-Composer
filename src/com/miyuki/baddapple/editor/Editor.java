package com.miyuki.baddapple.editor;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;

import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.ui.UIHelper;

public class Editor extends JPanel {
	private static final long serialVersionUID = 1L;

	public JTextPane document;
	public JScrollPane scrollPane;
	public LineNumbers lineNumbers;
	public LinePainter linePainter;

	public Editor() {
		document = new JTextPane();
		document.setBorder(BorderFactory.createEmptyBorder());
		document.setFont(Resource.editorFont);
		document.setEditorKit(new BaseEditorKit());
		document.setCaretColor(Theme.GetColor("editor-caret"));
		document.setForeground(Theme.GetColor("editor-foreground"));
		document.setBackground(Theme.GetColor("editor-background"));
		
		linePainter = new LinePainter(document);

		((AbstractDocument) document.getDocument()).setDocumentFilter(new TabSpacingFilter());

		scrollPane = UIHelper.ManufactureScroll(document);

		LineNumbers lineNumbers = new LineNumbers(document);
		scrollPane.setRowHeaderView(lineNumbers);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

}
