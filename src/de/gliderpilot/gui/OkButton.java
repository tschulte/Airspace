/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class OkButton extends JButton {
	ApplyAndCloseable owner;

	/**
	 * Creates a new OkButton object.
	 * 
	 * @param owner DOCUMENT ME!
	 */
	public OkButton(ApplyAndCloseable owner) {
		super(Msg.get(Msg.OK));
		this.owner = owner;
		addActionListener(new ActionListener() {
			/**
			 * @see ActionListener#actionPerformed(ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				OkButton.this.owner.applyAndClose();
			}
		});
	}
}