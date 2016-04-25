package esocial.vallasmobile.app;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.ImagenesListAdapter;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.obj.Imagen;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;
import esocial.vallasmobile.utils.ImageUtils;
import esocial.vallasmobile.utils.Utils;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public abstract class ImagenesListFragment extends BaseFragment {


    public RecyclerView list;
    public TextView emptyText;
    public ProgressBar progressBar;
    private FloatingActionButton fabAddImage;
    public ProgressDialog progressDialog;

    public ImagenesListAdapter adapter;
    public ArrayList<Imagen> images;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_imagenes, container, false);
        list = (RecyclerView) v.findViewById(R.id.imagenes_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        fabAddImage = (FloatingActionButton) v.findViewById(R.id.fab_add_image);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        emptyText = (TextView) v.findViewById(R.id.empty_images);
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
        getImages();
    }


    private void setListeners() {
        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission from location
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.PERMISSION_READ_STORAGE);
                } else {
                    openSelectDialog();
                }
            }
        });
    }

    private void openSelectDialog() {
        AlertDialog.Builder getImageFrom = new AlertDialog.Builder(getActivity());
        final CharSequence[] opsChars = {getResources().getString(R.string.take_pic), getResources().getString(R.string.open_gallery)};
        getImageFrom.setItems(opsChars, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(takePicture, Constants.REQUEST_CAMERA);

                } else if (which == 1) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(pickPhoto, Constants.REQUEST_GALLERY);
                }
                dialog.dismiss();
            }
        });
        getImageFrom.show();
    }

    public abstract void getImages();

    public abstract void postImage(String fileName, Bitmap bitmap);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String imgDecodableString = null;
            if (requestCode == Constants.REQUEST_GALLERY) {
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
            } else if (requestCode == Constants.REQUEST_CAMERA) {
                Uri imageUri = data.getData();
                imgDecodableString = ImageUtils.getRealPathFromUri(getActivity(), imageUri);
            }

            File f = new File(imgDecodableString);
            String imageName = f.getName();

            progressDialog = Dialogs.newProgressDialog(getActivity(), getString(R.string.saving_image), false);
            progressDialog.show();


            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(imgDecodableString, 500, 500);
            bitmap = ImageUtils.getImageRotated(imgDecodableString, bitmap);

            postImage(imageName, bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_READ_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSelectDialog();
                }
                break;
        }
    }


}
