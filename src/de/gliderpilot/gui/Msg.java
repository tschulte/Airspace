/**
 * Created on Nov 19, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.gui;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/**
 * @author Tobias Schulte
 *
 */
public class Msg implements TranslationKeys {
	static final String TOOLTIP_ = "TOOLTIP_";
	static ResourceBundle bundle;
	
	static Map defaults = new HashMap();
	
	static {
		try {
			String bundleName = "resources/messages/messages";
			bundle = ResourceBundle.getBundle(bundleName);
		} catch (Exception e) {
			Logger.getLogger(Msg.class).warn("ResourceBundle for language "
					+ Locale.getDefault()
					+" not found -- Using defaults.");
		}
		
		put(OK, "OK");
		put(CANCEL, "Cancel");
		put(APPLY, "Apply");
		put(FIT_VIEW_TO_0, "Fit view to {0}");
		put(PREFERENCES, "Preferences");
		put(FILE, "File");
		put(IGC_FILE, "IGC file");
		put(MAP, "Map");
		put(BARO, "Baro");
		put(ENL, "ENL");
		put(TRACKS, "Flights");
		put(TRACK, "flight");
		put(DATE, "Date");
		put(NAME, "Name");
		put(GIVEN_NAME, "Given name");
		put(BIRTHDAY, "Date of birth");
		put(GLIDER_TYPE, "Glider type");
		put(REMOVE, "remove");
		put(OPTIMIZE, "optimize");
		put(OLC_FILE, "OLC file");
		put(OLC_TASK_INFO, "OLC task info");
		put(DISTANCE, "distance");
		put(SCORE, "score");
		put(INDEX, "index");
		put(AIRFIELD, "airfield");
		put(GLIDER_SIGN, "glider sign");
		put(POWERED_GLIDER, "powered glider");
		put(AIRSPACE, "airspace");
		put(START_TIME, "Start of glide");
		put(END_TIME, "End of Glide");
	}
	
	
	public static String get(String key) {
		String value = null;
		if (bundle != null) {
			value = bundle.getString(key);
		}
		if (value != null) {
			return value;
		}
		value = (String) defaults.get(key);
		if (value != null) {
			return value;
		}
		return key;
	}
	
	public static String format(String key, Object[] arguments) {
		return MessageFormat.format(get(key), arguments);
	}
	
	public static String format(String key, Object argument) {
		return format(key, new Object[] {argument});
	}

	public static String format(String key, String argument) {
		return format(key, new Object[] {get(argument)});
	}
	
	static String put(String key) {
		return put(key, key);
	}
	
	static String put(String key, String value) {
		return (String) defaults.put(key, value);
	}
	
	static String put(String key, String value, String tooltip) {
		defaults.put(TOOLTIP_+key, tooltip);
		return (String) defaults.put(key, value);
	}
	
	public static void setLocale(Locale locale) {
		bundle = ResourceBundle.getBundle(Msg.class.getName(), locale);
	}

}
