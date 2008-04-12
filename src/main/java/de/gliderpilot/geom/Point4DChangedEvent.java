/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.geom;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Point4DChangedEvent {
	Point4D newPoint;

	/**
	 * Creates a new Point4DChangedEvent object.
	 * 
	 * @param newPoint DOCUMENT ME!
	 */
	public Point4DChangedEvent(Point4D newPoint) {
		this.newPoint = newPoint;
	}

	/**
	 * Returns the newPoint.
	 * 
	 * @return Point4D
	 */
	public Point4D getNewPoint() {
		return newPoint;
	}

	public interface Listener {
		public void pointChanged(Point4DChangedEvent e);
	}

	public interface Source {
		public void addPointChangedListener(Listener l);
	}
}