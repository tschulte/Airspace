/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.io;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import de.gliderpilot.trace.TraceLevels;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class NmeaListener extends Thread implements TraceLevels {
	protected BufferedReader bufferedReader;
	private boolean stop = false;
	private int baudrate = 4800;
	private int dataBits = 8;
	private int portId = 0;
	private int stopBits = 0;

	/**
	 * Creates a new NmeaListener object.
	 */
	public NmeaListener() {
	}

	/**
	 * Sets the dataBits.
	 * 
	 * @param dataBits The dataBits to set
	 */
	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	/**
	 * Gets the dataBits.
	 * 
	 * @return Returns a int
	 */
	public int getDataBits() {
		return dataBits;
	}

	/**
	 * DOCUMENT ME!
	 */
	public final void run() {
		connect();

		try {
			while (!stop) {
				String line = bufferedReader.readLine();
				Logger.getLogger(LOGGER).fine(line);
			}
		} catch (IOException ioe) {
			close();
			Logger.getLogger(LOGGER).severe(ioe.toString());
		} catch (NullPointerException npe) {
			Logger.getLogger(LOGGER).severe("NullPointerException in run()");
		}
	}

	/**
	 * Sets the baudrate.
	 * 
	 * @param baudrate The baudrate to set
	 */
	public void setBaudrate(int baudrate) {
		this.baudrate = baudrate;
	}

	/**
	 * Gets the baudrate.
	 * 
	 * @return Returns a int
	 */
	public int getBaudrate() {
		return baudrate;
	}

	/**
	 * Sets the portId.
	 * 
	 * @param portId The portId to set
	 */
	public void setPortId(int portId) {
		this.portId = portId;
	}

	/**
	 * Gets the portId.
	 * 
	 * @return Returns a int
	 */
	public int getPortId() {
		return portId;
	}

	/**
	 * Sets the stopBits.
	 * 
	 * @param stopBits The stopBits to set
	 */
	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	/**
	 * Gets the stopBits.
	 * 
	 * @return Returns a int
	 */
	public int getStopBits() {
		return stopBits;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void close() {
		stop = true;

		if (bufferedReader != null) {
			try {
				bufferedReader.close();
				bufferedReader = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected abstract boolean connect();
}