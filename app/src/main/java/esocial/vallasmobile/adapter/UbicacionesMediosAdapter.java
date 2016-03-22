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
import esocial.vallasmobile.utils.Text;


public class UbicacionesMediosAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private ArrayList<Medio> values;
    private Context context;

    public UbicacionesMediosAdapter(Context context, ArrayList<Medio> values) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Medio getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ubicaciones_medios_row, null);
            holder = new ViewHolder();
            holder.tvPosicion = (TextView) convertView.findViewById(R.id.medio_posicion);
            holder.tvTipoMedio = (TextView) convertView.findViewById(R.id.medio_tipo_medio);
            holder.tvEstatusInv = (TextView) convertView.findViewById(R.id.medio_estatus_inv);
            holder.tvSlots = (TextView) convertView.findViewById(R.id.medio_slots);
            holder.tvCoste = (TextView) convertView.findViewById(R.id.medio_coste);
            holder.ivIluminacion = (ImageView) convertView.findViewById(R.id.medio_iluminacion);
            holder.ivVisibilidad = (ImageView) convertView.findViewById(R.id.medio_visibilidad);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Medio item = values.get(position);

        holder.tvCoste.setText(item.coste.toString());
        holder.tvEstatusInv.setText(item.estatus_inventario);
        holder.tvPosicion.setText(item.posicion.toString());
        holder.tvSlots.setText(item.slots.toString());
        holder.tvTipoMedio.setText(item.tipo_medio);

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

        return convertView;
    }

    class ViewHolder {

        TextView tvPosicion;
        TextView tvTipoMedio;
        TextView tvEstatusInv;
        TextView tvSlots;
        TextView tvCoste;
        ImageView ivVisibilidad;
        ImageView ivIluminacion;
    }
}
