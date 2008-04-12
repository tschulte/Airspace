/**
 * Created on Oct 14, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.preferences;

/**
 * @author Tobias Schulte
 *
 */
public abstract class AbstractPreference implements Pref {
	protected Prefs prefs;
	protected String key;
	
	public AbstractPreference(Prefs prefs, String key) {
		this.prefs = prefs;
		this.key = key;
	}
	
	public void save() {
		prefs.put(this);
	}
	
	/**
	 * @see de.gliderpilot.preferences.Pref#getKey()
	 */
	public String getKey() {
		return key;
	}

}
