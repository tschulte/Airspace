/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.gui.AbstractZoomable4DPanel;
import de.gliderpilot.gui.AbstractZoomable4DPanelTime;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class VarioPanel extends AbstractZoomable4DPanelTime {
	/**
	 * Creates a new VarioPanel object.
	 * 
	 * @param trackTable DOCUMENT ME!
	 */
	public VarioPanel(TrackTable trackTable) {
		super(trackTable);
		setName("Vario");
		setMode(VARIO);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public TrackLog getTrack() {
		return (TrackLog) getDrawable(TRACK + 0);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected MouseListener getMapMouseListener() {
		return new MapMouseListener();
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void calculateTranslation() {
		super.calculateTranslation();

		double zoomY = (double) getHeight() / 20;
		setZoomY(zoomY);

		Point4D center = getRealRect().getCenter4D();
		AffineTransform xform = getXform();
		xform.setToTranslation(getWidth() / 2, getHeight() / 2);
		xform.scale(getZoomX(), -getZoomY());
		xform.translate(-center.getDate().getTime(), 0);

		createReverseXform(xform);
	}

	class MapMouseListener extends AbstractZoomable4DPanel.MapMouseListener {
		/**
		 * Invoked when a mouse button has been released on a component.
		 * 
		 * @param e DOCUMENT ME!
		 */
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);

			if (((e.getModifiers() & MouseEvent.BUTTON2_MASK) != 0) && 
					(getTrack() != null)) {
				fitViewToRect(getTrack().getBounds4D());
			}
		}
	}
}