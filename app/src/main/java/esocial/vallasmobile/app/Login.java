package esocial.vallasmobile.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import esocial.vallasmobile.R;
import esocial.vallasmobile.listeners.LoginListener;
import esocial.vallasmobile.tasks.LoginTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class Login extends BaseActivity implements LoginListener{

    private EditText etUsername, etPass;
    private TextView tvRecoverPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPass = (EditText) findViewById(R.id.et_pass);
        tvRecoverPass = (TextView) findViewById(R.id.tv_recover_pass);

        if(Constants.isDebug){
            etUsername.setText("admin");
            etPass.setText("123");
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition enterTrans = new Fade();
            enterTrans.setDuration(700);
            enterTrans.setStartDelay(600);
            getWindow().setEnterTransition(enterTrans);
        }
    }

    public void onClickLogin(View v){
        if (TextUtils.isEmpty(etUsername.getText().toString()) || TextUtils.isEmpty(etPass.getText().toString())) {
            Dialogs.showAlertDialog(this, getString(R.string.warning), getString(R.string.fill_fields));
            return;
        }

        progressDialog = Dialogs.newProgressDialog(this, getString(R.string.checking_credentials), false);
        progressDialog.show();

        new LoginTask(this, etUsername.getText().toString(), etPass.getText().toString(), this);
    }

    @Override
    public void onLoginOK() {
        progressDialog.dismiss();

        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onLoginError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(this, title, description);
    }
}
