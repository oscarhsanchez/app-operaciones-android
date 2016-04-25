package esocial.vallasmobile.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import esocial.vallasmobile.obj.GeoLocalizacion;
import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.obj.OrdenImagen;
import esocial.vallasmobile.obj.Session;
import esocial.vallasmobile.obj.Ubicacion;
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.services.SendImagesService;
import esocial.vallasmobile.tasks.ImageSender;
import esocial.vallasmobile.tasks.PostOrdenImagenTask;
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

    public static ArrayList<GeoLocalizacion> localizaciones;
    public static Date refreshTime;

    public static ImageSender sender;

    @Override
    public void onCreate() {
        super.onCreate();

        prefsEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    }

    public void initImageSender(Context context){
        sender = new ImageSender(this);

        Intent intent = new Intent(context, SendImagesService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context,  0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5); // first time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                Constants.sendImageFrequency, pendingIntent);
    }

    public void setPendingOrdenImage(OrdenImagen imagen){
        sender.addImage(imagen);
    }

    public void setPendingIncidenciaImage(IncidenciaImagen imagen){
        sender.addImage(imagen);
    }

    public void setPendingUbicacionImage(UbicacionImagen imagen){
        sender.addImage(imagen);
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