package esocial.vallasmobile.listeners;

import java.util.ArrayList;

import esocial.vallasmobile.obj.Medio;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface MediosListener {

    void onGetMediosOK(ArrayList<Medio> medios);
    void onGetMediosError(String title, String description);
}
