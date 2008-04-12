/**
 * Created on Oct 19, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.task;

import java.util.Iterator;

import de.gliderpilot.geom.Point4D;

/**
 * @author Tobias Schulte
 *
 */
public class UnlimitedTask extends AbstractTask {

	/**
	 * Constructor for UnlimitedTask.
	 * @param trackLog
	 * @param capacity
	 */
	public UnlimitedTask() {
		super(0);
	}

	/**
	 * @see de.gliderpilot.task.AbstractTask#calculateScore()
	 */
	protected double calculateScore() {
		return 0;
	}

	public Object clone() {
		Task cloned = new UnlimitedTask();
		Iterator it = iterator();
		while (it.hasNext()) {
			cloned.addTaskPoint((Point4D) it.next());
		}
		return cloned;
	}
	

}
