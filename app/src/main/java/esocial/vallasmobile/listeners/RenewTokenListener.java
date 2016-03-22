package esocial.vallasmobile.listeners;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface RenewTokenListener {

    void onRenewTokenOK();
    void onRenewTokenError(String title, String description);
}
