/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import de.gliderpilot.TrackTable;
import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.gui.event.TrackParamsChangedEvent;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.preferences.PrefsChangeEvent;
import de.gliderpilot.util.Util;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class AbstractZoomable4DPanelTime
		extends AbstractZoomable4DPanel {
	protected static final DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	static {
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	static int numXScale = Integer.parseInt(AirspacePrefs.NUM_XSCALE.stringValue());
	static int numYScale = Integer.parseInt(AirspacePrefs.NUM_YSCALE.stringValue());

	static {
		AirspacePrefs.getReference().addPreferenceChangeListener(new Prefs.Listener() {
			public void preferenceChange(PrefsChangeEvent evt) {
				if (evt.getNode() instanceof AirspacePrefs) {
					if (evt.getNewPref() == AirspacePrefs.NUM_XSCALE) {
						numXScale = Integer.parseInt(evt.getNewPref().stringValue());
					}

					if (evt.getNewPref() == AirspacePrefs.NUM_YSCALE) {
						numYScale = Integer.parseInt(evt.getNewPref().stringValue());
					}
				}
			}
		});
	}

	/**
	 * Creates a new AbstractZoomable4DPanelTime object.
	 * 
	 * @param trackTable DOCUMENT ME!
	 */
	public AbstractZoomable4DPanelTime(TrackTable trackTable) {
		super(trackTable);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param center DOCUMENT ME!
	 */
	public void setCenter(Point4D center) {
		double diff = realRect.date2 - realRect.date1;
		realRect.setDate1((long) (center.getDate().getTime() - diff / 2));
		realRect.setDate2((long) (center.getDate().getTime() + diff / 2));
		realRect.center = null;
		calculateTranslation();
	}

	/**
	 * Sets the numYScale.
	 * 
	 * @param numYScale The numYScale to set
	 */
	public static void setNumYScale(int numYScale) {
		AbstractZoomable4DPanelTime.numYScale = numYScale;
	}

	/**
	 * Returns the numYScale.
	 * 
	 * @return int
	 */
	public static int getNumYScale() {
		return numYScale;
	}

	/**
	 * @see AbstractZoomable4DPanel#setZoom(double, double)
	 */
	public void setZoom(double zoomX, double zoomY) {
//		if (zoomX > 0 && zoomY > 0) {
//			super.setZoom(zoomX, zoomY);
//			Point4D p = realRect.getCenter4D().getPointAt(xOffs, yOffs);
//			realRect.setFrameFromCenter(realRect.getCenter4D(), p);
//			calculateTranslation();
//		}
	}

	/**
	 * @see AbstractZoomable4DPanel#moveCenter(int, int)
	 */
	public void moveCenter(int systemX, int systemY) {
		Point4D p = realRect.getCenter4D();
		p.move(Util.getRealValue(systemX, zoomX), 
			   Util.getRealValue(-systemY, zoomX));
		Logger.getLogger(LOGGER).info("After: " + p.getDumpString());
		setCenter(p);
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void calculateTranslation() {
		super.calculateTranslation();

		long diffDate = realRect.date2 - realRect.date1;

		if (diffDate == 0) {
			diffDate = 1;
		}

		double zoomX = (double) getWidth() / diffDate;

		if (zoomX <= 0) {
			repaint();

			return;
		}

		this.zoomX = zoomX;

		Logger.getLogger(LOGGER).info("ZOOM: " + zoomX);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param g2 DOCUMENT ME!
	 */
	protected void drawCrossHair(Graphics2D g2) {
		g2.drawLine((int) mousePoint.getX(), 0, (int) mousePoint.getX(), 
					getHeight());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param g2 DOCUMENT ME!
	 */
	protected void drawScale(Graphics2D g2) {
		long dateDiff = (realRect.date2 - realRect.date1) / numXScale;
		long date = realRect.date1;
		int xDiff = getWidth() / numXScale;
		int x = 0;
		g2.setColor(Color.black);

		int stringY = getHeight();

		for (int i = 0; i < numXScale; i++) {
			g2.drawLine(x, 0, x, getHeight());
			g2.drawString(timeFormat.format(new Date(date)), x, stringY);
			x += xDiff;
			date += dateDiff;
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void fitViewToZoomRect() {
		Point4D corner1 = new Point4D(zoomPoint1.x, zoomPoint1.y);
		reverseXform.transform(corner1, corner1);

		Point4D corner2 = new Point4D(zoomPoint2.x, zoomPoint2.y);
		reverseXform.transform(corner2, corner2);

		realRect.date1 = (long) Math.min(corner1.x, corner2.x);
		realRect.date2 = (long) Math.max(corner1.x, corner2.x);

		realRect.center = null;

		zoomPoint1 = null;
		zoomPoint2 = null;
		zoomRect = null;

		fitViewToRect(realRect);
	}

	public void trackParamsChanged(TrackParamsChangedEvent evt) {
		int param = evt.getParam();
		if (param == TrackParamsChangedEvent.START_TIME
			|| param == TrackParamsChangedEvent.END_TIME) {
			super.trackParamsChanged(evt);
		}
	}

}