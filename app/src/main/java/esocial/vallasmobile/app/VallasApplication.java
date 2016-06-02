package esocial.vallasmobile.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Date;

import esocial.vallasmobile.obj.GeoLocalizacion;
import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.obj.Motivo;
import esocial.vallasmobile.obj.OrdenImagen;
import esocial.vallasmobile.obj.Session;
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.services.SendImagesService;
import esocial.vallasmobile.tasks.ImageSender;
import esocial.vallasmobile.tasks.PutIncidenciaEstadoTask;
import esocial.vallasmobile.tasks.PutOrdenEstadoTask;
import esocial.vallasmobile.tasks.PutOrdenMotivoTask;
import esocial.vallasmobile.utils.Constants;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class VallasApplication extends Application {

    private Session session;
    private Boolean location_geo_permission;
    private Boolean user_geo;
    private Boolean refreshOrdenesPendientes;
    private Boolean refreshOrdenesCerradas;
    private Boolean refreshIncidenciasAsignadas;
    private Boolean refreshIncidenciasCreadas;
    private SharedPreferences.Editor prefsEditor;
    public static Location currentLocation;

    public static ArrayList<GeoLocalizacion> localizaciones;
    public static ArrayList<Motivo> motivosCierre;
    public static Date refreshTime;

    public static ImageSender sender;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        prefsEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    }


    //region sharedPreferences
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

    public Boolean getUserGeo() {
        if (user_geo == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            user_geo = prefs.getBoolean(Constants.USERGEO, false);
        }
        return user_geo;
    }

    public void setUserGeo(Boolean user_geo) {
        if (user_geo != null) {
            prefsEditor.putBoolean(Constants.USERGEO, user_geo);
            prefsEditor.apply();
        }
        this.user_geo = user_geo;
    }

    public Boolean getLocationGeoPermission() {
        if (location_geo_permission == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            location_geo_permission = prefs.getBoolean(Constants.GEOPERMISSION, false);
        }
        return location_geo_permission;
    }

    public void setLocationGeoPermission(Boolean geoPermission) {
        if (geoPermission != null) {
            prefsEditor.putBoolean(Constants.GEOPERMISSION, geoPermission);
            prefsEditor.apply();
        }
        this.location_geo_permission = geoPermission;
    }

    public Boolean getRefreshOrdenesPendientes() {
        if (refreshOrdenesPendientes == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            refreshOrdenesPendientes = prefs.getBoolean(Constants.REFRESH_ORDENES_PENDIENTES, false);
        }
        return refreshOrdenesPendientes;
    }

    public void setRefreshOrdenesPendientes(Boolean refreshOrdenesPendientes) {
        if (refreshOrdenesPendientes != null) {
            prefsEditor.putBoolean(Constants.REFRESH_ORDENES_PENDIENTES, refreshOrdenesPendientes);
            prefsEditor.apply();
        }
        this.refreshOrdenesPendientes = refreshOrdenesPendientes;
    }

    public Boolean getRefreshOrdenesCerradas() {
        if (refreshOrdenesCerradas == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            refreshOrdenesCerradas = prefs.getBoolean(Constants.REFRESH_ORDENES_CERRADAS, false);
        }
        return refreshOrdenesCerradas;
    }

    public void setRefreshOrdenesCerradas(Boolean refreshOrdenesCerradas) {
        if (refreshOrdenesCerradas != null) {
            prefsEditor.putBoolean(Constants.REFRESH_ORDENES_CERRADAS, refreshOrdenesCerradas);
            prefsEditor.apply();
        }
        this.refreshOrdenesCerradas = refreshOrdenesCerradas;
    }

    public Boolean getRefreshIncidenciasAsignadas() {
        if (refreshIncidenciasAsignadas == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            refreshIncidenciasAsignadas = prefs.getBoolean(Constants.REFRESH_INCIDENCIAS_ASIGNADAS, false);
        }
        return refreshIncidenciasAsignadas;
    }

    public Boolean getRefreshIncidenciasCreadas() {
        if (refreshIncidenciasCreadas == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            refreshIncidenciasCreadas = prefs.getBoolean(Constants.REFRESH_INCIDENCIAS_CREADAS, false);
        }
        return refreshIncidenciasCreadas;
    }

    public void setRefreshIncidenciasCreadas(Boolean refreshIncidenciasCreadas) {
        if (refreshIncidenciasCreadas != null) {
            prefsEditor.putBoolean(Constants.REFRESH_INCIDENCIAS_CREADAS, refreshIncidenciasCreadas);
            prefsEditor.apply();
        }
        this.refreshIncidenciasCreadas = refreshIncidenciasCreadas;
    }

    public void setRefreshIncidenciasAsignadas(Boolean refreshIncidenciasAsignadas) {
        if (refreshIncidenciasAsignadas != null) {
            prefsEditor.putBoolean(Constants.REFRESH_INCIDENCIAS_ASIGNADAS, refreshIncidenciasAsignadas);
            prefsEditor.apply();
        }
        this.refreshIncidenciasAsignadas = refreshIncidenciasAsignadas;
    }

    //endregion

    //region imageSender
    public void initImageSender(boolean forzarEnvio){
        if(sender == null)
            sender = new ImageSender();

        Intent intent = new Intent(context, SendImagesService.class);
        intent.putExtra("showMessage", forzarEnvio);
        PendingIntent pendingIntent = PendingIntent.getService(context,  1253, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                Constants.sendImageFrequency, pendingIntent);
    }

    public void stopImageSender(){
        Intent intent = new Intent(context, SendImagesService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1253, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void forzarImageSender(){
        stopImageSender();
        initImageSender(true);
    }

    public void sendCambioEstadoOrden(String pk_orden_trabajo, Integer estado,String observaciones,
                                      String pk_motivo){

        if(TextUtils.isEmpty(pk_motivo)) {
            new PutOrdenEstadoTask(this, pk_orden_trabajo, estado, observaciones);
        }else{
            new PutOrdenMotivoTask(this, pk_orden_trabajo, pk_motivo);
        }
    }

    public void sendCambioEstadoIncidencia(String pk_incidencia, Integer estado, String observaciones,
                                      String pk_motivo){

        new PutIncidenciaEstadoTask(this, pk_incidencia, estado, observaciones);
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

    //endregion
}