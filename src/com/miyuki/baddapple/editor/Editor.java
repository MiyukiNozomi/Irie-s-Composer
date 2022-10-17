package com.miyuki.baddapple.editor;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.ui.TabCompView;
import com.miyuki.baddapple.ui.UIHelper;

public class Editor extends JPanel {
	private static final long serialVersionUID = 1L;

	public File targetFile;
	
	public JTextPane document;
	public JScrollPane scrollPane;
	public LineNumbers lineNumbers;
	public LinePainter linePainter;

	public Editor() {
		document = new NoWrapJTextPane();
		document.setBorder(BorderFactory.createEmptyBorder());
		document.setFont(Resource.editorFont);
		document.setEditorKit(new BaseEditorKit());
		document.setSelectionColor(Theme.GetColor("editor-selection"));
		document.setSelectedTextColor(Theme.GetColor("editor-foreground"));
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
	
	public void openFile(File f) {
		this.targetFile = f;
		
		document.setText(Resource.GetFile(f.getPath()));
		JTabbedPane p = BadApple.Get.tabPanel.tabbedPanel;
		int index = p.indexOfComponent(this);
		TabCompView view = (TabCompView) p.getTabComponentAt(index);
		
		p.setTitleAt(index, f.getName());
		view.titleLbl.setText(f.getName());
	}
}
