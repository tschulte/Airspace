/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.geom;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Date;

import de.gliderpilot.trace.Dumpable;

/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Point4D extends Point2D.Double implements Dumpable {
	/** DOCUMENT ME! */
	public static final int EARTH_RADIUS = 6371; // due to ICAO

	/** DOCUMENT ME! */
	public static final int EARTH_CIRCUMFERENCE =
		(int) Math.PI * EARTH_RADIUS * 2;

//	private HashMap distances = new HashMap();
	private Date date;
	private int enl;
	private int gpsHeight;
	private int pressureHeight;

	/**
	 * Creates a new Point4D object.
	 */
	public Point4D() {
	}

	/**
	 * Creates a new Point4D object.
	 * 
	 * @param wD DOCUMENT ME!
	 * @param wM DOCUMENT ME!
	 * @param wS DOCUMENT ME!
	 * @param north DOCUMENT ME!
	 * @param lD DOCUMENT ME!
	 * @param lM DOCUMENT ME!
	 * @param lS DOCUMENT ME!
	 * @param east DOCUMENT ME!
	 */
	public Point4D(
		int wD,
		int wM,
		double wS,
		boolean north,
		int lD,
		int lM,
		double lS,
		boolean east) {
		x = (double) lD + ((double) lM / 60) + (lS / 3600);
		x *= (east ? 1 : -1);

		y = (double) wD + ((double) wM / 60) + (wS / 3600);
		y *= (north ? 1 : -1);
	}

	/**
	 * Creates a new Point4D object.
	 * 
	 * @param x DOCUMENT ME!
	 * @param y DOCUMENT ME!
	 */
	public Point4D(double x, double y) {
		super(x, y);
	}

	public Point4D(Point p) {
		super();
		x = p.x;
		y = p.y;
	}
	/**
	 * Sets the date.
	 * 
	 * @param date The date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the date.
	 * 
	 * @return Returns a Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param pt DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getDistanceFrom(Point4D pt) {
//		java.lang.Double d = (java.lang.Double) distances.get(pt);
//		if (d != null) {
//			return d.doubleValue();
//		}

		double rady = Math.toRadians(y);
		double siny = Math.sin(rady);
		double cosy = Math.cos(rady);
		double radpty = Math.toRadians(pt.y);
		double sinpty = Math.sin(radpty);
		double cospty = Math.cos(radpty);
		double radxminptx = Math.toRadians(x - pt.x);
		double cosxminptx = Math.cos(radxminptx);
		double distance =
			Math.acos((siny * sinpty) + (cosy * cospty * cosxminptx))
				* EARTH_RADIUS;
//		distances.put(pt, new java.lang.Double(distance));
		if (java.lang.Double.isNaN(distance)) return 0;
		return distance;
	}

	public static int getDegrees(double value) {
		return (int) Math.abs(value);
	}
	
	public static int getMinutes(double value) {
		if (getSeconds(value) == 0) {
			return (int) Math.round((Math.abs(value) - getDegrees(value)) * 60);
		}
		return (int) ((Math.abs(value) - getDegrees(value)) * 60);
	}
	
	public static int getSeconds(double value) {
//		int seconds = (int) Math.round(((Math.abs(value) - getDegrees(value)) * 60 - getMinutes(value)) * 60);
		int seconds = (int) Math.round(((Math.abs(value) - (int) Math.abs(value)) * 60 - (int) ((Math.abs(value) - getDegrees(value)) * 60)) * 60);
		if (seconds >= 60) {
			seconds = 0;
		}
		return seconds;
	}
	
	public static int getMinDecimals(double value) {
		int minDecimals = (int) Math.round(((Math.abs(value) - (int) Math.abs(value)) * 60 - (int) ((Math.abs(value) - getDegrees(value)) * 60)) * 1000);
		if (minDecimals >= 1000) {
			minDecimals = 0;
		}
		return minDecimals;
	}
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getDumpString() {
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append((y > 0) ? "N" : "S");
		sBuffer.append(" ");

		sBuffer.append(getDegrees(y));
		sBuffer.append(" ");

		sBuffer.append(getMinutes(y));
		sBuffer.append("'");

		sBuffer.append(getSeconds(y));
		sBuffer.append("\"");

		sBuffer.append(",");
		sBuffer.append((x > 0) ? "E" : "W");
		sBuffer.append(" ");

		sBuffer.append(getDegrees(x));
		sBuffer.append(" ");

		sBuffer.append(getMinutes(x));
		sBuffer.append("'");

		sBuffer.append(getSeconds(x));
		sBuffer.append("\"");

		return sBuffer.toString();
	}

	/**
	 * Sets the enl.
	 * 
	 * @param enl The enl to set
	 */
	public void setEnl(int enl) {
		this.enl = enl;
	}

	/**
	 * Gets the enl.
	 * 
	 * @return Returns a int
	 */
	public int getEnl() {
		return enl;
	}

	/**
	 * Sets the gpsHeight.
	 * 
	 * @param gpsHeight The gpsHeight to set
	 */
	public void setGpsHeight(int gpsHeight) {
		this.gpsHeight = gpsHeight;
	}

	/**
	 * Gets the gpsHeight.
	 * 
	 * @return Returns a int
	 */
	public int getGpsHeight() {
		return gpsHeight;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param xOffs DOCUMENT ME!
	 * @param yOffs DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Point4D getPointAt(double xOffs, double yOffs) {
		Point4D point = (Point4D) clone();

		return point.move(xOffs, yOffs);
	}

	/**
	 * Sets the pressureHeight.
	 * 
	 * @param pressureHeight The pressureHeight to set
	 */
	public void setPressureHeight(int pressureHeight) {
		this.pressureHeight = pressureHeight;
	}

	/**
	 * Gets the pressureHeight.
	 * 
	 * @return Returns a int
	 */
	public int getPressureHeight() {
		return pressureHeight;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param reference DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getXCorrection(Point4D reference) {
		double y = this.y;

		if (reference != null) {
			y = (y + reference.y) / 2;
		}

		return Math.cos((y * Math.PI) / 180);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getXDegrees() {
		return x;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param reference DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getXDegreesFrom(Point4D reference) {
		return this.getXDegrees() - reference.getXDegrees();
	}

	// TODO: Implement the real distance

	/**
	 * Returns the x distance in km from the reference point. If reference is
	 * west of  this, a positive value vill be returned.
	 * 
	 * @param reference DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getXDistanceFrom(Point4D reference) {
		double retval =
			getXDegreesFrom(reference) * (EARTH_CIRCUMFERENCE / 360);
		retval *= getXCorrection(reference);

		return retval;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getYDegrees() {
		return y;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param reference DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getYDegreesFrom(Point4D reference) {
		return this.getYDegrees() - reference.getYDegrees();
	}

	/**
	 * Returns the y distance in km from the reference point. If reference is
	 * south of  this, a positive value vill be returned.
	 * 
	 * @param reference DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getYDistanceFrom(Point4D reference) {
		return getYDegreesFrom(reference) * (EARTH_CIRCUMFERENCE / 360);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param arguments DOCUMENT ME!
	 */
	public static void main(String[] arguments) {
		Point4D p1 = new Point4D(0, 0);
		Point4D p2 = new Point4D(0, 1);
		double last = System.currentTimeMillis();
		double now;
		System.out.println(p1.getDistanceFrom(p2));
		now = System.currentTimeMillis();
		System.out.println(now - last);
		last = now;

		// 	s=6371km*acos(
		// 		sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon1-lon2)
		//	)
		double distance =
			6371d
				* Math.acos(
					(Math.sin((p1.y * Math.PI) / 180)
						* Math.sin((p2.y * Math.PI) / 180))
						+ (Math.cos((p1.y * Math.PI) / 180)
							* Math.cos((p2.y * Math.PI) / 180)
							* Math.cos(((p1.x - p2.x) * Math.PI) / 180)));
		System.out.println(distance);
		now = System.currentTimeMillis();
		System.out.println(now - last);
		last = now;

		// 	6371km*asin(
		//		sqrt(
		//			sin((lat1-lat2)/2)^2
		//			+sin(lon1-lon2)/2)^2
		//		)
		//		*cos(lat1)*cos(lat2)
		//	)
		distance =
			6371d
				* Math.asin(
					Math.sqrt(
						(Math
							.pow(
								Math.sin(((p1.y - p2.y) / 2 * Math.PI) / 180),
								2)
							+ Math.pow(
								Math.sin(((p1.x - p2.x) / 2 * Math.PI) / 180),
								2)))
						* Math.cos((p1.y * Math.PI) / 180)
						* Math.cos((p2.y * Math.PI) / 180));
		System.out.println(distance);
		now = System.currentTimeMillis();
		System.out.println(now - last);
	}

	/**
	 * move the point by the given amount (in km)
	 * 
	 * @param xOffs DOCUMENT ME!
	 * @param yOffs DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Point4D move(double xOffs, double yOffs) {
		// TO DO: add real calculation of x value
		double degrees = (xOffs * 360) / EARTH_CIRCUMFERENCE;
		degrees /= getXCorrection(null);
		x += degrees;

		y += ((yOffs * 360) / EARTH_CIRCUMFERENCE);

		return this;
	}

	public Object clone() {
		Point4D cloned = (Point4D) super.clone();
		if (date != null) {
			cloned.date = (Date) date.clone();
		}
		return cloned;
	}
	
	public String toString() {
		return getDumpString();
	}
}