/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import java.util.Iterator;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.tracklog.Glider;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class AbstractOlcTask extends AbstractTask implements OlcTask {
	private TrackLog trackLog;
	
	/**
	 * Constructor for AbstractOlcTask.
	 * 
	 * @param trackLog
	 * @param numPoints DOCUMENT ME!
	 */
	public AbstractOlcTask(TrackLog trackLog, int numPoints) {
		super(numPoints);
		this.trackLog = trackLog;
	}
	
	public void addTrackPoint(int index) {
		addTaskPoint(trackLog.getPointAt(index));
	}
	
	public void removeTrackPoint(int index) {
		removeTaskPoint(trackLog.getPointAt(index));
	}
	
	public boolean containsTrackPoint(int index) {
		return contains(trackLog.getPointAt(index));
	}

	/**
	 * Returns the trackLog.
	 * 
	 * @return TrackLog
	 */
	public TrackLog getTrackLog() {
		return trackLog;
	}

	
	/**
	 * 	 * @see de.gliderpilot.task.Task#isValid()	 */
	public boolean isValid() {
		Iterator iterator = iterator();
		Point4D first = null;
		Point4D last = null;
		if (iterator.hasNext()) {
			first = (Point4D) iterator.next();
		}
		while (iterator.hasNext()) {
			Point4D element = (Point4D) iterator.next();
			if (!iterator.hasNext()) {
				last = element;
				break;
			}
		}
		if (first != null && last != null) {
			return first.getPressureHeight() - 1000 <  last.getPressureHeight();
		}
		return false;
	}
	
	/**
	 * @see de.gliderpilot.task.AbstractTask#getIndex()
	 */
	public int getIndex() {
		Glider glider = trackLog.getGlider();
		if (glider != null) {
			return glider.getIndex();
		}
		return super.getIndex();
	}

	/**
	 * @see de.gliderpilot.task.AbstractTask#setIndex(int)
	 */
	public void setIndex(int index) {
		Glider glider = trackLog.getGlider();
		if (glider != null) {
			glider.setIndex(index);
		}
		super.setIndex(index);
	}

}