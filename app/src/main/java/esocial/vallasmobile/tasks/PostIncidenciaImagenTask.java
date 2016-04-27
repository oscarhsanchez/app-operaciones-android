package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasImagenesListener;
import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.ws.request.PostIncidenciaImageRequest;
import esocial.vallasmobile.ws.response.PostIncidenciaImageResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostIncidenciaImagenTask extends AsyncTask<Object, Integer, PostIncidenciaImageResponse> {

    private Context application;
    private IncidenciasImagenesListener listener;


    public PostIncidenciaImagenTask(Context activity, IncidenciaImagen eSend,
                                    IncidenciasImagenesListener listener) {
        this.application = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, eSend);
        else
            execute(eSend);
    }

    @Override
    protected PostIncidenciaImageResponse doInBackground(Object... params) {
        PostIncidenciaImageRequest request = new PostIncidenciaImageRequest((VallasApplication) application.getApplicationContext());
        PostIncidenciaImageResponse response = request.execute((IncidenciaImagen)params[0], PostIncidenciaImageResponse.class);

        return response;
    }

    protected void onPostExecute(PostIncidenciaImageResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onPostIncidenciaImageOK();
            } else {
                listener.onPostIncidenciaImageError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onPostIncidenciaImageError(application.getString(R.string.opps),
                    application.getString(R.string.check_connection));
        }
    }
}

