/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.airspace;


import java.util.Enumeration;

import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.gui.Drawable;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public interface AirspaceContainer extends Drawable {
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Rectangle4D getBounds4D();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param o DOCUMENT ME!
	 */
	public void addElement(Object o);

	/**
	 * DOCUMENT ME!
	 */
	public void close();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Enumeration elements();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean open();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int size();

	/**
	 * DOCUMENT ME!
	 */
	public void trimToSize();
}