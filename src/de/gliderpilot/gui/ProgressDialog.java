/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JProgressBar;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class ProgressDialog extends JDialog implements Cancelable {
	Cancelable cancelable;
	JProgressBar progressBar;

	/**
	 * Creates a new ProgressDialog object.
	 * 
	 * @param owner DOCUMENT ME!
	 * @param cancelable DOCUMENT ME!
	 * @param min DOCUMENT ME!
	 * @param max DOCUMENT ME!
	 */
	public ProgressDialog(Frame owner, Cancelable cancelable, int min, int max) {
		super(owner);
		this.cancelable = cancelable;
		getContentPane().setLayout(new FlowLayout());
		progressBar = new JProgressBar(min, max);

		if (min == max) {
			progressBar.setIndeterminate(true);
		}

		CancelButton cancelButton = new CancelButton(this);

		getContentPane().add(progressBar);
		getContentPane().add(cancelButton);
		pack();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
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
	 * DOCUMENT ME!
	 * 
	 * @param value DOCUMENT ME!
	 */
	public void setValue(int value) {
		progressBar.setValue(value);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void cancel() {
		cancelable.cancel();
		setVisible(false);
	}
}