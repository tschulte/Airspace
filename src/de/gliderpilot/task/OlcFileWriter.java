/**
 * Created on Oct 27, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Tobias Schulte
 *
 */
public class OlcFileWriter extends AbstractOlcDefinitionWriter {

	/**
	 * Constructor for OlcFileWriter.
	 */
	public OlcFileWriter(File file, OlcTask task, boolean autoUploadIgcFile) throws IOException {
		super(task);
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		writer.print(getOlcDefinition());

		if (autoUploadIgcFile) {
			//	At the end of the string you may append the string IGCigcIGC= followed by
			//  the content of the IGC-file.
			File igcFile = new File(task.getTrackLog().getIgcFileName());
			BufferedReader reader = new BufferedReader(new FileReader(igcFile));
			writer.write("&IGCigcIGC=");
			String line;
			while ((line = reader.readLine()) != null) {
				writer.println(line);
			}
		}	
		writer.flush();
		writer.close();
	}

}
