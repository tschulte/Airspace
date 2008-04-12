/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


import java.awt.Graphics;
import java.awt.geom.AffineTransform;

import de.gliderpilot.geom.Rectangle4D;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public interface Drawable {
	/**
	 * DOCUMENT ME!
	 * 
	 * @param g DOCUMENT ME!
	 * @param r DOCUMENT ME!
	 * @param xform DOCUMENT ME!
	 */
	public void draw(Graphics g, Rectangle4D r, AffineTransform xform);
}