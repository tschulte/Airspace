/**
 * Created on Oct 23, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.gliderpilot.trace.Dumpable;

/**
 * @author Tobias Schulte
 *
 */
public class DumpString extends PrintWriter {
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	int colonIndex = 20;
	
	public DumpString() {
		super(new StringWriter());
	}

	public DumpString(int initialCapacity) {
		super(new StringWriter(initialCapacity));
	}
	
	private void fill(String key) {
		print(key);
		for (int i = key.length(); i < colonIndex; i++) {
			print(" ");
		}
	}
	
	private void colon() {
		print(" : ");
	}

	public void println(String key, String value) {
		fill(key);
		colon();
		println(value);
	}

	public void println(String key, int value) {
		fill(key);
		colon();
		println(value);
	}

	public void println(String key, double value) {
		fill(key);
		colon();
		println(value);
	}

	public void println(String key, float value) {
		fill(key);
		colon();
		println(value);
	}

	public void println(String key, byte value) {
		fill(key);
		colon();
		println(value);
	}
	
	public void println(String key, Object value) {
		fill(key);
		colon();
		println(value);
	}

	public void println(String key, Date value) {
		fill(key);
		colon();
		if (value == null) {
			println("null");
		} else {
			println(dateFormat.format(value));
		}
	}
	
	public void println(Dumpable dumpable) {
		println(dumpable.getDumpString());
	}
	
	public String toString() {
		flush();
		return out.toString();
	}
}
