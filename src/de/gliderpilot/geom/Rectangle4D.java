/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.geom;


import java.awt.geom.Rectangle2D;
import java.util.Date;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Rectangle4D extends Rectangle2D.Double {
	/** DOCUMENT ME! */
	public Point4D center;

	/** DOCUMENT ME! */
	public double bottom;

	/** DOCUMENT ME! */
	public double top;

	/** DOCUMENT ME! */
	public double vario1;

	/** DOCUMENT ME! */
	public double vario2;

	/** DOCUMENT ME! */
	public int speed1;

	/** DOCUMENT ME! */
	public int speed2;

	/** DOCUMENT ME! */
	public long date1;

	/** DOCUMENT ME! */
	public long date2;

	/**
	 * Sets the bottom.
	 * 
	 * @param bottom The bottom to set
	 */
	public void setBottom(double bottom) {
		this.bottom = bottom;
		center = null;
	}

	/**
	 * Gets the bottom.
	 * 
	 * @return Returns a double
	 */
	public double getBottom() {
		return bottom;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Point4D getCenter4D() {
		if (center == null) {
			center = new Point4D(getCenterX(), getCenterY());
			center.setGpsHeight((int) (bottom + top) / 2);
			center.setPressureHeight(center.getGpsHeight());
			center.setDate(new Date((date1 + date2) / 2));
		}

		return center;
	}

	/**
	 * Sets the date1.
	 * 
	 * @param date1 The date1 to set
	 */
	public void setDate1(long date1) {
		this.date1 = date1;

		center = null;
	}

	/**
	 * Gets the date1.
	 * 
	 * @return Returns a long
	 */
	public long getDate1() {
		return date1;
	}

	/**
	 * Sets the date2.
	 * 
	 * @param date2 The date2 to set
	 */
	public void setDate2(long date2) {
		this.date2 = date2;

		center = null;
	}

	/**
	 * Gets the date2.
	 * 
	 * @return Returns a long
	 */
	public long getDate2() {
		return date2;
	}

	/**
	 * Sets the top.
	 * 
	 * @param top The top to set
	 */
	public void setTop(double top) {
		this.top = top;
		center = null;
	}

	/**
	 * Gets the top.
	 * 
	 * @return Returns a double
	 */
	public double getTop() {
		return top;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param src1 DOCUMENT ME!
	 * @param src2 DOCUMENT ME!
	 * @param dest DOCUMENT ME!
	 */
	public static void union(Rectangle4D src1, Rectangle4D src2, 
							 Rectangle4D dest) {
		Rectangle2D.union(src1, src2, dest);

		double bottom = Math.min(src1.getBottom(), src2.getBottom());
		double top = Math.max(src1.getTop(), src2.getTop());
		long date1 = Math.min(src1.date1, src2.date1);
		long date2 = Math.max(src1.date2, src2.date2);

		double vario1 = Math.min(src1.vario1, src2.vario1);
		double vario2 = Math.max(src1.vario2, src2.vario2);

		int speed1 = Math.min(src1.speed1, src2.speed1);
		int speed2 = Math.max(src1.speed2, src2.speed2);

		dest.setTop(top);
		dest.setBottom(bottom);
		dest.setDate1(date1);
		dest.setDate2(date2);

		dest.vario1 = vario1;
		dest.vario2 = vario2;

		dest.speed1 = speed1;
		dest.speed2 = speed2;

		dest.center = null;
	}

	/**
	 * @see Shape#contains(Point2D)
	 */
	public boolean contains(Point4D p) {
		int maxHeight = Math.max(p.getPressureHeight(), p.getGpsHeight());
		int minHeight = Math.min(p.getPressureHeight(), p.getGpsHeight());

		return super.contains(p) && (minHeight >= bottom) && 
			   (maxHeight <= top);
	}

	/**
	 * @see Shape#contains(Rectangle2D)
	 */
	public boolean contains(Rectangle4D r) {
		return super.contains(r) && (r.bottom >= bottom) && (r.top <= top) && 
			   (r.date1 >= date1) && (r.date2 <= date2) && 
			   (r.vario1 >= vario1) && (r.vario2 <= vario2) && 
			   (r.speed1 >= speed1) && (r.speed2 <= speed2);
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (super.equals(obj)) {
			if (obj instanceof Rectangle2D) {
				Rectangle4D r4d = (Rectangle4D) obj;

				return ((getBottom() == r4d.getBottom()) && 
					   (getTop() == r4d.getTop()) && 
					   (getDate1() == r4d.getDate1()) && 
					   (getDate2() == r4d.getDate2()));
			}
		}

		return false;
	}
}