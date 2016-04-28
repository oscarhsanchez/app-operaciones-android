package esocial.vallasmobile.app;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.MainTabPagerAdapter;
import esocial.vallasmobile.app.incidencias.IncidenciaAdd;
import esocial.vallasmobile.app.ordenes.OrdenComentarioCierre;
import esocial.vallasmobile.app.ordenes.OrdenDetalle;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.services.LocationService;
import esocial.vallasmobile.tasks.PutOrdenEstadoTask;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class MainTabActivity extends BaseActivity {

    private int[] imageResId = {R.drawable.tab_orders_selector,
            R.drawable.tab_incident_selector,  R.drawable.tab_location_selector};

    public static String POSITION = "POSITION";
    public String tabTitles[];

    private FloatingActionButton fabAddIncidencia;
    private Toolbar toolbar;
    private SearchView searchView;
    private ViewPager viewPager;
    private MainTabPagerAdapter adapter;
    private AppCompatImageButton btnFilterLocation;
    private TabLayout tabLayout;

    private OnUbicacionesFragmentInteractionListener ubicacionesListener;
    private OnIncidenciasFragmentListener incidenciasListener;
    private OnIncidenciasTabFragmentListener incidenciasAsignadasListener;
    private OnIncidenciasTabFragmentListener incidenciasCreadasListener;
    private OnOrdenesFragmentInteractionListener ordenesListener;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fabAddIncidencia = (FloatingActionButton) findViewById(R.id.fab_add_incidencia);
        btnFilterLocation = (AppCompatImageButton) findViewById(R.id.action_search_location);
        viewPager.setOffscreenPageLimit(3);

        tabTitles = new String[]{getString(R.string.orders), getString(R.string.incidents),
                getString(R.string.locations)};

        initToolBar();
        initTabLayout();
        setListeners();
        populateToolBar(0);

        initLocationService();
        initEnvioImagenesService();
    }

    private void initLocationService() {
        //Check permission from location
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.PERMISSION_LOCATION);
        }else {
            startService(new Intent(this, LocationService.class));
        }

    }

    private void initEnvioImagenesService() {
        //Creamos el servicio
        if(VallasApplication.sender == null)
            getVallasApplication().initImageSender(false);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.parseColor("#bbffffff"));
        searchAutoComplete.setTextColor(Color.parseColor("#DDFFFFFF"));
        searchAutoComplete.setTextSize(16);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (tabLayout.getSelectedTabPosition() == 0) {//Ordenes
                    ordenesListener.onSearch(searchAutoComplete.getText().toString(), false);
                } else if (tabLayout.getSelectedTabPosition() == 1) {//Incidencias
                    incidenciasAsignadasListener.onSearch(searchAutoComplete.getText().toString(), false);
                    incidenciasCreadasListener.onSearch(searchAutoComplete.getText().toString(), false);
                }
                if (tabLayout.getSelectedTabPosition() == 2) {//Ubicaciones
                    ubicacionesListener.onSearch(searchAutoComplete.getText().toString());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (lastTabSelected == 0 || tabLayout.getSelectedTabPosition() == 0) {//Ordenes
                        ordenesListener.onSearch("", false);
                    }
                    if (lastTabSelected == 1 || tabLayout.getSelectedTabPosition() == 1) {//Incidencias
                        incidenciasAsignadasListener.onSearch("", false);
                        incidenciasCreadasListener.onSearch("", false);
                    }
                    if (lastTabSelected == 2 || tabLayout.getSelectedTabPosition() == 2) {//Ubicaciones
                        ubicacionesListener.onSearch("");
                    }
                }
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));
    }

    private void initTabLayout() {

        adapter = new MainTabPagerAdapter(getSupportFragmentManager(), tabTitles.length,
                MainTabActivity.this);
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
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchView.setIconified(true);
                populateToolBar(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                lastTabSelected = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private View customView(int position){
        View view = getLayoutInflater().inflate(R.layout.main_tab,null);
        ImageView iconHome = (ImageView) view.findViewById(R.id.tabImage);
        TextView textView = (TextView) view.findViewById(R.id.tabText);
        iconHome.setImageResource(imageResId[position]);
        textView.setText(tabTitles[position]);
        return view;
    }

    private Integer lastTabSelected = 0;

    private void setListeners() {
        fabAddIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrimos la pantalla de incidencia
                Intent intent = new Intent(MainTabActivity.this, IncidenciaAdd.class);
                startActivityForResult(intent, Constants.REQUEST_ADD_INCIDENCIA);
            }
        });

        btnFilterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabLayout.getSelectedTabPosition() == 0) {//Ordenes
                    ordenesListener.onSearch("", true);
                }
                if (tabLayout.getSelectedTabPosition() == 1) {//Incidencias
                    incidenciasAsignadasListener.onSearch("", true);
                    incidenciasCreadasListener.onSearch("", true);
                }
                if (tabLayout.getSelectedTabPosition() == 2) {//Ubicaciones
                    ubicacionesListener.onSearch("");
                }
            }
        });
    }


    private void populateToolBar(int position) {
        switch (position) {
            case 0:
                showView(btnFilterLocation, R.anim.fade_in, 200);
                hideView(fabAddIncidencia, R.anim.fab_exit, 300);
                break;
            case 1:
                showView(btnFilterLocation, R.anim.fade_in, 200);
                showView(fabAddIncidencia, R.anim.fab_enter, 300);
                break;
            case 2:
                hideView(btnFilterLocation, R.anim.fade_out, 200);
                hideView(fabAddIncidencia, R.anim.fab_exit, 300);
                break;
        }
    }


    private void showView(final View v, int animation, int duration) {
        if(v.getVisibility() == View.GONE) {
            v.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(this, animation);
            anim.setDuration(duration);
            v.startAnimation(anim);
        }
    }

    private void hideView(final View v, int animation, int duration) {
        if(v.getVisibility() == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, animation);
            anim.setDuration(duration);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            v.startAnimation(anim);
        }
    }


    @Override
    public void onBackPressed() {
        Dialogs.showConfirmAlertDialog(this, R.string.confirm_exit, R.string.exit,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                },
                android.R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_ADD_INCIDENCIA && resultCode == RESULT_OK) {
            incidenciasListener.onAddIncidencia((Incidencia) data.getSerializableExtra("incidencia"));
        } else {
            Fragment frag = adapter.getItem(tabLayout.getSelectedTabPosition());
            frag.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, LocationService.class));
                } else {
                    // permission denied
                }
                break;
        }
    }

    public void setUbicacionesListener(OnUbicacionesFragmentInteractionListener listener) {
        ubicacionesListener = listener;
    }

    public void setIncidenciasListener(OnIncidenciasFragmentListener listener) {
        incidenciasListener = listener;
    }

    public void setIncidenciasAsignadasListener(OnIncidenciasTabFragmentListener listener) {
        incidenciasAsignadasListener = listener;
    }

    public void setIncidenciasCreadasListener(OnIncidenciasTabFragmentListener listener) {
        incidenciasCreadasListener = listener;
    }

    public void setOrdenesListener(OnOrdenesFragmentInteractionListener listener) {
        ordenesListener = listener;
    }


    public interface OnOrdenesFragmentInteractionListener {
        void onSearch(String criteria, boolean location);
    }

    public interface OnUbicacionesFragmentInteractionListener {
        void onSearch(String criteria);
    }

    public interface OnIncidenciasTabFragmentListener {
        void onSearch(String criteria, boolean location);
    }

    public interface OnIncidenciasFragmentListener {
        void onAddIncidencia(Incidencia incidencia);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_gray)),
                    0, spanString.length(), 0); //fix the color to text_gray
            item.setTitle(spanString);
        }

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(VallasApplication.sender!=null && VallasApplication.sender.getImagesCount()>0){
            switch (item.getItemId()) {
                case R.id.main_ver:
                    Intent intent = new Intent(this, ImagenesPendientesActivity.class);
                    startActivity(intent);
                    break;
                case R.id.main_forzar:
                    Toast.makeText(this, getString(R.string.forzando_envio), Toast.LENGTH_LONG).show();
                    getVallasApplication().forzarImageSender();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }else{
            Dialogs.showAlertDialog(this, null, getString(R.string.empty_imagenes_pendientes));
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
