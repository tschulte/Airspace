/*
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */

package de.gliderpilot.geom;


import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;


/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
class Line4DIterator implements PathIterator, Modes4D {
	AffineTransform affine;
	Line4D line;
	int index;
	int type;

	Line4DIterator(Line4D l, AffineTransform at, int type) {
		this.line = l;
		this.affine = at;
		this.type = type;
	}

	/**
	 * @see PathIterator#isDone()
	 */
	public boolean isDone() {
		return (index > 1);
	}

	/**
	 * @see PathIterator#getWindingRule()
	 */
	public int getWindingRule() {
		return WIND_NON_ZERO;
	}

	/**
	 * @see PathIterator#currentSegment(float[])
	 */
	public int currentSegment(float[] coords) {
		if (isDone()) {
			throw new NoSuchElementException("line iterator out of bounds");
		}

		int type;

		if (index == 0) {
			if (this.type == XY) {
				coords[0] = (float) line.getX1();
				coords[1] = (float) line.getY1();
			} else if (this.type == GPS) {
				coords[0] = line.getD1();
				coords[1] = line.getGA1();
			} else if (this.type == PRESSURE) {
				coords[0] = line.getD1();
				coords[1] = line.getPA1();
			} else if (this.type == ENL) {
				coords[0] = line.getD1();
				coords[1] = line.getEnl1();
			} else if (this.type == VARIO) {
				coords[0] = line.getD1();
				coords[1] = line.getVario();
			} else if (this.type == SPEED) {
				coords[0] = line.getD1();
				coords[1] = line.getSpeed();
			}

			type = SEG_MOVETO;
		} else {
			if (this.type == XY) {
				coords[0] = (float) line.getX2();
				coords[1] = (float) line.getY2();
			} else if (this.type == GPS) {
				coords[0] = line.getD2();
				coords[1] = line.getGA2();
			} else if (this.type == PRESSURE) {
				coords[0] = line.getD2();
				coords[1] = line.getPA2();
			} else if (this.type == ENL) {
				coords[0] = line.getD2();
				coords[1] = line.getEnl2();
			} else if (this.type == VARIO) {
				coords[0] = line.getD2();
				coords[1] = line.getVario();
			} else if (this.type == SPEED) {
				coords[0] = line.getD2();
				coords[1] = line.getSpeed();
			}

			type = SEG_LINETO;
		}

		if (affine != null) {
			affine.transform(coords, 0, coords, 0, 1);
		}

		return type;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param coords DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws NoSuchElementException DOCUMENT ME!
	 */
	public int currentSegment(double[] coords) {
		if (isDone()) {
			throw new NoSuchElementException("line iterator out of bounds");
		}

		int type;

		if (index == 0) {
			if (this.type == XY) {
				coords[0] = line.getX1();
				coords[1] = line.getY1();
			} else if (this.type == GPS) {
				coords[0] = line.getD1();
				coords[1] = line.getGA1();
			} else if (this.type == PRESSURE) {
				coords[0] = line.getD1();
				coords[1] = line.getPA1();
			} else if (this.type == ENL) {
				coords[0] = line.getD1();
				coords[1] = line.getEnl1();
			} else if (this.type == VARIO) {
				coords[0] = line.getD1();
				coords[1] = line.getVario();
			} else if (this.type == SPEED) {
				coords[0] = line.getD1();
				coords[1] = line.getSpeed();
			}

			type = SEG_MOVETO;
		} else {
			if (this.type == XY) {
				coords[0] = line.getX2();
				coords[1] = line.getY2();
			} else if (this.type == GPS) {
				coords[0] = line.getD2();
				coords[1] = line.getGA2();
			} else if (this.type == PRESSURE) {
				coords[0] = line.getD2();
				coords[1] = line.getPA2();
			} else if (this.type == ENL) {
				coords[0] = line.getD2();
				coords[1] = line.getEnl2();
			} else if (this.type == VARIO) {
				coords[0] = line.getD2();
				coords[1] = line.getVario();
			} else if (this.type == SPEED) {
				coords[0] = line.getD2();
				coords[1] = line.getSpeed();
			}

			type = SEG_LINETO;
		}

		if (affine != null) {
			affine.transform(coords, 0, coords, 0, 1);
		}

		return type;
	}

	/**
	 * @see PathIterator#next()
	 */
	public void next() {
		index++;
	}
}