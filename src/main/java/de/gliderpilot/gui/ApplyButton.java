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
public class ApplyButton extends JButton {
	Applyable owner;

	/**
	 * Creates a new ApplyButton object.
	 * 
	 * @param owner DOCUMENT ME!
	 */
	public ApplyButton(Applyable owner) {
		super(Msg.get(Msg.APPLY));
		this.owner = owner;
		addActionListener(new ActionListener() {
			/**
			 * @see ActionListener#actionPerformed(ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				ApplyButton.this.owner.apply();
			}
		});
	}
}