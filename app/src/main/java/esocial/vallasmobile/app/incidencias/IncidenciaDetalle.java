package esocial.vallasmobile.app.incidencias;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.IncidenciasTabAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.app.ordenes.OrdenComentarioCierre;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.utils.Constants;


/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class IncidenciaDetalle extends BaseActivity implements OnMapReadyCallback {

    public String tabTitles[];

    private Incidencia incidencia;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private ViewPager viewPager;
    private IncidenciasTabAdapter adapter;
    private TabLayout tabLayout;
    private FloatingActionButton fabGoNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidencia_detalle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition enterTrans = new Fade();
            enterTrans.setDuration(400);
            getWindow().setEnterTransition(enterTrans);

            Transition exitTrans = new Fade();
            exitTrans.setDuration(200);
            getWindow().setReturnTransition(exitTrans);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.orden_map);
        mapFragment.getMapAsync(this);
        fabGoNavigation = (FloatingActionButton) findViewById(R.id.orden_route_fab);
        viewPager = (ViewPager) findViewById(R.id.orden_pager);
        tabLayout = (TabLayout) findViewById(R.id.orden_tab_layout);
        viewPager.setOffscreenPageLimit(3);

        tabTitles = new String[]{getString(R.string.info), getString(R.string.images)};

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            incidencia = (Incidencia) extras.getSerializable("incidencia");
        }

        //Check permission from location
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.PERMISSION_LOCATION);
        } else {
            fabGoNavigation.setVisibility(View.VISIBLE);
        }

        initTabLayout();
        setListeners();
        loadData();
    }

    private void initTabLayout() {

        adapter = new IncidenciasTabAdapter(getSupportFragmentManager(), tabTitles.length,
                IncidenciaDetalle.this);
        viewPager.setAdapter(adapter);

        //add tabs
        for(int i=0; i<tabTitles.length;i++) {
            tabLayout.addTab(tabLayout.newTab().setCustomView(customView(i)));
        }
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_text_selector));


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private View customView(int position){
        View view = getLayoutInflater().inflate(R.layout.text_tab,null);
        TextView textView = (TextView) view.findViewById(R.id.ub_tabText);
        textView.setText(tabTitles[position]);
        return view;
    }


    public void setListeners() {
        fabGoNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VallasApplication.currentLocation != null) {
                    String destiny = incidencia.ubicacion.latitud + "," +
                            incidencia.ubicacion.longitud;
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
        getSupportActionBar().setTitle(getString(R.string.incidencia_detail));
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
        LatLng location = new LatLng(incidencia.ubicacion.latitud, incidencia.ubicacion.longitud);
        mMap.addMarker(new MarkerOptions().position(location).title(incidencia.ubicacion.direccion_comercial));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    fabGoNavigation.setVisibility(View.VISIBLE);
                } else {
                    // permission denied
                    fabGoNavigation.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    protected String getPkIncidencia() {
        return incidencia.pk_incidencia;
    }

    protected Incidencia getIncidencia() {
        return incidencia;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_CHANGE_INCIDENCIA_STATUS && resultCode == RESULT_OK){
            Toast.makeText(IncidenciaDetalle.this, getString(R.string.modificando_estado), Toast.LENGTH_LONG).show();
            getVallasApplication().sendCambioEstadoIncidencia(incidencia.pk_incidencia, changedStatus,
                    data.getStringExtra("observaciones_cierre"), null);

        }else {
            Fragment frag = adapter.getItem(tabLayout.getSelectedTabPosition());
            frag.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //Mostramos el menu unicamente si es el usuario asignado
        if(!TextUtils.isEmpty(incidencia.codigo_user_asignado) &&
                incidencia.codigo_user_asignado.equalsIgnoreCase(getVallasApplication().getSession().codigo)) {
            getMenuInflater().inflate(R.menu.menu_orden, menu);
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                if (item.getSubMenu() != null && item.getSubMenu().size() > 0) {
                    for (int j = 0; j < item.getSubMenu().size(); j++) {
                        MenuItem subItem = item.getSubMenu().getItem(j);
                        if (subItem.getTitle().equals(getString(R.string.pendiente))) {
                            subItem.setVisible(!incidencia.estado_incidencia.equals(0));
                        } else if (subItem.getTitle().equals(getString(R.string.en_proceso))) {
                            subItem.setVisible(!incidencia.estado_incidencia.equals(1));
                        } else if (subItem.getTitle().equals(getString(R.string.cerrada))) {
                            subItem.setVisible(!incidencia.estado_incidencia.equals(2));
                        }
                        SpannableString spanString = new SpannableString(subItem.getTitle().toString());
                        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_gray)),
                                0, spanString.length(), 0); //fix the color to text_gray
                        subItem.setTitle(spanString);
                    }
                }

                SpannableString spanString = new SpannableString(item.getTitle().toString());
                spanString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.white)),
                        0, spanString.length(), 0); //fix the color to text_gray
                item.setTitle(spanString);
            }
        }

        return true;
    }

    private Integer changedStatus;

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cambiar_estado:
                changedStatus = -1;
                break;
            case R.id.menu_pendiente:
                changedStatus = 0;
                break;
            case R.id.menu_encurso:
                changedStatus = 1;
                break;
            case R.id.menu_finalizado:
                changedStatus = 2;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        if (!changedStatus.equals(-1)) {
            if(changedStatus.equals(2)){
                Intent intent = new Intent(IncidenciaDetalle.this, OrdenComentarioCierre.class);
                startActivityForResult(intent, Constants.REQUEST_CHANGE_INCIDENCIA_STATUS);
            }else {

                Toast.makeText(IncidenciaDetalle.this, getString(R.string.modificando_estado), Toast.LENGTH_LONG).show();
                getVallasApplication().sendCambioEstadoIncidencia(incidencia.pk_incidencia, changedStatus,"", null);
            }
        }

        return true;
    }

}
