package esocial.vallasmobile.app.ordenes;

import android.Manifest;
import android.content.DialogInterface;
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

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.OrdenesTabAdapter;
import esocial.vallasmobile.app.BaseActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.app.incidencias.IncidenciaAdd;
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.listeners.OrdenesModifyListener;
import esocial.vallasmobile.listeners.OrdenesMotivoListener;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.obj.Motivo;
import esocial.vallasmobile.obj.Orden;
import esocial.vallasmobile.tasks.GetOrdenesMotivosTask;
import esocial.vallasmobile.tasks.PostIncidenciaTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;


/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class OrdenDetalle extends BaseActivity implements OnMapReadyCallback, OrdenesModifyListener,
        OrdenesMotivoListener, IncidenciasListener {

    public String tabTitles[];

    private Orden orden;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private ViewPager viewPager;
    private OrdenesTabAdapter adapter;
    private TabLayout tabLayout;
    private FloatingActionButton fabGoNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orden_detalle);

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
            orden = (Orden) extras.getSerializable("orden");
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

        adapter = new OrdenesTabAdapter(getSupportFragmentManager(), tabTitles.length);
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
                    String destiny = orden.ubicacion.latitud + "," +
                            orden.ubicacion.longitud;
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
        getSupportActionBar().setTitle(getString(R.string.order_detail));
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
        LatLng location = new LatLng(orden.ubicacion.latitud, orden.ubicacion.longitud);
        mMap.addMarker(new MarkerOptions().position(location).title(orden.ubicacion.direccion_comercial));
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

    protected String getPkOrden() {
        return orden.pk_orden_trabajo;
    }

    protected Orden getOrden() {
        return orden;
    }


    @Override
    public void onGetOrdenOK(final Orden ordenModificada) {
        progressDialog.dismiss();
        getVallasApplication().setRefreshOrdenesPendientes(true);
        getVallasApplication().setRefreshOrdenesCerradas(true);

        Dialogs.newAlertDialog(this, getString(R.string.orden_modificada),
                getString(R.string.orden_modificada_text), getString(R.string.accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(OrdenDetalle.this, OrdenDetalle.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("orden", ordenModificada);
                        startActivity(intent);
                    }
                }).show();
    }

    @Override
    public void onGetOrdenError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(this, title, description);
    }

    Motivo motivoSelected;

    @Override
    public void onGetOrdenesMotivoOK(final ArrayList<Motivo> motivos) {
        progressDialog.dismiss();
        VallasApplication.motivosCierre = motivos;

        openMotivoDialog(motivos);
    }

    @Override
    public void onGetOrdenesMotivoError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(this, title, description);
    }

    private void createIncidencia(){
        Intent intent = new Intent(this, IncidenciaAdd.class);
        intent.putExtra("tipo", motivoSelected.tipo_incidencia);
        intent.putExtra("medio", orden.ubicacion.medio);
        startActivityForResult(intent, Constants.REQUEST_ADD_INCIDENCIA);
    }

    @Override
    public void onAddIncidenciaOK() {
        progressDialog.dismiss();
        Toast.makeText(OrdenDetalle.this, getString(R.string.modificando_estado), Toast.LENGTH_LONG).show();
        getVallasApplication().sendCambioEstadoOrden(orden.pk_orden_trabajo, changedStatus,"",
                motivoSelected.pk_motivo);
    }

    @Override
    public void onAddIncidenciaError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(this, title, description);
    }

    private void openMotivoDialog(final ArrayList<Motivo> motivos){
        CharSequence[] array = new CharSequence[motivos.size()];
        for(int i=0; i< array.length;i++){
            array[i] = motivos.get(i).descripcion;
        }

        Dialogs.showNewListDialog(this, "Selecciona motivo", array, false,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        motivoSelected = motivos.get(which);
                        dialog.dismiss();
                        createIncidencia();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_CHANGE_ORDEN_STATUS && resultCode == RESULT_OK){

            Toast.makeText(OrdenDetalle.this, getString(R.string.modificando_estado), Toast.LENGTH_LONG).show();
            getVallasApplication().sendCambioEstadoOrden(orden.pk_orden_trabajo, changedStatus,
                    data.getStringExtra("observaciones_cierre"), null);


            getVallasApplication().setRefreshOrdenesPendientes(true);
            getVallasApplication().setRefreshOrdenesCerradas(true);
            finish();

        }else if(requestCode == Constants.REQUEST_ADD_INCIDENCIA && resultCode == RESULT_OK){
            progressDialog = Dialogs.newProgressDialog(OrdenDetalle.this, getString(R.string.aplicando_cambios), false);
            progressDialog.show();

            new PostIncidenciaTask(this, (Incidencia) data.getSerializableExtra("incidencia"), this);

        } else {
            Fragment frag = adapter.getItem(tabLayout.getSelectedTabPosition());
            frag.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //Mostramos los estados de la orden, excluyendo el actual
        getMenuInflater().inflate(R.menu.menu_orden, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if(item.getSubMenu()!=null && item.getSubMenu().size()>0){
                for (int j = 0; j < item.getSubMenu().size(); j++) {
                    MenuItem subItem = item.getSubMenu().getItem(j);
                    if (subItem.getTitle().equals(getString(R.string.pendiente))) {
                        subItem.setVisible(!orden.estado_orden.equals(0));
                    } else if (subItem.getTitle().equals(getString(R.string.en_proceso))) {
                        subItem.setVisible(!orden.estado_orden.equals(1));
                    } else if (subItem.getTitle().equals(getString(R.string.cerrada))) {
                        subItem.setVisible(!orden.estado_orden.equals(2));
                    } else if (subItem.getTitle().equals(getString(R.string.pendiente_impresion))) {
                        subItem.setVisible(!orden.estado_orden.equals(3));
                    } else if (subItem.getTitle().equals(getString(R.string.no_finalizada))) {
                        subItem.setVisible(!orden.estado_orden.equals(4));
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

        return true;
    }

    private Integer changedStatus;

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_cambiar_estado:
                changedStatus = -1;
                break;
            case R.id.menu_pendiente:
                changedStatus = 0;
                Toast.makeText(OrdenDetalle.this, getString(R.string.modificando_estado), Toast.LENGTH_LONG).show();
                getVallasApplication().sendCambioEstadoOrden(orden.pk_orden_trabajo, changedStatus,"", null);
                break;
            case R.id.menu_encurso:
                changedStatus = 1;
                Toast.makeText(OrdenDetalle.this, getString(R.string.modificando_estado), Toast.LENGTH_LONG).show();
                getVallasApplication().sendCambioEstadoOrden(orden.pk_orden_trabajo, changedStatus,"", null);
                break;
            case R.id.menu_pendiente_imp:
                changedStatus = 3;
                Toast.makeText(OrdenDetalle.this, getString(R.string.modificando_estado), Toast.LENGTH_LONG).show();
                getVallasApplication().sendCambioEstadoOrden(orden.pk_orden_trabajo, changedStatus,"", null);
                break;
            case R.id.menu_no_finalizado:
                changedStatus = 4;
                if(VallasApplication.motivosCierre==null) {
                    progressDialog = Dialogs.newProgressDialog(OrdenDetalle.this, getString(R.string.mostrando_motivos), false);
                    progressDialog.show();
                    new GetOrdenesMotivosTask(this, this);
                }else{
                    openMotivoDialog(VallasApplication.motivosCierre);
                }
                break;
            case R.id.menu_finalizado:
                changedStatus = 2;
                intent = new Intent(OrdenDetalle.this, OrdenComentarioCierre.class);
                startActivityForResult(intent, Constants.REQUEST_CHANGE_ORDEN_STATUS);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
