/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.tracklog;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.RandomAccess;
import java.util.Vector;
import java.util.logging.Logger;

import de.gliderpilot.geom.Line4D;
import de.gliderpilot.geom.Modes4D;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.gui.Drawable4D;
import de.gliderpilot.gui.event.TrackParamsChangedEvent;
import de.gliderpilot.task.OlcTask;
import de.gliderpilot.task.Task;
import de.gliderpilot.trace.Dumpable;
import de.gliderpilot.trace.TraceLevels;
import de.gliderpilot.util.DumpString;


/**
 * DOCUMENT ME!
 * 
 * @version $Revision$
 * @author $author$
 */
public class TrackLog implements Dumpable, RandomAccess, Drawable4D, TraceLevels, Modes4D,
								 TrackParamsChangedEvent.Source {
	ArrayList paramsChangedListeners = new ArrayList();
	ArrayList tasks = new ArrayList();
	Date endOfGlide;
	int endIndex;
	Date startOfGlide;
	int startIndex;
	String airField;
	private Date date;
	private Glider glider;
	private Pilot pilot;
	private Point4D lastPoint;
	private Rectangle4D rect;
	private String igcFileName;
	private Vector lines;
	private int deltaT;
	private int maxSpeed;
	private int minSpeed;
	private int sumSpeed;
	private int mode = XY;

	/**
	 * Creates a new TrackLog object.
	 */
	public TrackLog() {
		lines = new Vector();
	}

	/**
	 * Sets the airField.
	 * 
	 * @param airField The airField to set
	 */
	public void setAirField(String airField) {
		this.airField = airField;
	}

	/**
	 * Returns the airField.
	 * 
	 * @return String
	 */
	public String getAirField() {
		return airField;
	}

	/**
	 * Returns the task.
	 * 
	 * @return Task
	 */
	public Task getBestTask(Class filter) {
		int bestIndex = -1;
		double bestScore = -1;
		Task task = null;

		for (int i = 0; i < tasks.size(); i++) {
			task = (Task) tasks.get(i);
			if (filter.isInstance(task) && task.getScore() > bestScore) {
				bestIndex = i;
				bestScore = task.getScore();
			}
		}

		if (bestIndex >= 0) {
			task = (Task) tasks.get(bestIndex);
			return task;
		}

		return null;
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
	 * @return DOCUMENT ME!
	 */
	public int getDeltaT() {
		return deltaT;
	}

	/**
	 * Sets the endOfGlide.
	 * 
	 * @param endOfGlide The endOfGlide to set
	 */
	public void setEndOfGlide(Date endOfGlide) {
		if (startOfGlide.after(endOfGlide)) {
			return;
		}


		endIndex = size() - 1;

		for (int i = size() - 1; i >= 0; i--) {
			Point4D p = getPointAt(i);
			if (endOfGlide.after(p.getDate()) || endOfGlide.equals(p.getDate())) {
				endIndex = i;
				this.endOfGlide = p.getDate();
				break;
			}
		}
		Logger.getLogger(LOGGER).info("End of glide set to " + endOfGlide);
		fireParamsChanged(TrackParamsChangedEvent.END_TIME);
	}

	/**
	 * Returns the endOfGlide.
	 * 
	 * @return long
	 */
	public Date getEndOfGlide() {
		return endOfGlide;
	}

	/**
	 * Gets the gliderSign.
	 * 
	 * @return Returns a String
	 */
	public Glider getGlider() {
		return glider;
	}

	/**
	 * Sets the gliderSign.
	 * 
	 * @param glider The gliderSign to set
	 */
	public void setGlider(Glider glider) {
		this.glider = glider;
	}

	/**
	 * Sets the igcFileName.
	 * 
	 * @param igcFileName The igcFileName to set
	 */
	public void setIgcFileName(String igcFileName) {
		this.igcFileName = igcFileName;
	}

	/**
	 * Returns the igcFileName.
	 * 
	 * @return String
	 */
	public String getIgcFileName() {
		return igcFileName;
	}

	/**
	 * Gets the points.
	 * 
	 * @return Returns a Vector
	 */
	public Vector getLines() {
		return lines;
	}

	/**
	 * Returns the maxSpeed.
	 * 
	 * @return int
	 */
	public int getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * @see Drawable4D#setMode(int)
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Sets the pilotName.
	 * 
	 * @param pilot The pilotName to set
	 */
	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}

	/**
	 * Gets the pilotName.
	 * 
	 * @return Returns a String
	 */
	public Pilot getPilot() {
		return pilot;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Point4D getPointAt(int index) {
		if (index >= lines.size()) {
			return lastPoint;
		}

		return (Point4D) ((Line4D) lines.get(index)).getP1();
	}

	/**
	 * Sets the startOfGlide.
	 * 
	 * @param startOfGlide The startOfGlide to set
	 */
	public void setStartOfGlide(Date startOfGlide) {
		if (startOfGlide.after(endOfGlide)) {
			return;
		}

		startIndex = 0;

		for (int i = 0; i < size(); i++) {
			Point4D p = getPointAt(i);
			if (startOfGlide.before(p.getDate()) || startOfGlide.equals(p.getDate())) {
				startIndex = i;
				this.startOfGlide = p.getDate();
				break;
			}
		}
		Logger.getLogger(LOGGER).info("Start of glide set to " + 
									  startOfGlide);
		fireParamsChanged(TrackParamsChangedEvent.START_TIME);
	}

	/**
	 * Returns the startOfGlide.
	 * 
	 * @return long
	 */
	public Date getStartOfGlide() {
		return startOfGlide;
	}

	/**
	 * Sets the task.
	 * 
	 * @param task The task to set
	 */
	public void setTask(Task task) {
		tasks.add(task);
		fireParamsChanged(TrackParamsChangedEvent.TASK);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param point DOCUMENT ME!
	 */
	public void add(Point4D point) {
		Logger.getLogger(LOGGER)
			  .fine("Adding Point to Track: " + point.getDumpString());

		if (lastPoint != null) {
			deltaT = deltaT * lines.size();
			deltaT += (int) (point.getDate().getTime() - lastPoint.getDate()
																  .getTime());
			deltaT /= size();

			Line4D line = new Line4D(lastPoint, point);
			lines.addElement(line);

			if (maxSpeed < line.getSpeed()) {
				maxSpeed = line.getSpeed();
			}
			if (minSpeed > line.getSpeed()) {
				minSpeed = line.getSpeed();
			}
			sumSpeed += line.getSpeed();

			Rectangle4D rect = line.getBounds4D();

			if (this.rect == null) {
				this.rect = rect;
			} else if (!this.rect.contains(rect)) {
				Rectangle4D.union(this.rect, rect, this.rect);
			}

			if (point.getDate().before(startOfGlide)) {
				startOfGlide = point.getDate();
			}

			if (point.getDate().after(endOfGlide)) {
				endOfGlide = point.getDate();
				endIndex = size()-1;
			}
		} else {
			startOfGlide = point.getDate();
			endOfGlide = point.getDate();
		}

		lastPoint = point;
	}

	/**
	 * @see airspace.swing.TrackParamsChangedEvent.Source#addTrackParamsChangedListener(Listener)
	 */
	public void addTrackParamsChangedListener(TrackParamsChangedEvent.Listener listener) {
		paramsChangedListeners.add(listener);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void clear() {
		lines.clear();
		lastPoint = null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param g DOCUMENT ME!
	 * @param r DOCUMENT ME!
	 * @param xform DOCUMENT ME!
	 */
	public void draw(Graphics g, Rectangle4D r, AffineTransform xform) {
		Graphics2D g2 = (Graphics2D) g;

		Enumeration e = getLines().elements();

		while (e.hasMoreElements()) {
			Line4D line = (Line4D) e.nextElement();

			if (!r.intersectsLine(line)) {
				continue;
			}

			Line4D l = (Line4D) line.clone();
			l.setBaro(mode);
			l.transform(xform);

			if (mode == 0) {
				l.setBaro(XY);
				g2.setColor((l.getVario() > 0) ? Color.red : Color.blue);
				g2.draw(l);
			}

			if ((mode & PRESSURE) != 0) {
				l.setBaro(PRESSURE);

				g2.setColor(Color.blue);

				g2.draw(l);
			}

			if ((mode & GPS) != 0) {
				l.setBaro(GPS);

				g2.setColor(Color.red);

				g2.draw(l);
			}

			if ((mode & ENL) != 0) {
				l.setBaro(ENL);

				g2.setColor(Color.black);

				g2.draw(l);
			}

			if ((mode & VARIO) != 0) {
				l.setBaro(VARIO);

				g2.setColor(Color.black);

				g2.draw(l);
			}

			if ((mode & SPEED) != 0) {
				l.setBaro(SPEED);

				g2.setColor(Color.black);

				g2.draw(l);
			}
		}
		
		// in baro mode indicate the glide
		if ((mode & (GPS | PRESSURE | ENL)) != 0) {
			if (r.getTop() != r.getBottom() && r.getDate1() != r.getDate2()) {
				Rectangle4D rect = new Rectangle4D();
				Date d1 = getStartOfGlide();
				Date d2 = getEndOfGlide();
				if (d1 != null && d2 != null) {
					Point4D p1 = new Point4D(getStartOfGlide().getTime(), 0);
					Point4D p2 = new Point4D(getEndOfGlide().getTime(), 10000);
					xform.transform(p1, p1);
					xform.transform(p2, p2);
					rect.setFrameFromDiagonal(p1, p2);
		
					Color c = new Color(Color.red.getRed(), Color.red.getGreen(), 
										Color.red.getBlue(), 100);
					g2.setPaint(c);
					g2.fill(rect);
				}
			}
		} else {
			Task task = getBestTask(OlcTask.class);

			if (task != null) {
				task.draw(g, rect, xform);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Iterator iterate() {
		return new TrackIterator();
	}

	/**
	 * @see airspace.swing.TrackParamsChangedEvent.Source#removeTrackParamsChangedListener(Listener)
	 */
	public void removeTrackParamsChangedListener(TrackParamsChangedEvent.Listener listener) {
		paramsChangedListeners.remove(listener);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int size() {
		return lines.size() + 1;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void trim() {
		lines.trimToSize();
	}

	private void fireParamsChanged(int param) {
		TrackParamsChangedEvent e = new TrackParamsChangedEvent(param);

		for (int i = 0; i < paramsChangedListeners.size(); i++) {
			((TrackParamsChangedEvent.Listener) paramsChangedListeners.get(i)).trackParamsChanged(e);
		}
	}

	class TrackIterator implements Iterator {
		Iterator lineIterator = getLines().iterator();
		boolean firstTime = true;

		public boolean hasNext() {
			return lineIterator.hasNext();
		}

		public Object next() {
			if (firstTime) {
				firstTime = false;

				return ((Line4D) getLines().get(0)).getP1();
			}

			return ((Line4D) lineIterator.next()).getP2();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Returns the avgSpeed.
	 * @return int
	 */
	public int getAvgSpeed() {
		return sumSpeed / lines.size();
	}

	/**
	 * Returns the minSpeed.
	 * @return int
	 */
	public int getMinSpeed() {
		return minSpeed;
	}
	

	/**
	 * Returns the endIndex.
	 * @return int
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * Returns the startIndex.
	 * @return int
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @see de.gliderpilot.trace.Dumpable#getDumpString()
	 */
	public String getDumpString() {
		DumpString dump = new DumpString();
		dump.println("Date", date);
		dump.println("Airfield", airField);
		dump.println("Start of Glide", startOfGlide);
		dump.println("End of Glide", endOfGlide);
		dump.println("File", igcFileName);
		dump.println((Dumpable) pilot);
		dump.println((Dumpable) glider);
		dump.println("Min. Speed", minSpeed);
		dump.println("Avg. Speed", getAvgSpeed());
		dump.println("Max. Speed", maxSpeed);
		
		return dump.toString();
	}

	/**
	 * Returns the lastPoint.
	 * @return Point4D
	 */
	public Point4D getLastPoint() {
		return lastPoint;
	}

}