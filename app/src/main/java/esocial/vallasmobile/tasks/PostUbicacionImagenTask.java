package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.IncidenciasImagenesListener;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.ws.request.PostUbicacionImageRequest;
import esocial.vallasmobile.ws.response.PostUbicacionImageResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostUbicacionImagenTask extends AsyncTask<Object, Integer, PostUbicacionImageResponse> {

    private VallasApplication application;
    private UbicacionesImagenesListener listener;


    public PostUbicacionImagenTask(VallasApplication application, UbicacionImagen eSend,
                                   UbicacionesImagenesListener listener) {
        this.application = application;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, eSend);
        else
            execute(eSend);
    }

    @Override
    protected PostUbicacionImageResponse doInBackground(Object... params) {
        PostUbicacionImageRequest request = new PostUbicacionImageRequest(application);
        PostUbicacionImageResponse response = request.execute((UbicacionImagen)params[0], PostUbicacionImageResponse.class);

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
            listener.onPostImageError(application.getString(R.string.opps), application.getString(R.string.check_connection));
        }
    }
}

