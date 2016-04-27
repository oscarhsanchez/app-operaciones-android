package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasCreadasListener;
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.ws.request.GetIncidenciasAsignadasRequest;
import esocial.vallasmobile.ws.request.GetIncidenciasCreadasRequest;
import esocial.vallasmobile.ws.response.GetIncidenciasResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetIncidenciasCreadasTask extends AsyncTask<Object, Integer, GetIncidenciasResponse> {

    private Activity activity;
    private IncidenciasCreadasListener listener;


    public GetIncidenciasCreadasTask(Activity activity, String criteria, IncidenciasCreadasListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, criteria);
        else
            execute(criteria);
    }

    @Override
    protected GetIncidenciasResponse doInBackground(Object... params) {
        GetIncidenciasCreadasRequest request = new GetIncidenciasCreadasRequest((VallasApplication) activity.getApplicationContext());
        GetIncidenciasResponse response = request.execute((String) params[0], GetIncidenciasResponse.class);

        return response;
    }

    protected void onPostExecute(GetIncidenciasResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetIncidenciasCreadasOK(response.incidencias);
            } else {
                listener.onGetIncidenciasCreadasError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetIncidenciasCreadasError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

