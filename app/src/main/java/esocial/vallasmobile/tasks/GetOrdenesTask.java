package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesListener;
import esocial.vallasmobile.ws.request.GetOrdenesRequest;
import esocial.vallasmobile.ws.response.GetOrdenesResponse;


/**
 * Created by jesus.martinez on 01/02/2016.
 */
public class GetOrdenesTask extends AsyncTask<Object, Integer, GetOrdenesResponse> {

    private Activity activity;
    private OrdenesListener listener;


    public GetOrdenesTask(Activity activity, String criteria, OrdenesListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, criteria);
        else
            execute(criteria);
    }

    @Override
    protected GetOrdenesResponse doInBackground(Object... params) {
        GetOrdenesRequest request = new GetOrdenesRequest((VallasApplication) activity.getApplicationContext());
        GetOrdenesResponse response = request.execute((String) params[0], GetOrdenesResponse.class);

        return response;
    }

    protected void onPostExecute(GetOrdenesResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetOrdersOK(response.ordenes);
            } else {
                listener.onGetOrdersError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetOrdersError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

