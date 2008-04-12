/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class CancelApplyPanel extends JPanel implements Applyable, Cancelable {
	/**
	 * Constructor for CancelApplyPanel.
	 * 
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public CancelApplyPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/**
	 * Constructor for CancelApplyPanel.
	 * 
	 * @param layout
	 */
	public CancelApplyPanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 * Constructor for CancelApplyPanel.
	 * 
	 * @param isDoubleBuffered
	 */
	public CancelApplyPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * Constructor for CancelApplyPanel.
	 */
	public CancelApplyPanel() {
		super();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isCanceled() {
		return false;
	}

	/**
	 * @see Applyable#apply()
	 */
	public void apply() {
		int componentCount = getComponentCount();

		for (int i = 0; i < componentCount; i++) {
			Component comp = getComponent(i);

			if (comp instanceof Applyable) {
				((Applyable) comp).apply();
			}
		}
	}

	/**
	 * @see Cancelable#cancel()
	 */
	public void cancel() {
		int componentCount = getComponentCount();

		for (int i = 0; i < componentCount; i++) {
			Component comp = getComponent(i);

			if (comp instanceof Cancelable) {
				((Cancelable) comp).cancel();
			}
		}
	}
}