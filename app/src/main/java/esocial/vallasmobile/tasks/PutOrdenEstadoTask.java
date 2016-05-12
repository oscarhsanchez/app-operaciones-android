package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesModifyListener;
import esocial.vallasmobile.utils.Dialogs;
import esocial.vallasmobile.ws.request.PutOrdenRequest;
import esocial.vallasmobile.ws.response.PutOrdenResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PutOrdenEstadoTask extends AsyncTask<Object, Integer, PutOrdenResponse> {

    private VallasApplication activity;


    public PutOrdenEstadoTask(VallasApplication activity, String pk_ubicacion, Integer estado,
                              String observaciones) {
        this.activity = activity;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_ubicacion, estado, observaciones);
        else
            execute(pk_ubicacion, estado, observaciones);
    }

    @Override
    protected PutOrdenResponse doInBackground(Object... params) {
        PutOrdenRequest request = new PutOrdenRequest(activity);
        PutOrdenResponse response = request.execute((String) params[0], (Integer) params[1], (String) params[2],
                PutOrdenResponse.class);

        return response;
    }

    protected void onPostExecute(PutOrdenResponse response) {
        if (response != null) {
            if (!response.failed()) {
                Log.d("PUT ORDEN ESTADO TASK", "RESPONSE OK");
            } else {
                Log.d("PUT ORDEN ESTADO TASK", "Error " + response.error.code + " --Descripcion:"+ response.error.description);
                Toast.makeText(VallasApplication.context, "Error " + response.error.code +
                        " al cambiar estado de la orden", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("PUT ORDEN ESTADO TASK", "Error " +activity.getString(R.string.opps) +
                    " --Descripcion:"+ activity.getString(R.string.check_connection));
            Toast.makeText(VallasApplication.context, "Error inesperado" +
                    " al cambiar estado de la orden", Toast.LENGTH_LONG).show();
        }
    }
}

