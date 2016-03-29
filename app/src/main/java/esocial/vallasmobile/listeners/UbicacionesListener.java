package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Ubicacion;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface UbicacionesListener {

    void onGetUbicacionesOK(ArrayList<Ubicacion> ubicaciones);
    void onGetUbicacionesError(String title, String description);
}
