/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.gui.AbstractZoomable4DPanelTime;
import de.gliderpilot.gui.Msg;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.preferences.PrefsChangeEvent;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @version $Revision$
 * @author $author$
 */
public class BaroPanel extends AbstractZoomable4DPanelTime
		implements Prefs.Listener {
	/**
	 * Creates a new BaroPanel object.
	 * 
	 * @param trackTable DOCUMENT ME!
	 */
	public BaroPanel(TrackTable trackTable) {
		super(trackTable);
		setName(Msg.get(Msg.BARO));
		contextMenu = new BaroPanelContextMenu();

		Prefs airPref = AirspacePrefs.getReference();
		airPrefs.addPreferenceChangeListener(this);

		setMode(AirspacePrefs.BARO_MODE.intValue());
	}

	/**
	 * @see airspace.swing.AbstractZoomable4DPanel#setCenter(Point4D)
	 */
	public void setCenter(Point4D center) {
		super.setCenter(center);

		Rectangle4D realRect = getRealRect();
		double diff = realRect.top - realRect.bottom;
		realRect.setTop(center.getPressureHeight() + (diff / 2));
		realRect.setBottom(center.getPressureHeight() - (diff / 2));
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
	 * @see Listener#preferenceChange(PrefsChangeEvent)
	 */
	public void preferenceChange(PrefsChangeEvent evt) {
		if (evt.getNode() instanceof AirspacePrefs) {
			if (evt.getNewPref() == AirspacePrefs.BARO_MODE) {
				String mode = evt.getNewPref().stringValue();
				setMode(Integer.parseInt(mode));
				contentChanged();
				repaint();
			}

			if (evt.getNewPref() == AirspacePrefs.NUM_XSCALE) {
				contentChanged();
				repaint();
			}

			if (evt.getNewPref() == AirspacePrefs.NUM_YSCALE) {
				contentChanged();
				repaint();
			}
		}
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
	 * @see airspace.swing.AbstractZoomable4DPanel#calculateTranslation()
	 */
	protected void calculateTranslation() {
		super.calculateTranslation();

		double diffHeight = getRealRect().top - getRealRect().bottom;

		if (diffHeight == 0) {
			diffHeight = 1;
		}

		double zoomY = (double) getHeight() / diffHeight;

		if (zoomY <= 0) {
			repaint();

			return;
		}

		setZoomY(zoomY);

		Point4D center = getRealRect().getCenter4D();
		getXform().setToTranslation(getWidth() / 2, getHeight() / 2);
		getXform().scale(getZoomX(), -zoomY);
		getXform()
			.translate(-center.getDate().getTime(), -center.getPressureHeight());

		createReverseXform(getXform());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param g2 DOCUMENT ME!
	 */
	protected void drawScale(Graphics2D g2) {
		super.drawScale(g2);
		g2.setColor(Color.black);

		int heightDiff = (int) (getRealRect().top - getRealRect().bottom) / getNumYScale();
		int height = (int) getRealRect().top;
		int yDiff = getHeight() / getNumYScale();
		int y = 0;

		for (int i = 0; i < getNumYScale(); i++) {
			g2.drawLine(0, y, getWidth(), y);
			g2.drawString("" + height + " m", 0, y);
			y += yDiff;
			height -= heightDiff;
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void fitViewToZoomRect() {
		Point4D corner1 = new Point4D(getZoomPoint1().x, getZoomPoint1().y);
		getReverseXform().transform(corner1, corner1);

		Point4D corner2 = new Point4D(getZoomPoint2().x, getZoomPoint2().y);
		getReverseXform().transform(corner2, corner2);

		getRealRect().bottom = Math.min(corner1.y, corner2.y);
		getRealRect().top = Math.max(corner1.y, corner2.y);
		super.fitViewToZoomRect();
	}

	class BaroPanelContextMenu extends JPopupMenu {
		public BaroPanelContextMenu() {
			JMenuItem item = new JMenuItem(Msg.get(Msg.START_TIME));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getTrack() != null) {
						Point4D p = (Point4D) mousePoint.clone();
						getReverseXform().transform(p, p);
						getTrack().setStartOfGlide(new Date((long) p.getX()));
					}
				}
			});
			add(item);
			item = new JMenuItem(Msg.get(Msg.END_TIME));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getTrack() != null) {
						Point4D p = (Point4D) mousePoint.clone();
						getReverseXform().transform(p, p);
						getTrack().setEndOfGlide(new Date((long) p.getX()));
					}
				}
			});
			add(item);
			item = new JMenuItem(Msg.format(Msg.FIT_VIEW_TO_0, Msg.TRACK));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getTrack() != null) {
						fitViewToRect(getTrack().getBounds4D());
					}
				}
			});
			add(item);
		}
	}

	class MapMouseListener
			extends AbstractZoomable4DPanelTime.MapMouseListener {

		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);

			if (e.isPopupTrigger()) {
				mousePoint.x = e.getX();
				mousePoint.y = e.getY();
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);

			if (e.isPopupTrigger()) {
				mousePoint.x = e.getX();
				mousePoint.y = e.getY();
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);

			if (e.isPopupTrigger()) {
				mousePoint.x = e.getX();
				mousePoint.y = e.getY();
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}