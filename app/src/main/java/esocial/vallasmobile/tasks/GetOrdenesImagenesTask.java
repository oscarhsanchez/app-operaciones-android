package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesImagenesListener;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.ws.request.GetOrdenesImagenesRequest;
import esocial.vallasmobile.ws.request.GetUbicacionesImagenesRequest;
import esocial.vallasmobile.ws.response.GetOrdenesImagenesResponse;
import esocial.vallasmobile.ws.response.GetUbicacionesImagenesResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetOrdenesImagenesTask extends AsyncTask<Object, Integer, GetOrdenesImagenesResponse> {

    private Activity activity;
    private OrdenesImagenesListener listener;


    public GetOrdenesImagenesTask(Activity activity, String pk_orden_trabajo, OrdenesImagenesListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_orden_trabajo);
        else
            execute(pk_orden_trabajo);
    }

    @Override
    protected GetOrdenesImagenesResponse doInBackground(Object... params) {
        GetOrdenesImagenesRequest request = new GetOrdenesImagenesRequest((VallasApplication) activity.getApplicationContext());
        GetOrdenesImagenesResponse response = request.execute((String)params[0], GetOrdenesImagenesResponse.class);

        return response;
    }

    protected void onPostExecute(GetOrdenesImagenesResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetOrdenesImagenesOK(response.imagenes);
            } else {
                listener.onGetOrdenesImagenesError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetOrdenesImagenesError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

