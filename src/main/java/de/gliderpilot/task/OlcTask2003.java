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
public class OlcTask2003 extends AbstractOlcTask {
	/**
	 * Creates a new OlcTask2003 object.
	 * 
	 * @param trackLog DOCUMENT ME!
	 */
	public OlcTask2003(TrackLog trackLog) {
		super(trackLog, 7);
	}


	/**
	 * @see de.gliderpilot.task.Task#getScore()
	 */
	protected double calculateScore() {
		double distance = 0;
		double score = 0;
		Point4D p1 = null;
		Point4D p2 = null;
		
		int i = 0;
		Iterator it = iterator();
		if (it.hasNext()) {
			p1 = (Point4D) it.next();
			i++;
		}
		while (it.hasNext() && i < 5) {
			p2 = (Point4D) it.next();
			distance += p1.getDistanceFrom(p2);
			p1 = p2;
			i++;
		}

		score = distance;
		if (it.hasNext()) {
			p2 = (Point4D) it.next();
			distance = p1.getDistanceFrom(p2);
			p1 = p2;
			score += (distance * 0.8);
		}
		if (it.hasNext()) {
			p2 = (Point4D) it.next();
			distance = p1.getDistanceFrom(p2);
			score += (distance * 0.6);
		}

		return score;
	}

}