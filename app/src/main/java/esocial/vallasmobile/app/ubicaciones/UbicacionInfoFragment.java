package esocial.vallasmobile.app.ubicaciones;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.obj.Ubicacion;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class UbicacionInfoFragment extends BaseFragment {

    private EditText etCategoria, etDirComer, etEstatus, etFecha, etLatitud, etLongitud, etLugCerc,
            etObserv, etReferencia, etUbicacion;
    private ImageView ivTranseuntes, ivVehicular;

    private Ubicacion ubicacion;
    private boolean isEditMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ubicacion_info, container, false);

        etCategoria = (EditText) v.findViewById(R.id.et_ub_categoria);
        etDirComer = (EditText) v.findViewById(R.id.et_ub_dir_comercial);
        etEstatus = (EditText) v.findViewById(R.id.et_ub_estatus);
        etFecha = (EditText) v.findViewById(R.id.et_ub_fecha);
        etLatitud = (EditText) v.findViewById(R.id.et_ub_latitud);
        etLongitud = (EditText) v.findViewById(R.id.et_ub_longitud);
        etLugCerc = (EditText) v.findViewById(R.id.et_ub_lug_cerc);
        etObserv = (EditText) v.findViewById(R.id.et_ub_observaciones);
        etReferencia = (EditText) v.findViewById(R.id.et_ub_referencia);
        etUbicacion = (EditText) v.findViewById(R.id.et_ub_ubicacion);
        ivTranseuntes = (ImageView) v.findViewById(R.id.iv_ub_transeuntes);
        ivVehicular = (ImageView) v.findViewById(R.id.iv_ub_vehicular);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        enableViews();
        loadData();
    }

    private void loadData(){
        ubicacion = ((UbicacionDetalle)getActivity()).getUbicacion();
        etCategoria.setText(ubicacion.categoria);
        etDirComer.setText(ubicacion.direccion_comercial);
        etEstatus.setText(ubicacion.estatus);
        etFecha.setText(ubicacion.fecha_instalacion);
        etLatitud.setText(ubicacion.latitud.toString());
        etLongitud.setText(ubicacion.longitud.toString());
        etLugCerc.setText(ubicacion.lugares_cercanos);
        etObserv.setText(ubicacion.observaciones);
        etReferencia.setText(ubicacion.referencia);
        etUbicacion.setText(ubicacion.ubicacion);

        if(!TextUtils.isEmpty(ubicacion.trafico_vehicular)){
            if(ubicacion.trafico_vehicular.equalsIgnoreCase("ALTO")){
                ivVehicular.setImageResource(R.drawable.green_up_arrow);
            }else if(ubicacion.trafico_vehicular.equalsIgnoreCase("MODERADO")){
                ivVehicular.setImageResource(R.drawable.orange_right_arrow);
            }else if(ubicacion.trafico_vehicular.equalsIgnoreCase("BAJO")){
                ivVehicular.setImageResource(R.drawable.red_down_arrow);
            }
        }else{
            ivVehicular.setImageResource(R.drawable.icon_minus);
        }

        if(!TextUtils.isEmpty(ubicacion.trafico_transeuntes)){
            if(ubicacion.trafico_transeuntes.equalsIgnoreCase("ALTO")){
                ivTranseuntes.setImageResource(R.drawable.green_up_arrow);
            }else if(ubicacion.trafico_transeuntes.equalsIgnoreCase("MODERADO")){
                ivTranseuntes.setImageResource(R.drawable.orange_right_arrow);
            }else if(ubicacion.trafico_transeuntes.equalsIgnoreCase("BAJO")){
                ivTranseuntes.setImageResource(R.drawable.red_down_arrow);
            }
        }else{
            ivTranseuntes.setImageResource(R.drawable.icon_minus);
        }


    }

    private void enableViews(){
        etCategoria.setEnabled(isEditMode ? true : false);
        etDirComer.setEnabled(isEditMode ? true : false);
        etEstatus.setEnabled(isEditMode ? true : false);
        etFecha.setEnabled(isEditMode ? true : false);
        etLatitud.setEnabled(isEditMode ? true : false);
        etLongitud.setEnabled(isEditMode ? true : false);
        etLugCerc.setEnabled(isEditMode ? true : false);
        etObserv.setEnabled(isEditMode ? true : false);
        etReferencia.setEnabled(isEditMode ? true : false);
        etUbicacion.setEnabled(isEditMode ? true : false);
    }

}
