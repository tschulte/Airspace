/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.preferences;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.gui.ApplyAndCloseable;
import de.gliderpilot.gui.ApplyButton;
import de.gliderpilot.gui.Applyable;
import de.gliderpilot.gui.CancelButton;
import de.gliderpilot.gui.Cancelable;
import de.gliderpilot.gui.OkButton;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class PrefsDialog extends JDialog implements Applyable, ApplyAndCloseable,
													Cancelable {
	Hashtable prefPanels = new Hashtable();
	JTabbedPane tabbedPane;

	/**
	 * Constructor for PreferencesDialog.
	 * 
	 * @param owner
	 * @param prefs
	 * 
	 * @throws HeadlessException
	 */
	public PrefsDialog(Frame owner, Prefs prefs) throws HeadlessException {
		super(owner, "Preferences", true);

		getContentPane().setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		add(prefs);
		getContentPane().add(tabbedPane, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(new OkButton(this));
		buttonPanel.add(new ApplyButton(this));
		buttonPanel.add(new CancelButton(this));

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		pack();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		AirspacePrefs.getReference().addPreferenceChangeListener(new Prefs.Listener() {
			public void preferenceChange(PrefsChangeEvent evt) {
				if (evt.getNewPref() == AirspacePrefs.LOOK_AND_FEEL) {
					SwingUtilities.updateComponentTreeUI(PrefsDialog.this);
					pack();
				}
			}
		});
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isCanceled() {
		return false;
	}

//	public void remove(Prefs prefs) {
//	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param prefs DOCUMENT ME!
	 */
	public void add(Prefs prefs) {
		if (prefPanels.containsKey(prefs.getName())) {
			return;
		}

		PrefsPanel prefPanel = new PrefsPanel(prefs);
		tabbedPane.add(prefPanel);

		prefPanels.put(prefs.getName(), prefPanel);

		Iterator it = prefs.values.keySet().iterator();

		while (it.hasNext()) {
			String key = (String) it.next();
			Pref pref = prefs.get(key);

			if (pref instanceof ExternalClassPreference) {
				ExternalClassPreference ecp = (ExternalClassPreference) pref;
				ExternalClass externalClass = ecp.getExternalClass();

				if (externalClass != null) {
					Prefs externalPrefs = externalClass.getPrefs();
					if (externalPrefs != null) {
						add(externalPrefs);
					}
				}
			}
		}

		prefs.addPreferenceChangeListener(new Prefs.Listener() {
			public void preferenceChange(PrefsChangeEvent evt) {
				Prefs prefs = evt.getNode();
				Pref pref = evt.getNewPref();

				if (pref instanceof ExternalClassPreference) {
					ExternalClassPreference ecp = (ExternalClassPreference) pref;
					ExternalClass externalClass = ecp.getExternalClass();

					if (externalClass != null) {
						Prefs externalPrefs = externalClass.getPrefs();
						if (externalPrefs != null) {
							add(externalPrefs);
						}
					}
				}
			}
		});
	}

	/**
	 * DOCUMENT ME!
	 */
	public void apply() {
		Iterator it = prefPanels.values().iterator();

		while (it.hasNext()) {
			((Applyable) it.next()).apply();
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	public void applyAndClose() {
		apply();
		setVisible(false);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void cancel() {
		Iterator it = prefPanels.values().iterator();

		while (it.hasNext()) {
			((Cancelable) it.next()).cancel();
		}

		setVisible(false);
	}

	class PrefsPanel extends JPanel implements Applyable, Cancelable {
		JPanel valuePanel;
		Prefs prefs;
		Set keys;

		public PrefsPanel(Prefs prefs) {
			setName(prefs.getName());
			this.prefs = prefs;
			keys = prefs.values.keySet();


			//getContentPane().
			setLayout(new BorderLayout());

			JPanel labelPanel = new JPanel(new GridLayout(keys.size(), 1));
			valuePanel = new JPanel(new GridLayout(keys.size(), 1));

			Iterator keyIterator = keys.iterator();

			while (keyIterator.hasNext()) {
				String key = (String) keyIterator.next();
				labelPanel.add(new JLabel(key));

				Pref value = prefs.get(key);
				valuePanel.add(value.getJPanel());
			}

			add(labelPanel, BorderLayout.WEST);
			add(valuePanel, BorderLayout.EAST);
		}

		public boolean isCanceled() {
			return false;
		}

		public void apply() {
			for (int i = 0; i < valuePanel.getComponentCount(); i++) {
				Component comp = valuePanel.getComponent(i);
				((Applyable) comp).apply();
			}

			prefs.flush();
		}

		public void cancel() {
			for (int i = 0; i < valuePanel.getComponentCount(); i++) {
				Component comp = valuePanel.getComponent(i);
				((Cancelable) comp).cancel();
			}
		}
	}
}