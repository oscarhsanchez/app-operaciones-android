package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Orden;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface OrdenesListener {

    void onGetOrdersOK(ArrayList<Orden> orders);
    void onGetOrdersError(String title, String description);
}
