/**
 * Created on Oct 27, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.task;

import de.gliderpilot.tracklog.TrackLog;

/**
 * @author Tobias Schulte
 *
 */
public interface OlcTask extends Task {

	public TrackLog getTrackLog();

	public void addTrackPoint(int index);

	public void removeTrackPoint(int index);
	
	public boolean containsTrackPoint(int index);

}
