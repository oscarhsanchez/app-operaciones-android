package esocial.vallasmobile.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import esocial.vallasmobile.obj.Session;
import esocial.vallasmobile.utils.Constants;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class VallasApplication extends Application {

    public static String appEntornoPago;
    public static String currentEntity;
    private Session session;
    private SharedPreferences.Editor prefsEditor;
    public static String imagePath = "https://efinanzas.s3.amazonaws.com/";
    public static Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        prefsEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    }


    public Session getSession() {
        if (session == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            session = new Gson().fromJson(prefs.getString(Constants.SESSION, ""), Session.class);
        }
        return session;
    }

    public void setSession(Session session) {
        if (session != null) {
            prefsEditor.putString(Constants.SESSION, new Gson().toJson(session));
            prefsEditor.apply();
        }
        this.session = session;
    }
}