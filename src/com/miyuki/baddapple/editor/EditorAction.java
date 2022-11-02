package com.miyuki.baddapple.editor;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class EditorAction {

	public String actionName;
	public KeyStroke keyStroke;
	public AbstractAction action;

	/**
	 * the 'actionName' parameter is the action's name, don't use spaces.
	 * 
	 * the second parameter is the actual Action
	 * 
	 * this class is supposed to be passed to Registry#RegisterEditorAction()
	 */
	public EditorAction(KeyStroke defaultKeyStroke, String actionName, AbstractAction action) {
		this.actionName = actionName;
		this.action = action;
		this.keyStroke = defaultKeyStroke;
	}
}
