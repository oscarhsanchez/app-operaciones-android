package esocial.vallasmobile.app.incidencias;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.IncidenciasMainTabAdapter;
import esocial.vallasmobile.adapter.OrdenesTabAdapter;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.tasks.PostIncidenciaTask;
import esocial.vallasmobile.utils.Dialogs;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class IncidenciasFragment extends BaseFragment implements MainTabActivity.OnIncidenciasFragmentListener,
        IncidenciasListener{


    private ProgressDialog progressDialog;

    public String tabTitles[];
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incidencias, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.inc_pager);
        tabLayout = (TabLayout) v.findViewById(R.id.inc_tab_layout);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Listener para comunicarnos con tabactivity
        ((MainTabActivity)getActivity()).setIncidenciasListener(this);

        tabTitles = new String[]{getString(R.string.asignadas), getString(R.string.creadas)};

        initTabLayout();
    }

    private void initTabLayout() {

        final IncidenciasMainTabAdapter adapter = new IncidenciasMainTabAdapter(getActivity().getSupportFragmentManager(),
                tabTitles.length);
        viewPager.setAdapter(adapter);

        viewPager.setAdapter(adapter);

        //add tabs
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setCustomView(customView(i)));
        }


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
        View view = getActivity().getLayoutInflater().inflate(R.layout.text_tab,null);
        TextView textView = (TextView) view.findViewById(R.id.ub_tabText);
        textView.setText(tabTitles[position]);
        return view;
    }


    @Override
    public void onAddIncidenciaOK() {
        progressDialog.dismiss();
        Dialogs.newAlertDialog(getActivity(), getString(R.string.incidencia_created),
                getString(R.string.incidencia_created_text), getString(R.string.accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //loadIncidencias();
                    }
                }).show();
    }

    @Override
    public void onAddIncidenciaError(String title, String description) {
        progressDialog.dismiss();
        Dialogs.showAlertDialog(getActivity(), title, description);
    }

    //-------------MainTabListener--------------//
    @Override
    public void onAddIncidencia(Incidencia incidencia) {
        progressDialog = Dialogs.newProgressDialog(getActivity(), getString(R.string.creating_incidencia), false);
        progressDialog.show();

        new PostIncidenciaTask(getActivity(), incidencia, this);
    }
}
