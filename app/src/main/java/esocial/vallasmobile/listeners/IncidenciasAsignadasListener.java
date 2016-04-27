package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Incidencia;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface IncidenciasAsignadasListener {

    void onGetIncidenciasAsignadasOK(ArrayList<Incidencia> incidencias);
    void onGetIncidenciasAsignadasError(String title, String description);

}
