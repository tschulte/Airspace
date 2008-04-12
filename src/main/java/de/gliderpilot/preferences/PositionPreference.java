/**
 * Created on Oct 4, 2002
 *
 * (c) Copyright 2002 Gliderpilot.de.
 * All Rights Reserved.
 */
package de.gliderpilot.preferences;

import java.awt.GridLayout;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JTextField;

import de.gliderpilot.geom.Point4D;
import de.gliderpilot.gui.CancelApplyPanel;

/**
 * @author Tobias Schulte
 *
 */
public class PositionPreference extends AbstractPreference {
	PositionPrefPanel positionPrefPanel;
	Point4D pos = new Point4D();

	public PositionPreference(Prefs prefs, String key, Point4D defP) {
		super(prefs, key);
		setValue(defP);
		String def = prefs.get(key, stringValue());
		setValue(def);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JPanel getJPanel() {
		if (positionPrefPanel == null) {
			positionPrefPanel = new PositionPrefPanel();
		}

		return positionPrefPanel;
	}

	/**
	 * @see Pref#getString()
	 */
	public String stringValue() {
		return ""+pos.getX()+";"+pos.getY()+";"+pos.getGpsHeight()+";"+pos.getPressureHeight()+";"+(pos.getDate()!=null?pos.getDate().getTime():0);
	}
	
	public Point4D getPosition() {
		return pos;
	}
	
	public void setValue(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value, ";");
		if (tokenizer.countTokens() == 5) {
			pos.x = Double.parseDouble(tokenizer.nextToken());
			pos.y = Double.parseDouble(tokenizer.nextToken());
			pos.setGpsHeight(Integer.parseInt(tokenizer.nextToken()));
			pos.setPressureHeight(Integer.parseInt(tokenizer.nextToken()));
			pos.setDate(new Date(Long.parseLong(tokenizer.nextToken())));
		}
	}
	
	public void setValue(Point4D pos) {
		this.pos = pos;
	}

	class PositionPrefPanel extends CancelApplyPanel implements Prefs.Listener {
		JTextField textField;

		public PositionPrefPanel() {
			setLayout(new GridLayout(1, 1));
			textField = new JTextField(stringValue());

			add(textField);
		}

		public void apply() {
			super.apply();

			String value = textField.getText();

			if (!stringValue().equals(value)) {
				setValue(value);
				save();
			}
		}

		public void cancel() {
			super.cancel();
			String value = stringValue();
			if (!value.equals(textField.getText())) {
				textField.setText(value);
			}
		}

		/**
		 * @see Listener#preferenceChange(PrefsChangeEvent)
		 */
		public void preferenceChange(PrefsChangeEvent evt) {
			if (evt.getNewPref().getKey().equals(key)) {
				textField.setText(stringValue());
			}
		}
	}
}