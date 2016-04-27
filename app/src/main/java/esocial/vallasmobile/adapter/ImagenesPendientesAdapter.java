package esocial.vallasmobile.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.obj.Imagen;


public class ImagenesPendientesAdapter extends RecyclerView.Adapter<ImagenesPendientesAdapter.ViewHolder> {


    private ArrayList<Object> values;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public ImagenesPendientesAdapter(Context context, ArrayList<Object> values) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Imagen item = (Imagen) values.get(position);

        holder.progressBar.setVisibility(View.VISIBLE);
        byte[] imageAsBytes = Base64.decode(item.data.getBytes(), Base64.DEFAULT);
        holder.ivImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        holder.progressBar.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return (null != values ? values.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivImage;
        ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.ub_image);
            progressBar = (ProgressBar) view.findViewById(R.id.imageProgressBar);
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
