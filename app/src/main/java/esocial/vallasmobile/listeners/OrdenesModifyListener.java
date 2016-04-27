package esocial.vallasmobile.listeners;

import esocial.vallasmobile.obj.Orden;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface OrdenesModifyListener {

    void onPutOrdenEstadoOK();
    void onPutOrdenEstadoError(String title, String description);

    void onPutOrdenMotivoOK();
    void onPutOrdenMotivoError(String title, String description);

    void onGetOrdenOK(Orden orden);
    void onGetOrdenError(String title, String description);
}
