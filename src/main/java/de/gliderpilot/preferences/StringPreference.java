/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import de.gliderpilot.gui.CancelApplyPanel;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class StringPreference extends AbstractPreference {
	String value;
	StringPrefPanel prefPanel;


	/**
	 * Creates a new StringPreference object.
	 * 
	 * @param prefs DOCUMENT ME!
	 * @param key DOCUMENT ME!
	 */
	public StringPreference(Prefs prefs, String key, String def) {
		super(prefs, key);

		value = prefs.get(key, def);
	}

	/**
	 * @see Pref#getJPanel()
	 */
	public JPanel getJPanel() {
		if (prefPanel == null) {
			prefPanel = new StringPrefPanel();
		}

		return prefPanel;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @see Pref#getString()
	 */
	public String stringValue() {
		return value;
	}


	class StringPrefPanel extends CancelApplyPanel implements Prefs.Listener {
		JTextField textField;

		public StringPrefPanel() {
			setLayout(new GridLayout(1, 1));
			textField = new JTextField(value);

			add(textField);
		}

		public void apply() {
			super.apply();

			String value = textField.getText();

			if (!stringValue().equals(value)) {
				setValue(value);
				save();
			}
		}

		public void cancel() {
			super.cancel();

			if (!value.equals(textField.getText())) {
				textField.setText(value);
			}
		}

		/**
		 * @see Listener#preferenceChange(PrefsChangeEvent)
		 */
		public void preferenceChange(PrefsChangeEvent evt) {
			if (evt.getNewPref().getKey().equals(key)) {
				textField.setText(value);
			}
		}
	}
}