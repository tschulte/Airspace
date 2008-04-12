/**
 * Created on 20.01.2003
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.trace;

import java.util.Hashtable;

/**
 * @author Tobias Schulte
 *
 */
public class Logger extends org.apache.log4j.Logger {
	
	static Hashtable loggers = new Hashtable();

	public static synchronized org.apache.log4j.Logger getLogger(String arg) {
		Logger logger = (Logger) loggers.get(arg);
		if (logger == null) {
			logger = new Logger(arg);
			loggers.put(arg, logger);
		}
		return logger;
	}
	

	/**
	 * Constructor for Logger.
	 * @param arg0
	 */
	public Logger(String arg0) {
		super(arg0);
	}

}
