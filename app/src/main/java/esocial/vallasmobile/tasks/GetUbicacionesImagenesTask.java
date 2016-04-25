package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.listeners.UbicacionesListener;
import esocial.vallasmobile.ws.request.GetUbicacionesImagenesRequest;
import esocial.vallasmobile.ws.request.GetUbicacionesRequest;
import esocial.vallasmobile.ws.response.GetUbicacionesImagenesResponse;
import esocial.vallasmobile.ws.response.GetUbicacionesResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetUbicacionesImagenesTask extends AsyncTask<Object, Integer, GetUbicacionesImagenesResponse> {

    private Activity activity;
    private UbicacionesImagenesListener listener;


    public GetUbicacionesImagenesTask(Activity activity, String pk_ubicacion, UbicacionesImagenesListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_ubicacion);
        else
            execute(pk_ubicacion);
    }

    @Override
    protected GetUbicacionesImagenesResponse doInBackground(Object... params) {
        GetUbicacionesImagenesRequest request = new GetUbicacionesImagenesRequest((VallasApplication) activity.getApplicationContext());
        GetUbicacionesImagenesResponse response = request.execute((String)params[0], GetUbicacionesImagenesResponse.class);

        return response;
    }

    protected void onPostExecute(GetUbicacionesImagenesResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetUbicacionesImagenesOK(response.imagenes);
            } else {
                listener.onGetUbicacionesImagenesError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetUbicacionesImagenesError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

