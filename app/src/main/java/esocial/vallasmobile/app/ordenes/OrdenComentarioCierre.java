package esocial.vallasmobile.app.ordenes;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class OrdenComentarioCierre extends BaseActivity {

    private EditText etObservaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orden_comentario_cierre);

        Display display = getWindowManager().getDefaultDisplay();
        int width;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();
        }

        getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        etObservaciones = (EditText) findViewById(R.id.et_observaciones);
    }


    public void onClickCancel(View v){
        finish();
    }

    public void onClickAccept(View v){

        Intent intent = new Intent();
        intent.putExtra("observaciones_cierre", etObservaciones.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
