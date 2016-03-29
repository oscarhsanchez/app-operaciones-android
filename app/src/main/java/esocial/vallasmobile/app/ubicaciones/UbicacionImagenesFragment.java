package esocial.vallasmobile.app.ubicaciones;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.ImagenesListAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.FullScreenImage;
import esocial.vallasmobile.app.ImagenesListFragment;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.obj.UbicacionImagen;
import esocial.vallasmobile.tasks.GetUbicacionesImagenesTask;
import esocial.vallasmobile.tasks.PostUbicacionImagenTask;
import esocial.vallasmobile.utils.Dialogs;

public class UbicacionImagenesFragment extends ImagenesListFragment implements UbicacionesImagenesListener {


    @Override
    public void getImages() {
        new GetUbicacionesImagenesTask(getActivity(),
                ((UbicacionDetalle) getActivity()).getPkUbicacion(),
                this);
    }

    @Override
    public void postImage(String fileName, Bitmap bitmap) {
        new PostUbicacionImagenTask((BaseActivity) getActivity(),
                ((UbicacionDetalle) getActivity()).getPkUbicacion(),
                fileName,
                bitmap,
                UbicacionImagenesFragment.this);
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
