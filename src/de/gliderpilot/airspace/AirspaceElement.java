/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.airspace;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.gui.Drawable;
import de.gliderpilot.trace.TraceLevels;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class AirspaceElement implements Drawable, AirspaceTypes, TraceLevels,
										Shape {
	/** DOCUMENT ME! */
	public static Color COLOR_ALPHA = new Color(Color.red.getRed(), 
												Color.red.getGreen(), 
												Color.red.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_BRAVO = new Color(Color.red.getRed(), 
												Color.red.getGreen(), 
												Color.red.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_CHARLY = new Color(Color.green.getRed(), 
												 Color.green.getGreen(), 
												 Color.green.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_DELTA = new Color(Color.red.getRed(), 
												Color.red.getGreen(), 
												Color.red.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_RESTRICTED = new Color(Color.blue.getRed(), 
													 Color.blue.getGreen(), 
													 Color.blue.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_DANGER = new Color(Color.red.getRed(), 
												 Color.red.getGreen(), 
												 Color.red.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_PROHIBITED = new Color(Color.red.getRed(), 
													 Color.red.getGreen(), 
													 Color.red.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_GLIDER_PROHIBITED = new Color(Color.red.getRed(), 
															Color.red.getGreen(), 
															Color.red.getBlue(), 
															100);

	/** DOCUMENT ME! */
	public static Color COLOR_CTR = new Color(Color.red.getRed(), 
											  Color.red.getGreen(), 
											  Color.red.getBlue(), 100);

	/** DOCUMENT ME! */
	public static Color COLOR_WAVE = new Color(Color.red.getRed(), 
											   Color.red.getGreen(), 
											   Color.red.getBlue(), 100);

	/** DOCUMENT ME! */
	public static ArrayList colors = new ArrayList(10);

	static {
		colors.add(ALPHA, COLOR_ALPHA);
		colors.add(BRAVO, COLOR_BRAVO);
		colors.add(CHARLY, COLOR_CHARLY);
		colors.add(DELTA, COLOR_DELTA);
		colors.add(RESTRICTED, COLOR_RESTRICTED);
		colors.add(DANGER, COLOR_DANGER);
		colors.add(PROHIBITED, COLOR_PROHIBITED);
		colors.add(GLIDER_PROHIBITED, COLOR_GLIDER_PROHIBITED);
		colors.add(CTR, COLOR_CTR);
		colors.add(WAVE, COLOR_WAVE);
	}

	private GeneralPath airspacePath;
	private String name;
	private int ceiling;
	private int elements = 0;
	private int ground;
	private int type;

	/**
	 * Creates a new AirspaceElement object.
	 */
	public AirspaceElement() {
		airspacePath = new GeneralPath();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param type DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Color getColor(int type) {
		return (Color) colors.get(type);
	}

	/**
	 * @see Shape#getBounds()
	 */
	public Rectangle getBounds() {
		return airspacePath.getBounds();
	}

	/**
	 * @see Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		return airspacePath.getBounds2D();
	}

	/**
	 * Sets the ceiling.
	 * 
	 * @param ceiling The ceiling to set
	 */
	public void setCeiling(int ceiling) {
		this.ceiling = ceiling;
	}

	/**
	 * Gets the ceiling.
	 * 
	 * @return Returns a int
	 */
	public int getCeiling() {
		return ceiling;
	}

	/**
	 * Sets the ground.
	 * 
	 * @param ground The ground to set
	 */
	public void setGround(int ground) {
		this.ground = ground;
	}

	/**
	 * Gets the ground.
	 * 
	 * @return Returns a int
	 */
	public int getGround() {
		return ground;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name The name to set
	 */
	public void setName(String name) {
		Logger.getLogger(LOGGER).debug("set Name: " + name);
		this.name = name;
	}

	/**
	 * Gets the name.
	 * 
	 * @return Returns a String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see Shape#getPathIterator(AffineTransform, double)
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return airspacePath.getPathIterator(at, flatness);
	}

	/**
	 * @see Shape#getPathIterator(AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return airspacePath.getPathIterator(at);
	}

	/**
	 * Sets the type.
	 * 
	 * @param type The type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return Returns a int
	 */
	public int getType() {
		return type;
	}

	/**
	 * adds an arc, which is defined by the center of that arc, its destination
	 * Point and the direction
	 * 
	 * @param center DOCUMENT ME!
	 * @param p1 DOCUMENT ME!
	 * @param p2 DOCUMENT ME!
	 * @param clockwise DOCUMENT ME!
	 */
	public void addArc(Point4D center, Point4D p1, Point4D p2, 
					   boolean clockwise) {
		Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
		double radius = center.getDistanceFrom(p1);
		Point4D corner = center.getPointAt(radius, radius);
		arc.setFrameFromCenter(center, corner);

		arc.setAngles(p1, p2);

		// because we work in "right" y-coordinates, counter-clockwise means 
		// negative angle-extend in Java. We have the wrong value
		// (360 - angle-extend)
		if (!clockwise) {
			double angleExtend = -(360d - arc.getAngleExtent());
			arc.setAngleExtent(angleExtend);
		}


		// this is because calculation might differ from correct values
		addPoint(p1);

		airspacePath.append(arc, true);


		// this is because calculation might differ from correct values
		addPoint(p2);

		Logger.getLogger(LOGGER)
			  .debug("Arc: angle start : " + arc.getAngleStart());
		Logger.getLogger(LOGGER)
			  .debug("Arc: angle extend: " + arc.getAngleExtent());
		elements += 3;
	}

	/**
	 * Method addCircle.
	 * 
	 * @param center The center of the circle
	 * @param radius The radius in km
	 */
	public void addCircle(Point4D center, double radius) {
		Logger.getLogger(LOGGER).debug("Center: " + center.getDumpString());

		Point4D corner = center.getPointAt(radius, radius);
		Ellipse2D ellipse = new Ellipse2D.Double();
		;
		ellipse.setFrameFromCenter(center, corner);
		airspacePath.append(ellipse, true);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param p DOCUMENT ME!
	 */
	public void addPoint(Point4D p) {
		Logger.getLogger(LOGGER).debug("Adding " + p.getDumpString());

		if (elements++ == 0) {
			airspacePath.moveTo((float) p.x, (float) p.y);
		} else {
			airspacePath.lineTo((float) p.x, (float) p.y);
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	public void close() {
		if (elements > 2) {
			airspacePath.closePath();
		}
	}

	/**
	 * @see Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) {
		return airspacePath.contains(x, y, w, h);
	}

	/**
	 * @see Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return airspacePath.contains(x, y);
	}

	/**
	 * @see Shape#contains(Point2D)
	 */
	public boolean contains(Point2D p) {
		boolean ret = airspacePath.contains(p);

		if (ret && p instanceof Point4D) {
			Point4D p3 = (Point4D) p;
			int height = p3.getPressureHeight();
			ret = (height >= ground) && (height <= ceiling);
		}

		return ret;
	}

	/**
	 * @see Shape#contains(Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) {
		return airspacePath.contains(r);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param g DOCUMENT ME!
	 * @param r DOCUMENT ME!
	 * @param xform DOCUMENT ME!
	 */
	public void draw(Graphics g, Rectangle4D r, AffineTransform xform) {
		if (!r.intersects(getBounds2D())) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
		GeneralPath temp = (GeneralPath) airspacePath.clone();
		temp.transform(xform);
		g2.setPaint(getColor(type));
		g2.fill(temp);
		g2.setPaint(Color.black);
		g2.draw(temp);

		//		Rectangle bounds = temp.getBounds();
		//		g2.drawString(name, bounds.x, bounds.y);
	}

	/**
	 * @see Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		return airspacePath.intersects(x, y, w, h);
	}

	/**
	 * @see Shape#intersects(Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return airspacePath.intersects(r);
	}
}