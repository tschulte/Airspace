/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public abstract class Prefs {
	private final File PROPERTY_FILE = new File(getClass().getName());
	Properties properties = new Properties();
	Hashtable values = new Hashtable();
	ArrayList listeners = new ArrayList();

	// not instanciable
	protected Prefs() {
		try {
			properties.load(new FileInputStream(PROPERTY_FILE));
		} catch (Exception e) {
			Logger.getLogger(getClass()).warn(e);
		}
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
	 * TODO: flush to disk
	 */
	public void flush() {
		try {
			properties.store(new FileOutputStream(PROPERTY_FILE), "");
		} catch (Exception e) {
			Logger.getLogger(getClass()).warn(e);
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

	private void preferenceChange(Pref pref) {
		PrefsChangeEvent e = new PrefsChangeEvent(this, pref);

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
		put(get(key));
	}
	
	public void put(Pref pref) {
		properties.setProperty(pref.getKey(), pref.stringValue());
		preferenceChange(pref);
	}

	String get(String key, String def) {
		return properties.getProperty(key, def);
	}

	public interface Listener {
		public void preferenceChange(PrefsChangeEvent evt);
	}
}