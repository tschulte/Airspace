/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.optigc;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.preferences.Pref;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.task.ExternalAppOptimizer;
import de.gliderpilot.task.OlcTask2002;
import de.gliderpilot.task.Task;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class OptiGcOptimizer extends ExternalAppOptimizer {
	static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * Creates a new OptiGcOptimizer object.
	 */
	public OptiGcOptimizer() {
	}

	/**
	 * Constructor for OptiGcOptimizer.
	 * 
	 * @param track
	 */
	public OptiGcOptimizer(TrackLog track) {
		super(track);
	}

	/**
	 * @see de.gliderpilot.task.Optimizer#getPrefs()
	 */
	public Prefs getPrefs() {
		return OptigcPrefs.getReference();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected String getCommand() {
		Pref pref = OptigcPrefs.OPTIGC_APP;
		StringBuffer buffer = new StringBuffer();
		buffer.append(pref.stringValue());
		buffer.append(" -s");
		buffer.append(OptigcPrefs.OPTIGC_MAX_SPEED.stringValue());
		buffer.append(" -b");
		buffer.append(dateFormat.format(track.getStartOfGlide()));
		buffer.append(" -e");
		buffer.append(dateFormat.format(track.getEndOfGlide()));

		buffer.append(" \"");
		buffer.append(track.getIgcFileName());
		buffer.append("\"");

		return buffer.toString();
	}

	/**
	 * @see de.gliderpilot.task.ExternalAppOptimizer#parseResult()
	 */
	protected Task parseResult(InputStream inStream, InputStream errStream) throws Exception {
		BufferedReader reader = new BufferedReader(
										new InputStreamReader(inStream));
		BufferedReader errReader = new BufferedReader(
										new InputStreamReader(errStream));
										
		OlcTask2002 actualResult = new OlcTask2002(track);
		OlcTask2002 bestResult = new OlcTask2002(track);
		int count = -1;

		boolean parseStd = true;
		boolean parseErr = true;
		while (parseStd || parseErr) {
			String line = null;
			String errLine = null;
			if (reader.ready()) {
				line = reader.readLine();
			}
			if (errReader.ready()) {
				errLine = errReader.readLine();
			}
			if (errLine != null) {
				System.out.println(errLine);
			} else {
				parseErr = false;
			}
			if (line != null) {
				System.out.println(line);

				if (line.startsWith("beste")) {
					count = 0;
	
					if (bestResult.getScore() < actualResult.getScore()) {
						bestResult = (OlcTask2002) actualResult.clone();
					}
				} else if (line.startsWith("p") && (count >= 0)) {
					int start = line.indexOf(' ') + 1;
					int end = line.indexOf(' ', start);
					long millisecs = dateFormat.parse(line.substring(start, end))
											   .getTime();
					Date date = new Date(track.getDate().getTime() + millisecs);
	
					start = end + 1;
	
					boolean north = line.charAt(start) == 'N';
	
					start++;
					end = line.indexOf(':', start);
	
					int wD = Integer.parseInt(line.substring(start, end));
	
					start = end + 1;
					end = line.indexOf('.', start);
	
					int wM = Integer.parseInt(line.substring(start, end));
	
					start = end + 1;
					end = line.indexOf(' ', start);
	
					String tmp = line.substring(start, end);
					double wS = (Double.parseDouble(tmp) * 60) / Math.pow(10, 
																		  tmp.length());
	
					start = end + 1;
	
					boolean east = line.charAt(start) == 'E';
	
					start += 2;
					end = line.indexOf(':', start);
	
					int lD = Integer.parseInt(line.substring(start, end));
	
					start = end + 1;
					end = line.indexOf('.', start);
	
					int lM = Integer.parseInt(line.substring(start, end));
	
					start = end + 1;
					end = line.indexOf(' ', start);
	
					if (end == -1) {
						end = line.length();
					}
	
					tmp = line.substring(start, end);
	
					double lS = (Double.parseDouble(tmp) * 60) / Math.pow(10, 
																		  tmp.length());
	
					Point4D p = new Point4D(wD, wM, wS, north, lD, lM, lS, east);
					p.setDate(date);
					actualResult.replaceTaskPoint(p, count++);
				}
			} else {
				parseStd = false;
			}
		}

		if (bestResult.getScore() < actualResult.getScore()) {
			bestResult = (OlcTask2002) actualResult.clone();
		}

		System.out.println(bestResult.getDistance());
		System.out.println(bestResult.getScore());

		Point4D[] array = bestResult.getPoints();

		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i].getDumpString());
		}

		return bestResult;
	}
}