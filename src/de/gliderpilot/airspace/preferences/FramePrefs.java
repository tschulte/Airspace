package de.gliderpilot.airspace.preferences;

import java.awt.Dimension;
import java.awt.Toolkit;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.preferences.DimensionPreference;
import de.gliderpilot.preferences.PositionPreference;
import de.gliderpilot.preferences.Prefs;

/**
 * @author Tobias Schulte
 *
 */
public class FramePrefs extends Prefs {
	public static DimensionPreference MAIN_FRAME_DIMENSION;
	public static DimensionPreference MAP_PANEL_DIMENSION;
	public static DimensionPreference BARO_PANEL_DIMENSION;
	public static DimensionPreference TRACK_PANEL_DIMENSION;

	public static PositionPreference MAIN_FRAME_POS;
	public static PositionPreference MAP_PANEL_POS;
	public static PositionPreference BARO_PANEL_POS;
	public static PositionPreference TRACK_PANEL_POS;

	private static Prefs prefs = new FramePrefs();

	protected FramePrefs() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
				
		Dimension mapDim = new Dimension((int) (dim.getWidth()*3/4), (int) dim.getHeight());
		Dimension baroDim = new Dimension((int) (dim.getWidth()/4), (int) (dim.getHeight()/2));
		Dimension trackDim = new Dimension((int) (dim.getWidth()/4), (int) (dim.getHeight()/2));

		MAIN_FRAME_DIMENSION = new DimensionPreference(this, "Main Frame Dimension", dim);
		add(MAIN_FRAME_DIMENSION);
		MAP_PANEL_DIMENSION = new DimensionPreference(this, "Map Panel Dimension", mapDim);
		add(MAP_PANEL_DIMENSION);
		BARO_PANEL_DIMENSION = new DimensionPreference(this, "Baro Panel Dimension", baroDim);
		add(BARO_PANEL_DIMENSION);
		TRACK_PANEL_DIMENSION = new DimensionPreference(this, "Track Panel Dimension", trackDim);
		add(TRACK_PANEL_DIMENSION);

		MAIN_FRAME_POS = new PositionPreference(this, "Main Frame Position", new Point4D());
		add(MAIN_FRAME_POS);
		MAP_PANEL_POS = new PositionPreference(this, "Map Panel Position", new Point4D());
		add(MAP_PANEL_POS);
		BARO_PANEL_POS = new PositionPreference(this, "Baro Panel Position", new Point4D(mapDim.getWidth(), 0));
		add(BARO_PANEL_POS);
		TRACK_PANEL_POS = new PositionPreference(this, "Track Panel Position", new Point4D(mapDim.getWidth(), baroDim.getHeight()));
		add(TRACK_PANEL_POS);
	}

	/**
	 * @see Prefs#getReference()
	 */
	public static Prefs getReference() {
		return prefs;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getName() {
		return "Frame";
	}
}