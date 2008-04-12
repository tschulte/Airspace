/**
 * Created on Oct 20, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import de.gliderpilot.igc.IgcFile;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.task.ExternalAppOptimizer;
import de.gliderpilot.task.OlcOptimizer;
import de.gliderpilot.task.Task;
import de.gliderpilot.tracklog.TrackLog;

/**
 * @author Tobias Schulte
 *
 */
public class OptimizePerformanceTest extends ExternalAppOptimizer {
	static DateFormat timeFormat = new SimpleDateFormat("yy-MM-dd_HH:mm:ss");
	static {
		timeFormat.setTimeZone(SimpleTimeZone.getTimeZone("UTC"));
	}

	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) return;
		TrackLog track = new TrackLog();
		new IgcFile(args[0], track).parse();
		if (args.length > 1) {
			Date start = timeFormat.parse(args[1]);
			track.setStartOfGlide(start);
		}
		if (args.length > 2) {
			Date end = timeFormat.parse(args[2]);
			track.setEndOfGlide(end);
		}
		
		OlcOptimizer optimizer = new OlcOptimizer(track);
		new Thread(optimizer).start();
	}

	/**
	 * @see de.gliderpilot.task.ExternalAppOptimizer#getCommand()
	 */
	protected String getCommand() {
		StringBuffer buf = new StringBuffer();
		buf.append("java -cp ");
		buf.append(System.getProperty("java.class.path"));
		buf.append(" ");
		buf.append(getClass().getName());
		buf.append(" ");
		buf.append(track.getIgcFileName());
		buf.append(" ");
		buf.append(timeFormat.format(track.getStartOfGlide()));
		buf.append(" ");
		buf.append(timeFormat.format(track.getEndOfGlide()));
		return buf.toString();
	}

	/**
	 * @see de.gliderpilot.task.ExternalAppOptimizer#parseResult(java.io.InputStream)
	 */
	protected Task parseResult(InputStream inStream, InputStream errStream) throws Exception {
		BufferedReader reader = new BufferedReader(
										new InputStreamReader(inStream));
										
		BufferedReader errReader = new BufferedReader(
										new InputStreamReader(errStream));
										
		boolean parseStd = false;
		boolean parseErr = true;
		while (parseStd || parseErr) {
			String line = null;
			String errLine = null;
			if (parseStd) {
				line = reader.readLine();
			}
			if (parseErr) {
				errLine = errReader.readLine();
			}
			if (line != null) {
				System.out.println(line);
			} else { 
				parseStd = false;
			}
			if (errLine != null) {
				System.out.println(errLine);
			} else {
				parseErr = false;
			}
		}
		
		return null;
	}

	/**
	 * @see de.gliderpilot.preferences.ExternalClass#getPrefs()
	 */
	public Prefs getPrefs() {
		return null;
	}

}
