package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Incidencia;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface IncidenciasListener {

    void onGetIncidentsOK(ArrayList<Incidencia> incidents);
    void onGetIncidentsError(String title, String description);
}
