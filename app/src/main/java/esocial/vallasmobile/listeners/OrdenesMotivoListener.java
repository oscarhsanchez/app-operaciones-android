package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Motivo;
import esocial.vallasmobile.obj.Orden;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface OrdenesMotivoListener {

    void onGetOrdenesMotivoOK(ArrayList<Motivo> motivos);
    void onGetOrdenesMotivoError(String title, String description);
}
