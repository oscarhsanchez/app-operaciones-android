package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasImagenesListener;
import esocial.vallasmobile.listeners.OrdenesImagenesListener;
import esocial.vallasmobile.ws.request.PostIncidenciaImageRequest;
import esocial.vallasmobile.ws.request.PostOrdenImageRequest;
import esocial.vallasmobile.ws.response.PostIncidenciaImageResponse;
import esocial.vallasmobile.ws.response.PostOrderImageResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostIncidenciaImagenTask extends AsyncTask<Object, Integer, PostIncidenciaImageResponse> {

    private BaseActivity activity;
    private IncidenciasImagenesListener listener;


    public PostIncidenciaImagenTask(BaseActivity activity, String pk_incidencia, String name, Bitmap bitmap,
                                    IncidenciasImagenesListener listener) {
        this.activity = activity;
        this.listener = listener;

        execute(pk_incidencia, name, bitmap);
    }

    @Override
    protected PostIncidenciaImageResponse doInBackground(Object... params) {
        PostIncidenciaImageRequest request = new PostIncidenciaImageRequest((VallasApplication) activity.getApplicationContext());
        PostIncidenciaImageResponse response = request.execute((String)params[0], (String)params[1],
                (Bitmap)params[2], PostIncidenciaImageResponse.class);

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
            listener.onPostIncidenciaImageError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

