/*
 * Copyright (c) 2002 3plus4software GbR.  All rights reserved.
 *
 * You are granted the right to use this code in a) GPL based projects in 
 * which case this code shall be also protected by the GPL, or b) in other 
 * projects as long as you make all modifications or extensions to this 
 * code freely available, or c) make any other special agreement with the 
 * copyright holder.
 */

package de.gliderpilot;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JWindow;

public class SplashScreen extends JWindow {
	private Image back;
	private Image image;

	public SplashScreen(URL url) {
		super(new Frame());
		// start loading the splash image in the background... 
		image = Toolkit.getDefaultToolkit().getImage(url);
	}
	public void paint(Graphics g) {
		if (back != null) {
			g.drawImage(back, 0, 0, this);
		}
		g.drawImage(image, 0, 0, this);
	}
	public void show() {
		// we must delay show until at least the image dimension is known
		int w = image.getWidth(this);
		int h = image.getHeight(this);
		if (w != -1 && h != -1) {
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			setBounds((d.width - w) / 2, (d.height - h) / 3, w, h);
			try {
				back = new Robot().createScreenCapture(getBounds());
			} catch (AWTException e) {
			}
			super.show();
		}
	}
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		if ((infoflags & WIDTH + HEIGHT) != 0) {
			// we might know now the image dimension, try show again
			show();
		}
		return super.imageUpdate(img, infoflags, x, y, w, h);
	}
	public void dispose() {
		super.dispose();
		back = null;
		image = null;
	}

}