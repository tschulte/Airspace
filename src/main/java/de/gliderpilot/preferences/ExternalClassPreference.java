/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;

import javax.swing.JOptionPane;

import de.gliderpilot.Gliderpilot;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class ExternalClassPreference extends StringPreference {
	ExternalClass externalClass;

	/**
	 * Creates a new ExternalClassPreference object.
	 * 
	 * @param prefs DOCUMENT ME!
	 * @param key DOCUMENT ME!
	 */
	public ExternalClassPreference(Prefs prefs, String key, String def) {
		super(prefs, key, def);

		String className = prefs.get(key, def);

		try {
			externalClass = (ExternalClass) Class.forName(className)
												 .newInstance();
		} catch (IllegalAccessException iae) {
		} catch (ClassNotFoundException cnfe) {
		} catch (InstantiationException ie) {
		} catch (ClassCastException cce) {
		}
	}

	/**
	 * Returns the externalClass.
	 * 
	 * @return ExternalClass
	 */
	public ExternalClass getExternalClass() {
		return externalClass;
	}

	/**
	 * @see de.gliderpilot.preferences.StringPreference#setValue(String)
	 */
	public void setValue(String value) {
		try {
			externalClass = (ExternalClass) Class.forName(value).newInstance();
			this.value = value;
		} catch (IllegalAccessException iae) {
			JOptionPane.showMessageDialog(Gliderpilot.getFrame(), "External Class "+value+" :IllegalAccess.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException cnfe) {
			JOptionPane.showMessageDialog(Gliderpilot.getFrame(), "External Class "+value+" not found.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (InstantiationException ie) {
			JOptionPane.showMessageDialog(Gliderpilot.getFrame(), "External Class "+value+" could not be instantiated.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (ClassCastException cce) {
			JOptionPane.showMessageDialog(Gliderpilot.getFrame(), "External Class "+value+" must implement ExternalClass.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}