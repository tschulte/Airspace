/**
 * Created on Oct 27, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.tracklog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.gliderpilot.Gliderpilot;
import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.gui.ApplyButton;
import de.gliderpilot.gui.Applyable;
import de.gliderpilot.gui.CancelButton;
import de.gliderpilot.gui.Cancelable;
import de.gliderpilot.task.OlcFileWriter;
import de.gliderpilot.task.OlcTask;

/**
 * @author Tobias Schulte
 *
 */
public class CreateOlcFileDialog extends JDialog implements Applyable, Cancelable {
	OlcTask task;
	TrackDataPanel trackDataPanel;
	File olcFile;
	
	public CreateOlcFileDialog(Frame owner, OlcTask task) {
		super(owner);
		getContentPane().setLayout(new BorderLayout());
		this.task = task;
		trackDataPanel = new TrackDataPanel(task.getTrackLog());
		JPanel panel = new JPanel();
		final JTextField textField = new JTextField();
		final JFileChooser fileChooser = new JFileChooser(AirspacePrefs.CURRENT_PATH.stringValue());
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int retVal = fileChooser.showOpenDialog(CreateOlcFileDialog.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					olcFile = fileChooser.getSelectedFile();
					textField.setText(olcFile.getAbsolutePath());
				}
			}
		});
		panel.add(textField);
		panel.add(button);
		trackDataPanel.add("Olc file", panel);
		getContentPane().add(trackDataPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(new ApplyButton(this));
		buttonPanel.add(new CancelButton(this));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
		setModal(true);
		setVisible(true);
	}

	/**
	 * @see de.gliderpilot.gui.Cancelable#cancel()
	 */
	public void cancel() {
		trackDataPanel.cancel();
		setVisible(false);
	}

	/**
	 * @see de.gliderpilot.gui.Cancelable#isCanceled()
	 */
	public boolean isCanceled() {
		return false;
	}

	/**
	 * @see de.gliderpilot.gui.Applyable#apply()
	 */
	public void apply() {
		try {
			trackDataPanel.apply();
			if (olcFile != null) {
				try {
					new OlcFileWriter(olcFile, task, true);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				setVisible(false);
			} else {
				JOptionPane.showMessageDialog(Gliderpilot.getFrame(), "Please set OLC file", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IllegalArgumentException iae) {
			JOptionPane.showMessageDialog(Gliderpilot.getFrame(), "Date could not be parsed: "+iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
