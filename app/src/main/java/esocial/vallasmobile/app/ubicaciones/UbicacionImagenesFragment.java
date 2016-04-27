package esocial.vallasmobile.app.ubicaciones;

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
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.tasks.GetUbicacionesImagenesTask;
import esocial.vallasmobile.tasks.PostUbicacionImagenTask;
import esocial.vallasmobile.utils.Dialogs;
import esocial.vallasmobile.ws.response.GetIncidenciasImagenesResponse;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class UbicacionImagenesFragment extends ImagenesListFragment implements UbicacionesImagenesListener {


    @Override
    public void getImages() {
        new GetUbicacionesImagenesTask(getActivity(),
                ((UbicacionDetalle) getActivity()).getPkUbicacion(),
                this);
    }

    @Override
    public void postImage(String fileName, Bitmap bitmap) {
       new DecodeImageTask().execute(fileName, bitmap);
    }

    public class DecodeImageTask extends AsyncTask<Object, Integer, Boolean> {

        private UbicacionImagen imagen;

        @Override
        protected Boolean doInBackground(Object... params) {
            imagen = new UbicacionImagen();
            imagen.fk_ubicacion = ((UbicacionDetalle) getActivity()).getPkUbicacion();
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
            getVallasApplication().setPendingUbicacionImage(imagen);
        }
    }

    @Override
    public void onGetUbicacionesImagenesOK(ArrayList<UbicacionImagen> images) {
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
                    intent.putExtra("url", UbicacionImagenesFragment.this.images.get(position).url
                            + UbicacionImagenesFragment.this.images.get(position).nombre);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                                Pair.create((View) image, "medioImageTransition"));
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
    public void onGetUbicacionesImagenesError(String title, String description) {
        progressBar.setVisibility(View.GONE);
        Dialogs.showAlertDialog(getActivity(), title, description);
    }

    @Override
    public void onPostImageOK() {
        progressDialog.dismiss();

        images.clear();
        adapter = null;
        progressBar.setVisibility(View.VISIBLE);

        new GetUbicacionesImagenesTask(getActivity(),
                ((UbicacionDetalle) getActivity()).getPkUbicacion(), this);
    }

    @Override
    public void onPostImageError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(getActivity(), title, description);
    }
}
