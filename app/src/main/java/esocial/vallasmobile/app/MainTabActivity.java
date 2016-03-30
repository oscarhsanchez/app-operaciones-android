package esocial.vallasmobile.app;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.MainTabPagerAdapter;
import esocial.vallasmobile.app.incidencias.IncidenciaAdd;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.utils.Constants;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 21/03/2016.
 */
public class MainTabActivity extends BaseActivity {


    public static String POSITION = "POSITION";
    public String tabTitles[];

    private FloatingActionButton fabAddIncidencia;
    private Toolbar toolbar;
    private SearchView searchView;
    private ViewPager viewPager;
    private MainTabPagerAdapter adapter;
    private TabLayout tabLayout;

    private OnUbicacionesFragmentInteractionListener ubicacionesListener;
    private OnIncidenciasFragmentListener incidenciasListener;
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
        viewPager.setOffscreenPageLimit(3);

        tabTitles = new String[]{getString(R.string.orders), getString(R.string.incidents),
                getString(R.string.locations)};
        initToolBar();
        initTabLayout();
        setListeners();
        populateToolBar(0);

    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(10, 0);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.parseColor("#bbffffff"));
        searchAutoComplete.setTextColor(Color.parseColor("#DDFFFFFF"));
        searchAutoComplete.setTextSize(16);
        // searchAutoComplete.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (tabLayout.getSelectedTabPosition() == 0) {//Ordenes
                    ordenesListener.onSearch(searchAutoComplete.getText().toString());
                } else if (tabLayout.getSelectedTabPosition() == 1) {//Incidencias
                    incidenciasListener.onSearch(searchAutoComplete.getText().toString());
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
                        ordenesListener.onSearch("");
                    }
                    if (lastTabSelected == 1 || tabLayout.getSelectedTabPosition() == 1) {//Incidencias
                        incidenciasListener.onSearch("");
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
        //add tabs
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i])
                    .setIcon(MainTabPagerAdapter.imageDefaultResId[i]));
        }

        adapter = new MainTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                MainTabActivity.this);
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
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchView.setIconified(true);
                populateToolBar(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                adapter.updateCustomView(v, tab.getPosition(), false);
                lastTabSelected = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
    }


    private void populateToolBar(int position) {
        switch (position) {
            case 0:
                hideFABIncidencia();
                break;
            case 1:
                showFABIncidencia();
                break;
            case 2:
                hideFABIncidencia();
                break;
        }
    }

    private void showFABIncidencia() {
        fabAddIncidencia.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fab_enter);
        fabAddIncidencia.startAnimation(anim);
    }

    private void hideFABIncidencia() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fab_exit);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fabAddIncidencia.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fabAddIncidencia.startAnimation(anim);
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

    public void setUbicacionesListener(OnUbicacionesFragmentInteractionListener listener) {
        ubicacionesListener = listener;
    }

    public void setIncidenciasListener(OnIncidenciasFragmentListener listener) {
        incidenciasListener = listener;
    }

    public void setOrdenesListener(OnOrdenesFragmentInteractionListener listener) {
        ordenesListener = listener;
    }


    public interface OnOrdenesFragmentInteractionListener {
        void onSearch(String criteria);
    }

    public interface OnUbicacionesFragmentInteractionListener {
        void onSearch(String criteria);
    }

    public interface OnIncidenciasFragmentListener {
        void onSearch(String criteria);

        void onAddIncidencia(Incidencia incidencia);
    }
}
