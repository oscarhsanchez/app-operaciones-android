package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasAsignadasListener;
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.ws.request.GetIncidenciasAsignadasRequest;
import esocial.vallasmobile.ws.response.GetIncidenciasResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetIncidenciasAsignadasTask extends AsyncTask<Object, Integer, GetIncidenciasResponse> {

    private Activity activity;
    private IncidenciasAsignadasListener listener;


    public GetIncidenciasAsignadasTask(Activity activity, String criteria, Location location,
                                       IncidenciasAsignadasListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, criteria, location);
        else
            execute(criteria, location);
    }

    @Override
    protected GetIncidenciasResponse doInBackground(Object... params) {
        GetIncidenciasAsignadasRequest request = new GetIncidenciasAsignadasRequest(
                (VallasApplication) activity.getApplicationContext());
        GetIncidenciasResponse response = request.execute((String) params[0],(Location) params[1], GetIncidenciasResponse.class);

        return response;
    }

    protected void onPostExecute(GetIncidenciasResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetIncidenciasAsignadasOK(response.incidencias);
            } else {
                listener.onGetIncidenciasAsignadasError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetIncidenciasAsignadasError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

