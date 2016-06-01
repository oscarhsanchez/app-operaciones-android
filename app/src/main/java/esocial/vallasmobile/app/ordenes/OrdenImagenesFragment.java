package esocial.vallasmobile.app.ordenes;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.ImagenesListAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.FullScreenImage;
import esocial.vallasmobile.app.ImagenesListFragment;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.app.incidencias.IncidenciaDetalle;
import esocial.vallasmobile.app.ubicaciones.UbicacionDetalle;
import esocial.vallasmobile.listeners.OrdenesImagenesListener;
import esocial.vallasmobile.obj.Imagen;
import esocial.vallasmobile.obj.OrdenImagen;
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.tasks.GetOrdenesImagenesTask;
import esocial.vallasmobile.tasks.PostOrdenImagenTask;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class OrdenImagenesFragment extends ImagenesListFragment implements OrdenesImagenesListener{


    @Override
    public void getImages() {
        new GetOrdenesImagenesTask(getActivity(),
                ((OrdenDetalle) getActivity()).getPkOrden(),
                this);
    }

    @Override
    public void postImage(String time, byte[] bitmap) {
        String fileName = ((OrdenDetalle) getActivity()).getOrden().ubicacion.medio.pk_medio +"_"+ time+".jpg";
        new DecodeImageTask().execute(fileName, bitmap);
    }

    public class DecodeImageTask extends AsyncTask<Object, Integer, Boolean> {

        private OrdenImagen imagen;

        @Override
        protected Boolean doInBackground(Object... params) {
            imagen = new OrdenImagen();
            imagen.fk_orden_trabajo = ((OrdenDetalle) getActivity()).getPkOrden();
            imagen.fk_pais = getVallasApplication().getSession().fk_pais;
            imagen.nombre = (String)params[0];
            imagen.data = Base64.encodeToString((byte[]) params[1], Base64.DEFAULT);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), getString(R.string.imagen_almacenada), Toast.LENGTH_LONG).show();
            getVallasApplication().setPendingOrdenImage(imagen);
        }
    }


    @Override
    public void onGetOrdenesImagenesOK(ArrayList<OrdenImagen> images) {
        if (!isActivityFinished() && !isRemoving()) {
            progressBar.setVisibility(View.GONE);

            if(this.images==null) this.images = new ArrayList<>();
            this.images.addAll(images);

            adapter = new ImagenesListAdapter(getActivity(), this.images);
            list.setAdapter(adapter);

            adapter.SetOnItemClickListener(new ImagenesListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ImageView image = (ImageView) view.findViewById(R.id.ub_image);
                    Intent intent = new Intent(getActivity(), FullScreenImage.class);
                    intent.putExtra("url", OrdenImagenesFragment.this.images.get(position).url
                            + OrdenImagenesFragment.this.images.get(position).nombre);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                                Pair.create((View) image, "imageTransition"));
                        getActivity().startActivity(intent, options.toBundle());

                    } else {
                        getActivity().startActivity(intent);
                    }
                }
            });


            if (this.images.size() == 0) {
                emptyText.setVisibility(View.VISIBLE);
                list.setVisibility(View.INVISIBLE);
            } else {
                emptyText.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onGetOrdenesImagenesError(String title, String description) {
        progressBar.setVisibility(View.GONE);
        Dialogs.showAlertDialog(getActivity(), title, description);
    }

    @Override
    public void onPostOrderImageOK() {
        progressDialog.dismiss();

        images.clear();
        adapter = null;
        progressBar.setVisibility(View.VISIBLE);

        new GetOrdenesImagenesTask(getActivity(),
                ((OrdenDetalle) getActivity()).getPkOrden(),
                this);
    }

    @Override
    public void onPostOrderImageError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(getActivity(), title, description);
    }
}
