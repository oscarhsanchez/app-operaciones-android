package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Order;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface OrdenesListener {

    void onGetOrdersOK(ArrayList<Order> orders);
    void onGetOrdersError(String title, String description);
}
