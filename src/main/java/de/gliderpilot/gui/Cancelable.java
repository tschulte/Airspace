/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public interface Cancelable {
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isCanceled();

	/**
	 * DOCUMENT ME!
	 */
	public void cancel();
}