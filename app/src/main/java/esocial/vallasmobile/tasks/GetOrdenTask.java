package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesListener;
import esocial.vallasmobile.listeners.OrdenesModifyListener;
import esocial.vallasmobile.ws.request.GetOrdenRequest;
import esocial.vallasmobile.ws.request.GetOrdenesRequest;
import esocial.vallasmobile.ws.response.GetOrdenesResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetOrdenTask extends AsyncTask<Object, Integer, GetOrdenesResponse> {

    private Activity activity;
    private OrdenesModifyListener listener;


    public GetOrdenTask(Activity activity, String pk_orden_trabajo, OrdenesModifyListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pk_orden_trabajo);
        else
            execute(pk_orden_trabajo);
    }

    @Override
    protected GetOrdenesResponse doInBackground(Object... params) {
        GetOrdenRequest request = new GetOrdenRequest((VallasApplication) activity.getApplicationContext());
        GetOrdenesResponse response = request.execute((String) params[0], GetOrdenesResponse.class);

        return response;
    }

    protected void onPostExecute(GetOrdenesResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetOrdenOK(response.ordenes.get(0));
            } else {
                listener.onGetOrdenError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetOrdenError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

