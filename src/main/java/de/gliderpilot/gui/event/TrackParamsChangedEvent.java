/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.gui.event;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class TrackParamsChangedEvent {
	public static final int START_TIME = 1;
	public static final int END_TIME = 2;
	public static final int TASK = 3;
	
	private int param = 0;
	
	public TrackParamsChangedEvent(int param) {
		this.param = param;
	}
	
	public int getParam() {
		return param;
	}
	
	public interface Listener {
		public void trackParamsChanged(TrackParamsChangedEvent evt);
	}

	public interface Source {
		public void addTrackParamsChangedListener(Listener listener);

		public void removeTrackParamsChangedListener(Listener listener);
	}
}