/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.util;


import java.io.PrintStream;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.trace.TraceLevels;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Util implements TraceLevels {
	private static PrintStream trace;
	private static int traceLevel = INFO;
	private static double kmOneDegree = new Point4D(1, 0, 0, true, 1, 0, 0, 
													true).getXDistanceFrom(
												new Point4D());

	/**
	 * DOCUMENT ME!
	 * 
	 * @param pixel DOCUMENT ME!
	 * @param km DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static double getFactor(int pixel, double km) {
		return (kmOneDegree * pixel) / km;
	}

	/**
	 * Get an integer from an array.
	 * 
	 * @param buf DOCUMENT ME!
	 * @param offset DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static int getInt(byte[] buf, int offset) {
		return ((buf[offset + 0] & 0xff) << 24) | 
			   ((buf[offset + 1] & 0xff) << 16) | 
			   ((buf[offset + 2] & 0xff) << 8) | (buf[offset + 3] & 0xff);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param pixel DOCUMENT ME!
	 * @param factor DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static double getRealValue(int pixel, double factor) {
		return (kmOneDegree * pixel) / factor;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param buf DOCUMENT ME!
	 * @param offset DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static short getShort(byte[] buf, int offset) {
		return (short) ((buf[offset + 0] & 0xff) << 8 | 
			   (buf[offset + 1] & 0xff));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param km DOCUMENT ME!
	 * @param factor DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public static int getSystemValue(double km, double factor) {
		return (int) (km * factor / kmOneDegree);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param traceLevel DOCUMENT ME!
	 */
	public static void setTraceLevel(int traceLevel) {
		Util.traceLevel = traceLevel;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param trace DOCUMENT ME!
	 */
	public static void setTraceStream(PrintStream trace) {
		Util.trace = trace;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param arguments DOCUMENT ME!
	 */
	public static void main(String[] arguments) {
		System.out.println(kmOneDegree);

//		System.out.println(getRealValue(200, 1));
	}

	/**
	 * Put an integer to an array
	 * 
	 * @param buf DOCUMENT ME!
	 * @param offset DOCUMENT ME!
	 * @param value DOCUMENT ME!
	 */
	public static void putInt(byte[] buf, int offset, int value) {
		buf[offset] = (byte) (value >> 24);
		buf[offset + 1] = (byte) (value >> 16);
		buf[offset + 2] = (byte) (value >> 8);
		buf[offset + 3] = (byte) (value >> 0);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param buf DOCUMENT ME!
	 * @param offset DOCUMENT ME!
	 * @param value DOCUMENT ME!
	 */
	public static void putShort(byte[] buf, int offset, short value) {
		buf[offset + 0] = (byte) (value >> 8);
		buf[offset + 1] = (byte) (value >> 0);
	}
}