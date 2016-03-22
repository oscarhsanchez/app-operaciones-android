package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Ubicacion;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface UbicacionesModifyListener {

    void onPutUbicacionLocationOK();
    void onPutUbicacionLocationError(String title, String description);
}
