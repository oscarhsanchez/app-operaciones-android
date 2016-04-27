package esocial.vallasmobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.obj.Medio;
import esocial.vallasmobile.obj.Orden;
import esocial.vallasmobile.utils.Dates;
import esocial.vallasmobile.utils.Text;


public class UbicacionesMediosAdapter extends RecyclerView.Adapter<UbicacionesMediosAdapter.ViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<Medio> values;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public UbicacionesMediosAdapter(Context context, ArrayList<Medio> values) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.values = values;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.ubicaciones_medios_row,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medio item = values.get(position);

        holder.tvCoste.setText(item.coste.toString());
        holder.tvEstatusInv.setText(item.estatus_inventario);
        holder.tvPosicion.setText(item.posicion.toString());
        holder.tvSlots.setText(item.slots.toString());
        holder.tvTipoMedio.setText(item.subtipo!=null ? item.subtipo.descripcion : "");

        if (!TextUtils.isEmpty(item.visibilidad)) {
            if (item.visibilidad.equalsIgnoreCase("ALTO")) {
                holder.ivVisibilidad.setImageResource(R.drawable.green_up_arrow);
            } else if (item.visibilidad.equalsIgnoreCase("MODERADO")) {
                holder.ivVisibilidad.setImageResource(R.drawable.orange_right_arrow);
            } else if (item.visibilidad.equalsIgnoreCase("BAJO")) {
                holder.ivVisibilidad.setImageResource(R.drawable.red_down_arrow);
            }
        } else {
            holder.ivVisibilidad.setImageResource(R.drawable.icon_minus);
        }

        if (!TextUtils.isEmpty(item.estatus_iluminacion)
                && item.estatus_iluminacion.equalsIgnoreCase("ON")) {
            holder.ivIluminacion.setImageResource(R.drawable.icon_bulb_on);
        } else {
            holder.ivIluminacion.setImageResource(R.drawable.icon_bulb_off);
        }
    }


    @Override
    public int getItemCount() {
        return (null != values ? values.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvPosicion;
        TextView tvTipoMedio;
        TextView tvEstatusInv;
        TextView tvSlots;
        TextView tvCoste;
        ImageView ivVisibilidad;
        ImageView ivIluminacion;


        public ViewHolder(View view) {
            super(view);
            tvPosicion = (TextView) view.findViewById(R.id.medio_posicion);
            tvTipoMedio = (TextView) view.findViewById(R.id.medio_tipo_medio);
            tvEstatusInv = (TextView) view.findViewById(R.id.medio_estatus_inv);
            tvSlots = (TextView) view.findViewById(R.id.medio_slots);
            tvCoste = (TextView) view.findViewById(R.id.medio_coste);
            ivIluminacion = (ImageView) view.findViewById(R.id.medio_iluminacion);
            ivVisibilidad = (ImageView) view.findViewById(R.id.medio_visibilidad);
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
