package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.UbicacionesModifyListener;
import esocial.vallasmobile.ws.request.PutUbicacionRequest;
import esocial.vallasmobile.ws.response.PutUbicacionResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PutUbicacionLocationTask extends AsyncTask<Object, Integer, PutUbicacionResponse> {

    private BaseActivity activity;
    private UbicacionesModifyListener listener;


    public PutUbicacionLocationTask(BaseActivity activity, String pk_ubicacion, Double latitude, Double longitude,
                                    UbicacionesModifyListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_ubicacion, latitude, longitude);
        else
            execute(pk_ubicacion, latitude, longitude);
    }

    @Override
    protected PutUbicacionResponse doInBackground(Object... params) {
        PutUbicacionRequest request = new PutUbicacionRequest((VallasApplication) activity.getApplicationContext());
        PutUbicacionResponse response = request.executeLocation((String)params[0], (Double) params[1],
                (Double) params[2], PutUbicacionResponse.class);

        return response;
    }

    protected void onPostExecute(PutUbicacionResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onPutUbicacionLocationOK();
            } else {
                listener.onPutUbicacionLocationError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onPutUbicacionLocationError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

