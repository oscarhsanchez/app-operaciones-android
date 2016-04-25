package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.ws.request.PostIncidenciaRequest;
import esocial.vallasmobile.ws.request.PostUbicacionImageRequest;
import esocial.vallasmobile.ws.response.PostIncidenciaResponse;
import esocial.vallasmobile.ws.response.PostUbicacionImageResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostIncidenciaTask extends AsyncTask<Object, Integer, PostIncidenciaResponse> {

    private Activity activity;
    private IncidenciasListener listener;


    public PostIncidenciaTask(Activity activity, Incidencia incidencia, IncidenciasListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, incidencia);
        else
            execute(incidencia);
    }

    @Override
    protected PostIncidenciaResponse doInBackground(Object... params) {
        PostIncidenciaRequest request = new PostIncidenciaRequest((VallasApplication) activity.getApplicationContext());
        PostIncidenciaResponse response = request.execute((Incidencia)params[0], PostIncidenciaResponse.class);

        return response;
    }

    protected void onPostExecute(PostIncidenciaResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onAddIncidenciaOK();
            } else {
                listener.onAddIncidenciaError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onAddIncidenciaError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

