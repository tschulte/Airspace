/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.optigc;


import de.gliderpilot.preferences.FilePreference;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.preferences.StringPreference;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class OptigcPrefs extends Prefs {
	/** DOCUMENT ME! */
	public static FilePreference OPTIGC_APP;

	/** DOCUMENT ME! */
	public static StringPreference OPTIGC_MAX_SPEED;

	private static Prefs prefs = new OptigcPrefs();

	private OptigcPrefs() {
		OPTIGC_APP = new FilePreference(this, "OptigcApp", "");
		add(OPTIGC_APP);
		OPTIGC_MAX_SPEED = new StringPreference(this, "OptigcMaxSpeed", "600");		add(OPTIGC_MAX_SPEED);
		add(OPTIGC_MAX_SPEED);
	}

	/**
	 * @see Prefs#getReference()
	 */
	public static Prefs getReference() {
		return prefs;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getName() {
		return "Optigc";
	}
}