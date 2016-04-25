package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.UbicacionesListener;
import esocial.vallasmobile.ws.request.GetUbicacionesRequest;
import esocial.vallasmobile.ws.response.GetUbicacionesResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetUbicacionesTask extends AsyncTask<Object, Integer, GetUbicacionesResponse> {

    private Activity activity;
    private UbicacionesListener listener;


    public GetUbicacionesTask(Activity activity, String criteria, Integer from, Integer num, UbicacionesListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, criteria, from, num);
        else
            execute(criteria, from, num);
    }

    @Override
    protected GetUbicacionesResponse doInBackground(Object... params) {
        GetUbicacionesRequest request = new GetUbicacionesRequest((VallasApplication) activity.getApplicationContext());
        GetUbicacionesResponse response = request.execute((String)params[0], (Integer)params[1],
                (Integer)params[2], GetUbicacionesResponse.class);

        return response;
    }

    protected void onPostExecute(GetUbicacionesResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetUbicacionesOK(response.ubicaciones);
            } else {
                listener.onGetUbicacionesError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetUbicacionesError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

