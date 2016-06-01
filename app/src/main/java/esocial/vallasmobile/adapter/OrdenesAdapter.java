package esocial.vallasmobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.obj.Orden;
import esocial.vallasmobile.utils.Dates;


public class OrdenesAdapter extends RecyclerView.Adapter<OrdenesAdapter.ViewHolder> {


    private ArrayList<Orden> values;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public OrdenesAdapter(Context context, ArrayList<Orden> values) {
        this.context = context;
        this.values = values;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.ordenes_row,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Orden item = values.get(position);

        if(item.ubicacion!=null) {

            holder.tvCodigo.setText(item.ubicacion.pk_ubicacion);
            holder.tvUbicacion.setText(item.ubicacion.ubicacion);
            holder.tvFechaLimite.setText(Dates.ConvertSfDataStringToJavaString(item.fecha_limite));
            holder.tvTipoMedio.setText(item.ubicacion.medio.subtipo != null ? item.ubicacion.medio.subtipo.descripcion : "");
            holder.tvPosicion.setText(item.ubicacion.medio.posicion.toString());

            if (item.estado_orden != null) {
                if (item.tipo != null && item.tipo.equals(0) && item.estado_orden.equals(3)) {
                    holder.tvEstado.setText(context.getString(R.string.pendiente_impresion));
                } else if (item.estado_orden.equals(0)) {
                    holder.tvEstado.setText(context.getString(R.string.pendiente));
                } else if (item.estado_orden.equals(1)) {
                    holder.tvEstado.setText(context.getString(R.string.en_proceso));
                } else if (item.estado_orden.equals(2)) {
                    holder.tvEstado.setText(context.getString(R.string.cerrada));
                } else if (item.estado_orden.equals(3)) {
                    holder.tvEstado.setText(context.getString(R.string.pendiente_impresion));
                } else if (item.estado_orden.equals(4)) {
                    holder.tvEstado.setText(context.getString(R.string.no_finalizada));
                }
            } else {
                holder.tvEstado.setText("-");
            }

            if (item.tipo != null) {
                holder.vTipo.setVisibility(View.VISIBLE);
                if (item.tipo.equals(0)) {
                    holder.vTipo.setBackgroundColor(context.getResources().getColor(R.color.fijacion));
                } else if (item.tipo.equals(1)) {
                    holder.vTipo.setBackgroundColor(context.getResources().getColor(R.color.monitorizacion));
                } else if (item.tipo.equals(2)) {
                    holder.vTipo.setBackgroundColor(context.getResources().getColor(R.color.instalacion));
                } else if (item.tipo.equals(3)) {
                    holder.vTipo.setBackgroundColor(context.getResources().getColor(R.color.iluminacion));
                }
            } else {
                holder.vTipo.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != values ? values.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCodigo;
        TextView tvUbicacion;
        TextView tvEstado;
        TextView tvFechaLimite;
        TextView tvPosicion;
        TextView tvTipoMedio;
        View vTipo;


        public ViewHolder(View view) {
            super(view);
            tvUbicacion = (TextView) view.findViewById(R.id.or_ubicacion);
            tvEstado = (TextView) view.findViewById(R.id.or_estado);
            vTipo = view.findViewById(R.id.or_tipo);
            tvFechaLimite = (TextView) view.findViewById(R.id.or_fecha_limite);
            tvTipoMedio = (TextView) view.findViewById(R.id.or_tipo_medio);
            tvPosicion = (TextView) view.findViewById(R.id.or_posicion);
            tvCodigo = (TextView) view.findViewById(R.id.or_cod_ub);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
