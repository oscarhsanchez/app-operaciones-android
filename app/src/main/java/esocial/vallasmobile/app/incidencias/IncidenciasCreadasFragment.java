package esocial.vallasmobile.app.incidencias;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.adapter.IncidenciasAdapter;
import esocial.vallasmobile.app.BaseFragment;
import esocial.vallasmobile.app.MainTabActivity;
import esocial.vallasmobile.components.SpacesItemDecoration;
import esocial.vallasmobile.listeners.IncidenciasCreadasListener;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.tasks.GetIncidenciasCreadasTask;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

/**
 * Created by jesus.martinez on 28/03/2016.
 */
public class IncidenciasCreadasFragment extends BaseFragment implements IncidenciasCreadasListener,
                                                    MainTabActivity.OnIncidenciasTabFragmentListener{

    private RecyclerView list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView emptyText;
    private ErrorView errorView;

    private IncidenciasAdapter adapter;
    private ArrayList<Incidencia> incidencias;
    private String criteria = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incidencias_creadas, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.incident_refresh);
        errorView = (ErrorView) v.findViewById(R.id.incident_error_view);
        list = (RecyclerView) v.findViewById(R.id.incident_list);
        emptyText = (TextView) v.findViewById(R.id.empty_incidents);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Listener para comunicarnos con tabactivity
        ((MainTabActivity)getActivity()).setIncidenciasCreadasListener(this);

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

        new GetIncidenciasCreadasTask(getActivity(), criteria, this);
    }

    private void setListeners(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

        new GetIncidenciasCreadasTask(getActivity(), criteria, IncidenciasCreadasFragment.this);
    }

    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            loadIncidencias();
        }
    };


    @Override
    public void onGetIncidenciasCreadasOK(ArrayList<Incidencia> incidencias) {
        mSwipeRefreshLayout.setRefreshing(false);

        this.incidencias = incidencias;

        adapter = new IncidenciasAdapter(getActivity(), incidencias);
        list.setAdapter(adapter);

        adapter.SetOnItemClickListener(new IncidenciasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(IncidenciasCreadasFragment.this.incidencias.size()>0) {
                    Intent intent = new Intent(getActivity(), IncidenciaDetalle.class);
                    intent.putExtra("incidencia", IncidenciasCreadasFragment.this.incidencias.get(position));
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
    public void onGetIncidenciasCreadasError(String title, String description) {
        mSwipeRefreshLayout.setRefreshing(false);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }

    //-------------MainTabListener--------------//
    @Override
    public void onSearch(String criteria) {
        this.criteria = criteria;
        loadIncidencias();
    }

}
