/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.util.BitSet;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.gliderpilot.gui.CancelApplyPanel;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class ExclusiveBitPreference extends BitPreference {
	BitPrefPanel bitPrefPanel;

	/**
	 * Constructor for BitPreference.
	 * 
	 * @param prefs DOCUMENT ME!
	 * @param key DOCUMENT ME!
	 */
	public ExclusiveBitPreference(Prefs prefs, String key, int def, String[] indexNames) {
		super(prefs, key, def, indexNames);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JPanel getJPanel() {
		if (bitPrefPanel == null) {
			bitPrefPanel = new BitPrefPanel();
		}

		return bitPrefPanel;
	}

	public void set(int bitIndex, boolean value) {
		if (value) {
			bitSet.clear();
		}

		bitSet.set(bitIndex, value);
	}

	class BitPrefPanel extends CancelApplyPanel implements ChangeListener,
														   Prefs.Listener {
		boolean changed;

		public BitPrefPanel() {
			ButtonGroup buttonGroup = new ButtonGroup();

			for (int i = 0; i < indexNames.length; i++) {
				String indexName = indexNames[i];
				JRadioButton radioButton = new JRadioButton(indexName, bitSet.get(i));
				radioButton.setActionCommand("" + i);
				radioButton.addChangeListener(this);
				add(radioButton);
				buttonGroup.add(radioButton);
			}
		}

		public void apply() {
			super.apply();

			if (changed) {
				changed = false;
				for (int i = 0; i < indexNames.length; i++) {
					JRadioButton radioButton = (JRadioButton) getComponent(i);
					ExclusiveBitPreference.this.set(i, radioButton.isSelected());
				}
				save();
			}
		}

		public void cancel() {
			super.cancel();
			set(bitSet);
			changed = false;

		}

		void set(BitSet set) {
			for (int i = 0; i < indexNames.length; i++) {
				JRadioButton radioButton = (JRadioButton) getComponent(i);
				radioButton.setSelected(set.get(i));
			}
		}

		/**
		 * @see Listener#preferenceChange(PrefsChangeEvent)
		 */
		public void preferenceChange(PrefsChangeEvent evt) {
			if (evt.getNewPref().getKey().equals(key)) {
				set(bitSet);
			}
		}

		public void stateChanged(ChangeEvent e) {
			changed = true;
		}
	}
}