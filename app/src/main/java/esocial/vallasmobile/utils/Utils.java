package esocial.vallasmobile.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
	
	public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View view = activity.getCurrentFocus();
		
		if (view == null) {
			return;
		}
		
		inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

    public static void showInputKeyboard(Activity activity, View v) {
        InputMethodManager keyboard = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(v, 0);
    }

	public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
             
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

	public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
	public static String removeNull(String text) {
		if (text == null) {
			return "";
		}
		return text;
	}
	
	
	
	public static boolean checkInternetConnection(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean ret = true;

		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();
			if (i != null) {
				if (!i.isConnected() || !i.isAvailable()) {
					ret = false;
				}
			} else {
				ret = false;
			}
		} else {
			ret = false;
		}

		return ret;
	}
	

	
	public static Long getDiffTime() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
	    String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
	    String operator = timeZone.substring(0,1);
	    String hours = timeZone.substring(1, 3);
	    String minutes = timeZone.substring(3, 4);
	    
	    return operator.equals("+") ? (Long.parseLong(hours)*60*60*1000)+(Long.parseLong(minutes)*60*1000)
	    		: -(Long.parseLong(hours)*60*60*1000)+(Long.parseLong(minutes)*60*1000);
	}

	public static boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}



	public static String generateToken() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Calendar c = Calendar.getInstance();
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return Base64.encode(Utils.byteArrayToHexString(md.digest(((int) (Math.random() * 100000) + "asc/!*20aa" + formatDate.format(c.getTime()) + (int) (Math.random() * 100000)).getBytes("UTF-8"))));
	}

	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i=0; i < b.length; i++) {
			result +=
					Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}

	/**
	 * This method makes a "deep clone" of any Java object it is given.
	 */
	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
