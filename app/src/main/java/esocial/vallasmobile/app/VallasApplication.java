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

    private Session session;
    private Boolean refreshOrdenes;
    private Boolean refreshIncidencias;
    private SharedPreferences.Editor prefsEditor;
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

    public Boolean getRefreshOrdenes() {
        if (refreshOrdenes == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            refreshOrdenes = prefs.getBoolean(Constants.REFRESH_ORDENES, false);
        }
        return refreshOrdenes;
    }

    public void setRefreshOrdenes(Boolean refreshOrdenes) {
        if (refreshOrdenes != null) {
            prefsEditor.putBoolean(Constants.REFRESH_ORDENES, refreshOrdenes);
            prefsEditor.apply();
        }
        this.refreshOrdenes = refreshOrdenes;
    }

    public Boolean getRefreshIncidencias() {
        if (refreshIncidencias == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            refreshIncidencias = prefs.getBoolean(Constants.REFRESH_INCIDENCIAS, false);
        }
        return refreshIncidencias;
    }

    public void setRefreshIncidencias(Boolean refreshIncidencias) {
        if (refreshIncidencias != null) {
            prefsEditor.putBoolean(Constants.REFRESH_INCIDENCIAS, refreshIncidencias);
            prefsEditor.apply();
        }
        this.refreshIncidencias = refreshIncidencias;
    }
}