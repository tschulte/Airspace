/**
 * Created on Oct 27, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.gui;

import java.lang.reflect.Method;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * @author Tobias Schulte
 *
 */
public class GTextField extends JTextField implements Applyable, Cancelable {
	String value;
	boolean isCanceled = false;
	
	Object src;
	Method method;
	
	/**
	 * Constructor for GTextField.
	 */
	public GTextField() {
		super();
	}
	
	public GTextField(String text, int columns, Object src, String methodName) {
		this(text, columns);
		this.src = src;
		try {
			this.method = src.getClass().getMethod(methodName, new Class[] {String.class});
		} catch (NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}

	/**
	 * Constructor for GTextField.
	 * @param columns
	 */
	public GTextField(int columns) {
		super(columns);
	}

	/**
	 * Constructor for GTextField.
	 * @param text
	 * @param columns
	 */
	public GTextField(String text, int columns) {
		super(text, columns);
	}

	/**
	 * Constructor for GTextField.
	 * @param doc
	 * @param text
	 * @param columns
	 */
	public GTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	public GTextField(String text) {
		super(text);
	}

	/**
	 * @see de.gliderpilot.gui.Applyable#apply()
	 */
	public void apply() {
		value = getText();
		if (method != null) {
			try {
				method.invoke(src, new Object[] {value});
			} catch (Exception e) {
				throw new IllegalArgumentException(value);
			}
		}
	}

	/**
	 * @see de.gliderpilot.gui.Cancelable#cancel()
	 */
	public void cancel() {
		setText(value);
	}

	/**
	 * @see de.gliderpilot.gui.Cancelable#isCanceled()
	 */
	public boolean isCanceled() {
		return isCanceled;
	}

	/**
	 * @see javax.swing.text.JTextComponent#setText(String)
	 */
	public void setText(String t) {
		super.setText(t);
		value = t;
	}

}
