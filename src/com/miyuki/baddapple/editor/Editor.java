package com.miyuki.baddapple.editor;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Debug;
import com.miyuki.baddapple.Language;
import com.miyuki.baddapple.Registry;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.editor.completion.AutoClose;
import com.miyuki.baddapple.editor.completion.AutoComplete;
import com.miyuki.baddapple.editor.highlighting.HighlightEngine;
import com.miyuki.baddapple.ui.TabCompView;
import com.miyuki.baddapple.ui.UIHelper;

public class Editor extends JPanel {
	private static final long serialVersionUID = 1L;

	public boolean saved = true;
	
	public File targetFile;

	public JTextPane document;
	public JScrollPane scrollPane;
	public LineNumbers lineNumbers;
	public LinePainter linePainter;
	public AutoComplete autoComplete;
	public UndoManager undoManager;

	public Editor() {
		document = new NoWrapJTextPane();
		document.addKeyListener(new AutoClose(document));

		document.setEditorKit(new BaseEditorKit());
		document.setDocument(new HighlightEngine());
		
		document.setBorder(BorderFactory.createEmptyBorder());
		document.setFont(Resource.editorFont);
		document.setSelectionColor(Theme.GetColor("editor-selection"));
		document.setSelectedTextColor(Theme.GetColor("editor-foreground"));
		document.setCaretColor(Theme.GetColor("editor-caret"));
		document.setForeground(Theme.GetColor("editor-foreground"));
		document.setBackground(Theme.GetColor("editor-background"));

		undoManager = new UndoManager();
		document.getDocument().addUndoableEditListener(undoManager);

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
					if (saved) {
						BadApple.Get.tabPanel.SetTitleAt(Editor.this, "*" + targetFile.getName());
						saved = false;
					}
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
		MakeStrokes(KeyEvent.VK_S, "saveKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SaveFile();
			}
		});
		MakeStrokes(KeyEvent.VK_Z, "undoKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (undoManager.canUndo())
						undoManager.undo();
					else
						Debug.Info("Cannot Undo!");
				} catch (CannotUndoException err) {
					Debug.Info("Cannot Undo!");
				}
			}
		});
		MakeStrokes(KeyEvent.VK_Y, "redoKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (undoManager.canRedo())
						undoManager.redo();
					else
						Debug.Info("Cannot Redo!");
				} catch (CannotUndoException err) {
					Debug.Info("Cannot Redo!");
				}
			}
		});
		MakeStrokes(KeyEvent.VK_SPACE, "autoCompleteKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((HighlightEngine) document.getDocument()).RequestCompletion(Editor.this);
				autoComplete.RequestShow();
			}
		});
		
		for (EditorAction action : Registry.editorActions.values()) {
			document.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(action.keyStroke, action.actionName);
			document.getActionMap().put(action.actionName, action.action);
		}
	}

	private void MakeStrokes(int key, String name, AbstractAction action) {
		KeyStroke stroke = KeyStroke.getKeyStroke(key, Event.CTRL_MASK);

		document.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, name);
		document.getActionMap().put(name, action);
	}

	public void SaveFile() {
		try {
			if (targetFile == null)
				return;
			Files.write(Paths.get(targetFile.getPath()), document.getText().getBytes());
			Debug.Info("Successfully saved file!");
			// detail.setText("Saved!");
			// Footer.GetInstance().getLineAndColumnLbl().setText("Saved!");
			BadApple.Get.tabPanel.SetTitleAt(this, targetFile.getName());
			saved = true;
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
			if (engine != null) {
				document.setDocument(engine);
				((AbstractDocument) document.getDocument()).setDocumentFilter(new TabSpacingFilter());
			}
		}
		saved = true;
		try {
			document.setText(new String(Files.readAllBytes(Paths.get(f.getPath()))));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(BadApple.Get,
					Language.GetKey("error-opening").replace("%F%", f.getName()), Language.GetKey("error-title"),
					JOptionPane.ERROR_MESSAGE, null);
		}
		
		// resetting it
		undoManager.discardAllEdits();
		
		JTabbedPane p = BadApple.Get.tabPanel.tabbedPanel;
		int index = p.indexOfComponent(this);
		TabCompView view = (TabCompView) p.getTabComponentAt(index);

		p.setTitleAt(index, f.getName());
		view.titleLbl.setText(f.getName());
	}
}
