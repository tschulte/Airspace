/**
 * Created on Nov 17, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.task;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TimeZone;

import de.gliderpilot.Gliderpilot;
import de.gliderpilot.geom.Point4D;

/**
 * @author Tobias Schulte
 *
 */
public abstract class AbstractOlcDefinitionWriter {
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd%2'E'MM%2'E'yy");
	protected static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH%3'A'mm%3'A'ss");
	static {
		timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	protected static final DecimalFormat threeDigitFormat = new DecimalFormat("000");
	protected static final DecimalFormat twoDigitFormat = new DecimalFormat("00");

	protected OlcTask task;
	
	public AbstractOlcDefinitionWriter(OlcTask task) {
		this.task = task;
	}

	private String toHtmlString(String string) {
		if (string == null) return "";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9') {
				buf.append(c);
			} else {
				buf.append("%");
				String hex = Integer.toHexString(c).toUpperCase();
				if (hex.length() == 1) {
					buf.append("0");
				}
				buf.append(hex);
			}
		}
		return buf.toString();
	}

	//	The OLC files contains the string you have to append to the URL fragment ending /olc .
	//  There is a hidden parameter software that you 
	//  can use to store the name and version number of the software used to generate the OLC file.
	//	The parameters starting with ch have at the moment only a meaning for the NSFW, 
	//  but it is planned to use these parameters with the same meaning for other 
	//  national competitions.
	//	Find attached an exapmle of the string to be attached. Note that there are no 
	//  newlines or blanks in the string. The string starts with OLCvnolc and ends with 3A34 .
	//	
	//	OLCvnolc=Hans%20Ludwig&na=Trautenberg&geb=15%2E10%2E65&gty=St%2E%20Cirrus&igcfn=247c0351&sta=Amberg&ft=146558&gid=D%2D3267&ind=98&s0=09%3A36%3A33&w0bh=N&w0bg=49&w0bm=26&w0bmd=309&w0lh=E&w0lg=011&w0lm=47&w0lmd=979&w1bh=N&w1bg=49&w1bm=10&w1bmd=459&w1lh=E&w1lg=012&w1lm=56&w1lmd=948&w2bh=N&w2bg=50&w2bm=03&w2bmd=648&w2lh=E&w2lg=012&w2lm=13&w2lmd=009&w3bh=N&w3bg=49&w3bm=18&w3bmd=888&w3lh=E&w3lg=011&w3lm=29&w3lmd=239&w4bh=N&w4bg=49&w4bm=26&w4bmd=309&w4lh=E&w4lg=011&w4lm=47&w4lmd=979&s4=11%3A00%3A34
	//	
	//	    * Only valid fixes are considered for the OLC.
	//	    * For the German and Austrian OLC it is checked that at least one fix 
	//        of the flight is within a bounding polygone for Germany resp. Austria. 
	//        For other OLCs it is a bounding rectangle or a set of bounding rectangles, 
	//        but may change to a bounding polygone at any time.
	//	    * At the moment the integrity of a flight is not checked, but it is planned 
	//        to do this in the future. Flights may be rejected if the check with the IGC 
	//        short programs is not positive. For IGC files of Cambridge an automatic 
	//        expansion of the lines starting LBIN folled by up to 72 charcters which 
	//        are part of the base64 encoded representation of the original CAI-file 
	//        may be performed and the integrity check may be performed on the CAI-file 
	//        and after a conversion of the CAI-file to a IGC-file the new IGC-file will 
	//        be compared with the uploaded file.
	//	    * There is an automatic engine run detection, which is still in test mode. 
	//        This automatic engine run detection with rejection of flights, for which 
	//        an engine run is detected, may be activated at any time. An engine run 
	//        is detected if there is no ENL in the last 30 seconds that is below 
	//        halfe the maximum ENL of the FR and the current ENL is higher than 14/20 
	//        of the maximum ENL of the FR.
	//
	//	Hans (scoring@segelflugszene.de) 
	protected String getOlcDefinition() {
		StringBuffer sBuffer = new StringBuffer(600);
		// given name
		//	OLCvnolc=Hans%20Ludwig
		sBuffer.append("OLCvnolc=");
		sBuffer.append(toHtmlString(task.getTrackLog().getPilot().getGivenName()));

		// name
		//	&na=Trautenberg
		sBuffer.append("&na=");
		sBuffer.append(toHtmlString(task.getTrackLog().getPilot().getName()));
		
		// birthday
		//	&geb=15%2E10%2E65
		sBuffer.append("&geb=");
		if (task.getTrackLog().getPilot().getBirthDay() != null) {
			sBuffer.append(dateFormat.format(task.getTrackLog().getPilot().getBirthDay()));
		}

		
		// glider type
		//	&gty=St%2E%20Cirrus
		sBuffer.append("&flugzeug=");
		sBuffer.append(task.getTrackLog().getGlider().isMotorGlider()?"2":"1");
		sBuffer.append("&gty=");
		sBuffer.append(toHtmlString(task.getTrackLog().getGlider().getType()));
//<select NAME="klasse">  -- Deutschland
//<option VALUE="0"  SELECTED >mu&szlig; noch gew&auml;hlt werden<option VALUE="1" >HgPgUl<option VALUE="2" >Club<option VALUE="3" >Standard<option VALUE="4" >Renn<option VALUE="5" >18m<option VALUE="6" >DoSi<option VALUE="7" >Offen
//</select>
//<select NAME="klasse">  -- Schweiz
//<option VALUE="0"  SELECTED >mu&szlig; noch gew&auml;hlt werden<option VALUE="1" >Club<option VALUE="2" >Standard<option VALUE="3" >15m<option VALUE="4" >18m<option VALUE="5" >Offen/ouverte

		
		// igc filename
		//	&igcfn=247c0351
		sBuffer.append("&igcfn=");
		File igcFile = new File(task.getTrackLog().getIgcFileName());
		String igcFileName = igcFile.getName();
		sBuffer.append(igcFileName.substring(0, igcFileName.indexOf('.')));
		sBuffer.append("&ligcfn=");
		sBuffer.append(toHtmlString(igcFile.getAbsolutePath()));

		// airfield
		//	&sta=Amberg
		sBuffer.append("&sta=");
		sBuffer.append(toHtmlString(task.getTrackLog().getAirField()));

		//	&ft=146558

		// call sign
		//	&gid=D%2D3267
		sBuffer.append("&gid=");
		sBuffer.append(toHtmlString(task.getTrackLog().getGlider().getCallSign()));

		// index
		//	&ind=98
		sBuffer.append("&ind=");
		sBuffer.append(task.getTrackLog().getGlider().getIndex());
		
		sBuffer.append("&ft=");
		// day since 1600;
		long date = task.getTrackLog().getDate().getTime() / (24*60*60*1000) + 134774;
		sBuffer.append(date);

		//	&s0=09%3A36%3A33
		sBuffer.append("&s0");
		sBuffer.append("=");
		sBuffer.append(timeFormat.format(task.getTrackLog().getStartOfGlide()));
		
		//	&w0bh=N&w0bg=49&w0bm=26&w0bmd=309&w0lh=E&w0lg=011&w0lm=47&w0lmd=979
		Iterator it = task.iterator();
		int i = 0;
		while (it.hasNext()) {
			Point4D p = (Point4D) it.next();
			double b = p.getY();
			char bh = b >= 0 ?'N':'S';
			int bg = Point4D.getDegrees(b);
			int bm = Point4D.getMinutes(b);
			int bmd = Point4D.getMinDecimals(b);
			
			double l = p.getX();
			char lh = l >= 0 ?'E':'W';
			int lg = Point4D.getDegrees(l);
			int lm = Point4D.getMinutes(l);
			int lmd = Point4D.getMinDecimals(l);

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("bh=");
			sBuffer.append(bh);

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("bg=");
			sBuffer.append(twoDigitFormat.format(bg));

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("bm=");
			sBuffer.append(twoDigitFormat.format(bm));

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("bmd=");
			sBuffer.append(threeDigitFormat.format(bmd));

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("lh=");
			sBuffer.append(lh);

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("lg=");
			sBuffer.append(threeDigitFormat.format(lg));

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("lm=");
			sBuffer.append(twoDigitFormat.format(lm));

			sBuffer.append("&w");
			sBuffer.append(i);
			sBuffer.append("lmd=");
			sBuffer.append(threeDigitFormat.format(lmd));

			i++;
		}
		// &s4=11%3A00%3A34
		int capacity = task.capacity();
		sBuffer.append("&s");
		sBuffer.append(capacity-1);
		sBuffer.append("=");
		sBuffer.append(timeFormat.format(task.getTrackLog().getEndOfGlide()));

		sBuffer.append("&software=");
		sBuffer.append(toHtmlString(Gliderpilot.APP_NAME));
		
		return sBuffer.toString();
	}

}
