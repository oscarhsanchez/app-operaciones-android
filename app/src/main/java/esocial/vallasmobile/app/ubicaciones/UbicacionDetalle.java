package esocial.vallasmobile.app.ubicaciones;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.MainTabPagerAdapter;
import esocial.vallasmobile.adapter.UbicacionTabAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.listeners.LocationService;
import esocial.vallasmobile.listeners.UbicacionesModifyListener;
import esocial.vallasmobile.obj.Ubicacion;
import esocial.vallasmobile.tasks.PutUbicacionLocationTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;


/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class UbicacionDetalle extends BaseActivity implements OnMapReadyCallback, UbicacionesModifyListener {

    public String tabTitles[];

    private Ubicacion ubicacion;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private ViewPager viewPager;
    private UbicacionTabAdapter adapter;
    private TabLayout tabLayout;
    private FloatingActionButton fabSetLocation;
    private FloatingActionButton fabGoNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubicacion_detalle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition enterTrans = new Fade();
            enterTrans.setDuration(400);
            getWindow().setEnterTransition(enterTrans);

            Transition exitTrans = new Fade();
            exitTrans.setDuration(200);
            getWindow().setReturnTransition(exitTrans);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fabSetLocation = (FloatingActionButton) findViewById(R.id.location_fab);
        fabGoNavigation = (FloatingActionButton) findViewById(R.id.route_fab);
        viewPager = (ViewPager) findViewById(R.id.ub_pager);
        tabLayout = (TabLayout) findViewById(R.id.ub_tab_layout);
        viewPager.setOffscreenPageLimit(3);

        tabTitles = new String[]{getString(R.string.info), getString(R.string.images), getString(R.string.media)};

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ubicacion = (Ubicacion) extras.getSerializable("ubicacion");
        }

        //Check permission from location
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.PERMISSION_LOCATION);
        } else {
            fabGoNavigation.setVisibility(View.VISIBLE);
            fabSetLocation.setVisibility(View.VISIBLE);
            LocationService.getLocationManager(getApplicationContext());
        }

        initTabLayout();
        setListeners();
        loadData();
    }

    private void initTabLayout() {
        //add tabs
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i])
                    .setIcon(MainTabPagerAdapter.imageDefaultResId[i]));
        }

        adapter = new UbicacionTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                UbicacionDetalle.this);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i, i == 0));
        }


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                View v = tab.getCustomView();
                adapter.updateCustomView(v, tab.getPosition(), true);
                //populateToolBar(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                adapter.updateCustomView(v, tab.getPosition(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    public void setListeners() {
        fabSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationEnabled()) {
                    Dialogs.showConfirmAlertDialog(UbicacionDetalle.this, R.string.change_location,
                            R.string.change, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (VallasApplication.currentLocation != null) {

                                        progressDialog = Dialogs.newProgressDialog(UbicacionDetalle.this,
                                                getString(R.string.modifying_data), false);
                                        progressDialog.show();

                                        //Seteamos la ubicacion
                                        ubicacion.latitud = VallasApplication.currentLocation.getLatitude();
                                        ubicacion.longitud = VallasApplication.currentLocation.getLongitude();
                                        //Enviamos al WS
                                        new PutUbicacionLocationTask(UbicacionDetalle.this,
                                                ubicacion.pk_ubicacion,
                                                ubicacion.latitud,
                                                ubicacion.longitud,
                                                UbicacionDetalle.this);
                                    }

                                    dialog.dismiss();
                                }
                            }, android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, false);

                }
            }
        });

        fabGoNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VallasApplication.currentLocation != null) {
                    String destiny = ubicacion.latitud + "," +
                            ubicacion.longitud;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + destiny));
                    startActivity(i);
                }
            }
        });
    }

    private void loadData() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(5, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ubicacion.ubicacion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        // Add a marker in location
        LatLng location = new LatLng(ubicacion.latitud, ubicacion.longitud);
        mMap.addMarker(new MarkerOptions().position(location).title(ubicacion.direccion_comercial));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
    }


    private boolean checkLocationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            Dialogs.showConfirmAlertDialog(this, R.string.gps_network_not_enabled,
                    R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                            dialog.dismiss();
                        }
                    }, android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, false);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    LocationService.getLocationManager(getApplicationContext());
                    fabSetLocation.setVisibility(View.VISIBLE);
                    fabGoNavigation.setVisibility(View.VISIBLE);
                } else {
                    // permission denied
                    fabSetLocation.setVisibility(View.INVISIBLE);
                    fabGoNavigation.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    protected String getPkUbicacion() {
        return ubicacion.pk_ubicacion;
    }

    protected Ubicacion getUbicacion() {
        return ubicacion;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment frag = adapter.getItem(tabLayout.getSelectedTabPosition());
        frag.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPutUbicacionLocationOK() {
        progressDialog.dismiss();
        Dialogs.newAlertDialog(this, "", getString(R.string.location_changed), getString(R.string.accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMap.clear();
                        LatLng position = new LatLng(VallasApplication.currentLocation.getLatitude(),
                                VallasApplication.currentLocation.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f));
                        mMap.addMarker(new MarkerOptions().position(position));
                    }
                }).show();
    }

    @Override
    public void onPutUbicacionLocationError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(this, title, description);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ubicacion", ubicacion);
        setResult(RESULT_OK, intent);
        finish();
    }
}
