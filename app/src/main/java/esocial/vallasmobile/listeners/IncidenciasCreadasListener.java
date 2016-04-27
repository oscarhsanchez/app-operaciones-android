package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Incidencia;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface IncidenciasCreadasListener {

    void onGetIncidenciasCreadasOK(ArrayList<Incidencia> incidencias);
    void onGetIncidenciasCreadasError(String title, String description);

}
