package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesImagenesListener;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.ws.request.PostOrdenImageRequest;
import esocial.vallasmobile.ws.request.PostUbicacionImageRequest;
import esocial.vallasmobile.ws.response.PostOrderImageResponse;
import esocial.vallasmobile.ws.response.PostUbicacionImageResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostOrdenImagenTask extends AsyncTask<Object, Integer, PostOrderImageResponse> {

    private BaseActivity activity;
    private OrdenesImagenesListener listener;


    public PostOrdenImagenTask(BaseActivity activity, String pk_orden_trabajo, String name, Bitmap bitmap,
                               OrdenesImagenesListener listener) {
        this.activity = activity;
        this.listener = listener;

        execute(pk_orden_trabajo, name, bitmap);
    }

    @Override
    protected PostOrderImageResponse doInBackground(Object... params) {
        PostOrdenImageRequest request = new PostOrdenImageRequest((VallasApplication) activity.getApplicationContext());
        PostOrderImageResponse response = request.execute((String)params[0], (String)params[1], (Bitmap)params[2], PostOrderImageResponse.class);

        return response;
    }

    protected void onPostExecute(PostOrderImageResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onPostOrderImageOK();
            } else {
                listener.onPostOrderImageError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onPostOrderImageError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

