package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Incidencia;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface IncidenciasListener {

    void onGetIncidenciasOK(ArrayList<Incidencia> incidencias);
    void onGetIncidenciasError(String title, String description);

    void onAddIncidenciaOK();
    void onAddIncidenciaError(String title, String description);
}
