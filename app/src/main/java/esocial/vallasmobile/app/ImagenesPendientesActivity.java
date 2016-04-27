package esocial.vallasmobile.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.ImagenesPendientesAdapter;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.obj.Imagen;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class ImagenesPendientesActivity extends BaseActivity {

    public RecyclerView list;
    public TextView emptyText;
    public ProgressBar progressBar;

    public ImagenesPendientesAdapter adapter;
    public ArrayList<Object> images;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagenes_pendientes);

        list = (RecyclerView) findViewById(R.id.imagenes_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        emptyText = (TextView) findViewById(R.id.empty_images);
        list.setItemAnimator(new DefaultItemAnimator());

        mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);

        initToolBar();
        loadData();
    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.imagenes_toolbar);
        toolbar.setContentInsetsAbsolute(5, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.imagenes_pendientes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadData() {
        progressBar.setVisibility(View.GONE);

        if (this.images == null) this.images = new ArrayList<>();
        this.images.addAll(VallasApplication.sender.getImages());

        adapter = new ImagenesPendientesAdapter(this, this.images);
        list.setAdapter(adapter);

        adapter.SetOnItemClickListener(new ImagenesPendientesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView image = (ImageView) view.findViewById(R.id.ub_image);
                Intent intent = new Intent(ImagenesPendientesActivity.this, FullScreenImage.class);
                intent.putExtra("imagePosition", position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ImagenesPendientesActivity.this.getWindow().setSharedElementReturnTransition(
                            TransitionInflater.from(ImagenesPendientesActivity.this)
                                    .inflateTransition(R.transition.change_image_transform));

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                            ImagenesPendientesActivity.this,
                            Pair.create((View) image, "medioImageTransition"));
                    startActivity(intent, options.toBundle());

                } else {
                    startActivity(intent);
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
