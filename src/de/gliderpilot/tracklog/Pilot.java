/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.tracklog;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.gliderpilot.trace.Dumpable;
import de.gliderpilot.util.DumpString;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Pilot implements Dumpable {
	private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	Date birthDay;
	String club;
	String givenName;
	String name;

	/**
	 * Sets the birthDay.
	 * 
	 * @param birthDay The birthDay to set
	 */
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public void setDateOfBirth(String dateOfBirth) throws ParseException {
		birthDay = dateFormat.parse(dateOfBirth);
	}
	
	/**
	 * Returns the birthDay.
	 * 
	 * @return Date
	 */
	public Date getBirthDay() {
		return birthDay;
	}
	
	public String getDateOfBirth() {
		if (birthDay != null) {
			return dateFormat.format(birthDay);
		}
		return null;
	}

	/**
	 * Sets the club.
	 * 
	 * @param club The club to set
	 */
	public void setClub(String club) {
		this.club = club;
	}

	/**
	 * Returns the club.
	 * 
	 * @return String
	 */
	public String getClub() {
		return club;
	}

	/**
	 * Sets the givenName.
	 * 
	 * @param givenName The givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * Returns the givenName.
	 * 
	 * @return String
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
	/**
	 * @see de.gliderpilot.trace.Dumpable#getDumpString()
	 */
	public String getDumpString() {
		DumpString dump = new DumpString();
		dump.println("Name", name);
		dump.println("Given Name", givenName);
		dump.println("Date of birth", birthDay);
		dump.println("Club", club);
		return dump.toString();
	}

}