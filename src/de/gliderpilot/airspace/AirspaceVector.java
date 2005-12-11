/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.airspace;


import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.Vector;

import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.gui.Drawable;



/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author Tobias Schulte
 */
public class AirspaceVector extends Vector implements AirspaceContainer {
	private Rectangle4D rect;

	/**
	 * Creates a new AirspaceVector object.
	 */
	public AirspaceVector() {
		super();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Rectangle4D getBounds4D() {
		return rect;
	}

	/**
	 * @see Vector#addElement(Object)
	 */
	public synchronized void addElement(Object obj) {
		super.addElement(obj);

		if (obj instanceof AirspaceElement) {
			AirspaceElement element = (AirspaceElement) obj;
			Rectangle2D rect = element.getBounds2D();

			if (this.rect == null) {
				this.rect = new Rectangle4D();
				this.rect.setFrame(rect);
			} else if (!this.rect.contains(rect)) {
				Rectangle2D.union(this.rect, rect, this.rect);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	public void close() {
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param g DOCUMENT ME!
	 * @param r DOCUMENT ME!
	 * @param xform DOCUMENT ME!
	 */
	public void draw(Graphics g, Rectangle4D r, AffineTransform xform) {
		Enumeration e = elements();

		while (e.hasMoreElements()) {
			((Drawable) e.nextElement()).draw(g, r, xform);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean open() {
		return true;
	}
}