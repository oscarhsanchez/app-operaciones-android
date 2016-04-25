package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasImagenesListener;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.ws.request.GetIncidenciasImagenesRequest;
import esocial.vallasmobile.ws.request.GetUbicacionesImagenesRequest;
import esocial.vallasmobile.ws.response.GetIncidenciasImagenesResponse;
import esocial.vallasmobile.ws.response.GetUbicacionesImagenesResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetIncidenciasImagenesTask extends AsyncTask<Object, Integer, GetIncidenciasImagenesResponse> {

    private Activity activity;
    private IncidenciasImagenesListener listener;


    public GetIncidenciasImagenesTask(Activity activity, String pk_incidencia, IncidenciasImagenesListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_incidencia);
        else
            execute(pk_incidencia);
    }

    @Override
    protected GetIncidenciasImagenesResponse doInBackground(Object... params) {
        GetIncidenciasImagenesRequest request = new GetIncidenciasImagenesRequest((VallasApplication) activity.getApplicationContext());
        GetIncidenciasImagenesResponse response = request.execute((String)params[0], GetIncidenciasImagenesResponse.class);

        return response;
    }

    protected void onPostExecute(GetIncidenciasImagenesResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetIncidenciasImagenesOK(response.imagenes);
            } else {
                listener.onGetIncidenciasImagenesError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetIncidenciasImagenesError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

