/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.util.BitSet;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.gliderpilot.gui.CancelApplyPanel;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class BitPreference extends AbstractPreference {
	BitSet bitSet;
	BitPrefPanel bitPrefPanel;
	String[] indexNames;

	/**
	 * Constructor for BitPreference.
	 * 
	 * @param prefs DOCUMENT ME!
	 * @param key DOCUMENT ME!
	 */
	public BitPreference(Prefs prefs, String key, int def, String[] indexNames) {
		super(prefs, key);
		setIndexNames(indexNames);
		
		bitSet = new BitSet();

		int value = getInt(prefs.get(key, ""+def));
		setValue(value);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String[] getIndexNames() {
		return indexNames;
	}

	public void setIndexNames(String[] indexNames) {
		this.indexNames = indexNames;
	}


	public void setValue(String value) {
		setValue(Integer.parseInt(value));
	}
	
	public void setValue(int value) {
		for (int i = 0; i < indexNames.length; i++) {
			bitSet.set(i, ((value >> i) & 1) != 0);
		}

		if (bitPrefPanel != null) {
			bitPrefPanel.preferenceChange(null);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int intValue() {
		int ret = 0;

		for (int i = indexNames.length - 1; i >= 0; i--) {
			ret = (ret << 1) | (bitSet.get(i) ? 1 : 0);
		}

		return ret;
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

	/**
	 * Returns the key.
	 * 
	 * @return String
	 */
	public String getKey() {
		return key;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param bitIndex DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getName(int bitIndex) {
		return indexNames[bitIndex];
	}

	/**
	 * @see Pref#getString()
	 */
	public String stringValue() {
		return "" + intValue();
	}


	/**
	 * DOCUMENT ME!
	 *
	 * @param def DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected int getInt(String def) {
		return BitPreference.toInt(def);
	}

	static int toInt(String def) {
		return Integer.parseInt(def);
	}

	class BitPrefPanel extends CancelApplyPanel implements ChangeListener,
														   Prefs.Listener {
		boolean changed;

		public BitPrefPanel() {
			for (int i = 0; i < indexNames.length; i++) {
				String indexName = indexNames[i];
				JCheckBox checkBox = new JCheckBox(indexName, bitSet.get(i));
				checkBox.setActionCommand("" + i);
				checkBox.addChangeListener(this);
				add(checkBox);
			}
		}

		public void apply() {
			super.apply();

			if (changed) {
				changed = false;
				for (int i = 0; i < indexNames.length; i++) {
					JCheckBox checkBox = (JCheckBox) getComponent(i);
					bitSet.set(i, checkBox.isSelected());
				}
				save();
			}
		}

		public void cancel() {
			super.cancel();

			if (changed) {
				changed = false;
				set(bitSet);
			}
		}
		
		void set(BitSet set) {
			for (int i = 0; i < indexNames.length; i++) {
				JCheckBox checkBox = (JCheckBox) getComponent(i);
				checkBox.setSelected(set.get(i));
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