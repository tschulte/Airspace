/**
 * Created on Oct 27, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.gui;

import java.lang.reflect.Method;

import javax.swing.JCheckBox;

/**
 * @author Tobias Schulte
 *
 */
public class GCheckBox extends JCheckBox implements Applyable, Cancelable {
	boolean value;
	boolean isCanceled = false;
	
	Object src;
	Method method;
	
	public GCheckBox(String text, boolean value, Object src, String methodName) {
		super(text, value);
		this.value = value;
		this.src = src;
		try {
			this.method = src.getClass().getMethod(methodName, new Class[] {Boolean.class});
		} catch (NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}

	/**
	 * @see de.gliderpilot.gui.Applyable#apply()
	 */
	public void apply() {
		value = isSelected();
		if (method != null) {
			try {
				method.invoke(src, new Object[] {new Boolean(value)});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see de.gliderpilot.gui.Cancelable#cancel()
	 */
	public void cancel() {
		setSelected(value);
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
	public void setSelected(boolean v) {
		super.setSelected(v);
		value = v;
	}

}
