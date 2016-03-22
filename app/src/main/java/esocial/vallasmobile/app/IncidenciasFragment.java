package esocial.vallasmobile.app;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import esocial.vallasmobile.R;
import esocial.vallasmobile.listeners.IncidenciasListener;
import esocial.vallasmobile.obj.Incidencia;
import esocial.vallasmobile.tasks.GetIncidenciasTask;
import esocial.vallasmobile.utils.ErrorViewUtils;
import tr.xip.errorview.ErrorView;

public class IncidenciasFragment extends BaseFragment implements IncidenciasListener {

    private RecyclerView list;
    private ProgressBar progressBar;
    private TextView emptyText;
    private ErrorView errorView;

   // private OfertasAdapter adapter;
    private ArrayList<Incidencia> incidents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incidencias, container, false);

        errorView = (ErrorView) v.findViewById(R.id.incident_error_view);
        list = (RecyclerView) v.findViewById(R.id.incident_list);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        emptyText = (TextView) v.findViewById(R.id.empty_incidents);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new GetIncidenciasTask(getActivity(), this);
    }


    ErrorView.RetryListener listener = new ErrorView.RetryListener() {
        @Override
        public void onRetry() {
            errorView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            //offers.clear();
            new GetIncidenciasTask(getActivity(), IncidenciasFragment.this);
        }
    };


    @Override
    public void onGetIncidentsOK(ArrayList<Incidencia> incidents) {
        progressBar.setVisibility(View.GONE);

        this.incidents = incidents;

        /*adapter = new OfertasAdapter(getActivity(), offers);
        list.setAdapter(adapter);*/

        if(this.incidents.size()==0){
            emptyText.setVisibility(View.VISIBLE);
        }else{
            emptyText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetIncidentsError(String title, String description) {
        progressBar.setVisibility(View.GONE);
        ErrorViewUtils.showError(errorView, title, description,
                getString(R.string.error_view_retry), listener);
    }
}
