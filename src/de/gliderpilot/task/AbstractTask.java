/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

import de.gliderpilot.geom.Line4D;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.util.DumpString;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class AbstractTask implements Task {
	private static Comparator taskComparator;
	private TreeMap points;
	private double distance = 0;
	private double score = 0;
	private int capacity;
	private int index = 140;

	/**
	 * Creates a new AbstractTask object.
	 * 
	 * @param trackLog DOCUMENT ME!
	 * @param numPoints DOCUMENT ME!
	 */
	public AbstractTask(int capacity) {
		this.capacity = capacity;
		if (taskComparator == null) {
			taskComparator = new TaskComparator();
		}
		points = new TreeMap(taskComparator);
	}

	
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public final double getDistance() {
		if (distance != 0) {
			return distance;
		}

		distance = calculateDistance();

		return distance;
	}

	/**
	 * @see de.gliderpilot.task.Task#getPoints()
	 * @deprecated use iterate() instead
	 */
	public final Point4D[] getPoints() {
		if (size() == 0) {
			return null;
		}
		Point4D[] pointsA = new Point4D[points.size()];
		return (Point4D[]) points.entrySet().toArray(pointsA);
	}

	/**
	 * Returns the score.
	 * 
	 * @return double
	 */
	public final double getScore() {
		if (score == 0 || score == Double.NaN) {
			score = calculateScore();
		}


		return score * 100 / getIndex();
	}

	/**
	 * @see de.gliderpilot.task.Task#setTaskPoint(Point4D, int)
	 */
	public final void replaceTaskPoint(Point4D p, int index) {
		if (index >= points.size()) {
			addTaskPoint(p);
		} else {
			removeTaskPoint(index);
			addTaskPoint(p);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index DOCUMENT ME!
	 * @deprecated use iterator instead
	 * @return DOCUMENT ME!
	 */
	public final Point4D getTaskPoint(int index) {
		return (Point4D) points.entrySet().toArray()[index];
	}


	public final int size() {
		return points.size();
	}


	/**
	 * @see de.gliderpilot.airspace.Drawable#draw(Graphics, Rectangle2D,
	 *      AffineTransform)
	 */
	public void draw(Graphics g, Rectangle4D r, AffineTransform xform) {
//		if (r.getWidth() == 0 || r.getHeight() == 0) return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black);

		Point4D p1 = null;
		Point4D p2 = null;
		
		int i = 0;
		Iterator it = iterator();
		g2.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[] {10,10}, 0));
		while (it.hasNext()) {
			p2 = (Point4D) ((Point4D) it.next()).clone();
			xform.transform(p2, p2);
			
			if (i == 1) {
				g2.setColor(Color.blue);
			}
			if (i == size()-1) {
				g2.setColor(Color.red);
			}
			if (p1 != null) {
				Line4D line = new Line4D(p1, p2);
				g2.draw(line);
			}
			g2.setColor(Color.black);

			p1 = p2;
			i++;
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected double calculateDistance() {
		double distance = 0;
		Point4D p1 = null;
		Point4D p2 = null;

		Iterator it = iterator();
		while (it.hasNext()) {
			p2 = (Point4D) it.next();

			if ((p1 != null) && (p2 != null)) {
				distance += p1.getDistanceFrom(p2);
			}

			p1 = p2;
		}

		return distance;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected abstract double calculateScore();

	private void invalidate() {
		distance = 0;
		score = 0;
	}
	/**
	 * @see de.gliderpilot.task.Task#addTaskPoint(Point4D)
	 */
	public final void addTaskPoint(Point4D p) {
		if (capacity != 0 && size() >= capacity) {
			throw new ArrayIndexOutOfBoundsException();
		}
		invalidate();
		points.put(p.getDate(), p);
	}

	/**
	 * @see de.gliderpilot.task.Task#iterate()
	 */
	public Iterator iterator() {
		return points.values().iterator();
	}

	/**
	 * @see de.gliderpilot.task.Task#removeTaskPoint(int)
	 */
	public void removeTaskPoint(int index) {
		Object key = points.keySet().toArray()[index];
		points.remove(key);
		invalidate();
	}

	class TaskComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			if (o1 == o2) return 0;
			Date d1 = (Date) o1;
			Date d2 = (Date) o2;
			if (d1.after(d2)) return 1;
			return -1;
		}

		public boolean equals(Object obj) {
			return obj.getClass() == TaskComparator.class;
		}
	}



	/**
	 * @see de.gliderpilot.task.Task#removeTaskPoint(Point4D)
	 */
	public void removeTaskPoint(Point4D p) {
		points.remove(p.getDate());
		invalidate();
	}

	/**
	 * @see de.gliderpilot.task.Task#replaceTaskPoint(Point4D, Point4D)
	 */
	public void replaceTaskPoint(Point4D oldPoint, Point4D newPoint) {
		removeTaskPoint(oldPoint);
		addTaskPoint(newPoint);
	}

	public Object clone() {
		AbstractTask cloned = null;
		try {
			cloned = (AbstractTask) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
		cloned.points = new TreeMap();
		Iterator it = iterator();
		while (it.hasNext()) {
			cloned.addTaskPoint((Point4D) it.next());
		}
		return cloned;
	}

	/**
	 * @see de.gliderpilot.trace.Dumpable#getDumpString()
	 */
	public String getDumpString() {
		DumpString dump = new DumpString();
		Iterator it = iterator();
		for (int i = 0; it.hasNext(); i++) {
			dump.println(""+i, it.next());
		}
		dump.println("Distance", getDistance());
		dump.println("Score (Index "+getIndex()+")", getScore());
		return dump.toString();
	}

	/**
	 * Returns the capacity.
	 * @return int
	 */
	public int capacity() {
		return capacity;
	}
	
	public boolean isValid() {
		return true;
	}
	/**
	 * Returns the index.
	 * @return int
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 * @param index The index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public boolean contains(Point4D p) {
		return points.containsValue(p);
	}

}