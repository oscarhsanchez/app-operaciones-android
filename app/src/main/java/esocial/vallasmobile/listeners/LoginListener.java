package esocial.vallasmobile.listeners;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public interface LoginListener {

    void onLoginOK();
    void onLoginError(String title, String description);
}
