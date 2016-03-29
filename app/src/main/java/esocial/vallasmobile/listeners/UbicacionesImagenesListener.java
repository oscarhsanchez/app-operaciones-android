package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.UbicacionImagen;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface UbicacionesImagenesListener {

    void onGetUbicacionesImagenesOK(ArrayList<UbicacionImagen> images);
    void onGetUbicacionesImagenesError(String title, String description);

    void onPostImageOK();
    void onPostImageError(String title, String description);
}
