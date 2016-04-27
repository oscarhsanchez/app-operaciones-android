package esocial.vallasmobile.tasks;

/**
 * Created by jesus.martinez on 21/03/2016.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.OrdenesMotivoListener;
import esocial.vallasmobile.ws.request.GetOrdenesMotivosRequest;
import esocial.vallasmobile.ws.response.GetOrdenesMotivosResponse;


/**
 * Created by jesus.martinez on 27/04/2016.
 */
public class GetOrdenesMotivosTask extends AsyncTask<Object, Integer, GetOrdenesMotivosResponse> {

    private Activity activity;
    private OrdenesMotivoListener listener;


    public GetOrdenesMotivosTask(Activity activity, OrdenesMotivoListener listener) {
        this.activity = activity;
        this.listener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            execute();
    }

    @Override
    protected GetOrdenesMotivosResponse doInBackground(Object... params) {
        GetOrdenesMotivosRequest request = new GetOrdenesMotivosRequest((VallasApplication) activity.getApplicationContext());
        GetOrdenesMotivosResponse response = request.execute(GetOrdenesMotivosResponse.class);

        return response;
    }

    protected void onPostExecute(GetOrdenesMotivosResponse response) {
        if (response != null) {
            if (!response.failed()) {
                listener.onGetOrdenesMotivoOK(response.motivos);
            } else {
                listener.onGetOrdenesMotivoError("Error " + response.error.code, response.error.description);
            }
        } else {
            listener.onGetOrdenesMotivoError(activity.getString(R.string.opps), activity.getString(R.string.check_connection));
        }
    }
}

