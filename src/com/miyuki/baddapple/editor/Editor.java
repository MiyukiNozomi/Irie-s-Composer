package com.miyuki.baddapple.editor;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.AbstractDocument;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Debug;
import com.miyuki.baddapple.Registry;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.editor.CompletionSuggestion.CompletionType;
import com.miyuki.baddapple.ui.TabCompView;
import com.miyuki.baddapple.ui.UIHelper;

public class Editor extends JPanel {
	private static final long serialVersionUID = 1L;

	public File targetFile;
	
	public JTextPane document;
	public JScrollPane scrollPane;
	public LineNumbers lineNumbers;
	public LinePainter linePainter;
	public AutoComplete autoComplete;

	public Editor() {
		document = new NoWrapJTextPane();
		document.addKeyListener(new AutoClose(document));
		document.setDocument(new HighlightEngine());
		document.setBorder(BorderFactory.createEmptyBorder());
		document.setFont(Resource.editorFont);
		document.setEditorKit(new BaseEditorKit());
		document.setSelectionColor(Theme.GetColor("editor-selection"));
		document.setSelectedTextColor(Theme.GetColor("editor-foreground"));
		document.setCaretColor(Theme.GetColor("editor-caret"));
		document.setForeground(Theme.GetColor("editor-foreground"));
		document.setBackground(Theme.GetColor("editor-background"));
		
		autoComplete = new AutoComplete(document);
		
		document.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					document.repaint();
					if (autoComplete.isShow)
						e.consume();
					return;
				}
				if (e.getKeyCode() != KeyEvent.VK_LEFT && e.getKeyCode() != KeyEvent.VK_RIGHT) {
					BadApple.Get.tabPanel.SetTitleAt(Editor.this, "*"+targetFile.getName());	
					String regex = "([^a-zA-Z']+)'*\\1*";
					String[] split = document.getText().split(regex);
					List<String> words = Arrays.asList(split);
			        List<String> withoutDuplicates = words.stream().distinct().collect(Collectors.toList());
			        List<CompletionSuggestion> suggestions = new ArrayList<>();
			        
			        for(String s : withoutDuplicates) {
			        	suggestions.add(new CompletionSuggestion(CompletionType.Word, s, s));
			        }
			        
					autoComplete.words = suggestions;
				}
			}
		});
		
		linePainter = new LinePainter(document);

		((AbstractDocument) document.getDocument()).setDocumentFilter(new TabSpacingFilter());

		scrollPane = UIHelper.ManufactureScroll(document);

		LineNumbers lineNumbers = new LineNumbers(document);
		scrollPane.setRowHeaderView(lineNumbers);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		DoStrokes();
	}


	private void DoStrokes() {
		KeyStroke save = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);

		document.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(save, "saveKeyStroke");
		document.getActionMap().put("saveKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SaveFile();
			}
		});
	}
	
	public void SaveFile() {
		try {
			if (targetFile == null)
				return;
			Files.write(Paths.get(targetFile.getPath()), document.getText().getBytes());
			Debug.Info("Successfully saved file!");
			// detail.setText("Saved!");
			//Footer.GetInstance().getLineAndColumnLbl().setText("Saved!");
			BadApple.Get.tabPanel.SetTitleAt(this, targetFile.getName());
		} catch (Exception e) {
			Debug.Error("Unable to save file: " + targetFile.getPath());
			e.printStackTrace();
		}
	}
	
	public void OpenFile(File f) {
		this.targetFile = f;
		
		String ext = f.getName();
		if (ext.indexOf(".") != -1) {
			ext = ext.substring(ext.indexOf(".") + 1);
			Debug.Info("Its extension is: " + ext);
			HighlightEngine engine = Registry.GetEngineFor(ext);
			if (engine != null)
				document.setDocument(engine);
		}
		
		document.setText(Resource.GetFile(f.getPath()));
		JTabbedPane p = BadApple.Get.tabPanel.tabbedPanel;
		int index = p.indexOfComponent(this);
		TabCompView view = (TabCompView) p.getTabComponentAt(index);
		
		p.setTitleAt(index, f.getName());
		view.titleLbl.setText(f.getName());
	}
}
