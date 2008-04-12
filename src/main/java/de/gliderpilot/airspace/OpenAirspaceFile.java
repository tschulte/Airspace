/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.airspace;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import de.gliderpilot.geom.Point4D;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class OpenAirspaceFile implements AirspaceFile, AirspaceTypes {
//		32:50:25 N 112:49:00 W
//		39:29.9 N 119:46.1 W
//		36:42.0N 119:49.0 W
//		39:36.8 N 119:46.1W
//		25:46.625N 80:26.041W
//		25:38.52 S 027:11.16 E
//	private static Pattern pointPattern = Pattern.compile(
//												  "[\\p{Punct}\\p{Blank}]");
	private AirspaceContainer airspaceContainer;
	private BufferedReader asciiReader;
	private File file;
	private boolean eof = true;

	/**
	 * Creates a new OpenAirspaceFile object.
	 * 
	 * @param file DOCUMENT ME!
	 * @param airspaceContainer DOCUMENT ME!
	 */
	public OpenAirspaceFile(File file, AirspaceContainer airspaceContainer) {
		this.file = file;
		this.airspaceContainer = airspaceContainer;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean read() {
		boolean retval = false;

		try {
			asciiReader = new BufferedReader(
								  new InputStreamReader(
										  new FileInputStream(file)));

			if (asciiReader != null) {
				retval = parse();
				asciiReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();

			try {
				asciiReader.close();
			} catch (Exception ex) {
			}
		}

		return retval;
	}

	/**
	 * parses the given String and returns a Point3D
	 * 
	 * @param substring DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	private Point4D getPoint(String substring) {
//		32:50:25 N 112:49:00 W
//		39:29.9 N 119:46.1 W
//		36:42.0N 119:49.0 W
//		39:36.8 N 119:46.1W
//		25:46.625N 80:26.041W
//		25:38.52 S 027:11.16 E
		substring = substring.trim();

		Logger.getLogger(getClass()).debug(substring);
		
		StringTokenizer tokenizer = new StringTokenizer(substring, ".:\t ");

		if (tokenizer.countTokens() < 8) {
			Logger.getLogger(getClass()).debug("Less than 8 tokens");
			return null;
		}
		String[] data = new String[8];
		for (int i = 0; i < 8; i++) {
			data[i] = tokenizer.nextToken();
		}

		int wD;
		int wM;
		int wS;
		int lD;
		int lM;
		int lS;
		boolean north = true;
		boolean east = true;
		boolean seconds = substring.indexOf('.') < 0;

		wD = Integer.parseInt(data[0]);
		wM = Integer.parseInt(data[1]);

		if (seconds) {
			wS = Integer.parseInt(data[2]);
			lS = Integer.parseInt(data[6]);
		} else {
			wS = (int) (Double.parseDouble(data[2]) / Math.pow(10d, 
															   data[2].length()) * 60);
			lS = (int) (Double.parseDouble(data[6]) / Math.pow(10d, 
															   data[6].length()) * 60);
		}

		north = data[3].startsWith("N");
		lD = Integer.parseInt(data[4]);
		lM = Integer.parseInt(data[5]);
		east = data[7].startsWith("E");

		Point4D point = new Point4D(wD, wM, wS, north, lD, lM, lS, east);

		return point;

//		boolean seconds;
//		substring = substring.trim();
//		int index1 = 0;
//		int index2 = substring.indexOf(':');
//		wD = Integer.parseInt(substring.substring(index1, index2));
//
//		index1 = index2 + 1;
//		index2 = index1 + 2;
//		wM = Integer.parseInt(substring.substring(index1, index2));
//
//		seconds = substring.charAt(index2) == ':';
//
//		if (seconds) {
//			index1 = index2 + 1;
//		}
//		index2 = substring.indexOf('N');
//		if (index2 < 0) {
//			index2 = substring.indexOf('S');
//			north = false;
//		}
//		int indexBlank = substring.indexOf(' ', index1);
//		if (indexBlank > 0 && indexBlank < index2) {
//			int tmp = index2;
//			index2 = indexBlank;
//			indexBlank = tmp;
//		} else {
//			indexBlank = index2;
//		}
//		if (seconds) {
//			wS = Integer.parseInt(substring.substring(index1, index2));
//		} else {
//			wS = (int) (Double.parseDouble(substring.substring(index1, index2)) * 60);
//		}
//		index1 = substring.indexOf(' ', indexBlank) + 1; //indexBlank + 1;
//		if (index1 <= 0) index1 = indexBlank+2;
//		index2 = substring.indexOf(':', index1);
//		lD = Integer.parseInt(substring.substring(index1, index2));
//
//		index1 = index2 + 1;
//		index2 = index1 + 2;
////		index2 = substring.indexOf(':', index1);
//		lM = Integer.parseInt(substring.substring(index1, index2));
//
//		seconds = substring.charAt(index2) == ':';
//
//		if (seconds) {
//			index1 = index2 + 1;
//		}
//		index2 = substring.indexOf('E');
//		if (index2 < 0) {
//			index2 = substring.indexOf('W');
//			east = false;
//		}
//		indexBlank = substring.indexOf(' ', index1);
//		if (indexBlank > 0 && indexBlank < index2) {
//			int tmp = index2;
//			index2 = indexBlank;
//			indexBlank = tmp;
//		} else {
//			indexBlank = index2;
//		}
//		if (seconds) {
//			lS = Integer.parseInt(substring.substring(index1, index2));
//		} else {
//			lS = (int) (Double.parseDouble(substring.substring(index1, index2)) * 60);
//		}
//
//		
//		Point3D point = new Point3D(wD, wM, wS, north, lD, lM, lS, east);
//		return point;
	}

	private int getType(String line) {
		String subline = line.substring(3);

		if (subline.startsWith("CTR")) {
			return CTR;
		} else if (subline.startsWith("R")) {
			return RESTRICTED;
		} else if (subline.startsWith("Q")) {
			return DANGER;
		} else if (subline.startsWith("P")) {
			return PROHIBITED;
		} else if (subline.startsWith("A")) {
			return ALPHA;
		} else if (subline.startsWith("B")) {
			return BRAVO;
		} else if (subline.startsWith("C")) {
			return CHARLY;
		} else if (subline.startsWith("D")) {
			return DELTA;
		} else if (subline.startsWith("GP")) {
			return GLIDER_PROHIBITED;
		} else if (subline.startsWith("W")) {
			return WAVE;
		} else {
			return 0;
		}
	}

	private boolean parse() throws IOException {
		Logger.getLogger(getClass()).debug("start parsing");

		String line;
		AirspaceElement airspace = null;
		Point4D center = null;
		boolean clockwise = true;

		try {
			while ((line = asciiReader.readLine()) != null) {
				if (line.startsWith("AC")) {
					if (airspace != null) {
						airspace.close();

						airspaceContainer.addElement(airspace);
					}

					airspace = new AirspaceElement();
					clockwise = true;
					airspace.setType(getType(line));
				} else if (line.startsWith("AN")) { // name
					airspace.setName(line.substring(3));
				} else if (line.startsWith("AH")) { // ceiling
				} else if (line.startsWith("AL")) { // floor
				} else if (line.startsWith("DP")) { // polygon point
					airspace.addPoint(getPoint(line.substring(3)));
				} else if (line.startsWith("V X=")) { // definition of center
					center = getPoint(line.substring(4));
				} else if (line.startsWith("DC")) { // circle

					// we have the radius in NM, but we need it in km
					airspace.addCircle(center, 
									   Double.parseDouble(
											   (line.substring(3)).trim()) * 1.852);
				} else if (line.startsWith("V D=")) { // direction
					clockwise = line.substring(4).startsWith("+");
				} else if (line.startsWith("DB")) { // curve

					Point4D p1 = getPoint(line.substring(3));
					Point4D p2 = getPoint(line.substring(line.indexOf(',') + 1));
					airspace.addArc(center, p1, p2, clockwise);
				}
			}

			if (airspace != null) {
				airspace.close();
				airspaceContainer.addElement(airspace);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();

			return false;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}
}