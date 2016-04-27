package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesImagenesListener;
import esocial.vallasmobile.obj.OrdenImagen;
import esocial.vallasmobile.ws.request.PostOrdenImageRequest;
import esocial.vallasmobile.ws.response.PostOrderImageResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PostOrdenImagenTask extends AsyncTask<Object, Integer, PostOrderImageResponse> {

    private Context application;
    private OrdenesImagenesListener listener;


    public PostOrdenImagenTask(Context activity, OrdenImagen eSend,
                               OrdenesImagenesListener listener) {
        this.application = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, eSend);
        else
            execute(eSend);
    }

    @Override
    protected PostOrderImageResponse doInBackground(Object... params) {
        PostOrdenImageRequest request = new PostOrdenImageRequest((VallasApplication) application.getApplicationContext());
        PostOrderImageResponse response = request.execute((OrdenImagen) params[0], PostOrderImageResponse.class);

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
            listener.onPostOrderImageError(application.getString(R.string.opps), application.getString(R.string.check_connection));
        }
    }
}

