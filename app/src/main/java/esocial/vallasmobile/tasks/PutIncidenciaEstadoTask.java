package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.ws.request.PutIncidenciaRequest;
import esocial.vallasmobile.ws.response.PutIncidenciaResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PutIncidenciaEstadoTask extends AsyncTask<Object, Integer, PutIncidenciaResponse> {

    private VallasApplication activity;


    public PutIncidenciaEstadoTask(VallasApplication activity, String pk_incidencia, Integer estado,
                                   String observaciones) {
        this.activity = activity;

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
                Log.d("PUT INCIDENCIA ESTADO", "RESPONSE OK");
            } else {
                Log.d("PUT INCIDENCIA ESTADO", "Error " + response.error.code + " --Descripcion:"+ response.error.description);
                Toast.makeText(VallasApplication.context, "Error " + response.error.code +
                        " al cambiar estado de la orden", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("PUT INCIDENCIA ESTADO", "Error " +activity.getString(R.string.opps) +
                    " --Descripcion:"+ activity.getString(R.string.check_connection));
            Toast.makeText(VallasApplication.context, "Error inesperado" +
                    " al cambiar estado de la orden", Toast.LENGTH_LONG).show();
        }
    }
}

