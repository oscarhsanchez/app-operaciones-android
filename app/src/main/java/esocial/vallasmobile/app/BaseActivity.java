package esocial.vallasmobile.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public VallasApplication getVallasApplication(){
        return (VallasApplication) getApplicationContext();
    }

}
