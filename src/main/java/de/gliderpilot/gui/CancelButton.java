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
public class CancelButton extends JButton {
	Cancelable owner;

	/**
	 * Creates a new CancelButton object.
	 * 
	 * @param owner DOCUMENT ME!
	 */
	public CancelButton(Cancelable owner) {
		super(Msg.get(Msg.CANCEL));
		this.owner = owner;
		addActionListener(new ActionListener() {
			/**
			 * @see ActionListener#actionPerformed(ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				CancelButton.this.owner.cancel();
			}
		});
	}
}