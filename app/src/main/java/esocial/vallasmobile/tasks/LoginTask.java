package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.os.AsyncTask;
import android.provider.Settings;

import java.util.Locale;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.LoginListener;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.request.PostLoginRequest;
import esocial.vallasmobile.ws.response.PostLoginResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class LoginTask extends AsyncTask<Object, Integer, PostLoginResponse> {

    private BaseActivity activity;
    private LoginListener listener;


    public LoginTask(BaseActivity activity, String username, String password, LoginListener listener) {
        this.activity = activity;
        this.listener = listener;

        String cCode;
        if(Constants.isDebug)
            cCode = "MX";
        else
            cCode = Locale.getDefault().getCountry();

        String deviceId = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        execute(username, password, cCode, deviceId);
    }

    @Override
    protected PostLoginResponse doInBackground(Object... params) {
        PostLoginRequest request = new PostLoginRequest((VallasApplication) activity.getApplicationContext());
        PostLoginResponse response = request.execute((String)params[0],(String)params[1],
                (String)params[2], (String)params[3], PostLoginResponse.class);

        return response;
    }

    protected void onPostExecute(PostLoginResponse response) {
        if (response != null) {
            if (!response.failed()) {
                activity.getVallasApplication().setSession(response.Session);
                listener.onLoginOK();
            } else {
                listener.onLoginError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onLoginError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

