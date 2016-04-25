package esocial.vallasmobile.services;

/**
 * Created by jesus.martinez on 22/03/2016.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.GeoLocalizacionListener;
import esocial.vallasmobile.obj.GeoLocalizacion;
import esocial.vallasmobile.tasks.PostGeoLocalizacionTask;
import esocial.vallasmobile.utils.Constants;

/**
 * Created by jesus.martinez on 22/03/2016.
 */
public class AppLocationManager implements LocationListener, GeoLocalizacionListener {

    private final static boolean forceNetwork = false;

    private static AppLocationManager instance = null;

    private LocationManager locationManager;
    public Location location;
    public double longitude;
    public double latitude;

    public boolean isNetworkEnabled;
    public boolean isGPSEnabled;
    public boolean locationServiceAvailable;

    public VallasApplication application;

    /**
     * Singleton implementation
     *
     * @return
     */
    public static AppLocationManager getLocationManager(Context context) {
        if (instance == null) {
            instance = new AppLocationManager(context);
        }
        return instance;
    }

    /**
     * Local constructor
     */
    private AppLocationManager(Context context) {
        application = (VallasApplication)context;
        initLocationService(context);
    }

    public void stopLocationManager(){
        // Remove the listener you previously added
        locationManager.removeUpdates(this);
    }


    /**
     * Sets up location service after permissions is granted
     */
    @TargetApi(23)
    private void initLocationService(Context context) {

        try {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled) {
                // cannot get location
                this.locationServiceAvailable = false;
            }
            //else
            {
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            Constants.minTime,
                            Constants.minDistance, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (locationManager != null) updateCoordinates();
                }//end if

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            Constants.minTime,
                            Constants.minDistance, this);

                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (locationManager != null) updateCoordinates();
                }
            }
        } catch (Exception ex) {
            Log.e("LOCATION", "Error creating location service: " + ex.getMessage());

        }
    }

    private void updateCoordinates(){
        if(location!=null)
            VallasApplication.currentLocation = location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        updateCoordinates();

        if(this.location !=null) {
            if(VallasApplication.localizaciones == null){
                VallasApplication.localizaciones = new ArrayList<>();
            }
            //Creamos la localizacion
            GeoLocalizacion localizacion = new GeoLocalizacion(application,
                    this.location.getLatitude(), this.location.getLongitude());
            VallasApplication.localizaciones.add(localizacion);


            if(VallasApplication.refreshTime!=null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(VallasApplication.refreshTime);
                cal.add(Calendar.MINUTE, Constants.refreshLocationTime);

                Calendar now = Calendar.getInstance();
                //Enviamos las localizaciones si ha pasado el tiempo establecido
                if(cal.before(now)){
                    new PostGeoLocalizacionTask(application, VallasApplication.localizaciones, this);
                }
            }else{
                new PostGeoLocalizacionTask(application, VallasApplication.localizaciones, this);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onGeoLocalizacionOK() {
        Log.d("LOCATION SAVED", "USER LOCATION SAVED");
        VallasApplication.refreshTime = new Date();
        VallasApplication.localizaciones = null;
    }

    @Override
    public void onGeoLocalizacionError(String title, String description) {
        Log.d("ERROR SAVE LOCATION", description);
        VallasApplication.refreshTime = null;
        VallasApplication.localizaciones = null;
        stopLocationManager();
    }
}
