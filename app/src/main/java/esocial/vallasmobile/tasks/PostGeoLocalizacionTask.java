package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.os.AsyncTask;
import android.os.Build;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.GeoLocalizacionListener;
import esocial.vallasmobile.obj.GeoLocalizacion;
import esocial.vallasmobile.ws.request.PostGeoRequest;
import esocial.vallasmobile.ws.response.PostGeoResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostGeoLocalizacionTask extends AsyncTask<Object, Integer, PostGeoResponse> {

    private VallasApplication application;
    private GeoLocalizacionListener listener;


    public PostGeoLocalizacionTask(VallasApplication application, ArrayList<GeoLocalizacion> localizacion, GeoLocalizacionListener listener) {
        this.application = application;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, localizacion);
        else
            execute(localizacion);
    }

    @Override
    protected PostGeoResponse doInBackground(Object... params) {
        PostGeoRequest request = new PostGeoRequest(application);
        PostGeoResponse response = request.execute((ArrayList<GeoLocalizacion>)params[0], PostGeoResponse.class);

        return response;
    }

    protected void onPostExecute(PostGeoResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGeoLocalizacionOK();
            } else {
                listener.onGeoLocalizacionError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGeoLocalizacionError(application.getString(R.string.opps), application.getString(R.string.check_connection));
        }
    }
}

