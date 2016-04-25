package esocial.vallasmobile.app.ordenes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableRow;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.obj.Orden;
import esocial.vallasmobile.utils.Dates;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class OrdenInfoFragment extends BaseFragment {

    private EditText etEstado, etTipo, etFechaLimite, etObserv, etFechaCierre, etObservCierre, etUbicacion,
            etLatitud, etLongitud, etTipoMedio, etTipoInv, etMedioPos, etMedioSlots, etCamp, etVersion;
    private TableRow rowFechaCierre, rowObservCierre;
    private Orden orden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.orden_info, container, false);

        etEstado = (EditText) v.findViewById(R.id.et_orden_estado);
        etTipo = (EditText) v.findViewById(R.id.et_orden_tipo);
        etFechaLimite = (EditText) v.findViewById(R.id.et_orden_fecha_limite);
        etObserv = (EditText) v.findViewById(R.id.et_orden_observ);
        etFechaCierre = (EditText) v.findViewById(R.id.et_orden_fecha_cierre);
        etObservCierre = (EditText) v.findViewById(R.id.et_orden_observ_cierre);
        etUbicacion = (EditText) v.findViewById(R.id.et_orden_ubicacion);
        etLatitud = (EditText) v.findViewById(R.id.et_orden_latitud);
        etLongitud = (EditText) v.findViewById(R.id.et_orden_longitud);
        etCamp = (EditText) v.findViewById(R.id.et_orden_campania);
        etVersion = (EditText) v.findViewById(R.id.et_orden_version);
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
        orden = ((OrdenDetalle)getActivity()).getOrden();
        if (orden.estado_orden != null) {
            if (orden.tipo != null && orden.tipo.equals(0) && orden.estado_orden.equals(3)) {
                etEstado.setText(getString(R.string.pendiente_impresion));
            } else if (orden.estado_orden.equals(0)) {
                etEstado.setText(getString(R.string.pendiente));
            } else if (orden.estado_orden.equals(1)) {
                etEstado.setText(getString(R.string.en_proceso));
            } else if (orden.estado_orden.equals(2)) {
                etEstado.setText(getString(R.string.cerrada));
            }
        }
        if (orden.tipo != null) {
            if (orden.tipo.equals(0)) {
                etTipo.setText(getString(R.string.fijacion));
            } else if (orden.tipo.equals(1)) {
                etTipo.setText(getString(R.string.monitoreo));
            } else if (orden.tipo.equals(2)) {
                etTipo.setText(getString(R.string.instalacion));
            } else if (orden.tipo.equals(3)) {
                etTipo.setText(getString(R.string.iluminacion));
            }
        }
        etFechaLimite.setText(Dates.ConvertSfDataStringToJavaString(orden.fecha_limite));
        etObserv.setText(orden.observaciones);
        if(orden.estado_orden.equals(2)){//Cerrado
            rowFechaCierre.setVisibility(View.VISIBLE);
            rowObservCierre.setVisibility(View.VISIBLE);
            etFechaCierre.setText(!TextUtils.isEmpty(orden.fecha_cierre) ?
                    Dates.getStringWSFromDate(Dates.ConvertStringToDate(orden.fecha_cierre, " "), "dd/MM/yyyy")
                    : "");
            etObservCierre.setText(orden.observaciones_cierre);
        }else{
            rowFechaCierre.setVisibility(View.GONE);
            rowObservCierre.setVisibility(View.GONE);
        }

        etCamp.setText(orden.campania);
        etVersion.setText(orden.version);
        etUbicacion.setText(orden.ubicacion.ubicacion);
        etLatitud.setText(orden.ubicacion.latitud.toString());
        etLongitud.setText(orden.ubicacion.longitud.toString());
        etTipoMedio.setText(orden.ubicacion.medio.tipo_medio);
        etTipoInv.setText(orden.ubicacion.medio.estatus_inventario);
        etMedioPos.setText(orden.ubicacion.medio.posicion.toString());
        etMedioSlots.setText(orden.ubicacion.medio.slots.toString());
    }

}
