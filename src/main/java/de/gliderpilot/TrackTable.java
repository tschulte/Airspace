/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.Timer;

import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.gui.InfoDialog;
import de.gliderpilot.gui.Msg;
import de.gliderpilot.gui.ProgressDialog;
import de.gliderpilot.preferences.ExternalClass;
import de.gliderpilot.preferences.ExternalClassPreference;
import de.gliderpilot.task.OlcOptimizer;
import de.gliderpilot.task.OlcTask;
import de.gliderpilot.task.Optimizer;
import de.gliderpilot.tracklog.CreateOlcFileDialog;
import de.gliderpilot.tracklog.TrackLog;


/**
 * This table displays the list of currently loaded Tracks.
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class TrackTable extends JTable {
	TrackLogTableModel model = TrackLogTableModel.getInstance();
	TrackTableContextMenu contextMenu;

	/**
	 * Creates a new TrackTable object.
	 */
	public TrackTable() {
		super(TrackLogTableModel.getInstance());
		setName(Msg.get(Msg.TRACKS));
		contextMenu = new TrackTableContextMenu();

//		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseListener());
	}
	
	private void optimize() {
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return;
		}
		TrackLog track = model.getTrackLog(selectedRow);
		Optimizer olcOptimizer = null;
		int optiMethod = Integer.parseInt(AirspacePrefs.OPTI_METHOD.stringValue());

		if (optiMethod == 1) {
			olcOptimizer = new OlcOptimizer(track);
		} else {
			ExternalClassPreference exPref = AirspacePrefs.EXT_OPTI_CLASS;
			ExternalClass externalClass = exPref.getExternalClass();
			if (externalClass == null || !(externalClass instanceof Optimizer)) return;
			olcOptimizer = (Optimizer) externalClass;
			olcOptimizer.setTrack(track);
		}

		ProgressDialog progressDialog = new ProgressDialog(Gliderpilot.getFrame(), 
														   olcOptimizer, 
														   olcOptimizer.getMin(), 
														   olcOptimizer.getMax());

		ActionListener listener = new OptimizerTimerActionListener(
										  olcOptimizer, progressDialog);
		Timer timer = new Timer(100, listener);
		timer.start();
		new Thread(olcOptimizer).start();
		progressDialog.setModal(true);
		progressDialog.setVisible(true);
	}

	class TrackTableContextMenu extends JPopupMenu { 
		public TrackTableContextMenu() {
			JMenuItem item = new JMenuItem(Msg.get(Msg.REMOVE));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = getSelectedRow();
					if (selectedRow >= 0) {
						TrackLogTableModel.getInstance().removeTrackLog(selectedRow);
					}
				}
			});
			add(item);
			item = new JMenuItem(Msg.get(Msg.OPTIMIZE));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					optimize();
				}
			});
			add(item);
			item = new JMenuItem(Msg.get(Msg.OLC_FILE));
			item.addActionListener(new ActionListener() {
				CreateOlcFileDialog olcDialog;
				public void actionPerformed(ActionEvent e) {
					int selectedRow = getSelectedRow();
					if (selectedRow >= 0) {
						TrackLog track = model.getTrackLog(selectedRow);
						OlcTask task = (OlcTask) track.getBestTask(OlcTask.class);
						if (task != null) {
							olcDialog = new CreateOlcFileDialog(Gliderpilot.getFrame(), task);
						}
					}
				}
			});
			add(item);
			item = new JMenuItem(Msg.get(Msg.OLC_TASK_INFO));
			item.addActionListener(new ActionListener() {
				InfoDialog infoDialog;
				public void actionPerformed(ActionEvent e) {
					int selectedRow = getSelectedRow();
					if (selectedRow >= 0) {
						TrackLog track = model.getTrackLog(selectedRow);
						OlcTask task = (OlcTask) track.getBestTask(OlcTask.class);
						if (task != null) {
							if (infoDialog == null) {
								infoDialog = new InfoDialog(Gliderpilot.getFrame(), task.getDumpString());
							} else {
								infoDialog.setInfo(task.getDumpString());
								infoDialog.setVisible(true);
							}
						}
					}
				}
			});
			add(item);
		}
	}

	class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.isPopupTrigger()) {
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class OptimizerTimerActionListener implements ActionListener {
		Optimizer optimizer;
		ProgressDialog progressDialog;

		public OptimizerTimerActionListener(Optimizer optimizer, 
											ProgressDialog progressDialog) {
			this.optimizer = optimizer;
			this.progressDialog = progressDialog;
		}

		public void actionPerformed(ActionEvent e) {
			if (optimizer.isCanceled()) {
				((Timer) e.getSource()).stop();
				progressDialog.dispose();
			} else {
				int progress = optimizer.getProgress();
				progressDialog.setValue(progress);
			}
		}
	}
}