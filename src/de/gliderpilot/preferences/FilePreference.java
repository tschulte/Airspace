/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.gui.CancelApplyPanel;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class FilePreference extends AbstractPreference {
	File file;
	FilePanel filePanel;

	/**
	 * Creates a new FilePreference object.
	 * 
	 * @param prefs DOCUMENT ME!
	 * @param key DOCUMENT ME!
	 */
	public FilePreference(Prefs prefs, String key, String def) {
		super(prefs, key);
		
		file = new File(prefs.get(key, def));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param file DOCUMENT ME!
	 */
	public void setValue(File file) {
		this.file = file;
	}

	/**
	 * @see Pref#getJPanel()
	 */
	public JPanel getJPanel() {
		if (filePanel == null) {
			filePanel = new FilePanel();
		}

		return filePanel;
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
	 * @see Pref#getString()
	 */
	public String stringValue() {
		return file.getPath();
	}
	
	public File fileValue() {
		return file;
	}

	public void setValue(String value) {
		setValue(new File(value));
	}

	class FilePanel extends CancelApplyPanel implements Prefs.Listener {
		JFileChooser fileChooser;
		JTextField textField;
		boolean changed;

		public FilePanel() {
			setLayout(new BorderLayout());
			textField = new JTextField(stringValue());
			textField.setEditable(false);

			JButton button = new JButton("...");
			add(textField, BorderLayout.WEST);
			add(button, BorderLayout.EAST);

			fileChooser = new JFileChooser(file);

			AirspacePrefs.getReference().addPreferenceChangeListener(new Prefs.Listener() {
				public void preferenceChange(PrefsChangeEvent evt) {
					if (evt.getNewPref() == AirspacePrefs.LOOK_AND_FEEL) {
						SwingUtilities.updateComponentTreeUI(fileChooser);
					}
				}
			});

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int returnVal = fileChooser.showOpenDialog(FilePanel.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						changed = true;
						file = fileChooser.getSelectedFile();
						textField.setText(file.getPath());
					}
				}
			});

			if (file == null && !file.exists()) {
				return;
			}
			if (file.isDirectory()) {
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			} else {
				fileChooser.setSelectedFile(file);
			}
		}

		/**
		 * @see Applyable#apply()
		 */
		public void apply() {
			super.apply();

			if (changed) {
				changed = false;
				setValue(fileChooser.getSelectedFile());
				save();
			}
		}

		/**
		 * @see Cancelable#cancel()
		 */
		public void cancel() {
			super.cancel();

			if (changed) {
				changed = false;
				textField.setText(stringValue());
				fileChooser.setSelectedFile(file);
			}
		}

		/**
		 * @see Listener#preferenceChange(PrefsChangeEvent)
		 */
		public void preferenceChange(PrefsChangeEvent evt) {
			if (evt.getNewPref().getKey().equals(key)) {
				textField.setText(stringValue());
				fileChooser.setCurrentDirectory(file);
			}
		}
	}
}