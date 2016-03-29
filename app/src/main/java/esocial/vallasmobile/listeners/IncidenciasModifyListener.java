package esocial.vallasmobile.listeners;

import esocial.vallasmobile.obj.Incidencia;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface IncidenciasModifyListener {

    void onPutIncidenciaOK();
    void onPutIncidenciaError(String title, String description);

    void onGetIncidenciaOK(Incidencia incidencia);
    void onGetIncidenciaError(String title, String description);
}
