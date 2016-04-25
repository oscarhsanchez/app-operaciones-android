package esocial.vallasmobile.listeners;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface GeoLocalizacionListener {

    void onGeoLocalizacionOK();
    void onGeoLocalizacionError(String title, String description);
}
