package esocial.vallasmobile.listeners;

import esocial.vallasmobile.obj.Incidencia;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface IncidenciasModifyListener {

    void onGetIncidenciaOK(Incidencia incidencia);
    void onGetIncidenciaError(String title, String description);
}
