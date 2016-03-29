package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.obj.OrdenImagen;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface IncidenciasImagenesListener {

    void onGetIncidenciasImagenesOK(ArrayList<IncidenciaImagen> images);
    void onGetIncidenciasImagenesError(String title, String description);

    void onPostIncidenciaImageOK();
    void onPostIncidenciaImageError(String title, String description);
}
