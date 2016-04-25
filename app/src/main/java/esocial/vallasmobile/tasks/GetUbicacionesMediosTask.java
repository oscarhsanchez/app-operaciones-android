package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.MediosListener;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.ws.request.GetUbicacionesImagenesRequest;
import esocial.vallasmobile.ws.request.GetUbicacionesMediosRequest;
import esocial.vallasmobile.ws.response.GetUbicacionesImagenesResponse;
import esocial.vallasmobile.ws.response.GetUbicacionesMediosResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetUbicacionesMediosTask extends AsyncTask<Object, Integer, GetUbicacionesMediosResponse> {

    private Activity activity;
    private MediosListener listener;


    public GetUbicacionesMediosTask(Activity activity, String pk_ubicacion, MediosListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_ubicacion);
        else
            execute(pk_ubicacion);
    }

    @Override
    protected GetUbicacionesMediosResponse doInBackground(Object... params) {
        GetUbicacionesMediosRequest request = new GetUbicacionesMediosRequest((VallasApplication) activity.getApplicationContext());
        GetUbicacionesMediosResponse response = request.execute((String)params[0], GetUbicacionesMediosResponse.class);

        return response;
    }

    protected void onPostExecute(GetUbicacionesMediosResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetMediosOK(response.medios);
            } else {
                listener.onGetMediosError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetMediosError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

