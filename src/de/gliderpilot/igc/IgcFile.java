/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.igc;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.task.OlcTask2003;
import de.gliderpilot.task.Task;
import de.gliderpilot.task.UnlimitedTask;
import de.gliderpilot.trace.TraceLevels;
import de.gliderpilot.tracklog.Glider;
import de.gliderpilot.tracklog.Pilot;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class IgcFile implements TraceLevels {
	private static SimpleDateFormat timeFormat;
	private static SimpleDateFormat dateFormat;

//	private Logger logger;
	static {
		timeFormat = new SimpleDateFormat("HHmmss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		dateFormat = new SimpleDateFormat("ddMMyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private BufferedReader asciiReader;
	private Date date;
	private TrackLog track;
	private int enlEnd;
	private int enlStart;
	
	private Task task;

	/**
	 * Creates a new IgcFile object.
	 * 
	 * @param file DOCUMENT ME!
	 * @param track DOCUMENT ME!
	 */
	public IgcFile(String file, TrackLog track) {
		this(new File(file), track);
	}

	/**
	 * Creates a new IgcFile object.
	 * 
	 * @param file DOCUMENT ME!
	 * @param track DOCUMENT ME!
	 */
	public IgcFile(File file, TrackLog track) {
		this.track = track;
		track.setIgcFileName(file.getPath());

		try {
			asciiReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
//			logger.warning(e.toString());
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean parse() {
		if (asciiReader == null) {
			return false;
		}

		String line;

		try {
			while ((line = asciiReader.readLine()) != null) {
				if (line.startsWith("B")) {
					parseBRecord(line);
				} else if (line.startsWith("H")) {
					parseHRecord(line);
				} else if (line.startsWith("I")) {
					parseIRecord(line);
				} else if (line.startsWith("C")) {
					parseCRecord(line);
				} else if (line.startsWith("AOLC")) {
					task = new OlcTask2003(track);
					track.setTask(task);
				}
			}

			return true;
		} catch (Exception e) {
//			logger.warning(e.toString());
			return false;
		}
	}
	
	private void parseCRecord(String line) throws ParseException {
		if (line.indexOf('N') < 0 && line.indexOf('S') < 0 
			|| line.indexOf('W') < 0 && line.indexOf('E') < 0) {
			return;
		}
		if (task == null) {
			task = new UnlimitedTask();
			track.setTask(task);
		}
		Point4D p = parsePoint(line, 1);
		p.setDate(new Date(task.size()*1000));
		task.addTaskPoint(p);
	}
	
	private Point4D parsePoint(String line, int index) throws ParseException {
		int wD = Integer.parseInt(line.substring(index, index+2));
		int wM = Integer.parseInt(line.substring(index+2, index+4));
		int wMD = Integer.parseInt(line.substring(index+4, index+7));
		double wS = (double) wMD * 60 / 1000;
		boolean north = line.charAt(index+7) == 'N';

		int lD = Integer.parseInt(line.substring(index+8, index+11));
		int lM = Integer.parseInt(line.substring(index+11, index+13));
		int lMD = Integer.parseInt(line.substring(index+13, index+16));
		double lS = (double) lMD * 60 / 1000;
		boolean east = line.charAt(index+16) == 'E';
		Point4D p = new Point4D(wD, wM, wS, north, lD, lM, lS, east);
		
		return p;		
	}

	private void parseBRecord(String line) throws ParseException {
		long millisecs = timeFormat.parse(line.substring(1, 7)).getTime();
		Date date = new Date(this.date.getTime() + millisecs);
		Point4D lastPoint = track.getLastPoint();
		if (lastPoint != null && lastPoint.getDate().after(date)) {
			Calendar cal = new GregorianCalendar(SimpleTimeZone.getTimeZone("UTC"));
			cal.setTime(this.date);
			cal.add(Calendar.DATE, 1);
			this.date = cal.getTime();
			date = new Date(this.date.getTime() + millisecs);
		}
		
		Point4D p = parsePoint(line, 7);

		int pressureAlt = Integer.parseInt(line.substring(25, 30));
		int gpsAlt = Integer.parseInt(line.substring(30, 35));

		p.setPressureHeight(pressureAlt);
		p.setGpsHeight(gpsAlt);
		p.setDate(date);

		if (enlStart > 0) {
			int enl = Integer.parseInt(line.substring(enlStart, enlEnd));
			p.setEnl(enl);
		}

		track.add(p);
	}

	private void parseHRecord(String line) throws ParseException {
		if (line.startsWith("HFDTE") || line.startsWith("HPDTE")) {
			int i = line.indexOf(":");
			if (i > 0) {
				date = dateFormat.parse(line.substring(i+1).trim());
			} else {
				date = dateFormat.parse(line.substring(5, 11));
			}
			track.setDate(date);
		} else if (line.startsWith("HFPLT") || line.startsWith("HPPLT")) {
			int i = line.indexOf(":") + 1;
			if (i > 0 && i < line.length()) {
				Pilot pilot = track.getPilot();
				if (pilot == null) {
					pilot = new Pilot();
					track.setPilot(pilot);
				}
				String name = line.substring(i, line.length()).trim();
				String givenName = null;
				int j = name.indexOf(',');
				if (j >= 0) {
					givenName = name.substring(j+1).trim();
					name = name.substring(0, j).trim();
				} else {
					j = name.lastIndexOf(' ');
					if (j > 0) {
						givenName = name.substring(0, j).trim();
						name = name.substring(j+1);
					}
				}
				pilot.setName(name);
				pilot.setGivenName(givenName);
			}
		} else if (line.startsWith("HFG") || line.startsWith("HFC") || line.startsWith("HP")) {
			parseGliderInfo(line);
		}
	}
	
	private void parseGliderInfo(String line) {
		Glider glider = track.getGlider();
		if (glider == null) {
			glider = new Glider();
			track.setGlider(glider);
		}
		if (line.startsWith("HFGID") || line.startsWith("HPGID")) {
			int i = line.indexOf(":") + 1;
			if (i > 0 && i < line.length()) {
				glider.setCallSign(line.substring(i, line.length()).trim());
			}
		} else if (line.startsWith("HFGTY") || line.startsWith("HPGTY")) {
			int i = line.indexOf(":") + 1;
			if (i > 0 && i < line.length()) {
				glider.setType(line.substring(i, line.length()).trim());
			}
		} else if (line.startsWith("HFCID") || line.startsWith("HPGID")) {
			int i = line.indexOf(":") + 1;
			if (i > 0 && i < line.length()) {
				glider.setCompetitionSign(line.substring(i, line.length()).trim());
			}
		} else if (line.startsWith("HFCCL")) {
			int i = line.indexOf(":") + 1;
			if (i > 0 && i < line.length()) {
				glider.setClazz(line.substring(i, line.length()).trim());
			}
		}
	}

	private void parseIRecord(String line) throws ParseException {
		int enlIndex = line.indexOf("ENL") - 4;

		if (enlIndex > 2) {
			enlStart = Integer.parseInt(line.substring(enlIndex, enlIndex + 2)) - 1;
			enlEnd = Integer.parseInt(line.substring(enlIndex + 2, enlIndex + 4));
		}
	}
}