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
import esocial.vallasmobile.ws.request.PutOrdenMotivoRequest;
import esocial.vallasmobile.ws.response.PutOrdenResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PutOrdenMotivoTask extends AsyncTask<Object, Integer, PutOrdenResponse> {

    private VallasApplication activity;


    public PutOrdenMotivoTask(VallasApplication activity, String pk_orden, String pk_motivo) {
        this.activity = activity;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_orden, pk_motivo);
        else
            execute(pk_orden, pk_motivo);
    }

    @Override
    protected PutOrdenResponse doInBackground(Object... params) {
        PutOrdenMotivoRequest request = new PutOrdenMotivoRequest(activity);
        PutOrdenResponse response = request.execute((String) params[0], (String) params[1],
                PutOrdenResponse.class);

        return response;
    }

    protected void onPostExecute(PutOrdenResponse response) {
        if (response != null) {
            if (!response.failed()) {
                Log.d("PUT ORDEN MOTIVO TASK", "RESPONSE OK");
            } else {
                Log.d("PUT ORDEN MOTIVO TASK", "Error " + response.error.code + " --Descripcion:"+ response.error.description);
                Toast.makeText(VallasApplication.context, "Error " + response.error.code +
                        " al cambiar estado de la orden", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("PUT ORDEN MOTIVO TASK", "Error " +activity.getString(R.string.opps) +
                    " --Descripcion:"+ activity.getString(R.string.check_connection));
            Toast.makeText(VallasApplication.context, "Error inesperado" +
                    " al cambiar estado de la orden", Toast.LENGTH_LONG).show();
        }
    }
}

