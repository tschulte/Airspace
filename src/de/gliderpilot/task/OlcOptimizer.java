/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.task;


import java.util.Iterator;
import java.util.logging.Logger;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.preferences.Prefs;
import de.gliderpilot.tracklog.TrackLog;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class OlcOptimizer extends AbstractOptimizer {
	int deltaPOneKm;

	/**
	 * Creates a new OlcOptimizer object.
	 * 
	 * @param track DOCUMENT ME!
	 */
	public OlcOptimizer(TrackLog track) {
		super(track);

		int maxSpeed = track.getMaxSpeed();
		Logger.getLogger(LOGGER).info("maxSpeed: " + maxSpeed);
		int avgSpeed = track.getAvgSpeed();
		Logger.getLogger(LOGGER).info("avgSpeed: " + avgSpeed);
		int minSpeed = track.getMinSpeed();
		Logger.getLogger(LOGGER).info("minSpeed: " + minSpeed);
		int deltaT = track.getDeltaT();
		Logger.getLogger(LOGGER).info("deltaT: " + deltaT);
		double pointsPerHour = ((double) 60 * 60 * 1000) / deltaT;
		Logger.getLogger(LOGGER).info("pointsPerHour: " + pointsPerHour);
		deltaPOneKm = (int) Math.ceil(pointsPerHour / maxSpeed);
		Logger.getLogger(LOGGER).info("DeltaPOneKm: " + deltaPOneKm);
	}

	public Prefs getPrefs() {
		return null;
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void optimize() {
		OlcTask[] tasks = new AbstractOlcTask[endIndex - startIndex];
		for (int i = 0; i < tasks.length; i++) {
			int index = i + startIndex;
			tasks[i] = new OlcTask2003(track);
			tasks[i].addTrackPoint(index);
		}
		OlcTask bestTask;

		Logger.getLogger(LOGGER).info("Number of TrackPoints " + 
									  track.size());

		// first leg
		bestTask = addLeg(tasks);
		setBestTask(bestTask);
		setProgress((endIndex-startIndex) *1/6);
		// second
		bestTask = addLeg(bestTask);
		bestTask = optimize(bestTask);
		setBestTask(bestTask);
		setProgress((endIndex-startIndex) *2/6);
		// third
		bestTask = addLeg(bestTask);
		bestTask = optimize(bestTask);
		setBestTask(bestTask);
		setProgress((endIndex-startIndex) *3/6);
		// 4th
		bestTask = addLeg(bestTask);
		bestTask = optimize(bestTask);
		setBestTask(bestTask);
		setProgress((endIndex-startIndex) *4/6);
		// 5th
		bestTask = addLeg(bestTask);
		bestTask = optimize(bestTask);
		setBestTask(bestTask);
		setProgress((endIndex-startIndex) *5/6);
		// 6th
		bestTask = addLeg(bestTask);
		bestTask = optimize(bestTask);
		setBestTask(bestTask);
		setProgress((endIndex-startIndex));
		
		// this is for the progress dialog
		cancel();
		
		Logger.getLogger(LOGGER).info("\n"+bestTask.getDumpString());
	}
	
	private OlcTask optimize(OlcTask task) {
		OlcTask best = (OlcTask) task.clone();
		Iterator iterator = best.iterator();
		while (iterator.hasNext()) {
			Point4D p = (Point4D) iterator.next();
			task.removeTaskPoint(p);
			for (int i = startIndex; i <= endIndex; i++) {
				if (!task.containsTrackPoint(i)) {;
					task.addTrackPoint(i);
					if (task.getScore() > best.getScore() && task.isValid()) {
						best = (OlcTask) task.clone();
					}
					task.removeTrackPoint(i);
				}
			}
			task.addTaskPoint(p);
		}
		return best;
	}
	
	private OlcTask addLeg(OlcTask task) {
		int iLeaveOuts = 0;
		OlcTask bestTask = (OlcTask) task.clone();
		for (int i = startIndex; i <= endIndex; i += 1+iLeaveOuts) {
			if (!task.contains(track.getPointAt(i))) {
				task.addTrackPoint(i);
				if (task.getScore() > bestTask.getScore() && task.isValid()) {
					bestTask = (OlcTask) task.clone();
				}
				task.removeTrackPoint(i);
			}
		}
		return bestTask;
	}
	
	private OlcTask addLeg(OlcTask[] tasks) {
		int iLeaveOuts = 0;
		int jLeaveOuts = 0;
		int bestJ = 0;
		int end = endIndex;
		OlcTask bestTask = new OlcTask2003(track);

		for (int i = startIndex; (i < (endIndex - 1)) && !isCanceled(); i+= 1+iLeaveOuts) {

			int k = i - startIndex;
			AbstractOlcTask best = (AbstractOlcTask) tasks[k].clone();
			int j = i+1;
			if (bestJ != 0) {
				best.addTrackPoint(bestJ);
//				j   = bestJ - 10;//(int) (1f/6 * (endIndex-startIndex));
//				if (j <= i) j = i+1;
//				end = bestJ + 10;//(int) (1f/6 * (endIndex-startIndex));
//				if (end > endIndex) end = endIndex;
			}
			OlcTask task = tasks[k];
			jLeaveOuts = 0;
			for (; j < end; j += (1 + jLeaveOuts)) {
				task.addTrackPoint(j);
				boolean isValid = task.isValid();
				if (task.getScore() > best.getScore() && isValid) {
					best = (AbstractOlcTask) task.clone();
					bestJ = j;
					jLeaveOuts = 0;
				} else if (isValid) {
					jLeaveOuts = calculateLeaveOuts(best, task);
				} else {
					jLeaveOuts = 0;
				}
				task.removeTrackPoint(j);
			}
			tasks[k] = best;
			if (best.getScore() > bestTask.getScore()) {
				bestTask = best;
			}
		}
		return bestTask;
	}

	private int calculateLeaveOuts(OlcTask bestTask, OlcTask task) {
		if (task.getScore() > bestTask.getScore()) {
			return 0;
		} else {
			return (int) (bestTask.getScore() - task.getScore()) * deltaPOneKm * bestTask.getIndex()/100;
		}
	}
}