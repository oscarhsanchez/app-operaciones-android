package esocial.vallasmobile.app.incidencias;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.ImagenesListAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.FullScreenImage;
import esocial.vallasmobile.app.ImagenesListFragment;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.app.ordenes.OrdenDetalle;
import esocial.vallasmobile.listeners.IncidenciasImagenesListener;
import esocial.vallasmobile.listeners.OrdenesImagenesListener;
import esocial.vallasmobile.obj.IncidenciaImagen;
import esocial.vallasmobile.obj.OrdenImagen;
import esocial.vallasmobile.tasks.GetIncidenciasImagenesTask;
import esocial.vallasmobile.tasks.GetOrdenesImagenesTask;
import esocial.vallasmobile.tasks.PostIncidenciaImagenTask;
import esocial.vallasmobile.tasks.PostOrdenImagenTask;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class IncidenciaImagenesFragment extends ImagenesListFragment implements IncidenciasImagenesListener{

    @Override
    public void getImages() {
        new GetIncidenciasImagenesTask(getActivity(),
                ((IncidenciaDetalle) getActivity()).getPkIncidencia(),
                this);
    }

    @Override
    public void postImage(String fileName, Bitmap bitmap) {
        new DecodeImageTask().execute(fileName, bitmap);
    }

    public class DecodeImageTask extends AsyncTask<Object, Integer, Boolean> {

        private IncidenciaImagen imagen;

        @Override
        protected Boolean doInBackground(Object... params) {
            imagen = new IncidenciaImagen();
            imagen.fk_incidencia = ((IncidenciaDetalle) getActivity()).getPkIncidencia();
            imagen.fk_pais = getVallasApplication().getSession().fk_pais;
            imagen.nombre = (String)params[0];

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ((Bitmap)params[1]).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imagen.data = Base64.encodeToString(byteArray, Base64.DEFAULT);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), getString(R.string.imagen_almacenada), Toast.LENGTH_LONG).show();

            getVallasApplication().setPendingIncidenciaImage(imagen);

        }
    }


    @Override
    public void onGetIncidenciasImagenesOK(ArrayList<IncidenciaImagen> images) {
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
                    intent.putExtra("url", IncidenciaImagenesFragment.this.images.get(position).url
                            + IncidenciaImagenesFragment.this.images.get(position).nombre);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
    public void onGetIncidenciasImagenesError(String title, String description) {
        progressBar.setVisibility(View.GONE);
        Dialogs.showAlertDialog(getActivity(), title, description);
    }

    @Override
    public void onPostIncidenciaImageOK() {
        progressDialog.dismiss();

        images.clear();
        adapter = null;
        progressBar.setVisibility(View.VISIBLE);

        new GetIncidenciasImagenesTask(getActivity(),
                ((IncidenciaDetalle) getActivity()).getPkIncidencia(),
                this);
    }

    @Override
    public void onPostIncidenciaImageError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(getActivity(), title, description);
    }
}
