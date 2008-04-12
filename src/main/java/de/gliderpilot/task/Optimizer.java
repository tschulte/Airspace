/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import de.gliderpilot.gui.Cancelable;
import de.gliderpilot.preferences.ExternalClass;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public interface Optimizer extends Cancelable, Runnable, ExternalClass {
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getMax();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getMin();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getProgress();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param track DOCUMENT ME!
	 */
	public void setTrack(TrackLog track);
}