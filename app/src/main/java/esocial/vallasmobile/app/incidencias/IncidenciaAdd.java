package esocial.vallasmobile.app.incidencias;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.obj.Medio;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dates;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class IncidenciaAdd extends BaseActivity {

    private int mYear;
    private int mMonth;
    private int mDay;

    private Spinner spnTipo;
    private EditText etObservaciones;
    private TextView tvFechaLimite;
    private TextView tvMedio;

    private Medio selectedMedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidencias_add);

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


        tvFechaLimite = (TextView) findViewById(R.id.tv_fecha_limite);
        tvMedio = (TextView) findViewById(R.id.tv_medio);
        etObservaciones = (EditText) findViewById(R.id.et_observaciones);
        spnTipo = (Spinner) findViewById(R.id.spn_tipo);

        fillSpinner();
        setListeners();
    }


    private void fillSpinner() {
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                R.layout.simple_spinner_item, Incidencia.tipos);
        spnTipo.setAdapter(spnAdapter);
    }

    private void setListeners() {
        tvFechaLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFechaLimite();
            }
        });

        tvMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncidenciaAdd.this, IncidenciaSelectUbicacion.class);
                startActivityForResult(intent, Constants.REQUEST_SELECT_UBI);
            }
        });
    }

    public void onClickCancel(View v) {
        finish();
    }

    public void onClickAccept(View v) {
        if (selectedMedio == null) {
            Dialogs.showAlertDialog(this, getString(R.string.warning), getString(R.string.medio_empty));
            return;
        }

        if (TextUtils.isEmpty(tvFechaLimite.getText().toString())) {
            Dialogs.showAlertDialog(this, getString(R.string.warning), getString(R.string.fecha_limite_empty));
            return;
        }

        //Creamos incidencia
        Incidencia inc = new Incidencia();
        inc.tipo = spnTipo.getSelectedItemPosition();
        inc.observaciones = etObservaciones.getText().toString();
        inc.fecha_limite = Dates.ConvertJavaStringToSfDataString(tvFechaLimite.getText().toString());
        inc.codigo_user = getVallasApplication().getSession().codigo;
        inc.fk_medio = selectedMedio.pk_medio;
        inc.estado = 1;
        inc.estado_incidencia = 1;
        inc.fk_pais = getVallasApplication().getSession().fk_pais;

        Intent intent = new Intent();
        intent.putExtra("incidencia", inc);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onClickFechaLimite() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, R.style.Dialog,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvFechaLimite.setText(String.format("%02d", dayOfMonth) + "/"
                                + String.format("%02d", (monthOfYear + 1)) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        //Get today date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dpd.setTitle("");
        dpd.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_SELECT_UBI && resultCode == RESULT_OK) {
            selectedMedio = (Medio) data.getSerializableExtra("medio");
            tvMedio.setText(selectedMedio.tipo_medio + ", " + selectedMedio.posicion);
        }
    }
}
