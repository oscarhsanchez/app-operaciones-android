package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.ws.request.GetIncidenciasRequest;
import esocial.vallasmobile.ws.response.GetIncidenciasResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetIncidenciasTask extends AsyncTask<Object, Integer, GetIncidenciasResponse> {

    private Activity activity;
    private IncidenciasListener listener;


    public GetIncidenciasTask(Activity activity, IncidenciasListener listener) {
        this.activity = activity;
        this.listener = listener;

        execute();
    }

    @Override
    protected GetIncidenciasResponse doInBackground(Object... params) {
        GetIncidenciasRequest request = new GetIncidenciasRequest((VallasApplication) activity.getApplicationContext());
        GetIncidenciasResponse response = request.execute(GetIncidenciasResponse.class);

        return response;
    }

    protected void onPostExecute(GetIncidenciasResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetIncidenciasOK(response.incidencias);
            } else {
                listener.onGetIncidenciasError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetIncidenciasError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

