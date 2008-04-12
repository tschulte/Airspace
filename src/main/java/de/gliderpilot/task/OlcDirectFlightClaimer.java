/**
 * Created on Oct 27, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author Tobias Schulte
 *
 */
public abstract class OlcDirectFlightClaimer extends AbstractOlcDefinitionWriter {

	/**
	 * Constructor for OlcDirectFlightClaimer.
	 */
	public OlcDirectFlightClaimer(URL url, OlcTask task) throws IOException, ProtocolException {
		super(task);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
	}

	//	There is an additional parameter olcdirect. If this parameter is present 
	//  in the OLC file, the server will give you back only the ref number for the flight 
	//  claimed with the OLC-file. If the flight is scored the word flight scored will 
	//  follow after the ref number.
	protected String getOlcDefinition() {
		StringBuffer buf = new StringBuffer();
		buf.append(super.getOlcDefinition());
		return buf.toString();
	}

}
