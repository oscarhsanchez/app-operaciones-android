package esocial.vallasmobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.callback.ImageLoadedCallback;
import esocial.vallasmobile.obj.Imagen;
import esocial.vallasmobile.obj.UbicacionImagen;


public class ImagenesListAdapter extends RecyclerView.Adapter<ImagenesListAdapter.ViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<Imagen> values;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public ImagenesListAdapter(Context context, ArrayList<Imagen> values) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.values = values;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_imagenes_row, null);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Imagen item = values.get(position);

        Picasso.with(context)
                .load(item.url + item.nombre)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.logo_orange)
                .error(R.drawable.logo_orange)
                .into(holder.ivImage, new ImageLoadedCallback(holder.ivImage));
    }


    @Override
    public int getItemCount() {
        return (null != values ? values.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivImage;

        public ViewHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.ub_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener{
         void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
}
