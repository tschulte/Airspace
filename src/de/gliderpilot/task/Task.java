/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import java.util.Iterator;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.gui.Drawable;
import de.gliderpilot.trace.Dumpable;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public interface Task extends Cloneable, Drawable, Dumpable {
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getDistance();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Point4D[] getPoints();
	
	
	public Iterator iterator();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getScore();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param p DOCUMENT ME!
	 * @param index DOCUMENT ME!
	 */
	public void replaceTaskPoint(Point4D p, int index);
	public void replaceTaskPoint(Point4D oldPoint, Point4D newPoint);
	
	public void addTaskPoint(Point4D p);
	
	public void removeTaskPoint(int index);
	public void removeTaskPoint(Point4D p);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Point4D getTaskPoint(int index);

	public int size();
	
	public boolean isValid();
	
	public boolean contains(Point4D p);

	public int getIndex();

	public void setIndex(int index);
	
	public int capacity();
	
	public Object clone();
}