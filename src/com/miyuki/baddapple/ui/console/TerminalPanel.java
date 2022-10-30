package com.miyuki.baddapple.ui.console;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.miyuki.baddapple.BadApple;
import com.miyuki.baddapple.Debug;
import com.miyuki.baddapple.Resource;
import com.miyuki.baddapple.Theme;
import com.miyuki.baddapple.editor.NoWrapJTextPane;
import com.miyuki.baddapple.ui.UIHelper;

public class TerminalPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public Process process;
	public Thread inputReader, errReader;
	
	public boolean terminalReleased;

	protected TerminalProtector protector;
	protected BufferedWriter stdin;
	protected JTextPane panel;

	public TerminalPanel() {
		setLayout(new BorderLayout());
		panel = new NoWrapJTextPane();
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				EmulateKey(e);
			}
		});
		panel.setBackground(Theme.GetColor("panel-background"));
		panel.setForeground(Theme.GetColor("panel-foreground"));
		panel.setCaretColor(Theme.GetColor("panel-foreground"));
		panel.setSelectedTextColor(Theme.GetColor("panel-background"));
		panel.setSelectionColor(Theme.GetColor("panel-foreground"));
		panel.setFont(Resource.editorFont);
		
		protector = new TerminalProtector();
		
		((AbstractDocument) panel.getDocument()).setDocumentFilter(protector);

		add(UIHelper.ManufactureScroll(panel), BorderLayout.CENTER);
		try {
			process = Runtime.getRuntime().exec(BadApple.Get.settings.terminalEmulator);

			this.terminalReleased = false;

			stdin = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			
			inputReader = new Thread(new TerminalStreamReader(this, process.getInputStream()));
			errReader = new Thread(new TerminalStreamReader(this, process.getErrorStream()));

			inputReader.start();
			errReader.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void EmulateKey(KeyEvent e) {
		e.consume();
		try {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				Debug.Info("Enter Key Pressed!");
				stdin.newLine();
				stdin.flush();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				stdin.write("\r");
				stdin.flush();
				return;
			}
			// yeah, i shouldn't really need to do this. but sure.
			this.panel.setText(this.panel.getText() + e.getKeyChar());
			stdin.write(e.getKeyChar());
			stdin.flush();
		} catch (IOException e1) {
			Debug.Warn("Unable to pass keypress to terminal process!");
			e1.printStackTrace();
		}
	}

	public class TerminalStreamReader implements Runnable {

		BufferedReader reader;
		TerminalPanel panel;

		public TerminalStreamReader(TerminalPanel panel, InputStream stream) {
			reader = new BufferedReader(new InputStreamReader(stream));
			this.panel = panel;
		}

		@Override
		public void run() {
			while (!panel.terminalReleased) {
				try {
					int i = reader.read();
					char c = (char) i;
					
					//TODO support cls
					// and fix the weird bugs
					this.panel.panel.setText(this.panel.panel.getText() + c);
					
					int position = this.panel.panel.getText().length() - 1;
					// move caret to end of stream.
					//this.panel.panel.setCaretPosition(position);
					this.panel.protector.promptPosition = position;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected class TerminalProtector extends DocumentFilter {
		public int promptPosition;
		public boolean enabled;
		
	    public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr)
	            throws BadLocationException {
	        if (!enabled)
	            super.insertString(fb, offset, string, attr);
	        
	    	if (offset >= promptPosition) {
	            super.insertString(fb, offset, string, attr);
	        }
	    }

	    public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
	    	 if (!enabled)
		           super.remove(fb, offset, length);
		        
	    	if (offset >= promptPosition) {
	            super.remove(fb, offset, length);
	        }
	    }

	    public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
	            throws BadLocationException {
	        if (offset >= promptPosition) {
	            super.replace(fb, offset, length, text, attrs);
	        }
	    }
	}
}