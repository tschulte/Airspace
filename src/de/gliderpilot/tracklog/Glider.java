/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.tracklog;

import de.gliderpilot.trace.Dumpable;
import de.gliderpilot.util.DumpString;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Glider implements Dumpable {
	String callSign;
	String clazz;
	String competitionSign;
	String type;
	int index = 140;
	boolean isMotorGlider;

	/**
	 * Creates a new Glider object.
	 */
	public Glider() {
	}

	/**
	 * Sets the callSign.
	 * 
	 * @param callSign The callSign to set
	 */
	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	/**
	 * Returns the callSign.
	 * 
	 * @return String
	 */
	public String getCallSign() {
		return callSign;
	}

	/**
	 * Sets the clazz.
	 * 
	 * @param clazz The clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**
	 * Returns the clazz.
	 * 
	 * @return String
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * Sets the competitionSign.
	 * 
	 * @param competitionSign The competitionSign to set
	 */
	public void setCompetitionSign(String competitionSign) {
		this.competitionSign = competitionSign;
	}

	/**
	 * Returns the competitionSign.
	 * 
	 * @return String
	 */
	public String getCompetitionSign() {
		return competitionSign;
	}

	/**
	 * Sets the index.
	 * 
	 * @param index The index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setIndex(String index) {
		this.index = Integer.parseInt(index);
	}

	/**
	 * Returns the index.
	 * 
	 * @return int
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type The type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the type.
	 * 
	 * @return String
	 */
	public String getType() {
		return type;
	}
	
	public String getDumpString() {
		DumpString dump = new DumpString();
		dump.println("Call Sign", callSign);
		dump.println("Competition Sign", competitionSign);
		dump.println("Type", type);
		dump.println("index", index);
		return dump.toString();
	}
	/**
	 * Method isMotorGlider.
	 * @return boolean
	 */
	public boolean isMotorGlider() {
		return isMotorGlider;
	}
	
	public void isMotorGlider(boolean value) {
		isMotorGlider = value;
	}

	public void isMotorGlider(Boolean value) {
		isMotorGlider = value.booleanValue();
	}
}