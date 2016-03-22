package esocial.vallasmobile.app.ubicaciones;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.UbicacionesImagenesAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.FullScreenImage;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.UbicacionesImagenesListener;
import esocial.vallasmobile.obj.Imagen;
import esocial.vallasmobile.tasks.GetUbicacionesImagenesTask;
import esocial.vallasmobile.tasks.PostUbicacionImagenTask;
import esocial.vallasmobile.utils.Dialogs;
import esocial.vallasmobile.utils.ImageUtils;

public class UbicacionImagenesFragment extends BaseFragment implements UbicacionesImagenesListener {

    private final int PERMISSION_READ_STORAGE = 102;
    private final int REQUEST_GALLERY = 103;
    private final int REQUEST_CAMERA = 104;

    private RecyclerView list;
    private TextView emptyText;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddImage;
    private ProgressDialog progressDialog;

    private UbicacionesImagenesAdapter adapter;
    private ArrayList<Imagen> images;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ubicaciones_imagenes, container, false);
        list = (RecyclerView) v.findViewById(R.id.ub_imagenes_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        fabAddImage = (FloatingActionButton) v.findViewById(R.id.fab_add_image);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        emptyText = (TextView) v.findViewById(R.id.empty_ub_images);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(mLayoutManager);

        setListeners();

        new GetUbicacionesImagenesTask(getActivity(),
                ((UbicacionDetalle) getActivity()).getPkUbicacion(), this);
    }

    private void setListeners() {
        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission from location
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_READ_STORAGE);
                } else {
                    AlertDialog.Builder getImageFrom = new AlertDialog.Builder(getActivity());
                    final CharSequence[] opsChars = {getResources().getString(R.string.take_pic), getResources().getString(R.string.open_gallery)};
                    getImageFrom.setItems(opsChars, new android.content.DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                getActivity().startActivityForResult(takePicture, REQUEST_CAMERA);

                            } else if (which == 1) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                getActivity().startActivityForResult(pickPhoto, REQUEST_GALLERY);
                            }
                            dialog.dismiss();
                        }
                    });
                    getImageFrom.show();
                }
            }
        });
    }


    @Override
    public void onGetUbicacionesImagenesOK(ArrayList<Imagen> images) {
        if (!isActivityFinished() && !isRemoving()) {
            progressBar.setVisibility(View.GONE);

            this.images = images;

            adapter = new UbicacionesImagenesAdapter(getActivity(), this.images);
            list.setAdapter(adapter);

            adapter.SetOnItemClickListener(new UbicacionesImagenesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), FullScreenImage.class);
                    intent.putExtra("url", UbicacionImagenesFragment.this.images.get(position).url
                            + UbicacionImagenesFragment.this.images.get(position).nombre);
                    startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String imgDecodableString = null;
            if (requestCode == REQUEST_GALLERY) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
            } else if (requestCode == REQUEST_CAMERA) {
                imgDecodableString = data.getStringExtra("uri");
            }

            File f = new File(imgDecodableString);
            String imageName = f.getName();

            progressDialog = Dialogs.newProgressDialog(getActivity(), getString(R.string.saving_image), false);
            progressDialog.show();

            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(imgDecodableString, 500, 500);
            new PostUbicacionImagenTask((BaseActivity) getActivity(),
                    ((UbicacionDetalle) getActivity()).getPkUbicacion(),imageName, bitmap, UbicacionImagenesFragment.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Abrimos la pantalla que hubiera pinchado
                }
                break;
        }
    }

}
