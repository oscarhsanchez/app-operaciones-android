package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasModifyListener;
import esocial.vallasmobile.listeners.OrdenesModifyListener;
import esocial.vallasmobile.ws.request.GetIncidenciaRequest;
import esocial.vallasmobile.ws.request.GetOrdenRequest;
import esocial.vallasmobile.ws.response.GetIncidenciasResponse;
import esocial.vallasmobile.ws.response.GetOrdenesResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetIncidenciaTask extends AsyncTask<Object, Integer, GetIncidenciasResponse> {

    private Activity activity;
    private IncidenciasModifyListener listener;


    public GetIncidenciaTask(Activity activity, String pk_incidencia, IncidenciasModifyListener listener) {
        this.activity = activity;
        this.listener = listener;

        execute(pk_incidencia);
    }

    @Override
    protected GetIncidenciasResponse doInBackground(Object... params) {
        GetIncidenciaRequest request = new GetIncidenciaRequest((VallasApplication) activity.getApplicationContext());
        GetIncidenciasResponse response = request.execute((String) params[0], GetIncidenciasResponse.class);

        return response;
    }

    protected void onPostExecute(GetIncidenciasResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetIncidenciaOK(response.incidencias.get(0));
            } else {
                listener.onGetIncidenciaError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetIncidenciaError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

