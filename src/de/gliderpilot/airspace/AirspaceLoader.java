/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.airspace;


import java.io.File;

import org.apache.log4j.Logger;

import de.gliderpilot.trace.TraceLevels;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class AirspaceLoader extends Thread implements TraceLevels {
	AirspaceContainer airspace;
	File file;
	int fileType;

	/**
	 * Creates a new AirspaceLoader object.
	 * 
	 * @param airspace DOCUMENT ME!
	 * @param file DOCUMENT ME!
	 * @param fileType DOCUMENT ME!
	 */
	public AirspaceLoader(AirspaceContainer airspace, File file, int fileType) {
		this.airspace = airspace;
		this.file = file;
		this.fileType = fileType;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void run() {
		AirspaceFile airspaceFile = null;

		switch (fileType) {
		case AirspaceFile.OPEN_AIRSPACE:
			Logger.getLogger(LOGGER)
				  .debug("Load data from OpenAirspaceFile " + file);
			airspaceFile = new OpenAirspaceFile(file, airspace);

			break;
		}

		if (airspaceFile != null) {
			airspaceFile.read();
			airspace.trimToSize();
		}
	}
}