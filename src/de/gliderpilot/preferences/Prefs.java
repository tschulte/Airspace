/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class Prefs implements PreferenceChangeListener {
	Hashtable values = new Hashtable();
	ArrayList listeners = new ArrayList();
	Preferences preferences;

	// not instanciable
	protected Prefs() {
		preferences = Preferences.userNodeForPackage(getClass());
		preferences.addPreferenceChangeListener(this);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public abstract String getName();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param listener DOCUMENT ME!
	 */
	public void addPreferenceChangeListener(Listener listener) {
		listeners.add(listener);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void flush() {
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param key DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Pref get(String key) {
		Pref value = (Pref) values.get(key);
		return value;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param evt DOCUMENT ME!
	 */
	public void preferenceChange(PreferenceChangeEvent evt) {
		PrefsChangeEvent e = new PrefsChangeEvent(this, get(evt.getKey()));

		for (int i = 0; i < listeners.size(); i++) {
			((Listener) listeners.get(i)).preferenceChange(e);
		}
	}
	
	protected void add(Pref pref) {
		values.put(pref.getKey(), pref);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param key DOCUMENT ME!
	 * @param value DOCUMENT ME!
	 */
	public void put(String key, String value) {
		preferences.put(key, value);
	}

	String get(String key, String def) {
		return preferences.get(key, def);
	}

	public interface Listener {
		public void preferenceChange(PrefsChangeEvent evt);
	}
}