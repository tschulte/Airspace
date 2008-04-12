/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.gliderpilot.airspace.AirspaceContainer;
import de.gliderpilot.airspace.preferences.AirspacePrefs;
import de.gliderpilot.airspace.preferences.FramePrefs;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.gui.Msg;
import de.gliderpilot.igc.IgcFile;
import de.gliderpilot.preferences.DimensionPreference;
import de.gliderpilot.preferences.PositionPreference;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.preferences.PrefsChangeEvent;
import de.gliderpilot.preferences.PrefsDialog;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 *
 * @version 1.0
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class Gliderpilot extends JFrame implements Prefs.Listener {
	public static final String APP_NAME = "Gliderpilot 2003 Beta 03";
	static Logger logger = Logger.getLogger(Gliderpilot.class);
	static {
		Logger.getRootLogger().setLevel(Level.WARN);
		BasicConfigurator.configure();
	}
	static JFrame frame;
	
	public static JFrame getFrame() {
		return frame;
	}
	
	EnlPanel enlPanel;
	JDesktopPane desktopPane;
	JInternalFrame baroFrame;
	JInternalFrame mapFrame;
	JInternalFrame trackFrame;
	private AirspaceContainer airspace;
	private BaroPanel baroPanel;
	private File currentPath;
	private JMenu fileMenu;
	private JMenuBar menuBar;
	private JMenuItem igcFile;
	private MapPanel mapPanel;
	private TrackLog track;
	private TrackTable trackTable;

	/**
	 * Creates a new Gliderpilot object.
	 */
	public Gliderpilot() {
		super(APP_NAME);
		frame = this;
		final Prefs framePrefs = FramePrefs.getReference();
		final DimensionPreference mainFrameDim = FramePrefs.MAIN_FRAME_DIMENSION;
		final DimensionPreference mapPanelDim = FramePrefs.MAP_PANEL_DIMENSION;
		final DimensionPreference baroPanelDim = FramePrefs.BARO_PANEL_DIMENSION;
		final DimensionPreference trackPanelDim = FramePrefs.TRACK_PANEL_DIMENSION;

		final PositionPreference mainFramePos = FramePrefs.MAIN_FRAME_POS;
		final PositionPreference mapPanelPos = FramePrefs.MAP_PANEL_POS;
		final PositionPreference baroPanelPos = FramePrefs.BARO_PANEL_POS;
		final PositionPreference trackPanelPos = FramePrefs.TRACK_PANEL_POS;

		setSize(mainFrameDim.getDimension());
		setLocation((int) mainFramePos.getPosition().x, (int) mainFramePos.getPosition().y);

		desktopPane = new JDesktopPane();
		setContentPane(desktopPane);

		Prefs airspacePrefs = AirspacePrefs.getReference();
		airspacePrefs.addPreferenceChangeListener(this);

		String lookAndFeel = AirspacePrefs.LOOK_AND_FEEL.stringValue();

		try {
			logger.debug("Setting Look & Feel");
			UIManager.setLookAndFeel(lookAndFeel);
			SwingUtilities.updateComponentTreeUI(this);

		} catch (Exception e) {
			logger.warn("Error", e);
		}

		logger.debug("getting Preferences");

		String path = AirspacePrefs.CURRENT_PATH.stringValue();
		currentPath = new File(path);

		trackTable = new TrackTable();

		trackFrame = new JInternalFrame(Msg.get(Msg.TRACKS), true, false, true, true);
		JScrollPane trackTablePane = new JScrollPane(trackTable);
		trackFrame.setContentPane(trackTablePane);
		trackFrame.setSize(trackPanelDim.getDimension());
		trackFrame.setLocation((int) trackPanelPos.getPosition().x, (int) trackPanelPos.getPosition().y);
		trackFrame.setVisible(true);
		desktopPane.add(trackFrame);

		mapPanel = new MapPanel(trackTable);
		mapPanel.fitViewToRect(mapPanel.getAirspace().getBounds4D());
		mapFrame = new JInternalFrame(Msg.get(Msg.MAP), true, false, true, true);
		mapFrame.setContentPane(mapPanel);
		mapFrame.setSize(mapPanelDim.getDimension());
		mapFrame.setLocation((int) mapPanelPos.getPosition().x, (int) mapPanelPos.getPosition().y);
		mapFrame.setVisible(true);
		desktopPane.add(mapFrame);

		baroFrame = new JInternalFrame(Msg.get(Msg.BARO), true, false, true, true);
		baroFrame.setSize(baroPanelDim.getDimension());
		baroFrame.setLocation((int) baroPanelPos.getPosition().x, (int) baroPanelPos.getPosition().y);
		baroPanel = new BaroPanel(trackTable);
		Dimension preferredDim = new Dimension(baroPanelDim.getDimension());
		preferredDim.setSize(preferredDim.getWidth(), preferredDim.getHeight()*2/3);
		baroPanel.setPreferredSize(preferredDim);
		enlPanel = new EnlPanel(trackTable);
		baroPanel.addDisplayedRectListener(enlPanel);
		baroPanel.addPointChangedListener(enlPanel);
		enlPanel.addDisplayedRectListener(baroPanel);
		enlPanel.addPointChangedListener(baroPanel);

		JSplitPane baroSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		baroSplitPane.setName(Msg.get(Msg.BARO));
		baroSplitPane.add(baroPanel);
		baroSplitPane.add(enlPanel);
		baroSplitPane.setDividerSize(1);
		baroFrame.setContentPane(baroSplitPane);
		baroFrame.setVisible(true);
		desktopPane.add(baroFrame);

		desktopPane.validate();


		menuBar = new JMenuBar();

		fileMenu = new JMenu(Msg.get(Msg.FILE));
		igcFile = new JMenuItem(Msg.get(Msg.IGC_FILE));
		igcFile.addActionListener(new IgcFileListener());
		fileMenu.add(igcFile);

		JMenu prefsMenu = new JMenu(Msg.get(Msg.PREFERENCES));
		JMenuItem airPrefs = new JMenuItem(Msg.get(Msg.PREFERENCES));
		airPrefs.addActionListener(
				new AirspacePreferencesListener(AirspacePrefs.getReference()));
		prefsMenu.add(airPrefs);

		menuBar.add(fileMenu);
		menuBar.add(prefsMenu);

		setJMenuBar(menuBar);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				mainFrameDim.setDimension(new Dimension(getWidth(), getHeight()));
				framePrefs.put(FramePrefs.MAIN_FRAME_DIMENSION.getKey(), mainFrameDim.stringValue());

				mapPanelDim.setDimension(new Dimension(mapFrame.getWidth(), mapFrame.getHeight()));
				framePrefs.put(FramePrefs.MAP_PANEL_DIMENSION.getKey(), mapPanelDim.stringValue());

				baroPanelDim.setDimension(new Dimension(baroFrame.getWidth(), baroFrame.getHeight()));
				framePrefs.put(FramePrefs.BARO_PANEL_DIMENSION.getKey(), baroPanelDim.stringValue());

				trackPanelDim.setDimension(new Dimension(trackFrame.getWidth(), trackFrame.getHeight()));
				framePrefs.put(FramePrefs.TRACK_PANEL_DIMENSION.getKey(), trackPanelDim.stringValue());

				mainFramePos.setValue(new Point4D(getLocation()));
				framePrefs.put(FramePrefs.MAIN_FRAME_POS.getKey(), mainFramePos.stringValue());

				mapPanelPos.setValue(new Point4D(mapFrame.getLocation()));
				framePrefs.put(FramePrefs.MAP_PANEL_POS.getKey(), mapPanelPos.stringValue());

				baroPanelPos.setValue(new Point4D(baroFrame.getLocation()));
				framePrefs.put(FramePrefs.BARO_PANEL_POS.getKey(), baroPanelPos.stringValue());

				trackPanelPos.setValue(new Point4D(trackFrame.getLocation()));
				framePrefs.put(FramePrefs.TRACK_PANEL_POS.getKey(), trackPanelPos.stringValue());

				framePrefs.flush();
			}
		});
		setVisible(true);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		AirspaceSplashScreen splash = new AirspaceSplashScreen();
		splash.show();
		new Gliderpilot();
		splash.dispose();
	}

	/**
	 * @see de.gliderpilot.preferences.Prefs.Listener#preferenceChange(PrefsChangeEvent)
	 */
	public void preferenceChange(PrefsChangeEvent evt) {
		if (evt.getNewPref() == AirspacePrefs.LOOK_AND_FEEL) {
			SwingUtilities.updateComponentTreeUI(this);
		}
	}

	class AirspacePreferencesListener implements ActionListener {
		PrefsDialog prefsDialog;

		public AirspacePreferencesListener(Prefs prefs) {
			prefsDialog = new PrefsDialog(Gliderpilot.this, prefs);
		}

		public void actionPerformed(ActionEvent e) {
			prefsDialog.setVisible(true);
		}
	}

	class IgcFileListener implements ActionListener {
		JFileChooser fileChooser;

		public IgcFileListener() {
			fileChooser = new JFileChooser(currentPath);
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory() || f.getName().toLowerCase()
											.endsWith(".igc")) {
						return true;
					}

					return false;
				}

				public String getDescription() {
					return "*.igc";
				}
			});
			AirspacePrefs.getReference().addPreferenceChangeListener(new Prefs.Listener() {
				public void preferenceChange(PrefsChangeEvent evt) {
					if (evt.getNewPref() == AirspacePrefs.LOOK_AND_FEEL) {
						SwingUtilities.updateComponentTreeUI(fileChooser);
					}
				}
			});
		}

		/**
		 * @see ActionListener#actionPerformed(ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			int returnVal = fileChooser.showOpenDialog(Gliderpilot.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				currentPath = fileChooser.getCurrentDirectory();
				AirspacePrefs.getReference()
							 .put(AirspacePrefs.CURRENT_PATH.getKey(), 
								  currentPath.getAbsolutePath());
				AirspacePrefs.getReference().flush();

				File[] files = fileChooser.getSelectedFiles();
				for (int i = 0; files != null && i < files.length; i++) {
					TrackLog track = new TrackLog();
					new IgcFile(files[i], track).parse();
					track.trim();
					TrackLogTableModel.getInstance().addTrackLog(track);
				}
			}
		}
	}

//	class OptimizeListener implements ActionListener {
//		
//		public MapFileListener() {
//		}
//		
//		/**
//		 * @see ActionListener#actionPerformed(ActionEvent)
//		 */
//		public void actionPerformed(ActionEvent e) {
//			
//			int returnVal = fileChooser.showOpenDialog(AirspaceViewer.this);
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//				String fileName = fileChooser.getSelectedFile().getPath();
//				currentPath = fileChooser.getCurrentDirectory();
//				AirspacePrefs.getInstance().put(AirspacePrefs.CURRENT_PATH, currentPath.getAbsolutePath());
//				AirspacePrefs.getInstance().put(AirspacePrefs.MAP_FILE, fileName);
//				AirspacePrefs.getInstance().flush();
//				new OpenAirspaceFile(fileName, airspace).read();
//				airspace.trimToSize();
//				mapPanel.fitViewToRect(airspace.getBounds4D());
//			}
//		}
//	}
}