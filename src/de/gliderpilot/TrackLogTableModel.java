/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot;


import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class TrackLogTableModel extends AbstractTableModel {
	private static TrackLogTableModel tableModel;
	ArrayList tracks;

	/**
	 * Constructor for TrackLogTableModel.
	 */
	private TrackLogTableModel() {
		tracks = new ArrayList();

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static synchronized TrackLogTableModel getInstance() {
		if (tableModel == null) {
			tableModel = new TrackLogTableModel();
		}

		return tableModel;
	}

	/**
	 * @see TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 3;
	}

	/**
	 * @see TableModel#getRowCount()
	 */
	public int getRowCount() {
		return tracks.size();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param i DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public TrackLog getTrackLog(int i) {
		return (TrackLog) tracks.get(i);
	}

	/**
	 * @see TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		TrackLog track = (TrackLog) tracks.get(rowIndex);
		if (columnIndex == 0) {
			Date date = track.getDate();
			if (date == null) return "";
			return date;
		}
		if (columnIndex == 1) {
			if (track.getPilot() == null) return "";
			String name = track.getPilot().getName();
			if (name != null) return name;
			return "";
		}
		if (columnIndex == 2) {
			if (track.getGlider() == null) return "";
			String glider = track.getGlider().getCallSign();
			if (glider != null) return glider;
			return "";
		}
		return "";
	}

	/**
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	public void addTrackLog(TrackLog track) {
		tracks.add(track);
		fireTableRowsInserted(tracks.size() - 1, tracks.size() - 1);

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index DOCUMENT ME!
	 */
	public void removeTrackLog(int index) {
		fireTableRowsDeleted(index, index);

	}
	/**
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int columnIndex) {
		if (columnIndex == 0) return Date.class;
		return String.class;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) return "Date";
		if (columnIndex == 1) return "Pilot";
		return "Glider";
	}

}