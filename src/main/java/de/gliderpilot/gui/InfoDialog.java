/**
 * Created on Nov 7, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Tobias Schulte
 *
 */
public class InfoDialog extends JDialog implements Applyable {
	JTextArea infoArea;
	
	public InfoDialog(Frame owner, String info) {
		super(owner);
		getContentPane().setLayout(new BorderLayout());
		infoArea = new JTextArea(info);
		JScrollPane scrollPane = new JScrollPane(infoArea);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(new ApplyButton(this));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}
	
	public void setInfo(String info) {
		infoArea.setText(info);
	}

	/**
	 * @see de.gliderpilot.gui.Applyable#apply()
	 */
	public void apply() {
		setVisible(false);
		dispose();
	}

}
