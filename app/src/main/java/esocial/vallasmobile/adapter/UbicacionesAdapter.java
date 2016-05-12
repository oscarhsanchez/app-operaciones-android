package esocial.vallasmobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.obj.Ubicacion;


public class UbicacionesAdapter extends RecyclerView.Adapter<UbicacionesAdapter.ViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<Ubicacion> values;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public UbicacionesAdapter(Context context, ArrayList<Ubicacion> values) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.values = values;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.ubicaciones_row,
                parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ubicacion item = values.get(position);

        holder.tvCodigo.setText(item.pk_ubicacion);
        holder.tvUbicacion.setText(item.ubicacion);
        holder.tvEstatus.setText(item.estatus);
        holder.tvCategoria.setText(item.categoria);

        if(!TextUtils.isEmpty(item.trafico_vehicular)){
            if(item.trafico_vehicular.equalsIgnoreCase("ALTO")){
                holder.ivVehicular.setImageResource(R.drawable.green_up_arrow);
            }else if(item.trafico_vehicular.equalsIgnoreCase("MODERADO")){
                holder.ivVehicular.setImageResource(R.drawable.orange_right_arrow);
            }else if(item.trafico_vehicular.equalsIgnoreCase("BAJO")){
                holder.ivVehicular.setImageResource(R.drawable.red_down_arrow);
            }
        }else{
            holder.ivVehicular.setImageResource(R.drawable.icon_minus);
        }

        if(!TextUtils.isEmpty(item.trafico_transeuntes)){
            if(item.trafico_transeuntes.equalsIgnoreCase("ALTO")){
                holder.ivTranseuntes.setImageResource(R.drawable.green_up_arrow);
            }else if(item.trafico_transeuntes.equalsIgnoreCase("MODERADO")){
                holder.ivTranseuntes.setImageResource(R.drawable.orange_right_arrow);
            }else if(item.trafico_transeuntes.equalsIgnoreCase("BAJO")){
                holder.ivTranseuntes.setImageResource(R.drawable.red_down_arrow);
            }
        }else{
            holder.ivTranseuntes.setImageResource(R.drawable.icon_minus);
        }
    }


    @Override
    public int getItemCount() {
        return (null != values ? values.size() : 0);
    }


    public void modifyUbicacion(int pos, Ubicacion ubicacion){
        values.remove(pos);
        values.add(pos, ubicacion);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUbicacion;
        TextView tvEstatus;
        TextView tvCategoria;
        TextView tvCodigo;
        ImageView ivTranseuntes;
        ImageView ivVehicular;


        public ViewHolder(View view) {
            super(view);
            tvCodigo = (TextView) view.findViewById(R.id.ub_cod);
            tvUbicacion = (TextView) view.findViewById(R.id.ub_direccion);
            tvEstatus = (TextView) view.findViewById(R.id.ub_estatus);
            tvCategoria = (TextView) view.findViewById(R.id.ub_categoria);
            ivVehicular = (ImageView) view.findViewById(R.id.ub_vehicular);
            ivTranseuntes = (ImageView) view.findViewById(R.id.ub_transeuntes);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition()); //OnItemClickListener mItemClickListener;
        }
    }

    public interface OnItemClickListener{
         void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
}
