/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui.event;


import de.gliderpilot.geom.Rectangle4D;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class DisplayedRectChangeEvent {
	Rectangle4D rect;

	/**
	 * Creates a new DisplayedRectChangeEvent object.
	 * 
	 * @param rect DOCUMENT ME!
	 */
	public DisplayedRectChangeEvent(Rectangle4D rect) {
		this.rect = rect;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Rectangle4D getRect() {
		return rect;
	}

	public interface Listener {
		public void displayedRectChanged(DisplayedRectChangeEvent evt);
	}

	public interface Source {
		public void addDisplayedRectListener(Listener listener);
	}
}