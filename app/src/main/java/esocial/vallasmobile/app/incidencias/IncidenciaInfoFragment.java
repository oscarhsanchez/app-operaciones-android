package esocial.vallasmobile.app.incidencias;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableRow;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.utils.Dates;

public class IncidenciaInfoFragment extends BaseFragment {

    private EditText etEstado, etTipo, etFechaLimite, etObserv, etFechaCierre, etObservCierre, etUbicacion,
            etLatitud, etLongitud, etTipoMedio, etTipoInv, etMedioPos, etMedioSlots;
    private TableRow rowFechaCierre, rowObservCierre;
    private Incidencia incidencia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incidencia_info, container, false);

        etEstado = (EditText) v.findViewById(R.id.et_orden_estado);
        etTipo = (EditText) v.findViewById(R.id.et_orden_tipo);
        etFechaLimite = (EditText) v.findViewById(R.id.et_orden_fecha_limite);
        etObserv = (EditText) v.findViewById(R.id.et_orden_observ);
        etFechaCierre = (EditText) v.findViewById(R.id.et_orden_fecha_cierre);
        etObservCierre = (EditText) v.findViewById(R.id.et_orden_observ_cierre);
        etUbicacion = (EditText) v.findViewById(R.id.et_orden_ubicacion);
        etLatitud = (EditText) v.findViewById(R.id.et_orden_latitud);
        etLongitud = (EditText) v.findViewById(R.id.et_orden_longitud);
        etTipoMedio = (EditText) v.findViewById(R.id.et_orden_tipo_medio);
        etTipoInv = (EditText) v.findViewById(R.id.et_orden_medio_inv);
        etMedioPos = (EditText) v.findViewById(R.id.et_orden_medio_pos);
        etMedioSlots = (EditText) v.findViewById(R.id.et_orden_medio_slots);
        rowFechaCierre = (TableRow) v.findViewById(R.id.lay_fecha_cierre);
        rowObservCierre = (TableRow) v.findViewById(R.id.lay_obs_cierre);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadData();
    }

    private void loadData(){
        incidencia = ((IncidenciaDetalle)getActivity()).getIncidencia();
        if (incidencia.estado_incidencia != null) {
            if (incidencia.estado_incidencia.equals(0)) {
                etEstado.setText(getString(R.string.pendiente));
            } else if (incidencia.estado_incidencia.equals(1)) {
                etEstado.setText(getString(R.string.en_curso));
            } else if (incidencia.estado_incidencia.equals(2)) {
                etEstado.setText(getString(R.string.finalizado));
            }
        }
        if (incidencia.tipo != null) {
            if (incidencia.tipo.equals(0)) {
                etTipo.setText(getString(R.string.iluminacion));
            } else if (incidencia.tipo.equals(1)) {
                etTipo.setText(getString(R.string.fijacion));
            } else if (incidencia.tipo.equals(2)) {
                etTipo.setText(getString(R.string.instalacion));
            } else if (incidencia.tipo.equals(3)) {
                etTipo.setText(getString(R.string.otros));
            }
        }
        etFechaLimite.setText(Dates.ConvertSfDataStringToJavaString(incidencia.fecha_limite));
        etObserv.setText(incidencia.observaciones);
        if(incidencia.estado_incidencia.equals(2)){//Cerrado
            rowFechaCierre.setVisibility(View.VISIBLE);
            rowObservCierre.setVisibility(View.VISIBLE);
            etFechaCierre.setText(!TextUtils.isEmpty(incidencia.fecha_cierre) ?
                    Dates.getStringWSFromDate(Dates.ConvertStringToDate(incidencia.fecha_cierre, " "), "dd/MM/yyyy")
                    : "");
            etObservCierre.setText(incidencia.observaciones_cierre);
        }else{
            rowFechaCierre.setVisibility(View.GONE);
            rowObservCierre.setVisibility(View.GONE);
        }

        etUbicacion.setText(incidencia.ubicacion.ubicacion);
        etLatitud.setText(incidencia.ubicacion.latitud.toString());
        etLongitud.setText(incidencia.ubicacion.longitud.toString());
        etTipoMedio.setText(incidencia.ubicacion.medio.tipo_medio);
        etTipoInv.setText(incidencia.ubicacion.medio.estatus_inventario);
        etMedioPos.setText(incidencia.ubicacion.medio.posicion.toString());
        etMedioSlots.setText(incidencia.ubicacion.medio.slots.toString());
    }

}
