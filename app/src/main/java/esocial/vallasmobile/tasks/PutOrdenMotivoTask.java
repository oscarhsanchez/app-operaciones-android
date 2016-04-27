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
import esocial.vallasmobile.ws.request.PutOrdenMotivoRequest;
import esocial.vallasmobile.ws.response.PutOrdenResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class PutOrdenMotivoTask extends AsyncTask<Object, Integer, PutOrdenResponse> {

    private BaseActivity activity;
    private OrdenesModifyListener listener;


    public PutOrdenMotivoTask(BaseActivity activity, String pk_orden, String pk_motivo,
                              OrdenesModifyListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_orden, pk_motivo);
        else
            execute(pk_orden, pk_motivo);
    }

    @Override
    protected PutOrdenResponse doInBackground(Object... params) {
        PutOrdenMotivoRequest request = new PutOrdenMotivoRequest((VallasApplication) activity.getApplicationContext());
        PutOrdenResponse response = request.execute((String) params[0], (String) params[1],
                PutOrdenResponse.class);

        return response;
    }

    protected void onPostExecute(PutOrdenResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onPutOrdenMotivoOK();
            } else {
                listener.onPutOrdenMotivoError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onPutOrdenMotivoError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

