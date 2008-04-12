/**
 * Created on Oct 13, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class StatusBar extends Box {
	private Border border;
	private boolean isFirstElement;

	public StatusBar() {
		super(BoxLayout.X_AXIS);

		border = BorderFactory.createLoweredBevelBorder();
		isFirstElement = true;
	}

	public void addGlue() {
		JPanel glue;
		glue = new JPanel();
		glue.add(Box.createGlue(), BorderLayout.CENTER);
		add(glue);
	}

	public Component add(JComponent component) {
		if (isFirstElement) {
			isFirstElement = false;
		} else {
			super.add(Box.createHorizontalStrut(2));
		}
		component.setBorder(border);
		return super.add(component);
	}
}
