package esocial.vallasmobile.app.incidencias;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.IncidenciasAdapter;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.app.VallasApplication;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.IncidenciasAsignadasListener;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.tasks.GetIncidenciasAsignadasTask;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class IncidenciasAsignadasFragment extends BaseFragment implements IncidenciasAsignadasListener,
                                                    MainTabActivity.OnIncidenciasTabFragmentListener{

    private RecyclerView list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView emptyText;
    private ErrorView errorView;
    private LinearLayout filtroUbicacion;
    private AppCompatImageButton btnRemoveFilter;

    private IncidenciasAdapter adapter;
    private ArrayList<Incidencia> incidencias;
    private String criteria = "";
    private Boolean searchByLocation = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incidencias_asignadas, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.incident_refresh);
        errorView = (ErrorView) v.findViewById(R.id.incident_error_view);
        list = (RecyclerView) v.findViewById(R.id.incident_list);
        emptyText = (TextView) v.findViewById(R.id.empty_incidents);
        filtroUbicacion = (LinearLayout) v.findViewById(R.id.filtroUbicacion);
        btnRemoveFilter = (AppCompatImageButton) v.findViewById(R.id.btn_remove_filter);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Listener para comunicarnos con tabactivity
        ((MainTabActivity)getActivity()).setIncidenciasAsignadasListener(this);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        setListeners();

        new GetIncidenciasAsignadasTask(getActivity(), criteria,
                searchByLocation ? VallasApplication.currentLocation : null,
                this);
    }

    private void setListeners(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadIncidencias();
            }

        });

        btnRemoveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByLocation = false;
                filtroUbicacion.setVisibility(View.GONE);
                list.setPadding(10, 0, 10, 0);
                loadIncidencias();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getVallasApplication().getRefreshIncidencias()) {
            getVallasApplication().setRefreshIncidencias(false);
            loadIncidencias();
        }
    }

    private void loadIncidencias(){
        if(incidencias!=null) incidencias.clear();
        if(adapter!=null) adapter.SetOnItemClickListener(null);
        adapter = null;
        errorView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);

        new GetIncidenciasAsignadasTask(getActivity(), criteria,
                searchByLocation ? VallasApplication.currentLocation : null,
                IncidenciasAsignadasFragment.this);
    }

    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            loadIncidencias();
        }
    };


    @Override
    public void onGetIncidenciasAsignadasOK(ArrayList<Incidencia> incidencias) {
        mSwipeRefreshLayout.setRefreshing(false);

        this.incidencias = incidencias;

        adapter = new IncidenciasAdapter(getActivity(), incidencias);
        list.setAdapter(adapter);

        adapter.SetOnItemClickListener(new IncidenciasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(IncidenciasAsignadasFragment.this.incidencias.size()>0) {
                    Intent intent = new Intent(getActivity(), IncidenciaDetalle.class);
                    intent.putExtra("incidencia", IncidenciasAsignadasFragment.this.incidencias.get(position));
                    startActivity(intent);
                }
            }
        });

        if(this.incidencias.size()==0){
            emptyText.setVisibility(View.VISIBLE);
        }else{
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetIncidenciasAsignadasError(String title, String description) {
        mSwipeRefreshLayout.setRefreshing(false);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }

    //-------------MainTabListener--------------//
    @Override
    public void onSearch(String criteria, boolean location) {
        this.criteria = criteria;
        searchByLocation = location;
        if(searchByLocation) {
            filtroUbicacion.setVisibility(View.VISIBLE);
            list.setPadding(10, getResources().getDimensionPixelSize(R.dimen.vertical_list_filter_pad), 10, 0);
        }else {
            filtroUbicacion.setVisibility(View.GONE);
            list.setPadding(10, 0, 10, 0);
        }
        loadIncidencias();
    }
}
