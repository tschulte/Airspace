/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class AbstractOptimizer implements Optimizer {
	protected TrackLog track;
	protected int endIndex;
	protected int startIndex;
	boolean canceled = false;
	private Task bestTask;
	private int progress;

	/**
	 * Creates a new AbstractOptimizer object.
	 */
	public AbstractOptimizer() {
	}

	/**
	 * Creates a new AbstractOptimizer object.
	 * 
	 * @param track DOCUMENT ME!
	 */
	public AbstractOptimizer(TrackLog track) {
		this.track = track;

		startIndex = track.getStartIndex();
		endIndex = track.getEndIndex();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public synchronized boolean isCanceled() {
		return canceled;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getMax() {
		return endIndex;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getMin() {
		return startIndex;
	}

	/**
	 * DOCUMENT ME!
	 */
	public synchronized void cancel() {
		canceled = true;
	}

	/**
	 * DOCUMENT ME!
	 */
	public final void run() {
		optimize();
		cancel();
	}

	/**
	 * Sets the bestTask.
	 * 
	 * @param bestTask The bestTask to set
	 */
	public void setBestTask(Task bestTask) {
		this.bestTask = bestTask;
		track.setTask(bestTask);
	}

	/**
	 * Returns the bestTask.
	 * 
	 * @return Task
	 */
	public Task getBestTask() {
		return bestTask;
	}

	/**
	 * Returns the progress.
	 * 
	 * @return int
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * Sets the track.
	 * 
	 * @param track The track to set
	 */
	public void setTrack(TrackLog track) {
		this.track = track;
	}

	/**
	 * DOCUMENT ME!
	 */
	protected abstract void optimize();

	/**
	 * Sets the progress.
	 * 
	 * @param progress The progress to set
	 */
	protected synchronized void setProgress(int progress) {
		this.progress = progress;

//		Logger.getLogger(LOGGER).info("Progress: "+progress);
	}
	/**
	 * Sets the canceled.
	 * @param canceled The canceled to set
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

}