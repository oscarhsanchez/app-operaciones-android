package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.ws.request.PostUbicacionImageRequest;
import esocial.vallasmobile.ws.response.PostUbicacionImageResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostUbicacionImagenTask extends AsyncTask<Object, Integer, PostUbicacionImageResponse> {

    private BaseActivity activity;
    private UbicacionesImagenesListener listener;


    public PostUbicacionImagenTask(BaseActivity activity,String pk_ubicacion, String name, Bitmap bitmap,
                                   UbicacionesImagenesListener listener) {
        this.activity = activity;
        this.listener = listener;

        execute(pk_ubicacion, name, bitmap);
    }

    @Override
    protected PostUbicacionImageResponse doInBackground(Object... params) {
        PostUbicacionImageRequest request = new PostUbicacionImageRequest((VallasApplication) activity.getApplicationContext());
        PostUbicacionImageResponse response = request.execute((String)params[0], (String)params[1], (Bitmap)params[2], PostUbicacionImageResponse.class);

        return response;
    }

    protected void onPostExecute(PostUbicacionImageResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onPostImageOK();
            } else {
                listener.onPostImageError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onPostImageError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

