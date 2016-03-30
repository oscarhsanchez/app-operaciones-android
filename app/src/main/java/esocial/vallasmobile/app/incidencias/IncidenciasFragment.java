package esocial.vallasmobile.app.incidencias;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.tasks.GetIncidenciasTask;
import esocial.vallasmobile.tasks.PostIncidenciaTask;
import esocial.vallasmobile.utils.Dialogs;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

public class IncidenciasFragment extends BaseFragment implements IncidenciasListener,
                                                    MainTabActivity.OnIncidenciasFragmentListener{

    private RecyclerView list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView emptyText;
    private ErrorView errorView;
    private ProgressDialog progressDialog;

    private IncidenciasAdapter adapter;
    private ArrayList<Incidencia> incidencias;
    private String criteria = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incidencias, container, false);

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
        ((MainTabActivity)getActivity()).setIncidenciasListener(this);

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

        new GetIncidenciasTask(getActivity(), criteria, this);
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
        adapter = null;
        errorView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);

        new GetIncidenciasTask(getActivity(), criteria, IncidenciasFragment.this);
    }

    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            loadIncidencias();
        }
    };


    @Override
    public void onGetIncidenciasOK(final ArrayList<Incidencia> incidencias) {
        mSwipeRefreshLayout.setRefreshing(false);

        this.incidencias = incidencias;

        adapter = new IncidenciasAdapter(getActivity(), incidencias);
        list.setAdapter(adapter);

        adapter.SetOnItemClickListener(new IncidenciasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), IncidenciaDetalle.class);
                intent.putExtra("incidencia", incidencias.get(position));
                startActivity(intent);
            }
        });

        if(this.incidencias.size()==0){
            emptyText.setVisibility(View.VISIBLE);
        }else{
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetIncidenciasError(String title, String description) {
        mSwipeRefreshLayout.setRefreshing(false);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }

    @Override
    public void onAddIncidenciaOK() {
        progressDialog.dismiss();
        criteria = "";
        Dialogs.newAlertDialog(getActivity(), getString(R.string.incidencia_created),
                getString(R.string.incidencia_created_text), getString(R.string.accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadIncidencias();
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
    public void onSearch(String criteria) {
        this.criteria = criteria;
        loadIncidencias();
    }

    @Override
    public void onAddIncidencia(Incidencia incidencia) {
        progressDialog = Dialogs.newProgressDialog(getActivity(), getString(R.string.creating_incidencia), false);
        progressDialog.show();

        new PostIncidenciaTask(getActivity(), incidencia, this);
    }
}
