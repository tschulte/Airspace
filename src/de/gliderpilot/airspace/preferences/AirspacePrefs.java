/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.airspace.preferences;


import de.gliderpilot.geom.Modes4D;
import de.gliderpilot.preferences.BitPreference;
import de.gliderpilot.preferences.ExclusiveBitPreference;
import de.gliderpilot.preferences.ExternalClassPreference;
import de.gliderpilot.preferences.FilePreference;
import de.gliderpilot.preferences.LookAndFeelPreference;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.preferences.StringPreference;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class AirspacePrefs extends Prefs {
	
	/** DOCUMENT ME! */
	public static LookAndFeelPreference LOOK_AND_FEEL;

	/** DOCUMENT ME! */
	public static FilePreference MAP_FILE;

	/** DOCUMENT ME! */
	public static BitPreference BARO_MODE;

	/** DOCUMENT ME! */
	public static FilePreference CURRENT_PATH;

	/** DOCUMENT ME! */
	public static StringPreference NUM_XSCALE;

	/** DOCUMENT ME! */
	public static StringPreference NUM_YSCALE;

	/** DOCUMENT ME! */
	public static ExclusiveBitPreference OPTI_METHOD;

	/** DOCUMENT ME! */
	public static ExternalClassPreference EXT_OPTI_CLASS;

	private static Prefs prefs = new AirspacePrefs();

	private AirspacePrefs() {
		LOOK_AND_FEEL = new LookAndFeelPreference(this, "LookAndFeel", "Metal");
		add(LOOK_AND_FEEL);
		MAP_FILE = new FilePreference(this, "MapFile", "map/Luesse.txt");
		add(MAP_FILE);
		BARO_MODE = new BitPreference(this, "BaroMode", Modes4D.PRESSURE | Modes4D.GPS, new String[] {"Pressure","GPS"});
		add(BARO_MODE);
		CURRENT_PATH = new FilePreference(this, "CurrentPath", ".");
		add(CURRENT_PATH);
		NUM_XSCALE = new StringPreference(this, "NumXScale", "10");
		add(NUM_XSCALE);
		NUM_YSCALE = new StringPreference(this, "NumYScale", "10");
		add(NUM_YSCALE);
		OPTI_METHOD = new ExclusiveBitPreference(this, "OptiMethod", 1, new String[] {"Internal", "External"});
		add(OPTI_METHOD);
		EXT_OPTI_CLASS = new ExternalClassPreference(this, "OptiClass", "");
		add(EXT_OPTI_CLASS);
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
		return "Airspace";
	}
}