/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import java.io.InputStream;

import org.apache.log4j.Logger;

import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class ExternalAppOptimizer extends AbstractOptimizer {
	private Process process;

	/**
	 * Creates a new ExternalAppOptimizer object.
	 */
	public ExternalAppOptimizer() {
	}

	/**
	 * Creates a new ExternalAppOptimizer object.
	 * 
	 * @param track DOCUMENT ME!
	 */
	public ExternalAppOptimizer(TrackLog track) {
		super(track);
	}

	/**
	 * DOCUMENT ME!
	 */
	public final void cancel() {
		super.cancel();

		if (process != null) {
			process.destroy();
			process = null;
		}
	}

	/**
	 * @see de.gliderpilot.task.Optimizer#getMax()
	 */
	public int getMax() {
		return 0;
	}

	/**
	 * @see de.gliderpilot.task.Optimizer#getMin()
	 */
	public int getMin() {
		return 0;
	}

	/**
	 * @see de.gliderpilot.task.Optimizer#getProgress()
	 */
	public int getProgress() {
		return 0;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected abstract String getCommand();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param inStream DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws Exception DOCUMENT ME!
	 */
	protected abstract Task parseResult(InputStream inStream, InputStream errStream)
			throws Exception;

	/**
	 * DOCUMENT ME!
	 */
	protected final void optimize() {
		setCanceled(false);
		String command = getCommand();
		Logger.getLogger(LOGGER).info("Executing '" + command + "'");

		try {
			process = Runtime.getRuntime().exec(command);
			
			InputStream inStream = process.getInputStream();
			InputStream errStream = process.getErrorStream();

			setBestTask(parseResult(inStream, errStream));
		} catch (Exception e) {
			e.printStackTrace();
		}

		process = null;
		cancel();
	}
}