package com.miyuki.baddapple.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.Utilities;

import com.miyuki.baddapple.Theme;

public class LineNumbers extends JPanel implements CaretListener, DocumentListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	public final static float LEFT = 0.0f;
	public final static float CENTER = 0.5f;
	public final static float RIGHT = 1.0f;

	private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

	private JTextComponent component;

	private boolean updateFont;
	private int borderGap;
	private Color currentLineForeground, lineNumberForeground, lineNumberCurrentForeground;
	private float digitAlignment;
	private int minimumDisplayDigits;

	private int lastDigits, lastHeight, lastLine;

	private HashMap<String, FontMetrics> fonts;

	public LineNumbers(JTextComponent component) {
		this(component, 3);
	}

	public LineNumbers(JTextComponent component, int minimumDisplayDigits) {
		this.component = component;

		setFont(component.getFont());

		setBorderGap(5);
		setCurrentLineForeground(Theme.GetColor("line-numbers-current"));
		setDigitAlignment(CENTER);
		setMinimumDisplayDigits(minimumDisplayDigits);

		component.getDocument().addDocumentListener(this);
		component.addCaretListener(this);
		component.addPropertyChangeListener("font", this);

		lineNumberCurrentForeground = Theme.GetColor("line-numbers-current");
		lineNumberForeground = Theme.GetColor("line-numbers-foreground");

		setBackground(Theme.GetColor("line-numbers-background"));
	}

	public boolean getUpdateFont() {
		return updateFont;
	}

	public void setUpdateFont(boolean updateFont) {
		this.updateFont = updateFont;
	}

	public int getBorderGap() {
		return borderGap;
	}

	public void setBorderGap(int borderGap) {
		this.borderGap = borderGap;
		setBorder(BorderFactory.createEmptyBorder());
		lastDigits = 0;
		setPreferredWidth();
	}

	public Color getCurrentLineForeground() {
		return currentLineForeground == null ? getForeground() : currentLineForeground;
	}

	public void setCurrentLineForeground(Color currentLineForeground) {
		this.currentLineForeground = currentLineForeground;
	}

	public float getDigitAlignment() {
		return digitAlignment;
	}

	public void setDigitAlignment(float digitAlignment) {
		this.digitAlignment = digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
	}

	public int getMinimumDisplayDigits() {
		return minimumDisplayDigits;
	}

	public void setMinimumDisplayDigits(int minimumDisplayDigits) {
		this.minimumDisplayDigits = minimumDisplayDigits;
		setPreferredWidth();
	}

	private void setPreferredWidth() {
		Element root = component.getDocument().getDefaultRootElement();
		int lines = root.getElementCount();
		int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

		if (lastDigits != digits) {
			lastDigits = digits;
			FontMetrics fontMetrics = getFontMetrics(getFont());
			int width = fontMetrics.charWidth('0') * digits;
			Insets insets = getInsets();
			int preferredWidth = insets.left + insets.right + width + 50;

			Dimension d = getPreferredSize();
			d.setSize(preferredWidth, HEIGHT);
			setPreferredSize(d);
			setSize(d);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
		Insets insets = getInsets();
		int availableWidth = getSize().width - insets.left - insets.right;

		Rectangle clip = g.getClipBounds();
		int rowStartOffset = component.viewToModel(new Point(0, clip.y));
		int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));

		while (rowStartOffset <= endOffset) {
			try {

				g.setColor(isCurrentLine(rowStartOffset) ? lineNumberCurrentForeground : lineNumberForeground);
				
				String lineNumber = getTextLineNumber(rowStartOffset);
				int stringWidth = fontMetrics.stringWidth(lineNumber);
				int x = getOffsetX(availableWidth, stringWidth) + insets.left;
				int y = getOffsetY(rowStartOffset, fontMetrics);
				g.drawString(lineNumber, x - 2, y + 1);

				//TODO fix this.
			//	g.fillRect(0, y, 2, fontMetrics.getHeight() + 3);
				rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
			} catch (Exception e) {
				break;
			}
		}
	}

	private boolean isCurrentLine(int rowStartOffset) {
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();

		if (root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition))
			return true;
		else
			return false;
	}

	protected String getTextLineNumber(int rowStartOffset) {
		Element root = component.getDocument().getDefaultRootElement();
		int index = root.getElementIndex(rowStartOffset);
		Element line = root.getElement(index);

		if (line.getStartOffset() == rowStartOffset)
			return String.valueOf(index + 1);
		else
			return "";
	}

	protected int getLineNumber(int rowStartOffset) {
		Element root = component.getDocument().getDefaultRootElement();
		int index = root.getElementIndex(rowStartOffset);
		Element line = root.getElement(index);

		if (line.getStartOffset() == rowStartOffset)
			return index + 1;
		else
			return 0;
	}

	private int getOffsetX(int availableWidth, int stringWidth) {
		return (int) ((availableWidth - stringWidth) * digitAlignment);
	}

	private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics) throws BadLocationException {
		Rectangle r = component.modelToView(rowStartOffset);
		int lineHeight = fontMetrics.getHeight();
		int y = r.y + r.height;
		int descent = 0;

		if (r.height == lineHeight) {
			descent = fontMetrics.getDescent();
		} else {
			if (fonts == null)
				fonts = new HashMap<String, FontMetrics>();

			Element root = component.getDocument().getDefaultRootElement();
			int index = root.getElementIndex(rowStartOffset);
			Element line = root.getElement(index);

			for (int i = 0; i < line.getElementCount(); i++) {
				Element child = line.getElement(i);
				AttributeSet as = child.getAttributes();
				String fontFamily = (String) as.getAttribute(StyleConstants.FontFamily);
				Integer fontSize = (Integer) as.getAttribute(StyleConstants.FontSize);
				String key = fontFamily + fontSize;

				FontMetrics fm = fonts.get(key);

				if (fm == null) {
					Font font = new Font(fontFamily, Font.PLAIN, fontSize);
					fm = component.getFontMetrics(font);
					fonts.put(key, fm);
				}

				descent = Math.max(descent, fm.getDescent());
			}
		}

		return y - descent;
	}

	@Override
	public void caretUpdate(CaretEvent e) {

		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		int currentLine = root.getElementIndex(caretPosition);

		if (lastLine != currentLine) {
			getParent().repaint();
			lastLine = currentLine;
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		documentChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		documentChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		documentChanged();
	}

	private void documentChanged() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					int endPos = component.getDocument().getLength();
					Rectangle rect = component.modelToView(endPos);

					if (rect != null && rect.y != lastHeight) {
						setPreferredWidth();
						getParent().repaint();
						lastHeight = rect.y;
					}
				} catch (BadLocationException ex) { /* nothing to do */
				}
			}
		});
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof Font) {
			if (updateFont) {
				Font newFont = (Font) evt.getNewValue();
				setFont(newFont);
				lastDigits = 0;
				setPreferredWidth();
			} else {
				getParent().repaint();
			}
		}
	}
}
