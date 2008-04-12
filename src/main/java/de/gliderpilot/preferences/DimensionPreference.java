/**
 * Created on Oct 4, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.preferences;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JTextField;

import de.gliderpilot.gui.CancelApplyPanel;

/**
 * @author Tobias Schulte
 *
 */
public class DimensionPreference extends AbstractPreference {
	DimensionPrefPanel dimensionPrefPanel;
	Dimension dimension = new Dimension();

	public DimensionPreference(Prefs prefs, String key, Dimension def) {
		super(prefs, key);
		this.key = key;

		setDimension(def);
		String defString = stringValue();
		setValue(prefs.get(key, defString));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JPanel getJPanel() {
		if (dimensionPrefPanel == null) {
			dimensionPrefPanel = new DimensionPrefPanel();
		}

		return dimensionPrefPanel;
	}

	/**
	 * Returns the key.
	 * 
	 * @return String
	 */
	public String getKey() {
		return key;
	}
	
	public void setValue(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, "x");
		if (tokenizer.countTokens() == 2) {
			double width = Double.parseDouble(tokenizer.nextToken());
			double height = Double.parseDouble(tokenizer.nextToken());
			dimension.setSize(width, height);
		}
	}

	/**
	 * @see Pref#getString()
	 */
	public String stringValue() {
		return ""+dimension.getWidth()+"x"+dimension.getHeight();
	}
	
	public Dimension getDimension() {
		return dimension;
	}
		
	public void setDimension(Dimension dimension) {
		this.dimension.setSize((int) dimension.getWidth(), (int) dimension.getHeight());
	}

	class DimensionPrefPanel extends CancelApplyPanel implements Prefs.Listener {
		JTextField textField;

		public DimensionPrefPanel() {
			setLayout(new GridLayout(1, 1));
			textField = new JTextField(stringValue());

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
			String value = stringValue();
			if (!value.equals(textField.getText())) {
				textField.setText(value);
			}
		}

		/**
		 * @see Listener#preferenceChange(PrefsChangeEvent)
		 */
		public void preferenceChange(PrefsChangeEvent evt) {
			if (evt.getNewPref().getKey().equals(key)) {
				textField.setText(stringValue());
			}
		}
	}
}