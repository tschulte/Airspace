/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class LookAndFeelPreference extends ExclusiveBitPreference {

	/**
	 * Creates a new LookAndFeelPreference object.
	 * 
	 * @param prefs DOCUMENT ME!
	 * @param key DOCUMENT ME!
	 */
	public LookAndFeelPreference(Prefs prefs, String key, String def) {
		super(prefs, key, toInt(def), new String[]{"Windows", "Metal", "Motif"});
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JPanel getJPanel() {
		if (bitPrefPanel == null) {
			bitPrefPanel = new LookAndFeelPrefPanel();
		}

		return bitPrefPanel;
	}


	/**
	 * @see de.gliderpilot.preferences.Pref#getString()
	 */
	public String stringValue() {
		int i = intValue();

		if (i == 1) {
			return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		}

		if (i == 2) {
			return "javax.swing.plaf.metal.MetalLookAndFeel";
		}

		if (i == 4) {
			return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		}

		return "";
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param def DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected int getInt(String def) {
		return LookAndFeelPreference.toInt(def);
	}

	static int toInt(String def) {
		if (def.indexOf("Windows") > 0) {
			return 1;
		}

		if (def.indexOf("Metal") > 0) {
			return 2;
		}

		if (def.indexOf("Motif") > 0) {
			return 4;
		}

		return 0;
	}

	class LookAndFeelPrefPanel extends ExclusiveBitPreference.BitPrefPanel {
		public void apply() {
			boolean hasChanged = changed;
			super.apply();
			if (hasChanged) {
				try {
					UIManager.setLookAndFeel(stringValue());
				} catch (ClassNotFoundException e) {
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				} catch (UnsupportedLookAndFeelException e) {
				}
			}

		}
	}

}