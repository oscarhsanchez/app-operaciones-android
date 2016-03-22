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
import esocial.vallasmobile.listeners.RenewTokenListener;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.ws.request.PostLoginRequest;
import esocial.vallasmobile.ws.request.RenewTokenRequest;
import esocial.vallasmobile.ws.response.PostLoginResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class RenewTokenTask extends AsyncTask<Object, Integer, PostLoginResponse> {

    private BaseActivity activity;
    private RenewTokenListener listener;


    public RenewTokenTask(BaseActivity activity,  RenewTokenListener listener) {
        this.activity = activity;
        this.listener = listener;

        execute();
    }

    @Override
    protected PostLoginResponse doInBackground(Object... params) {
        RenewTokenRequest request = new RenewTokenRequest((VallasApplication) activity.getApplicationContext());
        PostLoginResponse response = request.execute(PostLoginResponse.class);

        return response;
    }

    protected void onPostExecute(PostLoginResponse response) {
        if (response != null) {
            if (!response.failed()) {
                activity.getVallasApplication().setSession(response.Session);
                listener.onRenewTokenOK();
            } else {
                listener.onRenewTokenError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onRenewTokenError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

