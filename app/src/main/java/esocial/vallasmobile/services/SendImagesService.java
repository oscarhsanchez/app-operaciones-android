package esocial.vallasmobile.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import esocial.vallasmobile.app.VallasApplication;

/**
 * Created by jesus.martinez on 25/04/2016.
 */
public class SendImagesService extends IntentService {

    public SendImagesService(){
        super("SendImageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        Boolean showMessage = false;
        if(extras!=null){
            showMessage = extras.getBoolean("showMessage", false);
        }

        if(VallasApplication.sender!=null)
            VallasApplication.sender.sendImages(showMessage);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
    }
}
