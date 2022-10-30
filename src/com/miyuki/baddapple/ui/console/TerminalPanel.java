package com.miyuki.baddapple.ui.console;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JPanel;
import javax.swing.JTextPane;

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
					this.panel.panel.setText(this.panel.panel.getText() + ((char)reader.read()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}