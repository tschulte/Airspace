/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


import java.awt.geom.NoninvertibleTransformException;

import org.apache.log4j.Logger;

import de.gliderpilot.TrackTable;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.gui.event.TrackParamsChangedEvent;
import de.gliderpilot.util.Util;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class AbstractZoomable4DPanelXY
		extends AbstractZoomable4DPanel {
	/**
	 * Creates a new AbstractZoomable4DPanelXY object.
	 * 
	 * @param trackTable DOCUMENT ME!
	 */
	public AbstractZoomable4DPanelXY(TrackTable trackTable) {
		super(trackTable);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param center DOCUMENT ME!
	 */
	public void setCenter(Point4D center) {
		double xOffs = Util.getRealValue(getWidth() / 2, zoomX);
		double yOffs = Util.getRealValue(getHeight() / 2, zoomX);
		realRect.setFrameFromCenter(center, center.getPointAt(xOffs, yOffs));
		realRect.center = null;
		calculateTranslation();
	}

	/**
	 * @see AbstractZoomable4DPanel#setZoom(double, double)
	 */
	public void setZoom(double zoomX, double zoomY) {
		if ((zoomX != 0) && (zoomX < 7000000)) {
			super.setZoom(zoomX, zoomY);

			double xOffs = Util.getRealValue(getWidth() / 2, zoomX);
			double yOffs = Util.getRealValue(getHeight() / 2, zoomX);
			Point4D p = realRect.getCenter4D().getPointAt(xOffs, yOffs);
			realRect.setFrameFromCenter(realRect.getCenter4D(), p);
			calculateTranslation();
		}
	}

	/**
	 * @see AbstractZoomable4DPanel#moveCenter(int, int)
	 */
	public void moveCenter(int systemX, int systemY) {
		Logger.getLogger(getClass())
			  .info("MoveCenter (" + systemX + ", " + systemY + ")");

		Point4D p = realRect.getCenter4D();
		Logger.getLogger(getClass()).info("Before: " + p.getDumpString());
		p.move(Util.getRealValue(systemX, zoomX), 
			   Util.getRealValue(-systemY, zoomX));
		Logger.getLogger(getClass()).info("After: " + p.getDumpString());
		setCenter(p);
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void calculateTranslation() {
		super.calculateTranslation();

		Point4D realUl = new Point4D(realRect.getX(), realRect.getY());
		Logger.getLogger(getClass()).info("Real Corner: " + 
									  realUl.getDumpString());

		double zoomX = Util.getFactor(getWidth(), 
									  2 * realRect.getCenter4D()
												  .getXDistanceFrom(realUl));
		double zoomY = Util.getFactor(getHeight(), 
									  2 * realRect.getCenter4D()
												  .getYDistanceFrom(realUl));
		double zoom = Math.min(zoomX, zoomY);

		if ((zoom <= 0) || (zoom > 7000000)) {
			repaint();

			return;
		}

		this.zoomX = zoom;

		Logger.getLogger(getClass()).info("ZOOM: " + zoom);

		double xOffs = Util.getRealValue(getWidth() / 2, zoom);
		double yOffs = Util.getRealValue(getHeight() / 2, zoom);
		Point4D ul = realRect.getCenter4D().getPointAt(-xOffs, yOffs);
		realRect.setFrameFromCenter(realRect.getCenter4D(), ul);

		xform.setToTranslation(getWidth() / 2, getHeight() / 2);
		xform.scale(zoom * realRect.getCenter4D().getXCorrection(null), -zoom);
		xform.translate(-realRect.getCenterX(), -realRect.getCenterY());

		try {
			reverseXform = xform.createInverse();
		} catch (NoninvertibleTransformException e) {
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
		realRect.setFrameFromDiagonal(corner1, corner2);
		realRect.center = null;

		zoomPoint1 = null;
		zoomPoint2 = null;
		zoomRect = null;

		fitViewToRect(realRect);
	}

	public void trackParamsChanged(TrackParamsChangedEvent evt) {
		int param = evt.getParam();
		if (param == TrackParamsChangedEvent.TASK) {
			super.trackParamsChanged(evt);
		}
	}

}