package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.OrdenImagen;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface OrdenesImagenesListener {

    void onGetOrdenesImagenesOK(ArrayList<OrdenImagen> images);
    void onGetOrdenesImagenesError(String title, String description);

    void onPostOrderImageOK();
    void onPostOrderImageError(String title, String description);
}
