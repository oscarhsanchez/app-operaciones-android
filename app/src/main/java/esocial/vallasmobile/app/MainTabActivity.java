package esocial.vallasmobile.app;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.MainTabPagerAdapter;
import esocial.vallasmobile.app.ubicaciones.UbicacionesFragment;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 04/01/2016.
 */
public class MainTabActivity extends BaseActivity {

    public static String POSITION = "POSITION";
    public String tabTitles[];

    private Toolbar toolbar;
    private SearchView searchView;
    private ViewPager viewPager;
    private MainTabPagerAdapter adapter;
    private TabLayout tabLayout;

    private OnFragmentInteractionListener ubicacionesListener;


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
        viewPager.setOffscreenPageLimit(3);

        tabTitles = new String[]{getString(R.string.orders), getString(R.string.incidents),
                getString(R.string.locations)};
        initToolBar();
        initTabLayout();
        //populateToolBar(0);

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
                ubicacionesListener.onSearch(searchAutoComplete.getText().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
                searchView.setIconified(true);
                searchView.setQuery("", false);
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



    public void selectTab(int pageIndex) {
        TabLayout.Tab tab = tabLayout.getTabAt(pageIndex);
        tab.select();
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
        Fragment frag = adapter.getItem(tabLayout.getSelectedTabPosition());
        frag.onActivityResult(requestCode, resultCode, data);
    }

    public void setUbicacionesListener(OnFragmentInteractionListener listener){
        ubicacionesListener = listener;
    }

    public interface OnFragmentInteractionListener {
        void onSearch(String criteria);
    }
}
