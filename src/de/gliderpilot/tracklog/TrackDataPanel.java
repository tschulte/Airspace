/**
 * Created on Oct 27, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.tracklog;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.gliderpilot.gui.CancelApplyPanel;
import de.gliderpilot.gui.GCheckBox;
import de.gliderpilot.gui.GTextField;
import de.gliderpilot.gui.Msg;

/**
 * @author Tobias Schulte
 *
 */
public class TrackDataPanel extends CancelApplyPanel {
	private TrackLog track;
	private JPanel keyPanel;
	private JPanel valuePanel;
	
	public TrackDataPanel(TrackLog track) {
		super(new BorderLayout());
		this.track = track;
		keyPanel = new JPanel(new GridLayout(0, 1));
		valuePanel = new CancelApplyPanel(new GridLayout(0, 1));
		
		Pilot pilot = track.getPilot();
		if (pilot == null) {
			pilot = new Pilot();
			track.setPilot(pilot);
		}

		keyPanel.add(new JLabel(Msg.get(Msg.NAME)));
		valuePanel.add(new GTextField(pilot.getName(), 30, pilot, "setName"));

		keyPanel.add(new JLabel(Msg.get(Msg.GIVEN_NAME)));
		valuePanel.add(new GTextField(pilot.getGivenName(), 30, pilot, "setGivenName"));

		keyPanel.add(new JLabel(Msg.get(Msg.BIRTHDAY)));
		valuePanel.add(new GTextField(pilot.getDateOfBirth(), 30, pilot, "setDateOfBirth"));

		keyPanel.add(new JLabel(Msg.get(Msg.AIRFIELD)));
		valuePanel.add(new GTextField(track.getAirField(), 30, track, "setAirField"));

		Glider glider = track.getGlider();
		if (glider == null) {
			glider = new Glider();
			track.setGlider(glider);
		}
		
		keyPanel.add(new JLabel(Msg.get(Msg.GLIDER_TYPE)));
		valuePanel.add(new GTextField(glider.getType(), 30, glider, "setType"));
		valuePanel.add(new GCheckBox(Msg.get(Msg.POWERED_GLIDER), glider.isMotorGlider(), glider, "isMotorGlider"));
		
		keyPanel.add(new JLabel(Msg.get(Msg.GLIDER_SIGN)));
		valuePanel.add(new GTextField(glider.getCallSign(), 30, glider, "setCallSign"));

		keyPanel.add(new JLabel(Msg.get(Msg.INDEX)));
		valuePanel.add(new GTextField(""+glider.getIndex(), 30, glider, "setIndex"));
		
		add(keyPanel, BorderLayout.WEST);
		add(valuePanel, BorderLayout.EAST);
	}
	
	public void add(String key, JComponent c) {
		keyPanel.add(new JLabel(key));
		valuePanel.add(c);
	}

}
