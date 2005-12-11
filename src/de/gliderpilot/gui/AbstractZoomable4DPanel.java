/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.gliderpilot.TrackLogTableModel;
import de.gliderpilot.TrackTable;
import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.geom.Modes4D;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.geom.Point4DChangedEvent;
import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.gui.event.DisplayedRectChangeEvent;
import de.gliderpilot.gui.event.TrackParamsChangedEvent;
import de.gliderpilot.preferences.Prefs;

import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class AbstractZoomable4DPanel extends JPanel
		implements Modes4D, DisplayedRectChangeEvent.Source,
				   DisplayedRectChangeEvent.Listener, Point4DChangedEvent.Source,
				   Point4DChangedEvent.Listener, TrackParamsChangedEvent.Listener {
	protected static String AIRSPACE = "Airspace";
	protected static String TRACK = "Track";
	protected static Prefs airPrefs = AirspacePrefs.getReference();
	protected JPopupMenu contextMenu;
	protected Point4D mousePoint = new Point4D();
	AffineTransform imageXform;
	AffineTransform reverseXform;
	AffineTransform xform;
	Point zoomPoint1;
	Point zoomPoint2;
	Rectangle systemRect = new Rectangle();
	Rectangle zoomRect;
	Rectangle4D realRect = new Rectangle4D();
	double imageZoom = 1;
	double scrollAmount = 0.2;
	double zoomAmount = 2;
	double zoomX;
	double zoomY;
	int mode;
	private ArrayList displayedRectListeners = new ArrayList();
	private ArrayList pointChangeEventListeners = new ArrayList();

//	Point systemCenter = new Point();
	private BufferedImage bufferedImage;
	private BufferedImage xformImage;
	private Hashtable drawables = new Hashtable();
	private TrackLogTableModel tracks = TrackLogTableModel.getInstance();
	private TrackTable trackTable;
	private int displayedTracks;
	private boolean contentChanged = true;
	
	protected void contentChanged() {
		contentChanged = true;
		repaint();
	}

	/**
	 * Creates a new AbstractZoomable4DPanel object.
	 * 
	 * @param trackTable DOCUMENT ME!
	 */
	public AbstractZoomable4DPanel(TrackTable trackTable) {
		this.trackTable = trackTable;
		trackTable.getSelectionModel()
		  .addListSelectionListener(new TrackListSelectionListener());
		Logger.getLogger(getClass())
			  .debug("Width:" + getWidth() + ",Height:" + getHeight());
		xform = new AffineTransform();
		reverseXform = new AffineTransform();
		imageXform = new AffineTransform();
		addMouseListeners();
		addKeyListener();
		setDoubleBuffered(false);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param center DOCUMENT ME!
	 */
	public abstract void setCenter(Point4D center);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param systemX DOCUMENT ME!
	 * @param systemY DOCUMENT ME!
	 */
	public abstract void moveCenter(int systemX, int systemY);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param rect DOCUMENT ME!
	 */
	public final void fitViewToRect(Rectangle4D rect) {
		if (rect == null) {
			return;
		}

		realRect = (Rectangle4D) rect.clone();
		realRect.center = null;


		// the further processing has to be done in the child-classes
		calculateTranslation();
		repaint();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param key DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Drawable getDrawable(String key) {
		return (Drawable) drawables.get(key);
	}

	/**
	 * Sets the mode.
	 * 
	 * @param mode The mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Returns the mode.
	 * 
	 * @return int
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Sets the realRect.
	 * 
	 * @param realRect The realRect to set
	 */
	public void setRealRect(Rectangle4D realRect) {
		this.realRect = realRect;
	}

	/**
	 * Returns the realRect.
	 * 
	 * @return Rectangle4D
	 */
	public Rectangle4D getRealRect() {
		return realRect;
	}

	/**
	 * Returns the reverseXform.
	 * 
	 * @return AffineTransform
	 */
	public AffineTransform getReverseXform() {
		return reverseXform;
	}

	/**
	 * Returns the xform.
	 * 
	 * @return AffineTransform
	 */
	public AffineTransform getXform() {
		return xform;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param zoomX DOCUMENT ME!
	 * @param zoomY DOCUMENT ME!
	 */
	public void setZoom(double zoomX, double zoomY) {
		if ((zoomX > 0) && (zoomY > 0)) {
			this.zoomX = zoomX;
			this.zoomY = zoomY;
		}
	}

	/**
	 * Sets the zoomPoint1.
	 * 
	 * @param zoomPoint1 The zoomPoint1 to set
	 */
	public void setZoomPoint1(Point zoomPoint1) {
		this.zoomPoint1 = zoomPoint1;
	}

	/**
	 * Returns the zoomPoint1.
	 * 
	 * @return Point
	 */
	public Point getZoomPoint1() {
		return zoomPoint1;
	}

	/**
	 * Sets the zoomPoint2.
	 * 
	 * @param zoomPoint2 The zoomPoint2 to set
	 */
	public void setZoomPoint2(Point zoomPoint2) {
		this.zoomPoint2 = zoomPoint2;
	}

	/**
	 * Returns the zoomPoint2.
	 * 
	 * @return Point
	 */
	public Point getZoomPoint2() {
		return zoomPoint2;
	}

	/**
	 * Sets the zoomX.
	 * 
	 * @param zoomX The zoomX to set
	 */
	public void setZoomX(double zoomX) {
		this.zoomX = zoomX;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getZoomX() {
		return zoomX;
	}

	/**
	 * Sets the zoomY.
	 * 
	 * @param zoomY The zoomY to set
	 */
	public void setZoomY(double zoomY) {
		this.zoomY = zoomY;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public double getZoomY() {
		return zoomY;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param g DOCUMENT ME!
	 */
	public final void paintComponent(Graphics g) {
		if ((getWidth() == 0) || (getHeight() == 0)) {
			return;
		}

		if ((getWidth() != systemRect.getWidth()) || 
				(getHeight() != systemRect.getHeight())) {
			systemRect = getBounds();
			calculateTranslation();
		}

		Graphics2D g2;

		if (contentChanged) {
			if (xformImage != null) {
				g2 = (Graphics2D) g;


				// Clear the background (fill with white)	   
				// The clip region will limit the area that
				// actually gets cleared to save time
				g2.setColor(Color.white);
				g2.fillRect(0, 0, getWidth(), getHeight());

				g2.drawRenderedImage(xformImage, imageXform);
				repaint();

				return;
			}

			bufferedImage = new BufferedImage(getWidth(), getHeight(), 
											  BufferedImage.TYPE_INT_RGB);
			g2 = (Graphics2D) bufferedImage.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
								RenderingHints.VALUE_ANTIALIAS_ON);

			Color oldColor = g2.getColor();


			// Clear the background (fill with white)	   
			// The clip region will limit the area that
			// actually gets cleared to save time
			g2.setColor(Color.white);
			g2.fillRect(0, 0, getWidth(), getHeight());

			Iterator it = drawables.values().iterator();

			while (it.hasNext()) {
				Object o = it.next();

				if (o instanceof Drawable4D) {
					((Drawable4D) o).setMode(mode);
					((Drawable4D) o).draw(g2, realRect, xform);
				} else {
					((Drawable) o).draw(g2, realRect, xform);
				}
			}

			drawScale(g2);
			g2.setColor(oldColor);
			contentChanged = false;
		}

		g2 = (Graphics2D) g;

		if (bufferedImage == null) {
			return;
		}

		g2.drawRenderedImage(bufferedImage, null);

		if (zoomRect != null) {
			g2.draw(zoomRect);
		}

		drawCrossHair(g2);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param listener DOCUMENT ME!
	 */
	public void addDisplayedRectListener(DisplayedRectChangeEvent.Listener listener) {
		displayedRectListeners.add(listener);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param key DOCUMENT ME!
	 * @param drawable DOCUMENT ME!
	 */
	public void addDrawable(String key, Drawable drawable) {
		drawables.put(key, drawable);
		Logger.getLogger(getClass()).info("Added Drawable");
	}

	/**
	 * @see de.gliderpilot.geom.Point4DChangedEvent.Source#addListener(Listener)
	 */
	public void addPointChangedListener(Point4DChangedEvent.Listener l) {
		pointChangeEventListeners.add(l);
	}

	/**
	 * Sets the reverseXform.
	 * 
	 * @param xform The reverseXform to set
	 */
	public void createReverseXform(AffineTransform xform) {
		try {
			this.reverseXform = xform.createInverse();
		} catch (NoninvertibleTransformException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param evt DOCUMENT ME!
	 */
	public void displayedRectChanged(DisplayedRectChangeEvent evt) {
		if ((realRect != null) && !evt.getRect().equals(realRect)) {
			fitViewToRect(evt.getRect());
		}
	}

	/**
	 * @see de.gliderpilot.geom.Point4DChangedEvent.Listener#pointChanged(Point4DChangedEvent)
	 */
	public void pointChanged(Point4DChangedEvent e) {
		mousePoint = e.getNewPoint();
		repaint();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param key DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Drawable removeDrawable(String key) {
		Logger.getLogger(getClass()).info("removed Drawable");

		return (Drawable) drawables.remove(key);
	}

	/**
	 * @see airspace.swing.TrackParamsChangedEvent.Listener#trackParamsChanged(TrackParamsChangedEvent)
	 */
	public void trackParamsChanged(TrackParamsChangedEvent evt) {
		contentChanged();
		repaint();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param x DOCUMENT ME!
	 * @param y DOCUMENT ME!
	 */
	protected void setMousePoint(int x, int y) {
		mousePoint.x = x;
		mousePoint.y = y;
		fireMousePointChanged();
		repaint();
		Logger.getLogger(getClass()).debug("X: " + x + "; Y: " + y);
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void addKeyListener() {
		addKeyListener(new MapKeyListener());
	}

	/**
	 * DOCUMENT ME!
	 */
	protected final void addMouseListeners() {
		addMouseListener(getMapMouseListener());
		addMouseMotionListener(getMapMouseMotionListener());
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void calculateTranslation() {
		contentChanged();
		fireDisplayedRectChanged();

		// the further processing has to be done in the child-classes
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param g2 DOCUMENT ME!
	 */
	protected void drawCrossHair(Graphics2D g2) {
		g2.drawLine(0, (int) mousePoint.getY(), getWidth(), 
					(int) mousePoint.getY());
		g2.drawLine((int) mousePoint.getX(), 0, (int) mousePoint.getX(), 
					getHeight());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param g2 DOCUMENT ME!
	 */
	protected void drawScale(Graphics2D g2) {
	}

	/**
	 * DOCUMENT ME!
	 */
	protected abstract void fitViewToZoomRect();

	/**
	 * Method getMapMouseListener.
	 * 
	 * @return MouseListener
	 */
	protected MouseListener getMapMouseListener() {
		return new MapMouseListener();
	}

	/**
	 * Method getMapMouseMotionListener.
	 * 
	 * @return MouseMotionListener
	 */
	protected MouseMotionListener getMapMouseMotionListener() {
		return new MapMouseMotionListener();
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void fireDisplayedRectChanged() {
		DisplayedRectChangeEvent evt = new DisplayedRectChangeEvent(realRect);

		for (int i = 0; i < displayedRectListeners.size(); i++) {
			((DisplayedRectChangeEvent.Listener) displayedRectListeners.get(i)).displayedRectChanged(
					evt);
		}
	}

	void fireMousePointChanged() {
		Point4DChangedEvent e = new Point4DChangedEvent(mousePoint);

		for (int i = 0; i < pointChangeEventListeners.size(); i++) {
			((Point4DChangedEvent.Listener) pointChangeEventListeners.get(i)).pointChanged(e);
		}
	}

	protected class MapKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent event) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_UP:
				moveCenter(0, (int) (-scrollAmount * getHeight()));

				break;

			case KeyEvent.VK_DOWN:
				moveCenter(0, (int) (scrollAmount * getHeight()));

				break;

			case KeyEvent.VK_RIGHT:
				moveCenter((int) (scrollAmount * getWidth()), 0);

				break;

			case KeyEvent.VK_LEFT:
				moveCenter((int) (-scrollAmount * getWidth()), 0);

				break;

			case KeyEvent.VK_PLUS:
				setZoom(getZoomX() * zoomAmount, 1);

				break;

			case KeyEvent.VK_MINUS:
				setZoom(getZoomX() / zoomAmount, 1);

				break;
			}

			repaint();
		}
	}

	protected class MapMouseListener extends MouseAdapter {
		/**
		 * Invoked when the mouse has been clicked on a component.
		 * 
		 * @param e DOCUMENT ME!
		 */
		public void mouseClicked(MouseEvent e) {
			Logger.getLogger(getClass()).debug("mouseClicked");
		}

		/**
		 * Invoked when the mouse enters a component.
		 * 
		 * @param e DOCUMENT ME!
		 */
		public void mouseEntered(MouseEvent e) {
			Logger.getLogger(getClass()).debug("mouseEntered");
		}

		/**
		 * Invoked when the mouse exits a component.
		 * 
		 * @param e DOCUMENT ME!
		 */
		public void mouseExited(MouseEvent e) {
			Logger.getLogger(getClass()).debug("mouseExited");
		}

		/**
		 * Invoked when a mouse button has been pressed on a component.
		 * 
		 * @param e DOCUMENT ME!
		 */
		public void mousePressed(MouseEvent e) {
			Logger.getLogger(getClass()).debug("mousePressed");

			if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
				zoomPoint1 = new Point(e.getX(), e.getY());
				zoomRect = new Rectangle(zoomPoint1);
			}
		}

		/**
		 * Invoked when a mouse button has been released on a component.
		 * 
		 * @param e DOCUMENT ME!
		 */
		public void mouseReleased(MouseEvent e) {
			Logger.getLogger(getClass()).debug("mouseReleased");

			if (((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) && 
					(zoomPoint1 != null) && (zoomPoint2 != null)) {
				double xDiff = Math.abs(zoomPoint1.x - zoomPoint2.x);

				if (xDiff < 10) {
					zoomPoint1 = null;
					zoomPoint2 = null;
					zoomRect = null;
					repaint();

					return;
				}


//				xformImage = bufferedImage;
//				if (xformImage != null && zoomRect != null) {
//					imageXform.setToTranslation(
//						getWidth() / 2,
//						getHeight() / 2);
//					imageXform.scale(imageZoom, imageZoom);
//					imageXform.translate(
//						-zoomRect.getCenterX(),
//						-zoomRect.getCenterY());
//				}
				fitViewToZoomRect();
			}
		}
	}

	protected class MapMouseMotionListener extends MouseMotionAdapter {
		/**
		 * @see MouseMotionListener#mouseDragged(MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {
			if (zoomPoint1 != null) {
				zoomPoint2 = new Point(e.getX(), e.getY());
				zoomRect.setFrameFromDiagonal(zoomPoint1, zoomPoint2);
				Logger.getLogger(getClass()).debug("ZoomRect set");
				repaint();
			}
		}

		/**
		 * @see MouseMotionListener#mouseMoved(MouseEvent)
		 */
		public void mouseMoved(MouseEvent e) {
			if ((contextMenu == null) || !contextMenu.isShowing()) {
				setMousePoint(e.getX(), e.getY());
			}
		}
	}

//	/**
//	 * Gets the mapKeyListener.
//	 * @return Returns a MapKeyListener
//	 */
//	public MapKeyListener getMapKeyListener() {
//		return mapKeyListener;
//	}
	protected class TrackListSelectionListener implements ListSelectionListener {
		/**
		 * @see ListSelectionListener#valueChanged(ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent e) {
			for (int i = 0; i < displayedTracks; i++) {
				((TrackLog) removeDrawable(TRACK + i)).removeTrackParamsChangedListener(
						AbstractZoomable4DPanel.this);
			}

			int[] selectedElements = trackTable.getSelectedRows();

			for (displayedTracks = 0;
				 displayedTracks < selectedElements.length;
				 displayedTracks++) {
				TrackLog track = tracks.getTrackLog(
										 selectedElements[displayedTracks]);
				track.addTrackParamsChangedListener(
						AbstractZoomable4DPanel.this);
				addDrawable(TRACK + displayedTracks, track);
			}

			contentChanged();
			repaint();
		}
	}
}