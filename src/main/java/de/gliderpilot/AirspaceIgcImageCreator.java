/*
 * (c) Copyright 2002 Gliderpilot.de. All Rights Reserved.
 */

package de.gliderpilot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import de.gliderpilot.airspace.AirspaceVector;
import de.gliderpilot.airspace.OpenAirspaceFile;
import de.gliderpilot.geom.Point4D;
import de.gliderpilot.geom.Rectangle4D;
import de.gliderpilot.igc.IgcFile;

import de.gliderpilot.tracklog.TrackLog;
import de.gliderpilot.util.Util;

/**
 * DOCUMENT ME!
 * 
 * @author <a href="mailto:tobias.schulte@gliderpilot.de">Tobias Schulte</a>
 */
public class AirspaceIgcImageCreator {

    static boolean antialise = false;

    AirspaceVector airspace;

    TrackLog track;

    /**
     * Creates a new AirspaceIgcImageCreator object.
     * 
     * @param airFile
     *            DOCUMENT ME!
     * @param igcFile
     *            DOCUMENT ME!
     */
    public AirspaceIgcImageCreator(File airFile, String igcFile) {
        Logger.getLogger(getClass()).setLevel(Level.INFO);
        airspace = new AirspaceVector();
        track = new TrackLog();

        if (!(new OpenAirspaceFile(airFile, airspace).read())) {
            System.out.println("Could not parse airspaceFile");
            System.exit(2);
        }

        if (!(new IgcFile(igcFile, track).parse())) {
            System.out.println("Could not parse igcFile");
            System.exit(3);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param fileName
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean createTrackImage(String fileName) {
        BufferedImage bi = new BufferedImage(400, 400,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        if (antialise) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        FileOutputStream fo;

        try {
            fo = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            return false;
        }

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fo);

        Rectangle4D rect = track.getBounds4D();
        Point4D center = new Point4D(rect.getCenterX(), rect.getY());

        Point4D ul = new Point4D(rect.getX(), rect.getY());
        double zoom = Util.getFactor(bi.getWidth(), 2 * center
                .getXDistanceFrom(ul));
        double zoom2 = Util.getFactor(bi.getHeight(), 2 * center
                .getYDistanceFrom(ul));
        zoom = (zoom < zoom2) ? zoom : zoom2;

        AffineTransform xform = new AffineTransform();
        xform.setToScale(zoom * center.getXCorrection(null), -zoom);
        xform.translate(-rect.getX(), -rect.getY() - rect.getHeight());

        g.setColor(Color.white);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        airspace.draw(g, rect, xform);
        track.draw(g, rect, xform);

        encoder.encode(bi);

        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public static void help() {
        System.out
                .println("Arguments must be: openAirspaceFile igcFile imageFile [-antialise]");
        System.exit(1);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param args
     *            DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            help();
        }

        if (args.length == 4) {
            antialise = true;
        }

        AirspaceIgcImageCreator imageCreator;
        long millis = System.currentTimeMillis();
        imageCreator = new AirspaceIgcImageCreator(new File(args[0]), args[1]);
        imageCreator.createTrackImage(args[2]);
        System.out.println("Image created. lasted "
                + (System.currentTimeMillis() - millis) + " ms.");
    }
}
