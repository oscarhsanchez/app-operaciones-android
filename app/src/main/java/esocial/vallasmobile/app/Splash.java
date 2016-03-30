package esocial.vallasmobile.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import esocial.vallasmobile.R;
import esocial.vallasmobile.listeners.RenewTokenListener;
import esocial.vallasmobile.tasks.RenewTokenTask;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class Splash extends BaseActivity implements RenewTokenListener {

    private ImageView logo;
    private ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        logo = (ImageView) findViewById(R.id.splash_logo);
        background = (ImageView) findViewById(R.id.splash_bg);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getVallasApplication().getSession() != null) {

                    new RenewTokenTask(Splash.this, Splash.this);
                } else {
                    goToLogin();
                }
            }
        }, 1500);

    }

    @Override
    public void onRenewTokenOK() {
        goToMainActivity();
    }

    @Override
    public void onRenewTokenError(String title, String description) {
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(Splash.this, Login.class);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create((View) background, "background"),
                    Pair.create((View) logo, "logoTransition"));
            startActivity(intent, options.toBundle());
            finishAfterTransition();
        } else {
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(Splash.this, MainTabActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
