/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.geom;


import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import de.gliderpilot.trace.TraceLevels;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Line4D extends Line2D.Double implements TraceLevels, Modes4D {
	float vario;
	int baro = XY;
	Point4D p1;
	Point4D p2;
	int speed;

	/**
	 * Creates a new Line4D object.
	 * 
	 * @param p1 DOCUMENT ME!
	 * @param p2 DOCUMENT ME!
	 */
	public Line4D(Point4D p1, Point4D p2) {
		super(p1, p2);
		this.p1 = p1;
		this.p2 = p2;

		Date d = p1.getDate();
		long d1 = 0, d2 = 0;
		if (d != null) {
			d1 = d.getTime();
		}
		d = p2.getDate();
		if (d != null) {
			d2 = d.getTime();
		}

		speed = (int) (p1.getDistanceFrom(p2) * 60 * 60 * 1000 / (d2 - d1));

		vario = ((float) (p2.getGpsHeight() - p1.getGpsHeight()) * 1000) / (d2 - d1);
	}

	/**
	 * Sets the baro.
	 * 
	 * @param baro The baro to set
	 */
	public void setBaro(int baro) {
		this.baro = baro;
	}

	/**
	 * Gets the baro.
	 * 
	 * @return Returns a int
	 */
	public int getBaro() {
		return baro;
	}

	/**
	 * Returns the high-precision bounding box of this <code>Line2D</code>.
	 * 
	 * @return a <code>Rectangle2D</code> that is the high-precision bounding
	 *         box of this <code>Line2D</code>.
	 */
	public Rectangle4D getBounds4D() {
		Rectangle2D rect = super.getBounds2D();
		double top;
		double bottom;
		top = Math.max(Math.max(getPA1(), getPA2()), Math.max(getGA1(), getGA2()));
		bottom = Math.min(Math.min(getPA1(), getPA2()), Math.min(getGA1(), getGA2()));

		Rectangle4D rect4D = new Rectangle4D();
		rect4D.setFrame(rect);
		rect4D.setTop(top);
		rect4D.setBottom(bottom);
		rect4D.setDate1(getD1());
		rect4D.setDate2(getD2());
		rect4D.vario1 = vario;
		rect4D.vario2 = vario;
		rect4D.speed1 = speed;
		rect4D.speed2 = speed;

		return rect4D;
	}

	/**
	 * Sets the d1.
	 * 
	 * @param d1 The d1 to set
	 */
	public void setD1(long d1) {
		p1.setDate(new Date(d1));
	}

	/**
	 * Gets the d1.
	 * 
	 * @return Returns a long
	 */
	public long getD1() {
		return p1.getDate().getTime();
	}

	/**
	 * Sets the d2.
	 * 
	 * @param d2 The d2 to set
	 */
	public void setD2(long d2) {
		p2.setDate(new Date(d2));
	}

	/**
	 * Gets the d2.
	 * 
	 * @return Returns a long
	 */
	public long getD2() {
		return p2.getDate().getTime();
	}

	/**
	 * Sets the enl1.
	 * 
	 * @param enl1 The enl1 to set
	 */
	public void setEnl1(int enl1) {
		p1.setEnl(enl1);
	}

	/**
	 * Gets the enl1.
	 * 
	 * @return Returns a int
	 */
	public int getEnl1() {
		return p1.getEnl();
	}

	/**
	 * Sets the enl2.
	 * 
	 * @param enl2 The enl2 to set
	 */
	public void setEnl2(int enl2) {
		p2.setEnl(enl2);
	}

	/**
	 * Gets the enl2.
	 * 
	 * @return Returns a int
	 */
	public int getEnl2() {
		return p2.getEnl();
	}

	/**
	 * Sets the gA1.
	 * 
	 * @param gA1 The gA1 to set
	 */
	public void setGA1(int gA1) {
		p1.setGpsHeight(gA1);
	}

	/**
	 * Gets the gA1.
	 * 
	 * @return Returns a int
	 */
	public int getGA1() {
		return p1.getGpsHeight();
	}

	/**
	 * Sets the gA2.
	 * 
	 * @param gA2 The gA2 to set
	 */
	public void setGA2(int gA2) {
		p2.setGpsHeight(gA2);
	}

	/**
	 * Gets the gA2.
	 * 
	 * @return Returns a int
	 */
	public int getGA2() {
		return p2.getGpsHeight();
	}

	/**
	 * @see Line2D#getP1()
	 */
	public Point2D getP1() {
		return p2;
	}

	/**
	 * @see Line2D#getP2()
	 */
	public Point2D getP2() {
		return p1;
	}

	/**
	 * Sets the pA1.
	 * 
	 * @param pA1 The pA1 to set
	 */
	public void setPA1(int pA1) {
		p1.setPressureHeight(pA1);
	}

	/**
	 * Gets the pA1.
	 * 
	 * @return Returns a int
	 */
	public int getPA1() {
		return p1.getPressureHeight();
	}

	/**
	 * Sets the pA2.
	 * 
	 * @param pA2 The pA2 to set
	 */
	public void setPA2(int pA2) {
		p2.setPressureHeight(pA2);
	}

	/**
	 * Gets the pA2.
	 * 
	 * @return Returns a int
	 */
	public int getPA2() {
		return p2.getPressureHeight();
	}

	/**
	 * @see Shape#getPathIterator(AffineTransform, double)
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return getPathIterator(at);
	}

	/**
	 * @see Shape#getPathIterator(AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return new Line4DIterator(this, at, baro);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public float getVario() {
		return vario;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param xform DOCUMENT ME!
	 */
	public void transform(AffineTransform xform) {
		double[] elements;

		if (baro == XY) {
			elements = new double[] { x1, y1, x2, y2 };
			xform.transform(elements, 0, elements, 0, elements.length / 2);
			x1 = elements[0];
			y1 = elements[1];
			x2 = elements[2];
			y2 = elements[3];
		}

		if (baro != XY) {
			elements = new double[] { getD1(), 0, getD2(), 0 };
			xform.transform(elements, 0, elements, 0, elements.length / 2);
			setD1((long) elements[0]);
			setD2((long) elements[2]);
		}

		if ((baro & PRESSURE) != 0) {
			elements = new double[] { getD1(), getPA1(), getD2(), getPA2() };
			xform.transform(elements, 0, elements, 0, elements.length / 2);
			setPA1((int) elements[1]);
			setPA2((int) elements[3]);
		}

		if ((baro & GPS) != 0) {
			elements = new double[] { getD1(), getGA1(), getD2(), getGA2() };
			xform.transform(elements, 0, elements, 0, elements.length / 2);
			setGA1((int) elements[1]);
			setGA2((int) elements[3]);
		}

		if ((baro & ENL) != 0) {
			elements = new double[] { getD1(), getEnl1(), getD2(), getEnl2()};
			xform.transform(elements, 0, elements, 0, elements.length / 2);
			setEnl1((int) elements[1]);
			setEnl2((int) elements[3]);
		}
	}
	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Line4D l = (Line4D) super.clone();
		l.p1 = (Point4D) p1.clone();
		l.p2 = (Point4D) p2.clone();
		return l;
	}

}