/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import java.util.Iterator;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class OlcTask2002 extends AbstractOlcTask {
	boolean isFaiTriangle;
	boolean isTriangle;

	/**
	 * Creates a new OlcTask2002 object.
	 * 
	 * @param trackLog DOCUMENT ME!
	 */
	public OlcTask2002(TrackLog trackLog) {
		super(trackLog, 5);
	}

	/**
	 * Returns the isFaiTriangle.
	 * 
	 * @return boolean
	 */
	public boolean isFaiTriangle() {
		return isFaiTriangle;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isTriangle() {
		isFaiTriangle = false;

		Point4D[] points = getPoints();

		double a = points[1].getDistanceFrom(points[2]);
		double b = points[2].getDistanceFrom(points[3]);
		double c = points[1].getDistanceFrom(points[3]);
		double d = points[0].getDistanceFrom(points[4]);
		double u = a + b + c;

		if ((d * 5) <= u) { // it's a triangle

			double tmp = u * 7;

			if ((a * 25 >= tmp) && (b * 25 >= tmp) && (c * 25 >= tmp)) {
				// it's a FAI-triangle
				isFaiTriangle = true;
			}

			return true;
		}

		return false;

//		return isTriangle;
	}


	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected double calculateDistance() {
		double a = getTaskPoint(1).getDistanceFrom(getTaskPoint(2));
		double b = getTaskPoint(2).getDistanceFrom(getTaskPoint(3));
		double c = getTaskPoint(1).getDistanceFrom(getTaskPoint(3));
		double d = getTaskPoint(0).getDistanceFrom(getTaskPoint(4));
	
		return (a + b + c) - d;
	}

	/**
	 * @see de.gliderpilot.task.Task#getScore()
	 */
	protected double calculateScore() {
		double score;
		score = getDistance();

		if (score == 0) {
			return 0;
		}

		if (isTriangle()) {
			if (isFaiTriangle()) {
				score *= 2;
			} else {
				score *= 1.75;
			}
		} else {
			score *= 1.5;
		}

		return score;
	}
	public Object clone() {
		Task cloned = new OlcTask2002(getTrackLog());
		Iterator it = iterator();
		while (it.hasNext()) {
			cloned.addTaskPoint((Point4D) it.next());
		}
		return cloned;
	}

}