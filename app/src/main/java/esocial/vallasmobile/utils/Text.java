package esocial.vallasmobile.utils;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

public class Text {

	public static String getMaxChars(String text, Integer max, String sufix){
		if (text.length() > max){
			//return text.substring(0, max) + sufix; 
		}
		return text;
	}
	
	public static void setTextViewText(Activity activity, int id, String text) {
		if (id > 0 && text != null) {
			TextView textView = (TextView) activity.findViewById(id);
			if (textView != null) textView.setText(text);
		}
	}
	
	public static void setEditTextText(Activity activity, int id, String text) {
		if (id > 0 && text != null) {
			EditText textView = (EditText) activity.findViewById(id);
			if (textView != null) textView.setText(text);
		}
	}

	public static Boolean isEmpty(String text){
		if (text == null || "".equals(text) || text.equals("null")) return true;
		return false;
	}
	
	public static String leftZeroComp(String text, int digits){
		String output = text;
		while (output.length() < digits) output = "0" + output;
		return output;
	}

}
