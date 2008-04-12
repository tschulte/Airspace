/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.util.EventObject;


/**
 * DOCUMENT ME!
 * 
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class PrefsChangeEvent extends EventObject {
	private Pref pref;
	
	/**
	 * Constructs a new <code>PreferenceChangeEvent</code> instance.
	 * 
	 * @param node  The Preferences node that emitted the event.
	 * @param key  The key of the preference that was changed.
	 * @param newValue  The new value of the preference, or <tt>null</tt> if
	 *        the preference is being removed.
	 */
	public PrefsChangeEvent(Prefs node, Pref pref) {
		super(node);
		this.pref = pref;
	}

	public Pref getNewPref() {
		return pref;
	}

	/**
	 * Returns the preference node that emitted the event.
	 * 
	 * @return The preference node that emitted the event.
	 */
	public Prefs getNode() {
		return (Prefs) getSource();
	}
}