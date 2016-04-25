package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesModifyListener;
import esocial.vallasmobile.ws.request.PutOrdenRequest;
import esocial.vallasmobile.ws.response.PutOrdenResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PutOrdenEstadoTask extends AsyncTask<Object, Integer, PutOrdenResponse> {

    private BaseActivity activity;
    private OrdenesModifyListener listener;


    public PutOrdenEstadoTask(BaseActivity activity, String pk_ubicacion, Integer estado,
                              String observaciones, OrdenesModifyListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_ubicacion, estado, observaciones);
        else
            execute(pk_ubicacion, estado, observaciones);
    }

    @Override
    protected PutOrdenResponse doInBackground(Object... params) {
        PutOrdenRequest request = new PutOrdenRequest((VallasApplication) activity.getApplicationContext());
        PutOrdenResponse response = request.execute((String) params[0], (Integer) params[1], (String) params[2],
                PutOrdenResponse.class);

        return response;
    }

    protected void onPostExecute(PutOrdenResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onPutOrdenOK();
            } else {
                listener.onPutOrdenError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onPutOrdenError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

