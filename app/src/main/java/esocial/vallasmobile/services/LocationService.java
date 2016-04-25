package esocial.vallasmobile.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by jesus.martinez on 25/04/2016.
 */
public class LocationService extends Service {

    private final IBinder mBinder = new MyBinder();
    public AppLocationManager locationManager;

    @Override
    public void onCreate()
    {
        super.onCreate();
        locationManager = AppLocationManager.getLocationManager(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.stopLocationManager();
    }
}
