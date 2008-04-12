/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.gliderpilot.airspace.AirspaceContainer;
import de.gliderpilot.airspace.AirspaceVector;
import de.gliderpilot.airspace.OpenAirspaceFile;
import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.gui.AbstractZoomable4DPanel;
import de.gliderpilot.gui.AbstractZoomable4DPanelXY;
import de.gliderpilot.gui.Msg;
import de.gliderpilot.preferences.FilePreference;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.preferences.PrefsChangeEvent;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 *
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class MapPanel extends AbstractZoomable4DPanelXY
		implements Prefs.Listener {
	/**
	 * Creates a new MapPanel object.
	 * 
	 * @param trackTable DOCUMENT ME!
	 */
	public MapPanel(TrackTable trackTable) {
		super(trackTable);
		setName(Msg.get(Msg.MAP));
		
		contextMenu = new MapPanelContextMenu();

		Prefs prefs = AirspacePrefs.getReference();
		prefs.addPreferenceChangeListener(this);

		File file = AirspacePrefs.MAP_FILE.fileValue();
		if (file != null && file.exists()) {
			AirspaceContainer airspace = new AirspaceVector();
			new OpenAirspaceFile(file, airspace).read();
			airspace.trimToSize();
			setAirspace(airspace);
		} else {
			setAirspace(new AirspaceVector());
		}
	}

	/**
	 * Sets the airspace.
	 * 
	 * @param airspace The airspace to set
	 */
	public void setAirspace(AirspaceContainer airspace) {
		addDrawable(AIRSPACE, airspace);
	}

	/**
	 * Gets the airspace.
	 * 
	 * @return Returns a AirspaceContainer
	 */
	public AirspaceContainer getAirspace() {
		return (AirspaceContainer) getDrawable(AIRSPACE);
	}

	/**
	 * Gets the track.
	 * 
	 * @return Returns a TrackLog
	 */
	public TrackLog getTrack() {
		return (TrackLog) getDrawable(TRACK + 0);
	}

	/**
	 * @see Listener#preferenceChange(PrefsChangeEvent)
	 */
	public void preferenceChange(PrefsChangeEvent evt) {
		if (evt.getNewPref() == AirspacePrefs.MAP_FILE) {
			File file = ((FilePreference) evt.getNewPref()).fileValue();
			if (file != null && file.exists()) {
				AirspaceContainer airspace = new AirspaceVector();
				new OpenAirspaceFile(file, airspace).read();
				airspace.trimToSize();
				setAirspace(airspace);
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

//
//	/**
//	 * Sets the track.
//	 * @param track The track to set
//	 */
////	public void setTrack(TrackLog track) {
////		addDrawable(TRACK, track);
////	}
//	
//	public void addTrack(TrackLog track) {
//		addDrawable(TRACK, track);
//	}
	class MapMouseListener extends AbstractZoomable4DPanel.MapMouseListener {
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);

			if (e.isPopupTrigger()) {
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);

			if (e.isPopupTrigger()) {
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);

			if (e.isPopupTrigger()) {
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class MapPanelContextMenu extends JPopupMenu {
		public MapPanelContextMenu() {
			JMenuItem item;
			item = new JMenuItem(Msg.format(Msg.FIT_VIEW_TO_0, Msg.TRACK));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getTrack() != null) {
						fitViewToRect(getTrack().getBounds4D());
					}
				}
			});
			add(item);
			item = new JMenuItem(Msg.format(Msg.FIT_VIEW_TO_0, Msg.AIRSPACE));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getAirspace() != null) {
						fitViewToRect(getAirspace().getBounds4D());
					}
				}
			});
			add(item);
		}
	}

}