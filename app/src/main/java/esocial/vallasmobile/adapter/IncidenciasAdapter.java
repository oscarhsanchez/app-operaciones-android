package esocial.vallasmobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.utils.Dates;


public class IncidenciasAdapter extends RecyclerView.Adapter<IncidenciasAdapter.ViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<Incidencia> values;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public IncidenciasAdapter(Context context, ArrayList<Incidencia> values) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.values = values;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.incidencias_row,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Incidencia item = values.get(position);

        holder.tvUbicacion.setText(item.ubicacion.ubicacion);
        holder.tvFechaLimite.setText(Dates.ConvertSfDataStringToJavaString(item.fecha_limite));
        holder.tvTipoMedio.setText(item.ubicacion.medio.subtipo != null ? item.ubicacion.medio.subtipo.descripcion : "");
        holder.tvPosicion.setText(item.ubicacion.medio.posicion.toString());

        if (item.estado_incidencia != null) {
            if (item.estado_incidencia.equals(0)) {
                holder.tvEstado.setText(context.getString(R.string.pendiente));
            } else if (item.estado_incidencia.equals(1)) {
                holder.tvEstado.setText(context.getString(R.string.en_proceso));
            } else if (item.estado_incidencia.equals(2)) {
                holder.tvEstado.setText(context.getString(R.string.cerrada));
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
            } else if (item.tipo.equals(4)) {
                holder.vTipo.setBackgroundColor(context.getResources().getColor(R.color.plano));
            } else if (item.tipo.equals(5)) {
                holder.vTipo.setBackgroundColor(context.getResources().getColor(R.color.otros));
            }
        } else {
            holder.vTipo.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return (null != values ? values.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUbicacion;
        TextView tvEstado;
        TextView tvFechaLimite;
        TextView tvPosicion;
        TextView tvTipoMedio;
        View vTipo;


        public ViewHolder(View view) {
            super(view);
            tvUbicacion = (TextView) view.findViewById(R.id.inc_ubicacion);
            tvEstado = (TextView) view.findViewById(R.id.inc_estado);
            vTipo = view.findViewById(R.id.inc_tipo);
            tvFechaLimite = (TextView) view.findViewById(R.id.inc_fecha_limite);
            tvTipoMedio = (TextView) view.findViewById(R.id.inc_tipo_medio);
            tvPosicion = (TextView) view.findViewById(R.id.inc_posicion);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
