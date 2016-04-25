package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasModifyListener;
import esocial.vallasmobile.listeners.OrdenesModifyListener;
import esocial.vallasmobile.ws.request.PutIncidenciaRequest;
import esocial.vallasmobile.ws.request.PutOrdenRequest;
import esocial.vallasmobile.ws.response.PutIncidenciaResponse;
import esocial.vallasmobile.ws.response.PutOrdenResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PutIncidenciaEstadoTask extends AsyncTask<Object, Integer, PutIncidenciaResponse> {

    private BaseActivity activity;
    private IncidenciasModifyListener listener;


    public PutIncidenciaEstadoTask(BaseActivity activity, String pk_incidencia, Integer estado,
                                   String observaciones, IncidenciasModifyListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_incidencia, estado, observaciones);
        else
            execute(pk_incidencia, estado, observaciones);
    }

    @Override
    protected PutIncidenciaResponse doInBackground(Object... params) {
        PutIncidenciaRequest request = new PutIncidenciaRequest((VallasApplication) activity.getApplicationContext());
        PutIncidenciaResponse response = request.execute((String) params[0], (Integer) params[1], (String) params[2],
                PutIncidenciaResponse.class);

        return response;
    }

    protected void onPostExecute(PutIncidenciaResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onPutIncidenciaOK();
            } else {
                listener.onPutIncidenciaError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onPutIncidenciaError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

